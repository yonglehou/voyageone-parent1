package com.voyageone.task2.cms.service.platform;

import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoAggregate;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.JomgoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkJomgoUpdateList;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jd.service.JdWareService;
import com.voyageone.components.tmall.service.TbItemService;
import com.voyageone.service.dao.cms.mongo.CmsBtPlatformActiveLogDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.CmsBtPlatformActiveLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * 记录上下架操作历史(新增记录), 并调用上下架API
 * @author jiangjusheng on 2016/07/11
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_TASK_PlatformActiveLogJob)
public class CmsPlatformActiveLogService extends BaseMQCmsService {

    @Autowired
    private CmsBtPlatformActiveLogDao platformActiveLogDao;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private TbItemService tbItemService;
    @Autowired
    private JdWareService jdWareService;
    @Autowired
    private MongoSequenceService sequenceService;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        $info("CmsPlatformActiceLogService start 参数 " + JacksonUtil.bean2Json(messageMap));
        String channelId = StringUtils.trimToNull((String) messageMap.get("channelId"));
        List<String> codeList = (List<String>) messageMap.get("codeList");
        String activeStatus = StringUtils.trimToNull((String) messageMap.get("activeStatus"));
        String userName = StringUtils.trimToNull((String) messageMap.get("creater"));
        if (channelId == null || codeList == null || codeList.isEmpty()
                || activeStatus == null || userName == null) {
            $error("CmsPlatformActiceLogService 缺少参数");
            return;
        }

        List<Integer> cartIdList = (List<Integer>) messageMap.get("cartIdList");
        if (cartIdList == null || cartIdList.isEmpty()) {
            $error("CmsPlatformActiceLogService 缺少cartid参数");
            return;
        }

        long batchNo = sequenceService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_PRODUCT_PLATFORMACTIVEJOB_ID);
        // 先记录上下架操作历史（必须以group为单位，不能用已选中的商品，会重复）
        for (Integer cartId : cartIdList) {
            JomgoQuery queryObj = new JomgoQuery();
            // 取得group信息
            queryObj.setQuery("{'productCodes':{$in:#},'cartId':#}");
            queryObj.setParameters(codeList, cartId);
            queryObj.setProjectionExt("mainProductCode", "productCodes", "groupId", "numIId");
            List<CmsBtProductGroupModel> grpObjList = cmsBtProductGroupDao.select(queryObj, channelId);
            if (grpObjList == null || grpObjList.isEmpty()) {
                $error("CmsPlatformActiceLogService 产品对应的group不存在 cartId=%d, channelId=%s, codes=%s", cartId, channelId, codeList.toString());
                continue;
            }

            for (CmsBtProductGroupModel grpObj : grpObjList) {
                List<String> pCodeArr = grpObj.getProductCodes();
                if (pCodeArr == null || pCodeArr.isEmpty()) {
                    $error("CmsPlatformActiceLogService cartId=%d, channelId=%s, grpInfo=%s", cartId, channelId, grpObj.toString());
                    continue;
                }
                for (String pCode : pCodeArr) {
                    CmsBtPlatformActiveLogModel model = new CmsBtPlatformActiveLogModel();
                    model.setBatchNo(batchNo);
                    model.setCartId(cartId);
                    model.setChannelId(channelId);
                    model.setActiveStatus(activeStatus);
                    model.setPlatformStatus(getPlatformStatus(pCode, channelId, cartId));
                    model.setComment((String) messageMap.get("comment"));
                    model.setGroupId(grpObj.getGroupId());
                    model.setMainProdCode(grpObj.getMainProductCode());
                    model.setProdCode(pCode);
                    model.setNumIId(grpObj.getNumIId());
                    model.setResult("0");
                    model.setCreater(userName);
                    model.setCreated(DateTimeUtil.getNow());
                    model.setModified("");
                    model.setModifier("");

                    WriteResult rs = platformActiveLogDao.insert(model);
                    $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, code=%s platformActiveLog保存结果=%s", cartId, channelId, pCode, rs.toString());
                }
            }
        }

        String platformStatus = null;
        if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
            // 上架
            platformStatus = CmsConstants.PlatformStatus.OnSale.name();
        } else if (CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)) {
            // 下架
            platformStatus = CmsConstants.PlatformStatus.InStock.name();
        }
        // 调用实际的上下架Api，记录调用结果，在group表和product表更新相关状态
        BulkJomgoUpdateList bulkList = new BulkJomgoUpdateList(1000, platformActiveLogDao, channelId);
        BulkJomgoUpdateList bulkList2 = new BulkJomgoUpdateList(1000, cmsBtProductGroupDao, channelId);
        BulkJomgoUpdateList bulkList3 = new BulkJomgoUpdateList(1000, cmsBtProductDao, channelId);

        boolean updRsFlg = false;
        BulkWriteResult rs = null;
        JomgoQuery queryObj = new JomgoQuery();
        for (Integer cartId : cartIdList) {
            ShopBean shopProp = Shops.getShop(channelId, cartId);
            if (shopProp == null) {
                $error("CmsPlatformActiceLogService 获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
                continue;
            }

            // 先处理numIId不存在/商品lock/状态异常的情况
            queryObj.setQuery("{'cartId':#,'batchNo':#,'result':'0'}");
            queryObj.setParameters(cartId, batchNo);
            queryObj.setProjectionExt("prodCode", "mainProdCode", "numIId");
            List<CmsBtPlatformActiveLogModel> actLogList = platformActiveLogDao.select(queryObj, channelId);
            if (actLogList.isEmpty()) {
                $warn("CmsPlatformActiceLogService 找不到商品上下架历史 cartId=%d，batchNo=%d", cartId, batchNo);
                continue;
            }
            for (CmsBtPlatformActiveLogModel actLogObj : actLogList) {
                String failedComment = null;
                String prodCode = actLogObj.getProdCode();
                if (StringUtils.isEmpty(actLogObj.getNumIId())) {
                    $warn("CmsPlatformActiceLogService numIId(group)错误 channelId=%s, code=%s", channelId, prodCode);
                    failedComment = "NumIId为空";
                } else if (StringUtils.isEmpty(actLogObj.getMainProdCode())) {
                    $warn("CmsPlatformActiceLogService 产品(group)数据错误(没有MainProductCode数据) channelId=%s, code=%s", channelId, prodCode);
                    failedComment = "未设置主商品";
                } else {
                    // 取得商品信息
                    queryObj.setQuery("{'common.fields.code':#}");
                    queryObj.setParameters(actLogObj.getProdCode());
                    queryObj.setProjectionExt("lock", "platforms.P" + cartId + ".pNumIId", "platforms.P" + cartId + ".status", "platforms.P" + cartId + ".pStatus", "platforms.P" + cartId + ".mainProductCode");
                    CmsBtProductModel prodObj = cmsBtProductDao.selectOneWithQuery(queryObj, channelId);
                    if (prodObj == null) {
                        $warn("CmsPlatformActiceLogService 找不到商品code cartId=%d", cartId);
                        failedComment = "商品不存在";
                    } else {
                        String mainCode = StringUtils.trimToNull(prodObj.getPlatformNotNull(cartId).getMainProductCode());
                        long numIId = NumberUtils.toLong(prodObj.getPlatformNotNull(cartId).getpNumIId());

                        if (numIId == 0) {
                            $warn("CmsPlatformActiceLogService numIId错误 channelId=%s, code=%s", channelId, prodCode);
                            failedComment = "NumIId为空";
                        } else if (mainCode == null) {
                            $warn("CmsPlatformActiceLogService 产品数据错误(没有MainProductCode数据) channelId=%s, code=%s", channelId, prodCode);
                            failedComment = "未设置主商品";
                        } else if ("1".equals(StringUtils.trimToNull(prodObj.getLock()))) {
                            $warn("CmsPlatformActiceLogService 商品lock channelId=%s, code=%s", channelId, prodCode);
                            failedComment = "商品已锁定";
                        } else if (!CmsConstants.ProductStatus.Approved.name().equals(StringUtils.trimToNull(prodObj.getPlatformNotNull(cartId).getStatus()))) {
                            $warn("CmsPlatformActiceLogService 商品未审批 channelId=%s, code=%s", channelId, prodCode);
                            failedComment = "商品未审批";
                        } else if (!CmsConstants.PlatformStatus.OnSale.equals(prodObj.getPlatformNotNull(cartId).getpStatus()) && !CmsConstants.PlatformStatus.InStock.equals(prodObj.getPlatformNotNull(cartId).getpStatus())) {
                            $warn("CmsPlatformActiceLogService 商品还未上下架 channelId=%s, code=%s", channelId, prodCode);
                            failedComment = "商品平台状态不是\"在售\"或\"在库\"";
                        }
                    }
                }
                if (failedComment == null) {
                    continue;
                }
                JomgoUpdate updObj = new JomgoUpdate();
                updObj.setQuery("{'cartId':#,'prodCode':#,'batchNo':#}");
                updObj.setQueryParameters(cartId, prodCode, batchNo);
                updObj.setUpdate("{$set:{'result':#,'failedComment':#,'modified':#,'modifier':#}}");
                updObj.setUpdateParameters("3", failedComment, DateTimeUtil.getNowTimeStamp(), userName);
                rs = bulkList.addBulkJomgo(updObj);
                if (rs != null) {
                    $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, code=%s, batchNo=%d platformActiveLog更新结果=%s", cartId, channelId, prodCode, batchNo, rs.toString());
                }
            }
            rs = bulkList.execute();
            if (rs != null) {
                $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, batchNo=%d platformActiveLog更新结果=%s", cartId, channelId, batchNo, rs.toString());
            }

            // 经过上面处理剩下的商品就是可以上下架的商品
            List<JomgoAggregate> aggrList = new ArrayList<>();
            // 查询条件
            aggrList.add(new JomgoAggregate("{ $match: {'cartId':#,'batchNo':#,'result':'0'} }", cartId, batchNo));
            // 分组
            String gp1 = "{ $group : { _id : '$numIId','pcdList':{$addToSet:'$prodCode'} } }";
            aggrList.add(new JomgoAggregate(gp1));

            List<Map<String, Object>> prs = platformActiveLogDao.aggregateToMap(channelId, aggrList);
            if (prs == null || prs.isEmpty()) {
                $error("CmsPlatformActiceLogService 产品不存在 cartId=%d, channelId=%s", cartId, channelId);
                continue;
            }

            // 然后对可以上下架的商品调用API并记录结果
            for (Map<String, Object> prodObj : prs) {
                long numIId = NumberUtils.toLong((String) prodObj.get("_id"));
                List<String> pcdList = (List<String>) prodObj.get("pcdList");
                if (numIId == 0 || pcdList == null || pcdList.isEmpty() ) {
                    $error("CmsPlatformActiceLogService 数据错误 cartId=%d, channelId=%s data=%s", cartId, channelId, prodObj.toString());
                    continue;
                }
                updRsFlg = false;
                try {
                    if (CartEnums.Cart.TG.getValue() == cartId) {
                        // 天猫国际上下架
                        if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
                            // 上架
                            updRsFlg = tbItemService.doWareUpdateListing(shopProp, numIId);
                        } else if (CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)) {
                            // 下架
                            updRsFlg = tbItemService.doWareUpdateDelisting(shopProp, numIId);
                        }
                    } else if (CartEnums.Cart.JG.getValue() == cartId) {
                        // 京东国际上下架
                        if (CmsConstants.PlatformActive.ToOnSale.name().equals(activeStatus)) {
                            // 上架
                            updRsFlg = jdWareService.doWareUpdateListing(shopProp, numIId, true);
                        } else if (CmsConstants.PlatformActive.ToInStock.name().equals(activeStatus)) {
                            // 下架
                            updRsFlg = jdWareService.doWareUpdateDelisting(shopProp, numIId, true);
                        }
                    } else {
                        $error("CmsPlatformActiceLogService 不正确的平台 cartId=%d", cartId);
                    }
                    if (!updRsFlg) {
                        $error("CmsPlatformActiceLogService API调用返回错误结果");
                    }
                } catch (Exception exp) {
                    $error("CmsPlatformActiceLogService API调用失败", exp);
                    updRsFlg = false;
                }

                // 保存调用结果
                for (String prodCode : pcdList) {
                    JomgoUpdate updObj = new JomgoUpdate();
                    updObj.setQuery("{'cartId':#,'prodCode':#,'batchNo':#}");
                    updObj.setQueryParameters(cartId, prodCode, batchNo);
                    if (updRsFlg) {
                        updObj.setUpdate("{$set:{'result':#,'modified':#,'modifier':#}}");
                        updObj.setUpdateParameters("1", DateTimeUtil.getNowTimeStamp(), userName);
                    } else {
                        updObj.setUpdate("{$set:{'result':#,'failedComment':#,'modified':#,'modifier':#}}");
                        updObj.setUpdateParameters("2", "调用API失败", DateTimeUtil.getNowTimeStamp(), userName);
                    }
                    rs = bulkList.addBulkJomgo(updObj);
                    if (rs != null) {
                        $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, code=%s, batchNo=%d platformActiveLog更新结果=%s", cartId, channelId, prodCode, batchNo, rs.toString());
                    }
                }
                if (!updRsFlg) {
                    continue;
                }
                // 在group表和product表更新相关状态
                JomgoUpdate updObj2 = new JomgoUpdate();
                updObj2.setQuery("{'cartId':#,'numIId':#}");
                updObj2.setQueryParameters(cartId, numIId);
                updObj2.setUpdate("{$set:{'platformStatus':#,'modified':#,'modifier':#}}");
                updObj2.setUpdateParameters(platformStatus, DateTimeUtil.getNowTimeStamp(), userName);
                rs = bulkList2.addBulkJomgo(updObj2);
                if (rs != null) {
                    $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, numIId=%d cmsBtProductGroup更新结果=%s", cartId, channelId, numIId, rs.toString());
                }

                // 更新product表时,要检查该group下商品的有效（numIId不存在/商品lock/状态异常）
                for (String prodCode : pcdList) {
                    JomgoUpdate updObj3 = new JomgoUpdate();
                    updObj3.setQuery("{'common.fields.code':#}");
                    updObj3.setQueryParameters(prodCode);
                    updObj3.setUpdate("{$set:{'platforms.P#.pStatus':#,'modified':#,'modifier':#}}");
                    updObj3.setUpdateParameters(cartId, platformStatus, DateTimeUtil.getNowTimeStamp(), userName);
                    rs = bulkList3.addBulkJomgo(updObj3);
                    if (rs != null) {
                        $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, code=%s cmsBtProduct更新结果=%s", cartId, channelId, prodCode, rs.toString());
                    }
                }
            }

            rs = bulkList.execute();
            if (rs != null) {
                $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, batchNo=%d platformActiveLog更新结果=%s", cartId, channelId, batchNo, rs.toString());
            }
            rs = bulkList2.execute();
            if (rs != null) {
                $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, batchNo=%d cmsBtProductGroup更新结果=%s", cartId, channelId, batchNo, rs.toString());
            }
            rs = bulkList3.execute();
            if (rs != null) {
                $debug("CmsPlatformActiceLogService cartId=%d, channelId=%s, batchNo=%d cmsBtProduct更新结果=%s", cartId, channelId, batchNo, rs.toString());
            }
        }
    }

    // 取得商品对应的平台状态
    private String getPlatformStatus(String prodCode, String channelId, int cartId) {
        JomgoQuery queryObj = new JomgoQuery();
        queryObj.setQuery("{'common.fields.code':#}");
        queryObj.setParameters(prodCode);
        queryObj.setProjectionExt("platforms.P" + cartId + ".pStatus");
        CmsBtProductModel prodObj = cmsBtProductDao.selectOneWithQuery(queryObj, channelId);
        if (prodObj == null) {
            $warn("CmsPlatformActiceLogService 找不到商品 channelId=%s，code=%s", channelId, prodCode);
            return null;
        }
        CmsConstants.PlatformStatus pStatus = prodObj.getPlatformNotNull(cartId).getpStatus();
        if (pStatus != null) {
            return pStatus.name();
        }
        return null;
    }
}

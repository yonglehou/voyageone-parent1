package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.domain.sellercat.ShopCategory;
import com.jd.open.api.sdk.domain.ware.ImageReadService.Image;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.bean.JdProductBean;
import com.voyageone.components.jd.service.JdShopService;
import com.voyageone.components.jd.service.JdWareService;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.*;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsMtDictModel;
import com.voyageone.service.model.cms.CmsMtPlatformSkusModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.CmsConstants;
import com.voyageone.task2.cms.bean.WorkLoadBean;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 京东平台产品上新服务
 * Product表中产品复存在就向京东平台新增商品，否则就更新商品
 *
 * @author desmond on 2016/4/12.
 * @version 2.0.0
 */
@Service
public class CmsBuildPlatformProductUploadJdMqService extends BaseMQCmsService {

    @Autowired
    private PlatformProductUploadJdService jdProductUploadService;

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    PlatformMappingService platformMappingService;

    @Autowired
    DictService dictService;

    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;

    @Autowired
    private JdShopService jdShopService;

    @Autowired
    private JdWareService jdWareService;

    @Autowired
    private CmsMtPlatformSkusService cmcMtPlatformSkusService;

    @Autowired
    private SxProductService sxProductService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    // workload对象列表
    private Set<WorkLoadBean> workLoadBeans;

    // 京东平台的操作类型(在售)
    private final static String OptioinType_onsale = "onsale";

    // 京东平台的操作类型(在库)
    private final static String OptioinType_offsale = "offsale";

    // 价格类型(市场价格)
    private final static String PriceType_marketprice = "retail_price";

    // 价格类型(京东价格)
    private final static String PriceType_jdprice = "sale_price";

    // SKU属性类型(颜色)
    private final static String AttrType_Color = "c";

    // SKU属性类型(尺寸)
    private final static String AttrType_Size = "s";

    // SKU属性Active
    private final static int AttrType_Active_1 = 1;

    // 分隔符(tab)
    private final static String Separtor_Tab = "\t";

    // 分隔符(|)
    private final static String Separtor_Vertical = "|";

    // 分隔符(:)
    private final static String Separtor_Colon = ":";

    // 分隔符(,)
    private final static String Separtor_Coma = ",";

    // work_load表publish_status会(1:成功)
    private final static int WorkLoad_status_1 = 1;

    // work_load表publish_status会(2:失败)
    private final static int WorkLoad_status_2 = 2;

    // 商品主图颜色值Id(0000000000)
    private final static String ColorId_MinPic = "0000000000";

    @RabbitListener(queues = MqRoutingKey.CMS_BATCH_PlatformProductUploadJdJob)
    protected void onMessage(Message message){
        super.onMessage(message);
    }

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        doMain(taskControlList);
    }

    /**
     * 京东平台上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    public void doMain(List<TaskControlBean> taskControlList) throws Exception {

        // 获取该任务可以运行的销售渠道
        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 循环所有销售渠道
        if (channelIdList != null && channelIdList.size() > 0) {
            for (String channelId : channelIdList) {
                // 京东平台商品信息新增或更新
                doProductUploadJd(channelId, Integer.parseInt(CartEnums.Cart.JD.getId()));
                // 京东国际商品信息新增或更新
                doProductUploadJd(channelId, Integer.parseInt(CartEnums.Cart.JG.getId()));
            }
        }

        // 正常结束
        $info("正常结束");
    }

    /**
     * 京东平台产品每个平台的上新主处理
     *
     * @param channelId String 渠道ID
     */
    private void doProductUploadJd(String channelId, int cartId) {

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            $error("获取到店铺信息失败, shopProp == null");
            return;
        }

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表
        List<CmsBtSxWorkloadModel> sxWorkloadModels = jdProductUploadService.getSxWorkloadWithChannelIdCartId(
                CmsConstants.PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE, channelId, cartId);
        // 根据上新任务列表中的groupid循环上新处理
        for(CmsBtSxWorkloadModel cmsBtSxWorkloadModel:sxWorkloadModels) {
            long groupId = cmsBtSxWorkloadModel.getGroupId();

            // 主数据产品信息取得
            SxData sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
            if (sxData == null) {
                String errMsg = "取得产品信息失败！channel_id:" + channelId + "group_id:" + groupId;
                $error(errMsg);
                // 回写workload表   (失败2)
                sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_2, this.getClass().getName());
//                return;         // FIXME: 2016/4/24 直接返回上传下一条还是抛出异常
                throw new BusinessException(errMsg);
            }
            // Sku排序
            sxProductService.sortSkuInfo(sxData.getSkuList());
            // 主产品等列表取得
            CmsBtProductModel mainProduct = sxData.getMainProduct();
            List<CmsBtProductModel> productList = sxData.getProductList();
            List<CmsBtProductModel_Sku> skuList = sxData.getSkuList();

            // 属性值准备
            // 取得主产品类目对应的platform mapping数据
            CmsMtPlatformMappingModel cmsMtPlatformMappingModel = platformMappingService.getMappingByMainCatId(channelId, cartId, mainProduct.getCatId());
            if (cmsMtPlatformMappingModel == null) {
                String errMsg = "共通PlatformMapping表中对应的平台类目信息不存在！channel_id:" + channelId + "cartId:" + cartId + "主产品类目：" + mainProduct.getCatId();
                $error(errMsg);
                // 回写workload表   (失败2)
                sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_2, this.getClass().getName());
                throw new BusinessException(errMsg);
            }
            // 取得主产品类目对应的平台类目
            String platformCategoryId = cmsMtPlatformMappingModel.getPlatformCategoryId();
            // 取得平台类目schema信息
            CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = platformCategoryService.getPlatformCatSchema(platformCategoryId, cartId);
            if (cmsMtPlatformCategorySchemaModel == null) {
                String errMsg = "获取平台类目schema信息失败！主产品catId:" + platformCategoryId + "cartId:" + cartId;
                $error(errMsg);
                // 回写workload表   (失败2)
                sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_2, this.getClass().getName());
                throw new BusinessException(errMsg);
            }

            // 获取cms_mt_platform_skus表里渠道指定类目对应的所有颜色和尺寸信息列表
            List<CmsMtPlatformSkusModel> cmsMtPlatformSkusList = new ArrayList<>();
            cmsMtPlatformSkusList = cmcMtPlatformSkusService.getModesByAttrType(channelId, cartId, platformCategoryId, AttrType_Active_1);
            // 取得cms_mt_platform_skus表里平台类目id对应的颜色信息列表
            List<CmsMtPlatformSkusModel> cmsColorList = new ArrayList<>();
            for (CmsMtPlatformSkusModel skuModel : cmsMtPlatformSkusList) {
                // 颜色
                if (AttrType_Color.equals(skuModel.getAttrType())) {
                    cmsColorList.add(skuModel);
                }
            }
            // 取得cms_mt_platform_skus表里平台类目id对应的尺寸信息列表
            List<CmsMtPlatformSkusModel> cmsSizeList = new ArrayList<>();
            for (CmsMtPlatformSkusModel skuModel : cmsMtPlatformSkusList) {
                // 尺寸
                if (AttrType_Size.equals(skuModel.getAttrType())) {
                    cmsSizeList.add(skuModel);
                }
            }

            // 获取预设属性设定表
            // 未设计

            // 获取尺码表
            // 7. product.fields（mongodb， 共通字段略）增加三个字段
            //       * sizeGroupId        <-设置SKU信息     //不是新任务列表中的groupid
            //       * sizeChartIdPc      <-上传图片用
            //       * sizeChartIdMobile  <-上传图片用
            // 调用共通函数
            // TODO

            // 获取字典表(根据channel_id)上传图片的规格等信息
            List<CmsMtDictModel> cmsMtDictModelList = dictService.getModesByChannelCartId(channelId, cartId);
            if (cmsMtDictModelList == null) {
                String errMsg = "获取字典表数据失败！channelId:" + channelId + "cartId:" + cartId;
                $error(errMsg);
                // 回写workload表   (失败2)
                sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_2, this.getClass().getName());
                throw new BusinessException(errMsg);
            }

            // 编辑京东共通属性
            JdProductBean jdProductBean = new JdProductBean();
            jdProductBean = setJdProductCommonInfo(sxData, platformCategoryId, groupId, shopProp);

            // 返回结果是否成功状态
            boolean retStatus = false;

            // 判断新增商品还是更新商品
            long jdWareId = 0;
            // 主product的platform的numIID是否为空，空为新增，非空为更新
            // 判断是新增商品还是更新商品
            if (!StringUtils.isEmpty(mainProduct.getGroups().getNumIId())) {
//            if (StringUtils.isEmpty(mainProduct.getGroups().getPlatformByGroupId(groupId).getNumIId())) {
                // 新增商品的时候

                // 调用京东新增商品API(360buy.ware.add)
                jdWareId = jdWareService.addProduct(shopProp, jdProductBean);
                // 返回的商品id是0的时候，新增商品失败
                if ( 0 == jdWareId ) {
                    $error(String.format("向京东新增商品失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [PlatformCategoryId:%s]", channelId, cartId, groupId, platformCategoryId));
                    // 回写workload表   (失败2)
                    sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_2, this.getClass().getName());
                    return;
                }

                // 回写商品id(wareId->numIId)-----------等待共通函数----------------
                // TODO  共通函数还没做
                // sxProductService.updateImsBtProduct();
//                this.saveGroupNumIId(mainProduct, groupId, String.valueOf(jdWareId), channelId);

                // 上传商品主图
                // 京东要求图片必须是5张， 如果不满5张，必须使用一张图片作为补充， 补图代码暂时忽略
                // 商品主图的第一张已经在前面的共通属性里面设置了，这里最多只需要设置4张非主图
//                jdWareService.addWarePropimg(shopProp, String.valueOf(jdWareId), "pic url1", true);
                try {
                    // TODO  怎么取得4张图片url
                    jdWareService.addWarePropimg(shopProp, String.valueOf(jdWareId), "0000000000", "pic url2", false);
                    jdWareService.addWarePropimg(shopProp, String.valueOf(jdWareId), "0000000000", "pic url3", false);
                    jdWareService.addWarePropimg(shopProp, String.valueOf(jdWareId), "0000000000", "pic url4", false);
                    jdWareService.addWarePropimg(shopProp, String.valueOf(jdWareId), "0000000000", "pic url5", false);
                } catch (Exception ex) {
                    $error("京东上传商品主图失败！ wareId:" + jdWareId);
                    // 回写workload表   (失败2)     // FIXME:这里出异常的时候需要回写为2吗？不要的话，就不要加try..catch
                    sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_2, this.getClass().getName());
                    throw ex;
                }

                // 新增商品成功后，设置SKU属性 调用京东商品修改API
                JdProductBean updateProductBean = new JdProductBean();
                // 新增商品id
                updateProductBean.setWareId(String.valueOf(jdWareId));
                // 构造更新用商品bean，主要设置SKU相关属性
                updateProductBean = setJdProductSkuInfo(updateProductBean, sxData, cmsColorList, cmsSizeList, Integer.parseInt(String.valueOf(groupId)));

                // 新增之后调用京东商品更新API
                // 调用京东商品更新API设置SKU信息的好处是可以一次更新SKU信息，不用再一个一个SKU去设置
                String modified = "";
                try {
                    modified = jdWareService.updateProduct(shopProp, updateProductBean);
                } catch (Exception ex) {
                    $error("新增之后调用京东商品更新API批量设置SKU信息失败! wareId:" + jdWareId);
                    // 回写workload表   (失败2)     // FIXME:这里出异常的时候需要回写为2吗？不要的话，就不要加try..catch
                    sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_2, this.getClass().getName());
                    throw ex;
                }

                if (!StringUtils.isEmpty(modified)) {
                    // 新增之后更新商品SKU信息成功
                    // 设置SKU图片(异常在共通函数里面捕捉，返回成功与否状态，不用加try catch)
                     // FIXME  图片url在product的sku列表的sku信息里面
                    retStatus = uploadJdProductSkuPics(shopProp, jdWareId, sxData);

                } else {
                    // 新增之后更新商品SKU信息失败
                    // 删除商品
                    // 参数：1.ware_id 2.trade_no(流水号：现在时刻)
                    try {
                        jdWareService.deleteWare(shopProp, String.valueOf(jdWareId), Long.toString(new Date().getTime()));
                    } catch (Exception ex) {
                        $error("新增之后更新商品SKU信息失败时删除该新增商品失败! wareId:" + jdWareId);
                        // 回写workload表   (失败2)     // FIXME:这里出异常的时候需要回写为2吗？不要的话，就不要加try..catch
                        sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_2, this.getClass().getName());
                        throw ex;
                    }

                    // 删除group表中的商品id(numIId)
                    // TODO   就用更新numIId的共通函数，传给空的wareid给它，应该就更新成空了
//                  updateImsBtProduct();   // FIXME: 这里共通函数最好能返回更新是否成功状态 << 不返回成功状态也可以

                    // 失败状态设定
                    retStatus = false;
                }

            } else {
                // 更新商品的时候

                // 设置SKU属性
                // 构造更新用商品bean，主要设置SKU相关属性
                jdProductBean = setJdProductSkuInfo(jdProductBean, sxData, cmsColorList, cmsSizeList, Integer.parseInt(String.valueOf(groupId)));

                // 京东商品更新API返回的更新时间
                String retModified = "";
                try {
                    // 调用京东商品更新API
                    retModified = jdWareService.updateProduct(shopProp, jdProductBean);
                } catch (Exception ex) {
                    retStatus = false;
                    // 回写workload表   (失败2)     // FIXME:这里出异常的时候需要回写为2吗？不要的话，就不要加try..catch
                    sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_2, this.getClass().getName());
                    throw ex;
                }

                // 更新商品是否成功
                if (!StringUtils.isEmpty(retModified)) {
                    // 更新商品成功的时候
                    retStatus = true;

                    // 更新商品成功之后，设置SKU图片
                    // 遍历当前group里的所有的product，每个product上传5张图片
                    // 检索到当前商品ware里所有图片，先全部删掉，再一张一张增加
                    // 第一张图设true， 其他的都设置为false（如果不满5张，必须使用一张图片作为补充， 补图代码暂时忽略。）
                    // 更新设置SKU图片失败的时候，不删除商品，group表中的商品id(wareId)也不需要更新
                    // TODO  图片url在product的sku列表的sku信息里面   // FIXME (异常在共通函数里面捕捉，返回返回false)
                    retStatus = uploadJdProductSkuPics(shopProp, jdWareId, sxData);
                }
            }

            // 新增或者更新商品结束时，根据状态回写product表（成功1 失败2）
            if (retStatus) {
                // 回写workload表   (成功1)
                sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_1, this.getClass().getName());
            } else {
                // 回写workload表   (失败2)
                sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, WorkLoad_status_2, this.getClass().getName());
            }

        }

        // 正常结束
        $info(String.format("京东新增或更新商品信息成功！[ChannelId:%s] [CartId:%s] [GroupId:%s]", channelId, cartId));

    }


    /**
     * 设置京东上新产品用共通属性
     * 不包含"sku属性","sku价格","sku库存","自定义属性值别名","SKU外部ID"的值
     *
     * @param sxData sxData 上新产品对象
     * @param platformCategoryId String     平台类目id
     * @param groupId long                  groupid
     * @param shop    ShopBean              店铺信息
     * @return JdProductBean 京东上新用bean
     * @throws BusinessException
     */
    private JdProductBean setJdProductCommonInfo(SxData sxData, String platformCategoryId, long groupId, ShopBean shop) throws BusinessException {
        CmsBtProductModel mainProduct = sxData.getMainProduct();
        List<CmsBtProductModel> productList = sxData.getProductList();
        List<CmsBtProductModel_Sku> skuList = sxData.getSkuList();

        // 京东上新产品共通属性设定
        JdProductBean jdProductBean = new JdProductBean();

        // 流水号(非必须)
//        jdProductBean.setTradeNo(mainProduct.getXXX());                  // 不使用
        // 产地(非必须)
        jdProductBean.setWareLocation(mainProduct.getFields().getOrigin());
        // 类目id(必须)
        jdProductBean.setCid(platformCategoryId);
        // 自定义店内分类(非必须)(123-111;122-222)
        String shopCagetory = this.getShopCategory(shop);
        jdProductBean.setTitle(shopCagetory);
        // 商品标题(必须)
        jdProductBean.setTitle(mainProduct.getFields().getLongTitle());
        // UPC编码(非必须)
//        jdProductBean.setUpcCode(mainProduct.getXXX());                 // 不使用
        // 操作类型 现只支持：offsale 或onsale,默认为下架状态 (非必须)
        jdProductBean.setOptionType(this.getOptionType(mainProduct, groupId));
        // 外部商品编号，对应商家后台货号(非必须)
//        jdProductBean.setItemNum(mainProduct.getFields().getCode());    // 不使用
        // 库存(必须)    --------------------- 共通函数（根据sku列表获取库存列表，制作中）-------------- //TODO
        jdProductBean.setStockNum(String.valueOf(mainProduct.getFields().getQuantity()));
        // 生产厂商(非必须)
//        jdProductBean.setProducter(mainProduct.getXXX());                // 不使用
        // 包装规格 (非必须)
//        jdProductBean.setWrap(mainProduct.getXXX());                     // 不使用
        // 长(单位:mm)(必须)
        jdProductBean.setLength("1");
        // 宽(单位:mm)(必须)
        jdProductBean.setWide("1");
        // 高(单位:mm)(必须)
        jdProductBean.setHigh("1");
        // 重量(单位:kg)(必须)
        jdProductBean.setWeight("1");
//        // 进货价,精确到2位小数，单位:元(非必须)
//        jdProductBean.setCostPrice(mainProduct.getXXX());                 // 不使用
//        // 市场价, 精确到2位小数，单位:元(必须)
        Double marketPrice = getItemPrice(productList, shop.getOrder_channel_id(), shop.getCart_id(), PriceType_marketprice);
        jdProductBean.setMarketPrice(String.valueOf(marketPrice));
//        // 京东价,精确到2位小数，单位:元(必须)
        Double jdPrice = getItemPrice(productList, shop.getOrder_channel_id(), shop.getCart_id(), PriceType_jdprice);
        jdProductBean.setJdPrice(String.valueOf(jdPrice));
        // 描述（最多支持3万个英文字符(必须)-----------------DICT->详情页描述----等共通函数做好后取得----------
//        jdProductBean.setNotes(mainProduct.getFields().getXX());// TODO
        // 图片信息（图片尺寸为800*800，单张大小不超过 1024K）(必须)
//        jdProductBean.setWareImage(imgBytes);---------DICT->产品图片----等共通函数做好后取得---// TODO
        // 包装清单(非必须)
//        jdProductBean.setPackListing(mainProduct.getXXX());               // 不使用
        // 售后服务(非必须)
//        jdProductBean.setService(mainProduct.getXXX());                   // 不使用
        // 商品属性列表,多组之间用|分隔，格式:aid:vid 或 aid:vid|aid1:vid1 或 aid1:vid1(必须)
        // 如输入类型input_type为1或2，则attributes为必填属性；如输入类型input_type为3，则用字段input_str填入属性的值
        //jdProductBean.setAttributes(mainProduct.getXXX());//---------等京东回答-------------// TODO
        // 是否先款后货,false为否，true为是 (非必须)
        jdProductBean.setPayFirst("true");
        // 发票限制：非必须输入，true为限制，false为不限制开增值税发票，FBP、LBP、SOPL、SOP类型商品均可输入(非必须)
        jdProductBean.setCanVAT("true");
        // 是否进口商品：非必须输入，false为否，true为是，FBP类型商品可输入(非必须)
//        jdProductBean.setImported(mainProduct.getXXX());                   // 不使用
        // 是否保健品：非必须输入，false为否，true为是，FBP类型商品可输入  (非必须)
//        jdProductBean.setHealthProduct(mainProduct.getXXX());              // 不使用
        // 是否保质期管理商品, false为否，true为是(非必须)
//        jdProductBean.setShelfLife(mainProduct.getXXX());                  // 不使用
        // 保质期：非必须输入，0-99999范围区间，FBP类型商品可输入(非必须)
//        jdProductBean.setShelfLifeDays(mainProduct.getXXX());              // 不使用
        // 是否序列号管理：非必须输入，false为否，true为是，FBP类型商品可输入(非必须)
//        jdProductBean.setSerialNo(mainProduct.getXXX());                   // 不使用
        // 大家电购物卡：非必须输入，false为否，true为是，FBP类型商品可输入  (非必须)
//        jdProductBean.setAppliancesCard(mainProduct.getXXX());             // 不使用
        // 是否特殊液体：非必须输入，false为否，true为是，FBP、LBP、SOPL类型商品可输入(非必须)
//        jdProductBean.setSpecialWet(mainProduct.getXXX());                // 不使用
        // 商品件型：FBP类型商品必须输入，非FBP类型商品可输入非必填，0免费、1超大件、2超大件半件、3大件、4大件半件、5中件、6中件半件、7小件、8超小件(必须)
//        jdProductBean.setWareBigSmallModel(mainProduct.getXXX());         // 不使用
        // 商品包装：FBP类型商品必须输入，非FBP类型商品可输入非必填，1普通商品、2易碎品、3裸瓶液体、4带包装液体、5按原包装出库(必须)
//        jdProductBean.setWarePackType(mainProduct.getXXX());              // 不使用
        // 用户自行输入的类目属性ID串结构：‘pid1|pid2|pid3’,属性的pid调用360buy.ware.get.attribute取得, 输入类型input_type=3即输入(非必须)
//        jdProductBean.setInputPids(mainProduct.getXXX());     // ----------有共通函数，正在写------- // TODO
        // 用户自行输入的属性值,结构:‘输入值|输入值2|输入值3’图书品类输入值规则： ISBN：数字、字母格式 出版时间：日期格式“yyyy-mm-dd”
        // 版次：数字格式 印刷时间：日期格式“yyyy-mm-dd” 印次：数字格式 页数：数字格式 字数：数字格式 套装数量：数字格式 附件数量：数字格式(非必须)
//        jdProductBean.setInputStrs(mainProduct.getXXX());     // ----------有共通函数，正在写------- // TODO
        // 是否输入验证码 true:是;false:否  (非必须)
        jdProductBean.setHasCheckCode("false");
        // 广告词内容最大支持45个字符(非必须)
//        jdProductBean.setAdContent(mainProduct.getXXX());                  // 不使用
        // 定时上架时间 时间格式：yyyy-MM-dd HH:mm:ss;规则是大于当前时间，10天内。(非必须)
//        jdProductBean.setListTime(mainProduct.getXXX());                   // 不使用

        return jdProductBean;
    }

    /**
     * 设置京东上新产品SKU属性
     * 更新产品时用，传入设置好共通属性的JdProductBean
     * 新增商品之后设置SKU属性时，传入一个全新的更新用JdProductBean(new一个全新的更新用bean)
     *
     * @param targetProductBean JdProductBean   产品对象
     * @param sxData SxData     产品对象
     * @param cmsColorList List<CmsMtPlatformSkusModel>  SKU颜色列表
     * @param cmsSizeList List<CmsMtPlatformSkusModel>   SKU尺寸列表
     * @param groupId int groupid
     * @return JdProductBean 京东上新用bean
     * @throws BusinessException
     */
    private JdProductBean setJdProductSkuInfo(JdProductBean targetProductBean, SxData sxData, List<CmsMtPlatformSkusModel> cmsColorList, List<CmsMtPlatformSkusModel> cmsSizeList, int groupId) throws BusinessException {
        CmsBtProductModel mainProduct = sxData.getMainProduct();
        List<CmsBtProductModel> productList = sxData.getProductList();
        List<CmsBtProductModel_Sku> skuList = sxData.getSkuList();

        // 调用共通函数从cms_bt_platform_sku_prop_value_option表中取得每个类目对应的颜色和尺寸信息
        // 并与产品SKU价格，库存，外部id等mapping起来

        // sku属性,一组sku 属性之间用^分隔，多组用|分隔格式(非必须)
        StringBuffer sbSkuProperties = new StringBuffer();
        // sku价格,多组之间用‘|’分隔，格式:p1|p2 (非必须)
        StringBuffer sbSkuPrice = new StringBuffer();
        // sku 库存,多组之间用‘|’分隔， 格式:s1|s2(非必须)
        StringBuffer sbSkuStocks = new StringBuffer();
        // 自定义属性值别名：属性ID:属性值ID:别名(非必须)
        StringBuffer sbPropertyAlias = new StringBuffer();
        // SKU外部ID,多组之间用‘|’分隔， 格式:s1|s2(非必须)
        StringBuffer sbSkuOuterId = new StringBuffer();

        // 根据颜色和尺寸循环设置SKU属性
        for (CmsMtPlatformSkusModel cmsColor:cmsColorList) {
            for (CmsMtPlatformSkusModel cmsSize:cmsSizeList) {
                // sku属性(1000021641:1523005913^1000021641:1523005771|1000021641:1523005913^1000021641:1523005772)
                sbSkuProperties.append(cmsColor.getAttrValue());
                sbSkuProperties.append(Separtor_Tab);        //"\t"
                sbSkuProperties.append(cmsSize.getAttrValue());
                sbSkuProperties.append(Separtor_Vertical);   //"|"

                // sku价格(100.0|150.0|100.0|100.0)
                // TODO 怎么取得价格
                // FIXME: 2016/4/24 取得的skulist里面没有包含颜色CODE，无法知道当前SIZE(42码)对应的是哪种颜色，所以不能渠道价格
                // FIXME: 2016/4/24 只能从product list里面一个循环取得颜色，之后再取得该颜色对应的尺码(42码)的价格
//                sbSkuPrice.append("price1");
//                sbSkuPrice.append(Separtor_Vertical);   //"|"

                // sku 库存(10|1|5|20)
                // TODO 调用共通函数取得库存之和   ============================================================
                sbSkuStocks.append("stocknum1");
                sbSkuStocks.append(Separtor_Vertical);   //"|"

                // SKU外部ID(200001-001-41|200001-001-42)    <-  用product里面的skuList
                for (CmsBtProductModel product:productList) {
                    List<CmsBtProductModel_Sku> productSkuList = product.getSkus();
                    for (CmsBtProductModel_Sku sku:productSkuList) {
                        // 颜色属性值和尺寸属性值取得                  // FIXME: 2016/4/24  这里的2个比较对象项目可能不对
                        if(cmsColor.getAttrValue() == product.getFields().getCode() &&
                           cmsSize.getAttrValue() == sku.getSize()) {

                            // TODO 取得该颜色该尺寸对应的价格
                            // sku价格(100.0|150.0|100.0|100.0)    // FIXME: 2016/4/24 有好几个价格，不知道取哪个价格
                            sbSkuPrice.append(sku.getPriceRetail());
                            sbSkuPrice.append(Separtor_Vertical);   //"|"

                            // SKU外部ID
                            sbSkuOuterId.append(sku.getSkuCode());
                            sbSkuOuterId.append(Separtor_Vertical);   //"|"
                            break;
                        }
                    }
                }

            }
        }

        // 根据颜色设置自定义属性值别名
        // 颜色1:颜色1的别名^颜色2:颜色2的别名^尺码1:尺码1的别名^尺码2:尺码2的别名
        // 颜色是product里面的code
        for (CmsMtPlatformSkusModel cmsColor:cmsColorList) {
            // 自定义属性值别名(Color1:美白款^Color2:美黑款) 颜色是product里面的code
            for (CmsBtProductModel product:productList) {
                // 根据渠道id,类目id和颜色属性值取得颜色别名
                if(cmsColor.getAttrValue() == product.getFields().getCode()) {
                    // 设置自定义属性值颜色别名
                    sbPropertyAlias.append(product.getFields().getColor());
                    sbPropertyAlias.append(Separtor_Colon);   // ":"->"^"  // FIXME: 1000021641:1523005913:美白款|Color2:美黑款
                    sbPropertyAlias.append(product.getFields().getProductNameCn());
                    sbPropertyAlias.append(Separtor_Tab);     //"\t"
                    break;
                }
            }
        }

        // 根据尺寸设置自定义属性值别名   <-  用skuList
        // 颜色1:颜色1的别名^颜色2:颜色2的别名^尺码1:尺码1的别名^尺码2:尺码2的别名
        for (CmsMtPlatformSkusModel cmsSize:cmsSizeList) {
            // 自定义属性值别名(Color1:美白款^Color2:美黑款) 颜色是product里面的code
            for (CmsBtProductModel_Sku sku:skuList) {
                // 根据渠道id,类目id，颜色属性值和尺寸属性值取得
//                if(cmsSize.getAttrValue() == sku.getSize()) {
                if(cmsSize.getAttrType() == sku.getSize()) {  // FIXME: 这里的比较对象有可能不对
                    // 尺寸设置自定义属性值别名
                    sbSkuOuterId.append(sku.getSize());
                    sbSkuOuterId.append(Separtor_Colon);   // ":"
                    // 尺码别名到尺码表转换一下(尺码对照表id,转换前size)
                    sbSkuOuterId.append(sxProductService.changeSize(groupId, sku.getSize()));
                    sbSkuOuterId.append(Separtor_Tab);     //"\t"
                    break;
                }
            }
        }

        // 移除sku属性最后的"|"
        if (sbSkuProperties.length() > 0) {
            sbSkuProperties.deleteCharAt(sbSkuProperties.length() - 1);
        }

        // 移除sku价格最后的"|"
        if (sbSkuPrice.length() > 0) {
            sbSkuPrice.deleteCharAt(sbSkuPrice.length() - 1);
        }

        // 移除sku库存最后的"|"
        if (sbSkuStocks.length() > 0) {
            sbSkuStocks.deleteCharAt(sbSkuStocks.length() - 1);
        }

        // 移除自定义属性值别名最后的"^"
        if (sbPropertyAlias.length() > 0) {
            sbPropertyAlias.deleteCharAt(sbPropertyAlias.length() - 1);
        }

        // 移除SKU外部ID最后的"|"
        if (sbSkuOuterId.length() > 0) {
            sbSkuOuterId.deleteCharAt(sbSkuOuterId.length() - 1);
        }

        // sku属性,一组sku 属性之间用^分隔，多组用|分隔格式(非必须)
        targetProductBean.setSkuProperties(sbSkuProperties.toString());
        // sku价格,多组之间用‘|’分隔，格式:p1|p2 (非必须)
        targetProductBean.setSkuPrices(sbSkuPrice.toString());
        // sku 库存,多组之间用‘|’分隔， 格式:s1|s2(非必须)
        targetProductBean.setSkuStocks(sbSkuStocks.toString());
        // 自定义属性值别名：属性ID:属性值ID:别名(非必须)
        targetProductBean.setPropertyAlias(sbPropertyAlias.toString());
        // SKU外部ID，对个之间用‘|’分隔格(非必须)
        targetProductBean.setOuterId(sbSkuOuterId.toString());

        return targetProductBean;
    }

    /**
     * 设置SKU图片
     * 遍历当前group里的所有的product，每个product上传5张图片
     * 检索到当前商品ware里所有图片，先全部删掉，再一张一张增加
     * 第一张图设true， 其他的都设置为false（如果不满5张，必须使用一张图片作为补充）
     *
     * @param wareId long 商品id
     * @param sxData SxData 产品对象
     * @return boolean 上传指定商品SKU图片是否成功
     */
    private boolean uploadJdProductSkuPics(ShopBean shopProp, long wareId, SxData sxData) {
        boolean retUploadPics = false;

        CmsBtProductModel mainProduct = sxData.getMainProduct();
        List<CmsBtProductModel> productList = sxData.getProductList();
        List<CmsBtProductModel_Sku> skuList = sxData.getSkuList();

        // 颜色的值要提前设置到puduct列表中去-----------------------还没做------------------------
        // FIXME: 新增商品时候也要去京东上面去查找图片和删除图片吗？如果不需要，加一个参数表示新增还是更新

        // 检索     调用API【根据商品Id，检索商品图片】
        // 删除图片  调用API【删除商品图片】
        // 增加图片  调用API【根据商品Id，销售属性值Id增加图片】

        // 根据商品Id，检索商品图片列表
        // 检索 调用API【根据商品Id，检索商品图片】
        // TODO
        // 指定商品Id对应的商品图片列表（包含颜色值Id为0000000000）
        List<Image> wareIdPics = new ArrayList<>();
        try {
            // 调用京东API根据商品Id，检索商品图片
            wareIdPics = jdWareService.getImagesByWareId(shopProp, wareId);
        } catch (Exception ex) {
            $error("调用京东根据商品Id检索商品图片列表失败！ wareId:" + wareId);
            return false;
        }

        // 删除图片 调用API【删除商品图片】批量删除该商品全部SKU图片，不删主图（颜色值Id0000000000）
        // 删除商品图片的接口，支持批量 颜色和排序的数组长度要一致 删除时按数组对应的坐标删除

        // 取出商品图片列表中的要删除颜色值Id列表(去掉重复，去掉主图颜色值Id(ID0000000000))
        List<String> picColorIds = new ArrayList<>();
        for (Image img : wareIdPics) {
            // 过滤掉主图的颜色值Id0000000000
            if (ColorId_MinPic.equals(img.getColorId())) {
                continue;
            }

            // 如果要删除颜色值Id列表中不存在的时候，追加
            if (!picColorIds.contains(img.getColorId())) {
                picColorIds.add(img.getColorId());
            }
        }

        // 根据要删除的列表及图片列表作成删除用的颜色id数组和图片位置数组("123,234,345")
        StringBuffer sbDelColorIds = new StringBuffer();
        StringBuffer sbDelImgIndexes = new StringBuffer();
        for (String strColorId : picColorIds) {
            // 颜色id数组("jingdong,yanfa,pop")
            sbDelColorIds.append(strColorId);
            sbDelColorIds.append(Separtor_Coma);   //","

            // 图片位置数组("123,234,345")
            for (Image pic : wareIdPics) {
                // 删除对象图片id与图片id一致的时候
                if (strColorId.equals(pic.getColorId())) {
                    // 该颜色对应图片位置拼起来("123")
                    sbDelImgIndexes.append(strColorId);
                }
            }
            // 一种颜色对应图片位置最后的逗号("123,")
            sbDelImgIndexes.append(Separtor_Coma);   //","
        }

        // 移除颜色id值数组最后的"，"
        if (sbDelColorIds.length() > 0) {
            sbDelColorIds.deleteCharAt(sbDelColorIds.length() - 1);
        }

        // 移除图片位置最后的"，"
        if (sbDelImgIndexes.length() > 0) {
            sbDelImgIndexes.deleteCharAt(sbDelImgIndexes.length() - 1);
        }

        try {
            // 调用API【删除商品图片】批量删除该商品全部SKU图片，不删主图（颜色值Id0000000000）
            retUploadPics = jdWareService.deleteImagesByWareId(shopProp, wareId, sbDelColorIds.toString(), sbDelImgIndexes.toString());
        } catch (Exception ex) {
            $error("调用京东根据商品Id批量删除该商品全部SKU图片失败！ wareId:" + wareId);
            return false;
        }

        // 增加图片  调用API【根据商品Id，销售属性值Id增加图片】
        // 每个颜色要传五张图，第一张图设为主图，其他的都设置为非主图（如果不满5张，必须使用一张图片作为补充）   <- FIXME 补图代码暂时忽略
        // FIXME 图片要按index来排序，把第一张设为主图吗？
        // TODO
//        for (CmsBtProductModel product:productList) {
//            List<CmsBtProductModel_Sku> productSkuList = product.getSkus();
//            for (CmsBtProductModel_Sku sku:productSkuList) {
//                // 颜色属性值和尺寸属性值取得
//                if(cmsColor.getAttrValue() == product.getFields().getCode() &&
//                        cmsSize.getAttrValue() == sku.getSize()) {
//
//                    // TODO 根据商品Id，销售属性值Id增加图片
//                    // sku价格(100.0|150.0|100.0|100.0)
//                    sbSkuPrice.append(sku.getPriceRetail());
//                    sbSkuPrice.append(Separtor_Vertical);   //"|"
//
//                    break;
//                }
//            }
//        }


        return false;
    }


    /**
     * 取得前台展示的商家自定义店内分类
     * 如果cms_bt_condition_prop_value表存在条件表达式，优先使用这个条件表达式；
     * 不存在的话，通过360buy.sellercats.get获取店铺分类的parent_id及cid，
     * 按“parent_id-cid"格式传入，同时设置多个以分号（;）分隔即可。
     * 店内分类，格式:206-208;207-208 206(一级)-208(二级);207(一级)-207(一级)
     *
     * @param shop ShopBean 店铺对象
     * @return String 前台展示的商家自定义店内分类
     */
    private String getShopCategory(ShopBean shop) {

        // 多个条件表达式用分号分隔用
        StringBuilder builder = new StringBuilder();
        // 条件表达式表platform_prop_id字段的检索条件为"seller_cids"加cartId
        String platformPropId = "seller_cids_" + shop.getCart_id();

        // 根据channelid和platformPropId取得cms_bt_condition_prop_value表的条件表达式
        List<ConditionPropValueModel> conditionPropValueModels = conditionPropValueRepo.get(shop.getOrder_channel_id(), platformPropId);

        // 优先使用条件表达式
        if (conditionPropValueModels != null && !conditionPropValueModels.isEmpty()) {
            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
                String conditionExpressionStr = conditionPropValueModel.getCondition_expression();
                RuleExpression conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                // ===================expressionParser会被共通函数替换掉================================
//                String propValue = expressionParser.parse(conditionExpression, null);  // TODO 调用共通函数
                String propValue = "";
                // 多个表达式(2392231-4345291格式)用分号分隔
                if (propValue != null) {
                    builder.append(propValue);
                    builder.append(";");
                }
            }
        } else {
            // 获取京东平台前台展示的商家自定义店内分类
            List<ShopCategory> shopCategoryList = jdShopService.getShopCategoryList(shop);
            if (shopCategoryList != null && shopCategoryList.size() > 0) {
                for (ShopCategory shopCategory:shopCategoryList) {
                    // 如果不是父类目的话，加到店内分类里，用分号分隔
                    if (!shopCategory.getParent()) {
                        // 转换成“parent_id-cid"格式，同时设置多个以分号（;）分隔
                        builder.append(String.valueOf(shopCategory.getParentId()));
                        builder.append("-");
                        builder.append(String.valueOf(shopCategory.getCid()));
                        builder.append(";");
                    }
                }
            }
        }
        // 移除最后的分号
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }

        // 店铺种类
        return builder.toString();
    }


    /**
     * 取得操作类型
     * 现只支持：offsale 或onsale,默认为下架状态
     *
     * @param mainProduct CmsBtProductModel 主产品对象
     * @param groupId long groupid
     * @return String 前台操作类型（offsale或onsale）
     */
    private String getOptionType(CmsBtProductModel mainProduct, long groupId) {
        String retOptionType = "";
        CmsBtProductGroupModel mainProductPlatform = mainProduct.getGroups();
        com.voyageone.common.CmsConstants.PlatformActive platformActive = mainProductPlatform.getPlatformActive();

        if (platformActive == com.voyageone.common.CmsConstants.PlatformActive.Instock) {
            // 如果是Instock， 那么offsale
            retOptionType = OptioinType_offsale;
        } else if (platformActive == com.voyageone.common.CmsConstants.PlatformActive.Onsale) {
            // 如果是Onsale， 那么onsale
            retOptionType = OptioinType_onsale;
        }

        return retOptionType;
    }

    /**
     * 取得所有SKU价格的最高价格
     *
     *  //库存为0的sku不参与计算
     *  //如果所有sku库存都为0， 第一个的价格作为商品价格
     *
     *  priceType "retail_price"(市场价)   "sale_price"(京东价)
     */
    private double getItemPrice(List<CmsBtProductModel> sxProducts, String channelId, String cartId, String priceType) {
        // 价格有可能是用priceSale, 也有可能用priceMsrp, 所以需要判断一下
        // priceType:"retail_price"(市场价)  "sale_price"(京东价)
        CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBean(channelId, "PRICE", cartId + "." + priceType);

        // 检查一下
        String sxPricePropName;
        if (sxPriceConfig == null) {
            return 0d;
        } else {
            // 取得价格属性名
            sxPricePropName = sxPriceConfig.getConfigValue1();
            if (StringUtils.isEmpty(sxPricePropName)) {
                return 0d;
            }
        }

        Double resultPrice = 0d;    //, onePrice = 0d;
        List<Double> skuPriceList = new ArrayList<>();
        for (CmsBtProductModel cmsProduct : sxProducts) {
            for (CmsBtProductModel_Sku cmsBtProductModelSku : cmsProduct.getSkus()) {
                // 库存信息不能获取，所以删除
//                int skuQuantity = 0;
//                Integer skuQuantityInteger = skuInventoryMap.get(cmsBtProductModelSku.getSkuCode());
//                if (skuQuantityInteger != null) {
//                    skuQuantity = skuQuantityInteger;
//                }
                double skuPrice = 0;
                skuPrice = cmsBtProductModelSku.getDoubleAttribute(sxPricePropName);
                skuPriceList.add(skuPrice);
//                try {
//                    skuPrice = cmsBtProductModelSku.getDoubleAttribute(sxPricePropName);
//                } catch (Exception e) {
//                    logger.warn("No price for sku " + cmsBtProductModelSku.getSkuCode());
//                }
//                if (onePrice - 0d == 0) {
//                    onePrice = skuPrice;
//                }
//                if (skuQuantity > 0)  {
//                    skuPriceList.add(skuPrice);
//                }
            }
        }

        // 取得所以skuPrice的最大值
        for (double skuPrice : skuPriceList) {
            resultPrice = Double.max(resultPrice, skuPrice);
        }
//        if (resultPrice - 0d == 0) {
//            resultPrice = onePrice;
//        }

        return resultPrice;
    }





    /**
     * 回写group表的商品id
     * 写到group表里， 上传商品时的返回值ware_id -> num_iid
     */
//    private void saveGroupNumIId(CmsBtProductModel mainProduct, long groupId, String wareId, String channelId) {
//
//        // 设置platform信息
//        CmsBtProductModel_Group_Platform cmsPlatform = mainProduct.getGroups().getPlatformByGroupId(groupId);
//        // 京东新增商品返回的商品id
//        cmsPlatform.setNumIId(wareId);
//
//        // 更新group
//        Set<Long> lngSet = new HashSet<>();
//        lngSet.add(mainProduct.getProdId());
//        productGroupService.saveGroups(channelId, lngSet, cmsPlatform);
//        $info(String.format("京东新增商品返回的商品id回写到group表里. cms:group:[ProdId:%s][WareId:%s]", mainProduct.getProdId(), wareId));
//    }

}

package com.voyageone.task2.cms.service.product.sales;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.base.dao.mongodb.model.BulkModelUpdateList;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.logger.VOAbsIssueLoggable;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.dao.cms.mongo.CmsMtProdSalesHisDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从订单历史记录表中统计出指定销量数据
 * 关联表:
 *   mongo: cms_bt_product_cxxx
 *   mongo: cms_mt_prod_sales_his
 *
 * @author jason.jiang on 2016/05/24
 * @version 2.0.0
 */
@Service
public class CmsSumGroupOrdersService extends VOAbsIssueLoggable {

    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private CmsMtProdSalesHisDao cmsMtProdSalesHisDao;

    private final static int PAGE_LIMIT = 500;
    private static String queryGrpStr = "{$match:{'date':{$gte:#,$lte:#},'cart_id':#,'channel_id':#,'prodCode':{$in:#}}}";
    private static String queryGrpStr2 = "{$match:{'cart_id':#,'channel_id':#,'prodCode':{$in:#}}}";
    private static String queryGrpStr3 = "{$group:{_id:{cart_id:'$cart_id',channel_id:'$channel_id'},count:{$sum:'$qty'}}}";

    private static String queryCartStr = "{$match:{'date':{$gte:#,$lte:#},'channel_id':#,'prodCode':{$in:#}}}";
    private static String queryCartStr2 = "{$match:{'channel_id':#,'prodCode':{$in:#}}}";
    private static String queryCartStr3 = "{$group:{_id:'$channel_id',count:{$sum:'$qty'}}}";

    /**
     * 统计group级(各个cart)的销售数据，直接从[cms_mt_prod_sales_his]统计
     */
    public void sumPerCartGroupOrders(List<CmsBtProductGroupModel> list, String channelId, String begDate1, String begDate2, String endDate, String taskName) {
        BulkModelUpdateList bulkList = new BulkModelUpdateList(PAGE_LIMIT, cmsBtProductGroupDao, channelId);
        for (CmsBtProductGroupModel grpObj : list) {
            List<String> codeList = grpObj.getProductCodes();
            if (codeList == null || codeList.isEmpty()) {
                $warn(String.format("CmsSumGroupOrdersService:sumPerCartGroupOrders 该group无产品code数据！ channel_id=%s groupId=%d", channelId, grpObj.getGroupId()));
                continue;
            }

            Map<String, Object> salesMap = new HashMap<>();

            // 7天销售group级数据
            Object[] params = new Object[]{begDate1, endDate, grpObj.getCartId(), channelId, codeList};
            List<Map<String, Object>> amt7days = cmsMtProdSalesHisDao.aggregateToMap(new JongoAggregate(queryGrpStr, params), new JongoAggregate(queryGrpStr3));
            if (amt7days.isEmpty()) {
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_7, 0);
            } else {
                int qty = ((Number) amt7days.get(0).get("count")).intValue();
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_7, qty);
            }

            // 30天销售group级数据
            params = new Object[]{begDate2, endDate, grpObj.getCartId(), channelId, codeList};
            List<Map<String, Object>> amt30days = cmsMtProdSalesHisDao.aggregateToMap(new JongoAggregate(queryGrpStr, params), new JongoAggregate(queryGrpStr3));
            if (amt30days.isEmpty()) {
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_30, 0);
            } else {
                int qty = ((Number) amt30days.get(0).get("count")).intValue();
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_30, qty);
            }

            // 所有销售group级数据
            params = new Object[]{grpObj.getCartId(), channelId, codeList};
            List<Map<String, Object>> amtall = cmsMtProdSalesHisDao.aggregateToMap(new JongoAggregate(queryGrpStr2, params), new JongoAggregate(queryGrpStr3));
            if (amtall.isEmpty()) {
                $debug(String.format("CmsFindProdOrdersInfoService 该产品group无销售数据！ + channel_id=%s, cart_id=%d, groupId=%d", channelId, grpObj.getCartId(), grpObj.getGroupId()));
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, 0);
            } else {
                int qty = ((Number) amtall.get(0).get("count")).intValue();
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, qty);
            }

            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("groupId", grpObj.getGroupId());
            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("sales", salesMap);
            updateMap.put("modifier", taskName);
            updateMap.put("modified", DateTimeUtil.getNow());

            BulkUpdateModel updModel = new BulkUpdateModel();
            updModel.setQueryMap(queryMap);
            updModel.setUpdateMap(updateMap);
            // 批量更新
            BulkWriteResult rs = bulkList.addBulkModel(updModel);
            if (rs != null) {
                $debug(String.format("更新group 店铺%s 执行结果1 %s", channelId, rs.toString()));
            }
        } // end for group list

        // 批量更新
        BulkWriteResult rs = bulkList.execute();
        if (rs != null) {
            $debug(String.format("更新group 店铺%s 执行结果2 %s", channelId, rs.toString()));
        }
    }

    /**
     * 统计group级(cart合计)的销售数据，直接从[cms_mt_prod_sales_his]统计
     */
    public void sumAllCartGroupOrders(List<CmsBtProductGroupModel> list, String channelId, String begDate1, String begDate2, String endDate, String taskName) {
        BulkModelUpdateList bulkList = new BulkModelUpdateList(PAGE_LIMIT, cmsBtProductGroupDao, channelId);
        for (CmsBtProductGroupModel grpObj : list) {
            List<String> codeList = grpObj.getProductCodes();
            if (codeList == null || codeList.isEmpty()) {
                $warn(String.format("CmsSumGroupOrdersService:sumAllCartGroupOrders 该group无产品code数据！ channel_id=%s groupId=%d", channelId, grpObj.getGroupId()));
                continue;
            }

            Map<String, Object> salesMap = new HashMap<>();

            // 7天销售group级数据
            Object[] params = new Object[] { begDate1, endDate, channelId, codeList };
            List<Map<String, Object>> amt7days = cmsMtProdSalesHisDao.aggregateToMap(new JongoAggregate(queryCartStr, params), new JongoAggregate(queryCartStr3));
            if (amt7days.isEmpty()) {
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_7, 0);
            } else {
                int qty = ((Number) amt7days.get(0).get("count")).intValue();
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_7, qty);
            }

            // 30天销售group级数据
            params = new Object[] { begDate2, endDate, channelId, codeList };
            List<Map<String, Object>> amt30days = cmsMtProdSalesHisDao.aggregateToMap(new JongoAggregate(queryCartStr, params), new JongoAggregate(queryCartStr3));
            if (amt30days.isEmpty()) {
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_30, 0);
            } else {
                int qty = ((Number) amt30days.get(0).get("count")).intValue();
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_30, qty);
            }

            // 所有销售group级数据
            params = new Object[] { channelId, codeList };
            List<Map<String, Object>> amtall = cmsMtProdSalesHisDao.aggregateToMap(new JongoAggregate(queryCartStr2, params), new JongoAggregate(queryCartStr3));
            if (amtall.isEmpty()) {
                $debug(String.format("CmsFindProdOrdersInfoService 该产品group无销售数据！ + channel_id=%s, groupId=%d", channelId, grpObj.getGroupId()));
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, 0);
            } else {
                int qty = ((Number) amtall.get(0).get("count")).intValue();
                salesMap.put(CmsBtProductModel_Sales.CODE_SUM_ALL, qty);
            }

            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("groupId", grpObj.getGroupId());
            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("sales", salesMap);
            updateMap.put("modifier", taskName);
            updateMap.put("modified", DateTimeUtil.getNow());

            BulkUpdateModel updModel = new BulkUpdateModel();
            updModel.setQueryMap(queryMap);
            updModel.setUpdateMap(updateMap);
            // 批量更新
            BulkWriteResult rs = bulkList.addBulkModel(updModel);
            if (rs != null) {
                $debug(String.format("更新group(合计) 店铺%s 执行结果1 %s", channelId, rs.toString()));
            }
        } // end for group list

        // 批量更新
        BulkWriteResult rs = bulkList.execute();
        if (rs != null) {
            $debug(String.format("更新group(合计) 店铺%s 执行结果2 %s", channelId, rs.toString()));
        }
    }
}

package com.voyageone.task2.cms.service.product;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JomgoAggregate;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.logger.VOAbsIssueLoggable;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.dao.cms.mongo.CmsMtProdSalesHisDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class CmsSumProdOrdersService extends VOAbsIssueLoggable {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private CmsMtProdSalesHisDao cmsMtProdSalesHisDao;

    private final static int PAGE_LIMIT = 500;
    private static String queryStr = "{$match:{'date':{$gte:#,$lte:#},'cart_id':{$in:#},'channel_id':#,'sku':{$in:#}}}";
    private static String queryStr2 = "{$match:{'cart_id':{$in:#},'channel_id':#,'sku':{$in:#}}}";
    private static String queryStr3 = "{$group:{_id:{cart_id:'$cart_id',channel_id:'$channel_id',sku:'$sku'},count:{$sum:'$qty'}}}";

    private static String queryCodeStr = "{$match:{'date':{$gte:#,$lte:#},'cart_id':{$in:#},'channel_id':#,'prodCode':#}}";
    private static String queryCodeStr2 = "{$match:{'cart_id':{$in:#},'channel_id':#,'prodCode':#}}";
    private static String queryCodeStr3 = "{$group:{_id:{cart_id:'$cart_id',channel_id:'$channel_id',prodCode:'$prodCode'},count:{$sum:'$qty'}}}";

    /**
     * 统计产品的销售数据
     */
    public void sumProdOrders(List<CmsBtProductModel> prodList, String channelId, String begDate1, String begDate2, String endDate, String taskName) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        for (CmsBtProductModel prodObj : prodList) {
            // 对每个产品统计其sku数据
            String prodCode = prodObj.getFields().getCode();
            List<CmsBtProductModel_Sku> skusList = prodObj.getSkus();
            if (skusList == null || skusList.isEmpty()) {
                $warn(String.format("CmsFindProdOrdersInfoService 该产品无sku数据！ + channel_id=%s, code=%s", channelId, prodCode));
                continue;
            }

            List<Integer> cartList = new ArrayList<>();
            List<String> skuCodeList = new ArrayList<>();
            skusList.forEach(skuObj -> {
                skuCodeList.add(skuObj.getSkuCode());
                cartList.addAll(skuObj.getSkuCarts());
            });
            List<Integer> cartList2 = cartList.stream().distinct().collect(Collectors.toList());
            if (cartList.isEmpty() || cartList2.isEmpty()) {
                continue;
            }

            Map salesMap = new HashMap<>();
            List<Map> skuSum7List = new ArrayList<>();
            List<Map> skuSum30List = new ArrayList<>();
            List<Map> skuSumAllList = new ArrayList<>();

            // 7天销售sku数据
            Object[] params = new Object[]{begDate1, endDate, cartList2, channelId, skuCodeList};
            List<Map> amt7days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryStr, params), new JomgoAggregate(queryStr3, null));
            if (!amt7days.isEmpty()) {
                Map sum7Map = new HashMap<>();
                for (Map hisInfo : amt7days) {
                    int qty = ((Number) hisInfo.get("count")).intValue();
                    Map groupKey = (Map) hisInfo.get("_id");
                    sum7Map.put("cartId_" + groupKey.get("cart_id"), qty);

                    Map skuSalesMap = new HashMap<>();
                    skuSalesMap.put("skuCode", groupKey.get("sku"));
                    skuSalesMap.put("cartId", groupKey.get("cart_id"));
                    skuSalesMap.put("code_sum_7", qty);
                    skuSum7List.add(skuSalesMap);
                }
            }

            // 30天销售sku数据
            params = new Object[]{begDate2, endDate, cartList2, channelId, skuCodeList};
            List<Map> amt30days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryStr, params), new JomgoAggregate(queryStr3, null));
            if (!amt30days.isEmpty()) {
                Map sum30Map = new HashMap<>();
                for (Map hisInfo : amt30days) {
                    int qty = ((Number) hisInfo.get("count")).intValue();
                    Map groupKey = (Map) hisInfo.get("_id");
                    sum30Map.put("cartId_" + groupKey.get("cart_id"), qty);

                    Map skuSalesMap = new HashMap<>();
                    skuSalesMap.put("skuCode", groupKey.get("sku"));
                    skuSalesMap.put("cartId", groupKey.get("cart_id"));
                    skuSalesMap.put("code_sum_30", qty);
                    skuSum30List.add(skuSalesMap);
                }
            }

            // 所有销售sku数据
            params = new Object[]{cartList2, channelId, skuCodeList};
            List<Map> amtall = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryStr2, params), new JomgoAggregate(queryStr3, null));
            if (amtall.isEmpty()) {
                $debug(String.format("CmsFindProdOrdersInfoService 该产品无销售数据！ + channel_id=%s, code=%s", channelId, prodCode));
                for (String skuCode : skuCodeList) {
                    for (Integer cartId : cartList2) {
                        Map skuSalesMap = new HashMap<>();
                        skuSalesMap.put("skuCode", skuCode);
                        skuSalesMap.put("cartId", cartId);
                        skuSalesMap.put("code_sum_all", 0);
                        skuSumAllList.add(skuSalesMap);
                    }
                }
            } else {
                Map sumallMap = new HashMap<>();
                for (Map hisInfo : amtall) {
                    int qty = ((Number) hisInfo.get("count")).intValue();
                    Map groupKey = (Map) hisInfo.get("_id");
                    sumallMap.put("cartId_" + groupKey.get("cart_id"), qty);

                    Map skuSalesMap = new HashMap<>();
                    skuSalesMap.put("skuCode", groupKey.get("sku"));
                    skuSalesMap.put("cartId", groupKey.get("cart_id"));
                    skuSalesMap.put("code_sum_all", qty);
                    skuSumAllList.add(skuSalesMap);
                }
            }

            // 合并sku销售数据
            for (Map sumInfo : skuSumAllList) {
                for (Map sum7Info : skuSum7List) {
                    if (sumInfo.get("skuCode").equals(sum7Info.get("skuCode")) && sumInfo.get("cartId").equals(sum7Info.get("cartId"))) {
                        sumInfo.put("code_sum_7", sum7Info.get("code_sum_7"));
                    }
                }
                for (Map sum30Info : skuSum30List) {
                    if (sumInfo.get("skuCode").equals(sum30Info.get("skuCode")) && sumInfo.get("cartId").equals(sum30Info.get("cartId"))) {
                        sumInfo.put("code_sum_30", sum30Info.get("code_sum_30"));
                    }
                }
            }

            List<Map> skuCartSumList = new ArrayList<>();
            Map<String, String> skuCodeMap = new HashMap<>();
            for (Map sumInfo : skuSumAllList) {
                skuCodeMap.put((String) sumInfo.get("skuCode"), "");
                if (sumInfo.get("code_sum_7") == null) {
                    sumInfo.put("code_sum_7", 0);
                }
                if (sumInfo.get("code_sum_30") == null) {
                    sumInfo.put("code_sum_30", 0);
                }
                if (sumInfo.get("code_sum_all") == null) {
                    sumInfo.put("code_sum_all", 0);
                }
            }

            // 再统计产品code级别的数据，由于是多维度的统计，由上面的sku数据合并较复杂，不如直接统计
            // 7天销售code数据
            params = new Object[]{begDate1, endDate, cartList2, channelId, prodCode};
            amt7days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryCodeStr, params), new JomgoAggregate(queryCodeStr3, null));
            if (!amt7days.isEmpty()) {
                Map sum7Map = new HashMap<>();
                int sum7 = 0;
                for (Map hisInfo : amt7days) {
                    int qty = ((Number) hisInfo.get("count")).intValue();
                    sum7 += qty;
                    Map groupKey = (Map) hisInfo.get("_id");
                    sum7Map.put("cartId_" + groupKey.get("cart_id"), qty);
                }
                sum7Map.put("cartId_0", sum7);
                salesMap.put("code_sum_7", sum7Map);
            }

            // 30天销售code数据
            params = new Object[]{begDate2, endDate, cartList2, channelId, prodCode};
            amt30days = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryCodeStr, params), new JomgoAggregate(queryCodeStr3, null));
            if (!amt30days.isEmpty()) {
                Map sum30Map = new HashMap<>();
                int sum30 = 0;
                for (Map hisInfo : amt30days) {
                    int qty = ((Number) hisInfo.get("count")).intValue();
                    sum30 += qty;
                    Map groupKey = (Map) hisInfo.get("_id");
                    sum30Map.put("cartId_" + groupKey.get("cart_id"), qty);
                }
                sum30Map.put("cartId_0", sum30);
                salesMap.put("code_sum_30", sum30Map);
            }

            // 所有销售code数据
            params = new Object[]{cartList2, channelId, prodCode};
            amtall = cmsMtProdSalesHisDao.aggregateToMap(new JomgoAggregate(queryCodeStr2, params), new JomgoAggregate(queryCodeStr3, null));
            if (amtall.isEmpty()) {
                Map sumallMap = new HashMap<>();
                for (Integer cartItem : cartList2) {
                    sumallMap.put("cartId_" + cartItem, 0);
                }
                sumallMap.put("cartId_0", 0);
                salesMap.put("code_sum_all", sumallMap);
            } else {
                Map sumallMap = new HashMap<>();
                int sumall = 0;
                for (Map hisInfo : amtall) {
                    int qty = ((Number) hisInfo.get("count")).intValue();
                    sumall += qty;
                    Map groupKey = (Map) hisInfo.get("_id");
                    sumallMap.put("cartId_" + groupKey.get("cart_id"), qty);
                }
                sumallMap.put("cartId_0", sumall);
                salesMap.put("code_sum_all", sumallMap);
            }

            // sku合计
            for (String skuCode : skuCodeMap.keySet()) {
                int skuSum7 = 0;
                int skuSum30 = 0;
                int skuSumAll = 0;
                for (Map sumInfo : skuSumAllList) {
                    if (skuCode.equals(sumInfo.get("skuCode"))) {
                        skuSum7 += StringUtils.toIntValue((Integer) sumInfo.get("code_sum_7"));
                        skuSum30 += StringUtils.toIntValue((Integer) sumInfo.get("code_sum_30"));
                        skuSumAll += StringUtils.toIntValue((Integer) sumInfo.get("code_sum_all"));
                    }
                }
                Map<String, Object> skuMap = new HashMap<>();
                skuMap.put("skuCode", skuCode);
                skuMap.put("cartId", 0);
                skuMap.put("code_sum_7", skuSum7);
                skuMap.put("code_sum_30", skuSum30);
                skuMap.put("code_sum_all", skuSumAll);
                skuCartSumList.add(skuMap);
            }
            skuSumAllList.addAll(skuCartSumList);
            salesMap.put("skus", skuSumAllList);

            // 没有的数据补零
            Map sum7Map = (Map) salesMap.get("code_sum_7");
            if (sum7Map == null) {
                sum7Map = new HashMap<>();
                for (Integer cartItem : cartList2) {
                    sum7Map.put("cartId_" + cartItem, 0);
                }
                salesMap.put("code_sum_7", sum7Map);
            } else {
                for (Integer cartItem : cartList2) {
                    Integer qty = (Integer) sum7Map.get("cartId_" + cartItem);
                    if (qty == null) {
                        sum7Map.put("cartId_" + cartItem, 0);
                    }
                }
            }
            Integer qty0 = (Integer) sum7Map.get("cartId_0");
            if (qty0 == null) {
                sum7Map.put("cartId_0", 0);
            }
            Map sum30Map = (Map) salesMap.get("code_sum_30");
            if (sum30Map == null) {
                sum30Map = new HashMap<>();
                for (Integer cartItem : cartList2) {
                    sum30Map.put("cartId_" + cartItem, 0);
                }
                salesMap.put("code_sum_30", sum30Map);
            } else {
                for (Integer cartItem : cartList2) {
                    Integer qty = (Integer) sum30Map.get("cartId_" + cartItem);
                    if (qty == null) {
                        sum30Map.put("cartId_" + cartItem, 0);
                    }
                }
            }
            qty0 = (Integer) sum30Map.get("cartId_0");
            if (qty0 == null) {
                sum30Map.put("cartId_0", 0);
            }
            Map sumAllMap = (Map) salesMap.get("code_sum_all");
            if (sumAllMap == null) {
                sumAllMap = new HashMap<>();
                for (Integer cartItem : cartList2) {
                    sumAllMap.put("cartId_" + cartItem, 0);
                }
                salesMap.put("code_sum_all", sumAllMap);
            } else {
                for (Integer cartItem : cartList2) {
                    Integer qty = (Integer) sumAllMap.get("cartId_" + cartItem);
                    if (qty == null) {
                        sumAllMap.put("cartId_" + cartItem, 0);
                    }
                }
            }
            qty0 = (Integer) sumAllMap.get("cartId_0");
            if (qty0 == null) {
                sumAllMap.put("cartId_0", 0);
            }

            Map queryMap = new HashMap<>();
            queryMap.put("fields.code", prodCode);
            Map updateMap = new HashMap<>();
            updateMap.put("sales", salesMap);
            updateMap.put("modifier", taskName);
            updateMap.put("modified", DateTimeUtil.getNow());

            BulkUpdateModel updModel = new BulkUpdateModel();
            updModel.setQueryMap(queryMap);
            updModel.setUpdateMap(updateMap);
            bulkList.add(updModel);

            // 批量更新
            if (!bulkList.isEmpty() && bulkList.size() % PAGE_LIMIT == 0) {
                BulkWriteResult rs = cmsBtProductGroupDao.bulkUpdateWithMap(channelId, bulkList, taskName, "$set", false);
                $debug(String.format("更新product 店铺%s 执行数 %d, 执行结果 %s", channelId, bulkList.size(), rs.toString()));
                bulkList = new ArrayList<>();
            }
        } // end for product list

        // 批量更新
        if (!bulkList.isEmpty()) {
            BulkWriteResult rs = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, taskName, "$set");
            $debug(String.format("更新product 店铺%s 执行数 %d, 执行结果 %s", channelId, bulkList.size(), rs.toString()));
        }
    }

}
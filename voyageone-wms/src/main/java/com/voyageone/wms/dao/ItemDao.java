package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.wms.modelbean.ItemDetailBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 5/4/2015.
 * Modified by Joans on 7/14/2015
 *
 * @author Jonas
 */
@Repository
public class ItemDao extends BaseDao {
    @Override
    protected String namespace() {
        return "com.voyageone.wms.sql";
    }

    /**
     * 查询商品的 SKU
     *
     * @param channel_id 目标渠道
     * @param barcode    商品 Barcode
     * @return String
     */
    public String getSku(String channel_id, String barcode) {
        Map<String, Object> params = new HashMap<>();

        params.put("channel_id", channel_id);
        params.put("barcode", barcode);

        return selectOne("wms_bt_item_details_get_sku", params);
    }

    /**
     * 根据 Code 或 Barcode 匹配商品，获取匹配的商品 Code 列表
     *
     * @param code             Code 或 Barcode
     * @param order_channel_id 目标渠道
     * @return List
     */
    public List<String> searchItemCode(String code, String order_channel_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("code", code);
        params.put("order_channel_id", order_channel_id);

        return selectList("wms_bt_item_details_search_item_code", params);
    }

    /**
     * 获取渠道下，该 Code (wms_bt_item_detail.itemcode) 的商品总数
     *
     * @param order_channel_id 目标渠道
     * @param code             商品 Code
     * @return int
     */
    public int countByCode(String order_channel_id, String code) {
        Map<String, Object> params = new HashMap<>();

        params.put("code", code);
        params.put("order_channel_id", order_channel_id);

        return selectOne("wms_bt_item_details_count_by_code", params);
    }

    /**
     * 获取，渠道内的所有商品
     *
     * @param code             商品 Code （itemcode 字段）
     * @param order_channel_id 渠道 ID
     * @return List/ ItemDetailBean
     */
    public List<ItemDetailBean> selectItemDetails(String code, String order_channel_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("code", code);
        params.put("order_channel_id", order_channel_id);

        return selectList("wms_bt_item_details_selectItemDetails", params);
    }

    public ItemDetailBean getItemDetail(String order_channel_id, String item_code, String size) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("item_code", item_code);
        params.put("size", size);
        return selectOne("wms_bt_item_details_getItemDetail", params);
    }

    public int insertItemDetail(ItemDetailBean itemDetailBean) {

        return updateTemplate.insert("wms_bt_item_details_insertItemDetail", itemDetailBean);
    }

    public int updateItemDetailBarcode(ItemDetailBean itemDetailBean) {

        return updateTemplate.update("wms_bt_item_details_updateItemDetailBarcode", itemDetailBean);
    }
}

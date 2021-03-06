package com.voyageone.task2.base.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.task2.base.modelbean.SetPriceBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fred on 2015/8/10.
 */
@Repository
public class SetPriceDao extends BaseDao {
    
    /**
     * //出库订单基本价格数据取得（大礼包解决方案）
     * @param order_channel_id
     * @param order_number
     *
     * @return List<SpaldingPriceBean>
     */
    public List<SetPriceBean> getVirtualPriceData(String order_number,String order_channel_id,String cart_id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("order_channel_id", order_channel_id);
        paramMap.put("order_number",order_number);
        paramMap.put("cart_id",cart_id);
        return selectList(Constants.DAO_NAME_SPACE_CORE + "getVirtualPriceData", paramMap);
    }

    /**
     * //出库订单基本价格数据取得
     * @param order_channel_id
     * @param order_number
     *
     * @return List<SpaldingPriceBean>
     */
    public List<SetPriceBean> getPriceData(String order_number,String order_channel_id,String cart_id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("order_channel_id", order_channel_id);
        paramMap.put("order_number",order_number);
        paramMap.put("cart_id",cart_id);
        return selectList(Constants.DAO_NAME_SPACE_CORE + "getPriceData", paramMap);
    }


    /**
     * //退货订单基本价格数据取得
     * @param order_channel_id
     * @param order_number
     *
     * @return List<SpaldingPriceBean>
     */
    public List<SetPriceBean> getReturnedPriceData(String order_number,String order_channel_id,String cart_id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("order_channel_id", order_channel_id);
        paramMap.put("order_number",order_number);
        paramMap.put("cart_id",cart_id);
        return selectList(Constants.DAO_NAME_SPACE_CORE + "getReturnedPriceData", paramMap);
    }

    /**
     * //出库订单基本价格数据取得（不含运费，正常订单）
     * @param order_channel_id
     * @param order_number
     *
     * @return List<SpaldingPriceBean>
     */
    public List<SetPriceBean> getPriceDataNotIncludeShipping(String order_number,String order_channel_id,String cart_id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("order_channel_id", order_channel_id);
        paramMap.put("order_number",order_number);
        paramMap.put("cart_id",cart_id);
        return selectList(Constants.DAO_NAME_SPACE_CORE + "getPriceDataNotShipping", paramMap);
    }

    /**
     * //出库订单基本价格数据取得（不含运费，Cancel,Return 订单）
     * @param order_channel_id
     * @param order_number
     *
     * @return List<SpaldingPriceBean>
     */
    public List<SetPriceBean> getPriceDataNotIncludeShippingForCancelOrReturn(String order_number,String order_channel_id,String cart_id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("order_channel_id", order_channel_id);
        paramMap.put("order_number",order_number);
        paramMap.put("cart_id",cart_id);
        return selectList(Constants.DAO_NAME_SPACE_CORE + "getPriceDataNotShippingForCancelOrReturn", paramMap);
    }

    /**
     * //出库订单基本价格数据取得（含运费，正常订单）
     * @param order_channel_id
     * @param order_number
     *
     * @return List<SpaldingPriceBean>
     */
    public List<SetPriceBean> getPriceDataIncludeShipping(String order_number,String order_channel_id,String cart_id) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("order_channel_id", order_channel_id);
        paramMap.put("order_number",order_number);
        paramMap.put("cart_id",cart_id);
        return selectList(Constants.DAO_NAME_SPACE_CORE + "getPriceDataIncludeShipping", paramMap);
    }
}

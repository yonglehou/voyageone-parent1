package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/10/29.
 */
@Repository
public class PromotionDao extends BaseDao{
    /**
     * 取得需要添加折扣的产品列表
     * @param channelId
     * @param cardId
     * @return
     */
    public List<Map> getPromotionItem(String channelId, String cartId, String priceField){
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("channelId",channelId);
        parameter.put("cartId", cartId);
        parameter.put("price", priceField);

        List<Map> data = selectList("ims_promotion_select", parameter);

        if (data == null){
            data = new ArrayList<>();
        }
        return data;
    }

    public int updatePromotionStatus(Map<String,Object> parameter){
        return update("ims_promotion_update", parameter);
    }
}

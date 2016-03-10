package com.voyageone.web2.cms.wsdl.dao;

import com.voyageone.web2.cms.wsdl.models.CmsBtTaskModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jonasvlag on 16/2/29.
 * @version 2.0.0
 */
@Repository
public class CmsBtTaskDao extends WsdlBaseDao {

    public int insert(CmsBtTaskModel model) {
        return insert("cms_bt_tasks_insert", model);
    }

    /**
     * 更新数据, 只更新 Config 和 Name
     */
    public int update(CmsBtTaskModel model) {
        return update("cms_bt_tasks_update", model);
    }

    public CmsBtTaskModel selectByIdWithPromotion(int task_id) {
        return selectOne("cms_bt_tasks_select", parameters("task_id", task_id));
    }

    public List<CmsBtTaskModel> selectByName(int promotion_id, String task_name, int task_type) {
        return selectList("cms_bt_tasks_select", parameters(
                "promotion_id", promotion_id,
                "task_name", task_name,
                "task_type", task_type+""));
    }

    public List<CmsBtTaskModel> selectTaskWithPromotionByChannel(Map<String,Object> searchInfo) {
        return selectList("cms_bt_tasks_select", searchInfo);
    }
}

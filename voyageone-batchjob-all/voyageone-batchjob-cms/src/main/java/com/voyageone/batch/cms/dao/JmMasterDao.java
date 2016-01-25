package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.bean.JmMasterBean;
import com.voyageone.common.components.jumei.Bean.JmCategoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Repository
public class JmMasterDao extends BaseDao {


    public int insertJmMaster(List<JmMasterBean> jmMasterBeanList){
        return updateTemplate.insert("insert_jm_mt_master", jmMasterBeanList);
    }

    public int clearJmMasterByCode(String code){
        return updateTemplate.delete("delete_jm_mt_master",code);
    }
}

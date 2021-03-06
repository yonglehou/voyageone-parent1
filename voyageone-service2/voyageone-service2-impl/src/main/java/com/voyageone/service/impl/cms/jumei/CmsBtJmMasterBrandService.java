package com.voyageone.service.impl.cms.jumei;

import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.components.jumei.bean.JmBrandBean;
import com.voyageone.components.jumei.service.JumeiBrandService;
import com.voyageone.service.dao.cms.CmsBtJmMasterBrandDao;
import com.voyageone.service.daoext.cms.CmsBtJmMasterBrandDaoExt;
import com.voyageone.service.model.cms.CmsBtJmMasterBrandModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class CmsBtJmMasterBrandService {
    @Autowired
    JumeiBrandService serviceJumeiBrand;
    @Autowired
    TransactionRunner transactionRunner;
    @Autowired
    CmsBtJmMasterBrandDao dao;
    @Autowired
    CmsBtJmMasterBrandDaoExt daoExtCmsBtJmMasterBrand;
    @Autowired
    JMShopBeanService serviceJMShopBean;
    public void loadJmMasterBrand(String userName,String channelId) throws Exception {
        List<CmsBtJmMasterBrandModel> listCmsBtJmMasterBrand = new ArrayList<>();
        List<JmBrandBean> list = serviceJumeiBrand.getBrands(serviceJMShopBean.getShopBean(channelId));
        CmsBtJmMasterBrandModel model = null;
        for (JmBrandBean bean : list) {
            model = daoExtCmsBtJmMasterBrand.selectByJmMasterBrandId(bean.getId());
            if (model == null) {
                model = new CmsBtJmMasterBrandModel();
            }
            listCmsBtJmMasterBrand.add(model);
            model.setJmMasterBrandId(bean.getId());
            model.setEnName(bean.getEnName());
            model.setName(bean.getName());
            model.setCreated(new Date());
            model.setCreater(userName);
            model.setModifier(userName);
        }
        transactionRunner.runWithTran(() -> {
            saveList(listCmsBtJmMasterBrand);                             //保存
        });
    }

    public void saveList(List<CmsBtJmMasterBrandModel> listCmsBtJmMasterBrand) {
        for (CmsBtJmMasterBrandModel model : listCmsBtJmMasterBrand) {
            if (model.getId() > 0) {
                dao.update(model);
            } else {
                dao.insert(model);
            }
        }
    }

    /**
     * 返回聚美所有的brand数据.
     * @return
     */
    public List<CmsBtJmMasterBrandModel> selectAll() {
        return dao.selectList(new HashMap<>());
    }
    
    /**
     * 删除全部的品牌数据
     */
	public void deleteAll() {
		daoExtCmsBtJmMasterBrand.deleteAll();
	}
	
}

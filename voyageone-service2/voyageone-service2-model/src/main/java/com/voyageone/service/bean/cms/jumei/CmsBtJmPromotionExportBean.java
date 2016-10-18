package com.voyageone.service.bean.cms.jumei;

import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSpecialExtensionModel;

/**
 * Created by james on 2016/10/17.
 */
public class CmsBtJmPromotionExportBean {
    private CmsBtJmPromotionModel model;
    private CmsBtJmPromotionSpecialExtensionModel extModel;

    public CmsBtJmPromotionExportBean(CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean){
        this.model = cmsBtJmPromotionSaveBean.getModel();
        this.extModel = cmsBtJmPromotionSaveBean.getExtModel();
    }

    public CmsBtJmPromotionModel getModel() {
        return model;
    }

    public void setModel(CmsBtJmPromotionModel model) {
        this.model = model;
    }

    public CmsBtJmPromotionSpecialExtensionModel getExtModel() {
        return extModel;
    }

    public void setExtModel(CmsBtJmPromotionSpecialExtensionModel extModel) {
        this.extModel = extModel;
    }

    public String getBrandString(){
        return String.format("%s (%s)",this.model.getCmsBtJmMasterBrandId(), this.model.getBrand());
    }
    public String getSyncMobile(){
        return getBooleanString(this.extModel.getSyncMobile());
    }
    public String getShowHiddenDeal(){
        return getBooleanString(this.extModel.getShowHiddenDeal());
    }
    public String getShowSoldOutDeal(){
        return getBooleanString(this.extModel.getShowSoldOutDeal());
    }
    private String getBooleanString(Boolean value){
        if(value){
            return "是";
        }else if(value){
            return "否";
        }else{
            return "";
        }
    }
}

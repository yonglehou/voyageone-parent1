package com.voyageone.service.model.jumei.businessmodel;

import com.voyageone.service.model.jumei.CmsBtJmProductModel;
import com.voyageone.service.model.jumei.CmsBtJmPromotionProductModel;

/**
 * Created by dell on 2016/4/12.
 */
public class JMUpdateProductWithPromotionInfo {
    CmsBtJmPromotionProductModel cmsBtJmPromotionProductModel;
    CmsBtJmProductModel cmsBtJmProductModel;

    public CmsBtJmProductModel getCmsBtJmProductModel() {
        return cmsBtJmProductModel;
    }

    public void setCmsBtJmProductModel(CmsBtJmProductModel cmsBtJmProductModel) {
        this.cmsBtJmProductModel = cmsBtJmProductModel;
    }

    public CmsBtJmPromotionProductModel getCmsBtJmPromotionProductModel() {
        return cmsBtJmPromotionProductModel;
    }

    public void setCmsBtJmPromotionProductModel(CmsBtJmPromotionProductModel cmsBtJmPromotionProductModel) {
        this.cmsBtJmPromotionProductModel = cmsBtJmPromotionProductModel;
    }
}

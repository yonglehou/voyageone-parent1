/*
 * CmsMtCommonPropModel.java
 * Copyright(C) 20xx-2015 Voyageonone Group Inc.
 * All rights reserved.
 * -----------------------------------------------
 * 2016-05-05 Created
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 *
 */
public class CmsMtCommonPropModel extends BaseModel {
    private String propId;

    private String propParentId;

    private String propName;

    private String propType;

    /**
     * 0:add 1:del 2:自动改名顺序+1
     */
    private Integer actionType;

    private String platformPropRefId;

    /**
     * 1:必须 0:非必须
     */
    private String rules;

    private String defult;

    private Integer isComm;

    private Integer isCode;

    /**
     * 提示信息
     */
    private String tips;

    private String valueType;

    public String getPropId() {
        return propId;
    }

    public void setPropId(String propId) {
        this.propId = propId == null ? null : propId.trim();
    }

    public String getPropParentId() {
        return propParentId;
    }

    public void setPropParentId(String propParentId) {
        this.propParentId = propParentId == null ? null : propParentId.trim();
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName == null ? null : propName.trim();
    }

    public String getPropType() {
        return propType;
    }

    public void setPropType(String propType) {
        this.propType = propType == null ? null : propType.trim();
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public String getPlatformPropRefId() {
        return platformPropRefId;
    }

    public void setPlatformPropRefId(String platformPropRefId) {
        this.platformPropRefId = platformPropRefId == null ? null : platformPropRefId.trim();
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules == null ? null : rules.trim();
    }

    public String getDefult() {
        return defult;
    }

    public void setDefult(String defult) {
        this.defult = defult == null ? null : defult.trim();
    }

    public Integer getIsComm() {
        return isComm;
    }

    public void setIsComm(Integer isComm) {
        this.isComm = isComm;
    }

    public Integer getIsCode() {
        return isCode;
    }

    public void setIsCode(Integer isCode) {
        this.isCode = isCode;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips == null ? null : tips.trim();
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType == null ? null : valueType.trim();
    }
}
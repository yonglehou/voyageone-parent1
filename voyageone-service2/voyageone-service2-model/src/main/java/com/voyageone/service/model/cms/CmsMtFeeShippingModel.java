/*
 * CmsMtFeeShippingModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 
 */
public class CmsMtFeeShippingModel extends BaseModel {
    /**
     * 发货方式
     */
    protected String shippingType;

    /**
     * 发货方式全称
     */
    protected String shippingTypeName;

    /**
     * 计费方式: 0, 记重；1, 计件
     */
    protected Byte feeType;

    /**
     * 首重
     */
    protected Integer firstWeight;

    /**
     * 首重价格
     */
    protected Double firstWeightFee;

    /**
     * 续重
     */
    protected Integer additionalWeight;

    /**
     * 续重单价
     */
    protected Double additionalWeightFee;

    /**
     * 计重单位
     */
    protected String weightUnit;

    /**
     * 计件单价
     */
    protected Double pcFee;

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType == null ? null : shippingType.trim();
    }

    public String getShippingTypeName() {
        return shippingTypeName;
    }

    public void setShippingTypeName(String shippingTypeName) {
        this.shippingTypeName = shippingTypeName == null ? null : shippingTypeName.trim();
    }

    public Byte getFeeType() {
        return feeType;
    }

    public void setFeeType(Byte feeType) {
        this.feeType = feeType;
    }

    public Integer getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(Integer firstWeight) {
        this.firstWeight = firstWeight;
    }

    public Double getFirstWeightFee() {
        return firstWeightFee;
    }

    public void setFirstWeightFee(Double firstWeightFee) {
        this.firstWeightFee = firstWeightFee;
    }

    public Integer getAdditionalWeight() {
        return additionalWeight;
    }

    public void setAdditionalWeight(Integer additionalWeight) {
        this.additionalWeight = additionalWeight;
    }

    public Double getAdditionalWeightFee() {
        return additionalWeightFee;
    }

    public void setAdditionalWeightFee(Double additionalWeightFee) {
        this.additionalWeightFee = additionalWeightFee;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit == null ? null : weightUnit.trim();
    }

    public Double getPcFee() {
        return pcFee;
    }

    public void setPcFee(Double pcFee) {
        this.pcFee = pcFee;
    }
}
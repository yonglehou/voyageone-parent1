/*
 * CmsMtFeeTaxModel.java
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
public class CmsMtFeeTaxModel extends BaseModel {
    /**
     * 税号
     */
    protected String hsCode;

    /**
     * 产品名
     */
    protected String productName;

    /**
     * 单位
     */
    protected String unit;

    /**
     * 发货方式
     */
    protected String shippingType;

    /**
     * 增值税率
     */
    protected Double vaTaxRate;

    /**
     * 消费税率
     */
    protected Double consumptionTaxRate;

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode == null ? null : hsCode.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType == null ? null : shippingType.trim();
    }

    public Double getVaTaxRate() {
        return vaTaxRate;
    }

    public void setVaTaxRate(Double vaTaxRate) {
        this.vaTaxRate = vaTaxRate;
    }

    public Double getConsumptionTaxRate() {
        return consumptionTaxRate;
    }

    public void setConsumptionTaxRate(Double consumptionTaxRate) {
        this.consumptionTaxRate = consumptionTaxRate;
    }
}
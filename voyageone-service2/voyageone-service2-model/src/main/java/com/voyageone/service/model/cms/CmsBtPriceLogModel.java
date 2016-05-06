/*
 * CmsBtPriceLogModel.java
 * Copyright(C) 20xx-2015 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 * 2016-05-06 Created
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 
 */
public class CmsBtPriceLogModel extends BaseModel {
    private String channelId;

    private Integer productId;

    private String code;

    private String sku;

    private String msrpPrice;

    private String retailPrice;

    private String salePrice;

    private String clientMsrpPrice;

    private String clientRetailPrice;

    private String clientNetPrice;

    private String comment;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getMsrpPrice() {
        return msrpPrice;
    }

    public void setMsrpPrice(String msrpPrice) {
        this.msrpPrice = msrpPrice == null ? null : msrpPrice.trim();
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice == null ? null : retailPrice.trim();
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice == null ? null : salePrice.trim();
    }

    public String getClientMsrpPrice() {
        return clientMsrpPrice;
    }

    public void setClientMsrpPrice(String clientMsrpPrice) {
        this.clientMsrpPrice = clientMsrpPrice == null ? null : clientMsrpPrice.trim();
    }

    public String getClientRetailPrice() {
        return clientRetailPrice;
    }

    public void setClientRetailPrice(String clientRetailPrice) {
        this.clientRetailPrice = clientRetailPrice == null ? null : clientRetailPrice.trim();
    }

    public String getClientNetPrice() {
        return clientNetPrice;
    }

    public void setClientNetPrice(String clientNetPrice) {
        this.clientNetPrice = clientNetPrice == null ? null : clientNetPrice.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }
}
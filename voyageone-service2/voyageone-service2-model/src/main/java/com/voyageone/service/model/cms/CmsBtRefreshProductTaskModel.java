/*
 * CmsBtRefreshProductTaskModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 默认属性功能下的商品重刷任务记录表
 */
public class CmsBtRefreshProductTaskModel extends BaseModel {
    protected String channelId;

    protected Integer cartId;

    protected String categoryPath;

    /**
     * 1：通用类目，2：具体类目
     */
    protected Integer categoryType;

    /**
     * 具体的字段
     */
    protected String fieldId;

    /**
     * 1: 全部商品，0：只处理待编辑商品
     */
    protected Boolean allProduct;

    /**
     * 0：待处理，1：已全部处理
     */
    protected Integer status;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath == null ? null : categoryPath.trim();
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId == null ? null : fieldId.trim();
    }

    public Boolean getAllProduct() {
        return allProduct;
    }

    public void setAllProduct(Boolean allProduct) {
        this.allProduct = allProduct;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
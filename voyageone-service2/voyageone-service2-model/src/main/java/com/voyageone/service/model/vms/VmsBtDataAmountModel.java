/*
 * VmsBtDataAmountModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.vms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 
 */
public class VmsBtDataAmountModel extends BaseModel {
    /**
     * 渠道id
     */
    protected String channelId;

    /**
     * 统计对象名称
     */
    protected String amountName;

    /**
     * 统计结果
     */
    protected String amountVal;

    /**
     * 注释
     */
    protected String comment;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getAmountName() {
        return amountName;
    }

    public void setAmountName(String amountName) {
        this.amountName = amountName == null ? null : amountName.trim();
    }

    public String getAmountVal() {
        return amountVal;
    }

    public void setAmountVal(String amountVal) {
        this.amountVal = amountVal == null ? null : amountVal.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }
}
/*
 * CmsBtRefreshProductTaskItemModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 默认属性功能下的商品重刷任务商品记录表
 */
public class CmsBtRefreshProductTaskItemModel extends BaseModel {
    protected Integer taskId;

    protected Integer productid;

    /**
     * 0：待处理，1：已处理
     */
    protected Integer status;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
/*
 * CmsMtTemplateImagesModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * JMBTImages|| 聚美图片
 */
public class CmsMtTemplateImagesModel extends BaseModel {
    /**
     * 平台id   聚美27
     */
    protected Integer platformId;

    /**
     * 店铺渠道编号（如：Sneakerhead为001,Jewelry为010）
     */
    protected String channelId;

    /**
     * 图片模板类别（1:宝贝图（白底方图）；2:；详情图（商品实拍图）；3：移动端宝贝图（竖图））
     */
    protected Integer templateType;

    /**
     * 图片锁定条件（1:白底方图的情况下为"main_image"；2:商品详情图的情况下为"deal_image"； 3:参数图的情况下为"property_image"）
     */
    protected String imageTemplateUrl;

    /**
     * 0:有变更需刷images表 1:已经同步images表
     */
    protected Integer synFlg;

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public String getImageTemplateUrl() {
        return imageTemplateUrl;
    }

    public void setImageTemplateUrl(String imageTemplateUrl) {
        this.imageTemplateUrl = imageTemplateUrl == null ? null : imageTemplateUrl.trim();
    }

    public Integer getSynFlg() {
        return synFlg;
    }

    public void setSynFlg(Integer synFlg) {
        this.synFlg = synFlg;
    }
}
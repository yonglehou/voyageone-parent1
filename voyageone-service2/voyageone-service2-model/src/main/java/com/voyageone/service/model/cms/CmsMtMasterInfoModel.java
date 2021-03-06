/*
 * CmsMtMasterInfoModel.java
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
public class CmsMtMasterInfoModel extends BaseModel {
    /**
     * 平台ID
     */
    protected Integer platformId;

    /**
     * 店铺渠道编号（如：Sneakerhead为001,Jewelry为010）
     */
    protected String channelId;

    /**
     * 品牌名称  data_type(3 4 5 6)  image_key
     */
    protected String brandName;

    /**
     * 商品类别  3 4 5 6
     */
    protected String productType;

    /**
     * 尺码类别    5
     */
    protected String sizeType;

    /**
     * 数据类型（3:特殊说明；4：品牌故事图 ；5：尺码图； 6：物流介绍）
     */
    protected Integer dataType;

    /**
     * 图片顺序 默认1
     */
    protected Integer imageIndex;

    /**
     * data_type为3，value1是文字（特殊说明）,data_type为其它，value1是origin_url自己服务器的图片url
     */
    protected String value1;

    /**
     * data_type为3，value1是NULL,data_type为其它，value2是jm_url聚美服务器的url
     */
    protected String value2;

    /**
     * 0:待上传；1:上传成功；2:上传失败
     */
    protected Integer synFlg;

    protected String errorMessage;

    /**
     * platform_id  channel_id  brand_name  product_type
     */
    protected Boolean active;

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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType == null ? null : productType.trim();
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType == null ? null : sizeType.trim();
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(Integer imageIndex) {
        this.imageIndex = imageIndex;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1 == null ? null : value1.trim();
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2 == null ? null : value2.trim();
    }

    public Integer getSynFlg() {
        return synFlg;
    }

    public void setSynFlg(Integer synFlg) {
        this.synFlg = synFlg;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage == null ? null : errorMessage.trim();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
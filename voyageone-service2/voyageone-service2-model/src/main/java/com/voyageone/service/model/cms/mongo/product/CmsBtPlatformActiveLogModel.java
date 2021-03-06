package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;

/**
 * 商品Model Group Channel
 * @author linanbin on 6/29/2016
 * @version 2.2.0
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtPlatformActiveLogModel extends ChannelPartitionModel {

    private Long groupId;
    private Integer cartId;
    private String numIId = null;
    private String prodCode = null;
    private String result = null;
    private String comment = null;
    private String failedComment = null;
    private String platformStatus = null;
    private String activeStatus = null;
    private String mainProdCode = null;
    private Long batchNo;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getNumIId() {
        return numIId;
    }

    public void setNumIId(String numIId) {
        this.numIId = numIId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFailedComment() {
        return failedComment;
    }

    public void setFailedComment(String failedComment) {
        this.failedComment = failedComment;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getMainProdCode() {
        return mainProdCode;
    }

    public void setMainProdCode(String mainProdCode) {
        this.mainProdCode = mainProdCode;
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

}
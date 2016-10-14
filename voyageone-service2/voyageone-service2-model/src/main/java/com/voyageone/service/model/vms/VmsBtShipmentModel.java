/*
 * VmsBtShipmentModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.vms;

import com.voyageone.base.dao.mysql.BaseModel;
import java.util.Date;

/**
 * 
 */
public class VmsBtShipmentModel extends BaseModel {
    /**
     * 渠道id
     */
    protected String channelId;

    /**
     * 发货名称
     */
    protected String shipmentName;

    /**
     *  发货日期
     */
    protected Date shippedDate;

    /**
     * 物流公司id
     */
    protected String expressCompany;

    /**
     * 物流订单号
     */
    protected String trackingNo;

    /**
     * 注释
     */
    protected String comment;

    /**
     * 1:Open；3：Shipped；4：Arrived；5：Received；6：Receive Error
     */
    protected String status;

    /**
     * 到达VoyageOne时间
     */
    protected Date arrivedTime;

    /**
     * 到达确认者
     */
    protected String arriver;

    /**
     * VoyageOne确认收货时间
     */
    protected Date receivedTime;

    /**
     * 确认收货者
     */
    protected String receiver;

    /**
     *  明细内容的打印时间
     */
    protected Date detailPrintTime;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName == null ? null : shipmentName.trim();
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany == null ? null : expressCompany.trim();
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo == null ? null : trackingNo.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(Date arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public String getArriver() {
        return arriver;
    }

    public void setArriver(String arriver) {
        this.arriver = arriver == null ? null : arriver.trim();
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? null : receiver.trim();
    }

    public Date getDetailPrintTime() {
        return detailPrintTime;
    }

    public void setDetailPrintTime(Date detailPrintTime) {
        this.detailPrintTime = detailPrintTime;
    }
}
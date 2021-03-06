package com.voyageone.service.bean.vms.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 单条的订单信息
 * Created by vantis on 16-7-6.
 */
public class SubOrderInfoBean extends AbstractSubOrderInfoBean {
    private String reservationId;
    private String consolidationOrderId;
    private Date consolidationOrderTime;
    private String clientSku;
    private String name;
    private String status;
    private Date containerizingTime;
    private String containerizer;
    private String barcode;
    @JsonProperty("voPrice")
    private BigDecimal clientPromotionPrice = BigDecimal.ZERO;
    @JsonProperty("salePrice")
    private BigDecimal retailPrice = BigDecimal.ZERO;
    private String attribute1;
    private String attribute2;
    private String attribute3;

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getConsolidationOrderId() {
        return consolidationOrderId;
    }

    public void setConsolidationOrderId(String consolidationOrderId) {
        this.consolidationOrderId = consolidationOrderId;
    }

    public Date getConsolidationOrderTime() {
        return consolidationOrderTime;
    }

    public void setConsolidationOrderTime(Date consolidationOrderTime) {
        this.consolidationOrderTime = consolidationOrderTime;
    }

    public String getClientSku() {
        return clientSku;
    }

    public void setClientSku(String clientSku) {
        this.clientSku = clientSku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getContainerizingTime() {
        return containerizingTime;
    }

    public void setContainerizingTime(Date containerizingTime) {
        this.containerizingTime = containerizingTime;
    }

    public String getContainerizer() {
        return containerizer;
    }

    public void setContainerizer(String containerizer) {
        this.containerizer = containerizer;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @JsonProperty
    public BigDecimal getClientPromotionPrice() {
        return clientPromotionPrice;
    }

    @JsonIgnore
    public void setClientPromotionPrice(BigDecimal clientPromotionPrice) {
        this.clientPromotionPrice = clientPromotionPrice;
    }

    @JsonProperty
    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    @JsonIgnore
    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    // TODO: 16-7-7 暂未想好后续处理 未做订单信息验证 vantis
    @JsonIgnore
    public boolean isValid() {
        return true;
    }
}

package com.voyageone.web2.cms.bean.search.index;

import java.math.BigDecimal;

/**
 * @author Edward
 * @version 2.0.0, 15/12/15
 */
public class CmsSearchInfoBean {

    private String catId;

    private String[] productStatus;

    private Integer platformCart;

    private String[] platformStatus;

    private String[] labelType;

    private String priceType;

    private BigDecimal priceStart;

    private BigDecimal priceEnd;

    private String createTimeStart;

    private String createTimeTo;

    private String publishTimeStart;

    private String publishTimeTo;

    private String compareType;

    private Integer inventory;

    private String brand;

    private String promotion;

    private String[] codeList;

    private String sortOneName;

    private String sortOneType;

    private String sortTwoName;

    private String sortTwoType;

    private String sortThreeName;

    private String sortThreeType;

    private Integer groupPageNum;

    private Integer groupPageSize;

    private Integer productPageNum;

    private Integer productPageSize;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String[] getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String[] productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getPlatformCart() {
        return platformCart;
    }

    public void setPlatformCart(Integer platformCart) {
        this.platformCart = platformCart;
    }

    public String[] getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(String[] platformStatus) {
        this.platformStatus = platformStatus;
    }

    public String[] getLabelType() {
        return labelType;
    }

    public void setLabelType(String[] labelType) {
        this.labelType = labelType;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(BigDecimal priceStart) {
        this.priceStart = priceStart;
    }

    public BigDecimal getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(BigDecimal priceEnd) {
        this.priceEnd = priceEnd;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeTo() {
        return createTimeTo;
    }

    public void setCreateTimeTo(String createTimeTo) {
        this.createTimeTo = createTimeTo;
    }

    public String getPublishTimeStart() {
        return publishTimeStart;
    }

    public void setPublishTimeStart(String publishTimeStart) {
        this.publishTimeStart = publishTimeStart;
    }

    public String getPublishTimeTo() {
        return publishTimeTo;
    }

    public void setPublishTimeTo(String publishTimeTo) {
        this.publishTimeTo = publishTimeTo;
    }

    public String getCompareType() {
        return compareType;
    }

    public void setCompareType(String compareType) {
        this.compareType = compareType;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String[] getCodeList() {
        return codeList;
    }

    public void setCodeList(String[] codeList) {
        this.codeList = codeList;
    }

    public String getSortOneName() {
        return sortOneName;
    }

    public void setSortOneName(String sortOneName) {
        this.sortOneName = sortOneName;
    }

    public String getSortOneType() {
        return sortOneType;
    }

    public void setSortOneType(String sortOneType) {
        this.sortOneType = sortOneType;
    }

    public String getSortTwoName() {
        return sortTwoName;
    }

    public void setSortTwoName(String sortTwoName) {
        this.sortTwoName = sortTwoName;
    }

    public String getSortTwoType() {
        return sortTwoType;
    }

    public void setSortTwoType(String sortTwoType) {
        this.sortTwoType = sortTwoType;
    }

    public String getSortThreeName() {
        return sortThreeName;
    }

    public void setSortThreeName(String sortThreeName) {
        this.sortThreeName = sortThreeName;
    }

    public String getSortThreeType() {
        return sortThreeType;
    }

    public void setSortThreeType(String sortThreeType) {
        this.sortThreeType = sortThreeType;
    }

    public Integer getGroupPageNum() {
        return groupPageNum;
    }

    public void setGroupPageNum(Integer groupPageNum) {
        this.groupPageNum = groupPageNum != null && groupPageNum > 0 ? groupPageNum : 1;
    }

    public Integer getGroupPageSize() {
        return groupPageSize;
    }

    public void setGroupPageSize(Integer groupPageSize) {
        this.groupPageSize = groupPageSize != null && groupPageSize > 0 ? groupPageSize : 1;
    }

    public Integer getProductPageNum() {
        return productPageNum;
    }

    public void setProductPageNum(Integer productPageNum) {
        this.productPageNum = productPageNum != null && productPageNum > 0 ? productPageNum : 1;
    }

    public Integer getProductPageSize() {
        return productPageSize;
    }

    public void setProductPageSize(Integer productPageSize) {
        this.productPageSize = productPageSize != null && productPageSize > 0 ? productPageSize : 1;
    }
}

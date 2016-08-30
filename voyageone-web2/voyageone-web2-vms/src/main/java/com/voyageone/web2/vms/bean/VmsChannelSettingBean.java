package com.voyageone.web2.vms.bean;

/**
 * 渠道配置
 * Created by vantis on 16-7-7.
 */
public class VmsChannelSettingBean {
    private String vendorOperateType;
    private String salePriceShow = "0";
    private String defaultDeliveryCompany;
    private String namingConverter;
    private String emailAddress;

    public String getVendorOperateType() {
        return vendorOperateType;
    }

    public void setVendorOperateType(String vendorOperateType) {
        this.vendorOperateType = vendorOperateType;
    }

    public String getSalePriceShow() {
        return salePriceShow;
    }

    public void setSalePriceShow(String salePriceShow) {
        this.salePriceShow = salePriceShow;
    }

    public String getDefaultDeliveryCompany() {
        return defaultDeliveryCompany;
    }

    public void setDefaultDeliveryCompany(String defaultDeliveryCompany) {
        this.defaultDeliveryCompany = defaultDeliveryCompany;
    }

    public String getNamingConverter() {
        return namingConverter;
    }

    public void setNamingConverter(String namingConverter) {
        this.namingConverter = namingConverter;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}

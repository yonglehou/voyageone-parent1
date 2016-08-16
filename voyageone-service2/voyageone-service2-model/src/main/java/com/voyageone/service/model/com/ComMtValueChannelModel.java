/*
 * ComMtValueChannelModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.com;

import com.voyageone.service.model.com.AdminBaseModel;

/**
 * 
 */
public class ComMtValueChannelModel extends AdminBaseModel {
    protected Integer id;

    protected Integer typeId;

    protected String channelId;

    protected String value;

    protected String name;

    protected String addName1;

    protected String addName2;

    protected String langId;

    protected Integer displayOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAddName1() {
        return addName1;
    }

    public void setAddName1(String addName1) {
        this.addName1 = addName1 == null ? null : addName1.trim();
    }

    public String getAddName2() {
        return addName2;
    }

    public void setAddName2(String addName2) {
        this.addName2 = addName2 == null ? null : addName2.trim();
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId == null ? null : langId.trim();
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
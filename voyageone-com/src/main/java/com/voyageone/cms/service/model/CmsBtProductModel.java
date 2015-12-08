package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.ChannelPartitionModel;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.*;

public class CmsBtProductModel extends ChannelPartitionModel {

    private int prodId;
    private int catId;
    private String catIdPath;

    private CmsBtProductModel_Field fields = new CmsBtProductModel_Field();
    private CmsBtProductModel_Group groups = new CmsBtProductModel_Group();
    private List<CmsBtProductModel_Sku> skus = new ArrayList<>();
    private List<CmsBtProductModel_Tag> tags = new ArrayList<>();
    private CmsBtProductModel_Feed feedAtts = new CmsBtProductModel_Feed();

    public CmsBtProductModel() {
    }

    public CmsBtProductModel(String channelId) {
        super(channelId);
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatIdPath() {
        return catIdPath;
    }

    public void setCatIdPath(String catIdPath) {
        this.catIdPath = catIdPath;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public CmsBtProductModel_Field getFields() {
        return fields;
    }

    public void setFields(CmsBtProductModel_Field fields) {
        this.fields = fields;
    }

    public CmsBtProductModel_Group getGroups() {
        return groups;
    }

    public void setGroups(CmsBtProductModel_Group groups) {
        this.groups = groups;
    }

    public List<CmsBtProductModel_Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<CmsBtProductModel_Sku> skus) {
        this.skus = skus;
    }

    public List<CmsBtProductModel_Tag> getTags() {
        return tags;
    }

    public void setTags(List<CmsBtProductModel_Tag> tags) {
        this.tags = tags;
    }

    public CmsBtProductModel_Feed getFeedAtts() {
        return feedAtts;
    }

    public void setFeedAtts(CmsBtProductModel_Feed feedAtt) {
        this.feedAtts = feedAtt;
    }

}
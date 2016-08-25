package com.voyageone.service.bean.com;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.security.model.ComResourceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan Shi on 2016-08-16.
 */
public class AdminResourceBean extends ComResourceModel {

    @JsonIgnore
    private String channelId;

    @JsonIgnore
    private Integer userId;

    @JsonIgnore
    private Integer cnt;

    private  int selected;

    private List<AdminResourceBean> children;

    public List<AdminResourceBean> getChildren() {
        return children == null ? new ArrayList<>() : children;
    }

    public void setChildren(List<AdminResourceBean> children) {
        this.children = children;
    }



    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }
}
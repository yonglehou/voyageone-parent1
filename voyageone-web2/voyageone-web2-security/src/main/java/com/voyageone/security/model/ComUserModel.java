/*
 * ComUserModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 */
public class ComUserModel extends CoreBaseModel {
    /**
     * 用户姓名
     */
    protected String userName;

    /**
     * 用户账号
     */
    protected String userAccount;

    /**
     * 密码
     */
    @JsonIgnore
    protected String password;

    /**
     * 盐
     */
    @JsonIgnore
    protected String credentialSalt;

    protected Integer orgId;

    /**
     * 说明
     */
    protected String description;

    protected String email;

    /**
     * 删除标识:1,激活;0,禁用;2,锁定
     */
    protected Integer active;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount == null ? null : userAccount.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getCredentialSalt() {
        return credentialSalt;
    }

    public void setCredentialSalt(String credentialSalt) {
        this.credentialSalt = credentialSalt == null ? null : credentialSalt.trim();
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
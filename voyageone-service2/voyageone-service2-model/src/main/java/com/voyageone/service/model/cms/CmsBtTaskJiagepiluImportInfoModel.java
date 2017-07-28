/*
 * CmsBtTaskJiagepiluImportInfoModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;
import java.util.Date;

/**
 * 
 */
public class CmsBtTaskJiagepiluImportInfoModel extends BaseModel {
    /**
     * 价格披露TaskId
     */
    protected Integer taskId;

    /**
     * 导入成功数
     */
    protected Integer successCount;

    /**
     * 导入失败行数
     */
    protected Integer failCount;

    /**
     * 导入开始时间
     */
    protected Date importBeginTime;

    /**
     * 导入结束时间
     */
    protected Date importEndTime;

    /**
     * 异常信息
     */
    protected String errorMsg;

    /**
     * 导入文件原始文件名称
     */
    protected String importFileName;

    /**
     * 导入错误文件名
     */
    protected String errorFileName;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Date getImportBeginTime() {
        return importBeginTime;
    }

    public void setImportBeginTime(Date importBeginTime) {
        this.importBeginTime = importBeginTime;
    }

    public Date getImportEndTime() {
        return importEndTime;
    }

    public void setImportEndTime(Date importEndTime) {
        this.importEndTime = importEndTime;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    public String getImportFileName() {
        return importFileName;
    }

    public void setImportFileName(String importFileName) {
        this.importFileName = importFileName == null ? null : importFileName.trim();
    }

    public String getErrorFileName() {
        return errorFileName;
    }

    public void setErrorFileName(String errorFileName) {
        this.errorFileName = errorFileName == null ? null : errorFileName.trim();
    }
}
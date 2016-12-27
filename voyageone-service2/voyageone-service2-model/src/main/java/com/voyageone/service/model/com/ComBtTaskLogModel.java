/*
 * ComBtTaskLogModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.com;

import com.voyageone.base.dao.mysql.BaseModel;
import java.util.Date;

/**
 * 
 */
public class ComBtTaskLogModel extends BaseModel {
    /**
     * 队列名称
     */
    protected String taskId;

    /**
     * 消息体json
     */
    protected String messageBody;

    /**
     * 日志类型
     */
    protected Short logType;

    /**
     * 1：成功 0：失败
     */
    protected Short status;

    /**
     * 发送时间
     */
    protected Date sendDate;

    /**
     * 开始处理时间
     */
    protected Date beginDate;

    /**
     * 结束处理时间
     */
    protected Date endDate;

    /**
     * 备注
     */
    protected String comment;

    /**
     * 异常堆栈
     */
    protected String stacktrace;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody == null ? null : messageBody.trim();
    }

    public Short getLogType() {
        return logType;
    }

    public void setLogType(Short logType) {
        this.logType = logType;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace == null ? null : stacktrace.trim();
    }
}
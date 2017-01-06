package com.voyageone.components.rabbitmq.bean;

import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;

/**
 * Created by dell on 2016/12/20.
 */
public  interface IMQMessageBody {

     int getConsumerRetryTimes();
     int getMqId();
     String getSender();

     void check() throws MQMessageRuleException;

}
package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.namesub.IMQMessageSubBeanName;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author piao
 * @description 高级检索批量lock产品
 */

@VOMQQueue(value = CmsMqRoutingKey.CMS_ADV_SEARCH_LOCK_PRODUCTS)
public class AdvSearchLockProductsMQMessageBody extends BaseMQMessageBody  implements IMQMessageSubBeanName {

    private Integer cartId;
    private String activeStatus;        //上下架状态
    private List<String> productCodes;
    private String comment;             //备注
    private String lock;                       //锁定状态

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isBlank(super.getChannelId())) {
            throw new MQMessageRuleException("高级检索-批量设置商品上下架MQ发送异常, 参数channelId为空.");
        }
        if (CollectionUtils.isEmpty(productCodes)) {
            throw new MQMessageRuleException("高级检索-批量设置商品上下架MQ发送异常, 参数productCodes为空.");
        }
        if (cartId == null) {
            throw new MQMessageRuleException("高级检索-批量设置商品上下架MQ发送异常, 参数cartId为空.");
        }
        if (StringUtils.isBlank(getSender())) {
            throw new MQMessageRuleException("高级检索-批量设置商品上下架MQ发送异常, 发送者为空.");
        }
        if (lock == null) {
            throw new MQMessageRuleException("高级检索-批量设置商品上下架MQ发送异常, 参数lock为空.");
        }
    }

    @Override
    public String getSubBeanName() {
        return getChannelId();
    }
}

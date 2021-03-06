package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.BaseTaobaoRequest;
import com.taobao.api.domain.TipItemPromDTO;
import com.taobao.api.request.TmallPromotionTipItemModifyRequest;
import com.taobao.api.request.TmallPromotionTipItemRemoveRequest;
import com.taobao.api.response.TmallPromotionTipItemModifyResponse;
import com.taobao.api.response.TmallPromotionTipItemRemoveResponse;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.components.rabbitmq.service.MqSenderService;
import com.voyageone.components.tmall.TbBase;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by Administrator on 2015/10/29.
 */
public class TbPromotionService extends TbBase {

    private final boolean async;

    private final MqSenderService mqSenderService;

    public TbPromotionService(boolean async, MqSenderService mqSenderService) {
        this.async = async;
        this.mqSenderService = mqSenderService;
    }

//    public TmallPromotionTipItemAddResponse addPromotion(ShopBean shopBean, TipItemPromDTO ItemProm) throws ApiException {
//
//        logger.info("天猫特价宝添加活动商品 " + ItemProm.getItemId());
//
//        TmallPromotionTipItemAddRequest req = new TmallPromotionTipItemAddRequest();
//
//        req.setItemProm(ItemProm);
//
//        TmallPromotionTipItemAddResponse response = reqTaobaoApi(shopBean, req);
//        if (response.getErrorCode() != null) {
//            logger.error(response.getSubMsg());
//        }
//
//        return response;
//    }

    public TmallPromotionTipItemModifyResponse updatePromotion(ShopBean shopBean, TipItemPromDTO ItemProm) throws ApiException {
        logger.info("天猫特价宝更新活动商品 " + ItemProm.getItemId());

        TmallPromotionTipItemModifyRequest req = new TmallPromotionTipItemModifyRequest();

        req.setItemProm(ItemProm);

        TmallPromotionTipItemModifyResponse response;

        if (!async) {
            response = reqTaobaoApi(shopBean, req);

            if (response.getErrorCode() != null) {
                String msg = (response.getSubMsg() == null ? "" : response.getSubMsg()) + (response.getMsg() == null ? "" : response.getMsg());
                logger.error(msg);
            }
        } else {

            @VOMQQueue("voyageone_cms_jushita_mq_tjb_promotion_tip_item_update_queue")
            class PromotionTipItemModifyMessage extends SimplePromotionTipItemOperatingMessage<TmallPromotionTipItemModifyRequest> {
                private PromotionTipItemModifyMessage() {
                    super(shopBean, OperatingType.UPDATE, req);
                }
            }

            mqSenderService.sendMessage(new PromotionTipItemModifyMessage());

            response = new TmallPromotionTipItemModifyResponse();
            response.setModifyRst(true);
        }

        return response;
    }

    public TmallPromotionTipItemRemoveResponse removePromotion(ShopBean shopBean, Long num_iid, Long campaign_id) throws ApiException {
        logger.info("天猫特价宝删除活动商品 " + num_iid);
        TmallPromotionTipItemRemoveRequest req = new TmallPromotionTipItemRemoveRequest();
        req.setItemId(num_iid);
        req.setCampaignId(campaign_id);

        TmallPromotionTipItemRemoveResponse response;

        if (!async) {
            response = reqTaobaoApi(shopBean, req);
            if (response.getErrorCode() != null) {
                logger.error(response.getSubMsg());
            }
        } else {

            @VOMQQueue("voyageone_cms_jushita_mq_tjb_promotion_tip_item_update_queue")
            class PromotionTipItemRemoveMessage extends SimplePromotionTipItemOperatingMessage<TmallPromotionTipItemRemoveRequest> {
                private PromotionTipItemRemoveMessage() {
                    super(shopBean, OperatingType.REMOVE, req);
                }
            }

            mqSenderService.sendMessage(new PromotionTipItemRemoveMessage());

            response = new TmallPromotionTipItemRemoveResponse();
            response.setRemoveRst(true);
        }

        return response;
    }

    enum OperatingType {
        REMOVE(0),
        UPDATE(1);

        private int value;

        OperatingType(int value) {
            this.value = value;
        }
    }

    @Configuration
    public static class AutoConfiguration {
        @Bean
        @Conditional(OnMissTbPromotionService.class)
        public TbPromotionService tbPromotionService(MqSenderService mqSenderService) {
            return new TbPromotionService(false, mqSenderService);
        }
    }

    public static class OnMissTbPromotionService implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ConfigurableListableBeanFactory configurableListableBeanFactory = context.getBeanFactory();
            String[] namesForType = configurableListableBeanFactory.getBeanNamesForType(TbPromotionService.class);
            return namesForType.length < 1;
        }
    }

    /**
     * 简单实现的接口调用 MQ 信息体，追加必要的 cartId 字段
     */
    abstract class SimplePromotionTipItemOperatingMessage<T extends BaseTaobaoRequest<?>> extends BaseMQMessageBody {
        private final int cartId;

        private final int operatingType;

        private final T req;

        SimplePromotionTipItemOperatingMessage(ShopBean shopBean, OperatingType operatingType, T req) {
            this.cartId = Integer.valueOf(shopBean.getCart_id());
            this.operatingType = operatingType.value;
            this.req = req;
            setChannelId(shopBean.getOrder_channel_id());
        }

        public int getCartId() {
            return cartId;
        }

        public int getOperatingType() {
            return operatingType;
        }

        public T getReq() {
            return req;
        }

        @Override
        public void check() throws MQMessageRuleException {
        }
    }
}

package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceCalculateException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 给一个channel生成新的platform
 * Created by james on 2016/12/9.
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_CartAppJob)
public class CmsCartAppMqService extends BaseMQCmsService {

    private final ProductService productService;

    private final ProductGroupService productGroupService;

    private final PriceService priceService;

    private final static int pageSize = 200;

    @Autowired
    public CmsCartAppMqService(ProductService productService, ProductGroupService productGroupService, PriceService priceService) {
        this.productService = productService;
        this.productGroupService = productGroupService;
        this.priceService = priceService;
    }

    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {
        String channelId = (String) messageMap.get("channelId");
        Integer cartId = (Integer) messageMap.get("cartId");
        Boolean isSingle = messageMap.get("isSingle") == null ? false : (Boolean) messageMap.get("isSingle");

        Assert.isTrue(channelId != null && cartId != null, "参数错误!");

        long sumCnt = productService.countByQuery("{}", null, channelId);

        long pageCnt = sumCnt / pageSize + (sumCnt % pageSize > 0 ? 1 : 0);
        for (int pageNum = 1; pageNum <= pageCnt; pageNum++) {
            JongoQuery jongoQuery = new JongoQuery();

            jongoQuery.setSkip((pageNum - 1) * pageSize);
            jongoQuery.setLimit(pageSize);
            List<CmsBtProductModel> cmsBtProductModels = productService.getList(channelId, jongoQuery);
            for (int i = 0; i < cmsBtProductModels.size(); i++) {
                $info(String.format("%d/%d  code:%s", (pageNum - 1) * pageSize + i + 1, sumCnt, cmsBtProductModels.get(i).getCommon().getFields().getCode()));
                createPlatform(cmsBtProductModels.get(i), cartId, isSingle);
            }
        }
    }

    private void createPlatform(CmsBtProductModel cmsBtProductModel, Integer cartId, Boolean isSingle) {

        if (cmsBtProductModel.getPlatform(cartId) != null) return;

        String code = cmsBtProductModel.getCommon().getFields().getCode();

        CmsBtProductModel_Platform_Cart platform = new CmsBtProductModel_Platform_Cart();
        // cartId
        platform.setCartId(cartId);
        // 设定是否主商品
        // 如果是聚美或者独立官网的话，那么就是一个Code对应一个Group
        CmsBtProductGroupModel group;


        if (isSingle) {
            group = productGroupService.createNewGroup(cmsBtProductModel.getChannelId(), cartId, code, false);
            group.setModifier(getTaskName());
            group.setCreater(getTaskName());
            platform.setpIsMain(1);
            platform.setMainProductCode(cmsBtProductModel.getCommon().getFields().getCode());
        } else {
            group = productGroupService.selectProductGroupByModelCodeAndCartId(cmsBtProductModel.getChannelId(), cmsBtProductModel.getCommon().getFields().getModel(), cartId.toString());
            if (group != null) {
                group.getProductCodes().add(code);
                group.setModifier(getTaskName());
                platform.setpIsMain(0);
            } else {
                group = productGroupService.createNewGroup(cmsBtProductModel.getChannelId(), cartId, code, false);
                group.setModifier(getTaskName());
                group.setCreater(getTaskName());
                platform.setpIsMain(1);
            }
            platform.setMainProductCode(group.getMainProductCode());
        }


        // 平台类目状态(新增时)
        platform.setpCatStatus("0");  // add desmond 2016/07/05

        // 商品状态
        platform.setStatus(CmsConstants.ProductStatus.Pending.toString());
        // 平台属性状态(新增时)
        platform.setpAttributeStatus("0");    // add desmond 2016/07/05

        // 平台sku
        List<BaseMongoMap<String, Object>> skuList = new ArrayList<>();
        for (CmsBtProductModel_Sku sku : cmsBtProductModel.getCommon().getSkus()) {
            BaseMongoMap<String, Object> skuInfo = new BaseMongoMap<>();
            skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.skuCode.name(), sku.getSkuCode());
            skuInfo.put(CmsBtProductConstants.Platform_SKU_COM.isSale.name(), true);
            skuList.add(skuInfo);
        }

        platform.setSkus(skuList);
        cmsBtProductModel.setPlatform(cartId, platform);
        try {
            priceService.setPrice(cmsBtProductModel, cartId, true);
        } catch (IllegalPriceConfigException | PriceCalculateException e) {
            $error(e);
        }
        productGroupService.update(group);
        productService.updateProductPlatform(cmsBtProductModel.getChannelId(), cmsBtProductModel.getProdId(), platform, getTaskName(), false, EnumProductOperationType.CreateNewCart, EnumProductOperationType.CreateNewCart.getName(), false);
    }
}

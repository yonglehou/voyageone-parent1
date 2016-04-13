package com.voyageone.web2.cms.views.jm;
import com.voyageone.common.mq.MqSender;
import com.voyageone.common.mq.enums.MqRoutingKey;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionProductService;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionService;
import com.voyageone.service.model.jumei.CmsBtJmPromotionModel;
import com.voyageone.service.model.jumei.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.jumei.CmsMtMasterInfoModel;
import com.voyageone.service.model.jumei.businessmodel.ProductIdListInfo;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

@RestController
@RequestMapping(
        value = CmsUrlConstants.JMPROMOTION.LIST.DETAIL.ROOT,
        method = RequestMethod.POST
)
public class CmsJmPromotionDetailController extends CmsController {
    @Autowired
    private CmsBtJmPromotionProductService serviceCmsBtJmPromotionProduct;
    @Autowired
    private MqSender sender;
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.GET_PROMOTION_PRODUCT_INFO_LIST_BY_WHERE)
    public AjaxResponse getPromotionProductInfoListByWhere(@RequestBody Map params) {
        return success(serviceCmsBtJmPromotionProduct.getPromotionProductInfoListByWhere(params));
    }
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UPDATE)
    public AjaxResponse update(@RequestBody CmsBtJmPromotionProductModel params) {
        String channelId = getUser().getSelChannelId();
        params.setChannelId(channelId);
        params.setModifier(getUser().getUserName());
        serviceCmsBtJmPromotionProduct.update(params);
        CallResult result = new CallResult();
        return success(result);
    }
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.UPDATEDEAlPRICE)
    public AjaxResponse updateDealPrice(@RequestBody Map<String, Object> map) {
        int id = Integer.parseInt((String) map.get("id"));
        BigDecimal dealPrice = new BigDecimal(map.get("dealPrice").toString());
        CmsBtJmPromotionProductModel model = serviceCmsBtJmPromotionProduct.select(id);
        model.setDealPrice(dealPrice);
        model.setModifier(getUser().getUserName());
        serviceCmsBtJmPromotionProduct.update(model);
        CallResult result = new CallResult();
        return success(result);
    }
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.DELETE)
    public AjaxResponse delete(@RequestBody int id) {
        serviceCmsBtJmPromotionProduct.delete(id);
        CallResult result = new CallResult();
        return success(result);
    }

    //全部删除
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.DELETEBYPPROMOTIONID)
    public AjaxResponse deleteByPromotionId(@RequestBody int promotionId) {
        serviceCmsBtJmPromotionProduct.deleteByPromotionId(promotionId);
        CallResult result = new CallResult();
        return success(result);
    }
    //部分商品删除
    ///cms/jmpromotion/detail/deleteByProductIdList
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.DELETEBYPRODUCTIDLIST)
    public AjaxResponse deleteByProductIdList(@RequestBody ProductIdListInfo parameter) {
        serviceCmsBtJmPromotionProduct.deleteByProductIdList(parameter);
        CallResult result = new CallResult();
        return success(result);
    }
    //所有未上新商品上新
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.JmNewUpdateAll)
    public AjaxResponse jmNewUpdateAll(@RequestBody int promotionId) {
        serviceCmsBtJmPromotionProduct.jmNewUpdateAll(promotionId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id",promotionId);
        sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdate, map);
        CallResult result = new CallResult();
        return success(result);
    }
    //部分商品上新
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.JmNewByProductIdListInfo)
    public AjaxResponse jmNewByProductIdListInfo(@RequestBody ProductIdListInfo parameter) {
        serviceCmsBtJmPromotionProduct.jmNewByProductIdListInfo(parameter);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id",parameter.getPromotionId());
        sender.sendMessage(MqRoutingKey.CMS_BATCH_JuMeiProductUpdate, map);
        CallResult result = new CallResult();
        return success(result);
    }
}

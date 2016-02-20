package com.voyageone.web2.cms.views.promotion.list;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by james.li on 2015/12/11.
 */
@RestController
@RequestMapping(
        value = PROMOTION.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsPromotionIndexController extends CmsController {

    @Autowired
    private CmsPromotionIndexService cmsPromotionService;

    @RequestMapping(PROMOTION.LIST.INDEX.INIT)
    public AjaxResponse init() {
        return success(cmsPromotionService.init(getUser().getSelChannelId(), getLang()));
    }

    @RequestMapping(PROMOTION.LIST.INDEX.GET_PROMOTION_LIST)
    public AjaxResponse queryList(@RequestBody Map params) {
        String channelId = getUser().getSelChannelId();
        params.put("channelId", channelId);
        return success(cmsPromotionService.queryByCondition(params));
    }

    @RequestMapping({PROMOTION.LIST.INDEX.INSERT_PROMOTION,PROMOTION.LIST.INDEX.UPDATE_PROMOTION})
    public AjaxResponse insertOrUpdate(@RequestBody CmsBtPromotionModel cmsBtPromotionModel) {
        String channelId = getUser().getSelChannelId();
        cmsBtPromotionModel.setChannelId(channelId);
        cmsBtPromotionModel.setCreater(getUser().getUserName());
        return success(cmsPromotionService.addOrUpdate(cmsBtPromotionModel));
    }

}
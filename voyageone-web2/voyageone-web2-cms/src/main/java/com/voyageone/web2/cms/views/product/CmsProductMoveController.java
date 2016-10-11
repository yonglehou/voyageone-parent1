package com.voyageone.web2.cms.views.product;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.model.util.MapModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 移动Code和移动Sku
 * @author jeff.duan
 * @version 2.3.0
 * @since 2.3.0
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.PRODUCT.DETAIL.ROOT)
public class CmsProductMoveController extends CmsController {

    private final CmsProductMoveService cmsProductMoveService;

    @Autowired
    public CmsProductMoveController(CmsProductMoveService cmsProductMoveService) {
        this.cmsProductMoveService = cmsProductMoveService;
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.MOVE_CODE_INIT)
    public AjaxResponse moveCodeInit(@RequestBody Map<String, Object> param) {
        return success(cmsProductMoveService.moveCodeInit(param, getUser().getSelChannelId()));
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.MOVE_CODE_SEARCH)
    public AjaxResponse moveCodeSearch(@RequestBody Map<String, Object> param) {
        return success(cmsProductMoveService.moveCodeSearch(param, getUser().getSelChannelId(), getLang()));
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.MOVE_CODE_PREVIEW)
    public AjaxResponse moveCodePreview(@RequestBody Map<String, Object> param) {
        return success(cmsProductMoveService.moveCodePreview(param, getUser().getSelChannelId(), getLang()));
    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.MOVE_CODE)
    public AjaxResponse moveCode(@RequestBody Map<String, Object> param) {
        cmsProductMoveService.moveCode(param, getUser().getSelChannelId(), getLang());
        return success(null);
    }
}

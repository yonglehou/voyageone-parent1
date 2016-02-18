package com.voyageone.web2.cms.views.product;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.CmsProductInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lewis on 15-12-16.
 */
@RestController
@RequestMapping(method = RequestMethod.POST,value = CmsUrlConstants.PRODUCT.DETAIL.ROOT)
public class CmsProductDetailController extends CmsController{

    @Autowired
    CmsProductDetailService productPropsEditService;

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.GET_PRODUCT_INFO)
    public AjaxResponse doGetProductInfo(@RequestBody Map params){

        Long productId = Long.parseLong(String.valueOf(params.get("productId")));

        String channelId = getUser().getSelChannelId();

        Map<String,Object> categoryInfo = new HashMap<>();

        CmsProductInfoBean productInfo = productPropsEditService.getProductInfo(channelId,productId);

        categoryInfo.put("productInfo",productInfo);

        return success(categoryInfo);

    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_PRODUCT_MASTER_INFO)
    public AjaxResponse doUpdateProductMasterInfo(@RequestBody Map requestMap){

        String channelId = getUser().getSelChannelId();

        String user = getUser().getUserName();

        String updateTime = productPropsEditService.updateProductMasterInfo(channelId, user, requestMap);

        Map<String,Object> updateInfo = new HashMap<>();

        updateInfo.put("modified",updateTime);

        return success(updateInfo);

    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_PRODUCT_SKU_INFO)
    public AjaxResponse doUpdateProductSkuInfo(@RequestBody Map requestMap){

        String channelId = getUser().getSelChannelId();
        String user = getUser().getUserName();
        String categoryId = requestMap.get("categoryId").toString();
        Long productId = Long.valueOf(requestMap.get("productId").toString());
        String categoryFullPath = requestMap.get("categoryFullPath").toString();
        Map skuMap = (Map) requestMap.get("skuFields");
        String modified = requestMap.get("modified").toString();

        String updateTime = productPropsEditService.updateProductSkuInfo(channelId, user, categoryId, productId, modified, categoryFullPath, skuMap);

        Map<String,Object> updateInfo = new HashMap<>();

        updateInfo.put("modified", updateTime);

        return success(updateInfo);

    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.UPDATE_PRODUCT_ALL_INFO)
    public AjaxResponse doUpdateProductAllInfo(@RequestBody Map requestMap){

        String channelId = getUser().getSelChannelId();
        String userName = getUser().getUserName();

        String updateTime = productPropsEditService.updateProductAllInfo(channelId, userName,requestMap);

        Map<String,Object> updateInfo = new HashMap<>();

        updateInfo.put("modified", updateTime);

        return success(updateInfo);

    }

    @RequestMapping(CmsUrlConstants.PRODUCT.DETAIL.CHANGE_CATEGORY)
    public AjaxResponse doChangeCategory(@RequestBody Map requestMap){

        Map<String,Object> resultMap = productPropsEditService.changeProductCategory(requestMap, getUser(), getLang());

        return success(resultMap);
    }

}
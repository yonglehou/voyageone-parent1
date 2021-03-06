package com.voyageone.web2.cms.views.tools.common;

import com.voyageone.service.impl.cms.tools.common.CmsMasterBrandMappingService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gjl on 2016/10/9.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.TOOLS.COMMON.ROOT, method = RequestMethod.POST)
public class CmsMasterBrandMappingController extends CmsController {

    @Autowired
    private CmsMasterBrandMappingService cmsMasterBrandMappingService;

    /**
     * Master品牌匹配初始化
     * @param param
     * @return resultBean
     */
    @RequestMapping(value = CmsUrlConstants.TOOLS.COMMON.SEARCH_MASTER_BRAND_INFO)
    public AjaxResponse doSearch(@RequestBody Map param) {
        Map<String, Object> result = new HashMap<>();
        //店铺渠道取得
        String channelId = this.getUser().getSelChannelId();
        // 检索Master品牌匹配的数据
        result.put("masterBrandList", cmsMasterBrandMappingService.searchMasterBrandInfo(channelId, param));
        // 检索Master品牌匹配的数量
        result.put("masterBrandsCount", cmsMasterBrandMappingService.searchMasterBrandCount(channelId, param));
        //返回数据的类型
        return success(result);
    }
}

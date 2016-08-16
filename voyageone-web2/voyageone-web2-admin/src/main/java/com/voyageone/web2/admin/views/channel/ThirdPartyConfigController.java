package com.voyageone.web2.admin.views.channel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.bean.admin.ComMtThirdPartyConfigBean;
import com.voyageone.service.impl.admin.channel.ThirdPartyConfigService;
import com.voyageone.service.model.admin.ComMtThirdPartyConfigModel;
import com.voyageone.service.model.admin.PageModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.channel.ThirdPartyConfigFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
@RequestMapping(value = AdminUrlConstants.Channel.ThirdParty.ROOT, method = RequestMethod.POST)
public class ThirdPartyConfigController extends AdminController {
	
	@Autowired
	private ThirdPartyConfigService thirdPartyConfigService;
	
	@RequestMapping(AdminUrlConstants.Channel.ThirdParty.SEARCH_THIRD_PARTY_CONFIG_BY_PAGE)
	public AjaxResponse searchThirdPartyConfigByPage(@RequestBody ThirdPartyConfigFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索第三方配置信息
		PageModel<ComMtThirdPartyConfigBean> smsConfigPage = thirdPartyConfigService.searchThirdPartyConfigByPage(
				form.getChannelId(), form.getPageNum(), form.getPropVal(), form.getPageNum(), form.getPageSize());
		
		return success(smsConfigPage);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.ThirdParty.ADD_THIRD_PARTY_CONFIG)
	public AjaxResponse addThirdPartyConfig(@RequestBody ThirdPartyConfigFormBean form) {
		return addOrUpdateThirdPartyConfig(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.ThirdParty.UPDATE_THIRD_PARTY_CONFIG)
	public AjaxResponse updateThirdPartyConfig(@RequestBody ThirdPartyConfigFormBean form) {
		Preconditions.checkNotNull(form.getSeq());
		return addOrUpdateThirdPartyConfig(form, false);
	}
	
	public AjaxResponse addOrUpdateThirdPartyConfig(@RequestBody ThirdPartyConfigFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getChannelId()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getPropName()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getPropVal1()));
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(SUCCESS, false);
		try {
			// 设置第三方配置信息
			ComMtThirdPartyConfigModel model = new ComMtThirdPartyConfigModel();
			BeanUtils.copyProperties(form, model);
			// 保存第三方配置信息
			thirdPartyConfigService.addOrUpdateThirdPartyConfig(model, getUser().getUserName(), append);
			result.put(SUCCESS, true);
		} catch (BusinessException e) {
			result.put(MESSAGE, e.getMessage());
		}
		
		return success(result);
	}

}

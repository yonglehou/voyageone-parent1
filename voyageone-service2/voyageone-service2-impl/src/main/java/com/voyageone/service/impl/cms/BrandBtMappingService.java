package com.voyageone.service.impl.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.service.bean.cms.CmsBtBrandBean;
import com.voyageone.service.bean.cms.CmsBtBrandMappingBean;
import com.voyageone.service.dao.cms.CmsMtBrandsMappingDao;
import com.voyageone.service.daoext.cms.CmsBtBrandMappingDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtBrandsMappingModel;

/**
 * 品牌映射服务层
 * @author Wangtd 2016/07/25
 * @since 2.3.0
 */
@Service
public class BrandBtMappingService extends BaseService {
	
	@Autowired
	private CmsMtBrandsMappingDao cmsMtBrandsMappingDao;
	
	@Autowired
	private CmsBtBrandMappingDaoExt cmsBtBrandMappingDaoExt;
	
	public boolean addNewBrandMapping(CmsMtBrandsMappingModel brandModel) {
		return cmsMtBrandsMappingDao.insert(brandModel) > 0;
	}
	
	public List<CmsBtBrandBean> searchMasterBrands(String channelId, int cartId, String brandName) {
		return cmsBtBrandMappingDaoExt.searchMasterBrands(channelId, cartId, brandName);
	}
	
	public List<CmsBtBrandBean> searchJmBrands(String langId, String brandName) {
		return cmsBtBrandMappingDaoExt.searchJmBrands(langId, brandName);
	}
	
	public List<CmsBtBrandMappingBean> searchBrandsByPage(String channelId, int cartId, String langId,
			Map<String, Object> others) {
		Map<String, Object> params = formatBrandsQueryParameter(channelId, cartId, langId, others);
		return cmsBtBrandMappingDaoExt.searchBrandsByPage(params);
	}

	public Long searchBrandsCount(String channelId, Integer cartId, String langId, Map<String, Object> others) {
		Map<String, Object> params = formatBrandsQueryParameter(channelId, cartId, langId, others);
		return cmsBtBrandMappingDaoExt.searchBrandsCount(params);
	}
	
	public List<CmsBtBrandMappingBean> searchMatchedBrands(String channelId, Integer cartId, String langId,
			String brandId) {
		Map<String, Object> params = new HashMap<String, Object>();
		// 必要参数设置
		params.put("channelId", channelId);
		params.put("cartId", cartId);
		params.put("langId", langId);
		params.put("brandId", brandId);
		// 区分聚美品牌与其他品牌
		if (CartEnums.Cart.JM.getId().equals(String.valueOf(cartId))) {
			params.put("isJmBrand", true);
		} else {
			params.put("isJmBrand", false);
		}
		return cmsBtBrandMappingDaoExt.searchMatchedBrands(params);
	}
	
	private Map<String, Object> formatBrandsQueryParameter(String channelId, int cartId, String langId,
			Map<String, Object> others) {
		Map<String, Object> params = new HashMap<String, Object>();
		// 必要参数设置
		params.put("channelId", channelId);
		params.put("cartId", cartId);
		params.put("langId", langId);
		params.put("typeId", TypeConfigEnums.MastType.brand.getId());
		// 区分聚美品牌与其他品牌
		if (CartEnums.Cart.JM.getId().equals(String.valueOf(cartId))) {
			params.put("isJmBrand", true);
		} else {
			params.put("isJmBrand", false);
		}
		// 添加其他非必要参数
		if (MapUtils.isNotEmpty(others)) {
			params.putAll(others);
		}
		
		return params;
	}

	public boolean addOrUpdateBrandMapping(CmsMtBrandsMappingModel brandModel) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("channelId", brandModel.getChannelId());
		paramMap.put("cartId", brandModel.getCartId());
		paramMap.put("cmsBrand", brandModel.getCmsBrand());
		CmsMtBrandsMappingModel brandMapping = cmsMtBrandsMappingDao.selectOne(paramMap);
		// 添加或更新品牌映射
		if (brandMapping == null) {
			// 找不到记录就添加品牌映射
			return cmsMtBrandsMappingDao.insert(brandModel) > 0;
		} else {
			// 找到记录就更新品牌映射
			brandModel.setCreated(brandMapping.getCreated());
			brandModel.setCreater(brandMapping.getCreater());
			brandModel.setId(brandMapping.getId());
			return cmsMtBrandsMappingDao.update(brandModel) > 0;
		}
	}

}

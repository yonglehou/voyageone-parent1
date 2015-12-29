package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductsCountGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * /puroduct/selectCount Request Model
 *
 * Product的id.两种方式来查看一个产品:
 *  1.传入productId List来查询Count
 *  2.传入productCode list来查询Count
 *  3:传入props来查询Count
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductsCountGetRequest extends VoApiRequest<ProductsCountGetResponse> {

	public String getApiURLPath() {
		return "/puroduct/selectCount";
	}

	/**
	 * channelId
	 */
	private String channelId;

	/**
	 * productId
	 */
	private Set<Long> productIds = new LinkedHashSet<>();

	/**
	 * Product的Code
	 */
	private Set<String> productCodes = new LinkedHashSet<>();

	/**
	 * 用户自定义关键属性,结构：pid1:value1;pid2:value2，如果有型号，系列等子属性用: 隔开 例如：“20000:优衣库;型号:001;632501:1234”，表示“品牌:优衣库;型号:001;货号:1234”
	 */
	private String props;

	public ProductsCountGetRequest() {}

	public ProductsCountGetRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(channelId);
		RequestUtils.checkNotEmpty(" productIdList or productCodeList or props", productIds, productCodes, props);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Set<Long> getProductIds() {
		return productIds;
	}

	public void setProductIds(Set<Long> productIds) {
		this.productIds = productIds;
	}

	public Set<String> getProductCodes() {
		return productCodes;
	}

	public void setProductCodes(Set<String> productCodes) {
		this.productCodes = productCodes;
	}

	public String getProps() {
		return props;
	}
	public void setProps(String props) {
		this.props = props;
	}

	public void addProp(String key, Object value) {
		this.props = RequestUtils.addProp(props, key, value);
	}

}
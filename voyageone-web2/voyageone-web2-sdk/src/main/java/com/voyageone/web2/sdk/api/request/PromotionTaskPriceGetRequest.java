package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionTaskPriceGetResponse;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author aooer 2016/2/19.
 * @version 2.0.0
 * @since 2.0.0
 */
public class PromotionTaskPriceGetRequest extends VoApiRequest<PromotionTaskPriceGetResponse>{

    private Map<String,Object> params;

    @Override
    public String getApiURLPath() {
        return "/promotion/task/price/getList";
    }

    @Override
    public void requestCheck() throws ApiRuleException {
        Assert.notNull(params);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}

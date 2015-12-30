package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.ProductSkuService;
import com.voyageone.web2.sdk.api.request.ProductSkusGetRequest;
import com.voyageone.web2.sdk.api.request.ProductSkusPutRequest;
import com.voyageone.web2.sdk.api.request.ProductUpdatePriceRequest;
import com.voyageone.web2.sdk.api.response.ProductSkusGetResponse;
import com.voyageone.web2.sdk.api.response.ProductSkusPutResponse;
import com.voyageone.web2.sdk.api.response.ProductUpdatePriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * product Controller
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/rest/puroduct/sku",
        method = RequestMethod.POST
)
public class ProductSkuController extends BaseController {

    @Autowired
    private ProductSkuService productSkuService;

    /**
     * selectList
     * @return List<CmsBtProductModel>
     */
    @RequestMapping("selectList")
    public ProductSkusGetResponse selectList(@RequestBody ProductSkusGetRequest request) {
        return productSkuService.selectList(request);
    }


    /**
     * selectList
     * @return List<CmsBtProductModel>
     */
    @RequestMapping("put")
    public ProductSkusPutResponse put(@RequestBody ProductSkusPutRequest request) {
        return productSkuService.saveSkus(request);
    }

    /**
     * updatePrices
     * @return List<CmsBtProductModel>
     */
    @RequestMapping("updatePrices")
    public ProductUpdatePriceRequest updatePrices(@RequestBody ProductUpdatePriceRequest request) {
        return productSkuService.updatePrices(request);
    }
}

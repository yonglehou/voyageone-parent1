package com.voyageone.web2.openapi.channeladvisor.service;

import com.voyageone.web2.sdk.api.channeladvisor.request.ProductGroupRequest;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;

import java.util.List;

public interface CAProductService {

     ActionResponse getProducts(String groupFields, String buyableFields) ;

     ActionResponse updateProducts(List<ProductGroupRequest> request);

     ActionResponse updateQuantityPrice(List<ProductGroupRequest> request) ;

     ActionResponse updateStatus(List<ProductGroupRequest> request) ;

}

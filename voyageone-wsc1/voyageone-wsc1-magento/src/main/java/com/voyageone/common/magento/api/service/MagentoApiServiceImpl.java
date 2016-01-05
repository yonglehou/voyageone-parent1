package com.voyageone.common.magento.api.service;

import com.voyageone.common.configs.Codes;
import com.voyageone.common.magento.api.bean.*;
import com.voyageone.common.magento.api.base.*;
import com.voyageone.common.magento.api.service.*;
import com.voyageone.common.util.StringUtils;
import magento.*;

import java.util.ArrayList;
import java.util.List;

public class MagentoApiServiceImpl {
	/**
	 * 配置头
	 */
	private static final String MAGENTO_API_CONFIG = "MAGENTO_API_CONFIG_";
	/**
	 * 店铺渠道
	 */
	private String orderChannelId;
	/**
	 * 登陆用数据Bean
	 */
	private LoginParam loginParam = new LoginParam();
	
	/**
	 * 顾客信息
	 */
	private CustomerBean customer = new CustomerBean();
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 密码
	 */
	private String apiKey;
	
	private MagentoServiceStub stub;
	
	public MagentoApiServiceImpl(String orderChannelId) {
		super();
		this.orderChannelId = orderChannelId;

//		String realConfigName = MAGENTO_API_CONFIG + orderChannelId;
//
//		// magento api调用 URL
//		String url = Codes.getCodeName(realConfigName, "url");
//		// magento api调用 userName
//		String userName = Codes.getCodeName(realConfigName, "userName");
//		// magento api调用 密钥
//		String apiKey = Codes.getCodeName(realConfigName, "apiKey");
//
//		loginParam.setUsername(userName);
//		loginParam.setApiKey(apiKey);
		
//		loginParam.setUsername("juicy_couture");
//		loginParam.setApiKey("abc.123");
		loginParam.setUsername("VOYAGEONE_API_USER");
		loginParam.setApiKey("hjA=fs2H0n+%PFd,b4wB");

//		String customerId = Codes.getCodeName(realConfigName, "customerId");
//		String customerMode = Codes.getCodeName(realConfigName, "customerMode");
//		String storeId = Codes.getCodeName(realConfigName, "storeId");
//		String webSiteId = Codes.getCodeName(realConfigName, "webSiteId");
//		String storeName = Codes.getCodeName(realConfigName, "storeName");
//
//		customer.setCustomerId(Integer.valueOf(customerId));
//		customer.setMode(customerMode);
//		customer.setStoreId(Integer.valueOf(storeId));
//		customer.setWebsiteId(Integer.valueOf(webSiteId));
//		customer.setStore(storeName);
		
//		customer.setCustomerId(21);
//		customer.setMode("customer");
//		customer.setStoreId(1);
//		customer.setWebsiteId(1);
//		customer.setStore("juicy_cn");
	}
	
	/**
	 * @return the orderChannelId
	 */
	public String getOrderChannelId() {
		return orderChannelId;
	}
	
	/**
	 * @param orderChannelId the orderChannelId to set
	 */
	public void setOrderChannelId(String orderChannelId) {
		this.orderChannelId = orderChannelId;

		String realConfigName = MAGENTO_API_CONFIG + orderChannelId;

		// magento api调用 userName
		String userName = Codes.getCodeName(realConfigName, "userName");
		// magento api调用 密钥
		String apiKey = Codes.getCodeName(realConfigName, "apiKey");

		loginParam.setUsername(userName);
		loginParam.setApiKey(apiKey);

//		loginParam.setUsername("juicy_couture");
//		loginParam.setApiKey("abc.123");
//		loginParam.setUsername("VOYAGEONE_API_USER");
//		loginParam.setApiKey("hjA=fs2H0n+%PFd,b4wB");

		String customerId = Codes.getCodeName(realConfigName, "customerId");
		String customerMode = Codes.getCodeName(realConfigName, "customerMode");
		String storeId = Codes.getCodeName(realConfigName, "storeId");
		String webSiteId = Codes.getCodeName(realConfigName, "webSiteId");
		String storeName = Codes.getCodeName(realConfigName, "storeName");

		customer.setCustomerId(Integer.valueOf(customerId));
		customer.setMode(customerMode);
		customer.setStoreId(Integer.valueOf(storeId));
		customer.setWebsiteId(Integer.valueOf(webSiteId));
		customer.setStore(storeName);

//		customer.setCustomerId(21);
//		customer.setMode("customer");
//		customer.setStoreId(1);
//		customer.setWebsiteId(1);
//		customer.setStore("juicy_cn");
	}
	
	/**
	 * Login to Magento
	 *
	 * @return
	 */
	private String login() throws Exception {
//		String realConfigName = MAGENTO_API_CONFIG + this.orderChannelId;
//
//		// magento api调用 URL
//		String url = Codes.getCodeName(realConfigName, "url");
//		stub = new MagentoServiceStub(url);
		stub = new MagentoServiceStub("http://www.wmf.com/api/v2_soap");
//		System.setProperty("javax.net.ssl.trustStore", "D:/tmp/trustStore");
//
//		stub = new MagentoServiceStub("https://api.juicycouture.asia/api/v2_soap");
		
		// 登陆
		LoginResponseParam response = stub.login(loginParam);

		// session ID
		String sessionId = response.getResult();
		
		return sessionId;
	}
	
	/**
	 * Close Session
	 *
	 * @return
	 */
	private void logout(String sessionId) throws Exception {
		EndSessionParam endSession = new EndSessionParam();
		endSession.setSessionId(sessionId);
		stub.endSession(endSession);
	}
	
	/**
	 * 往magento推定单
	 * 
	 * @throws Exception
	 */
	public String pushOrder(OrderDataBean order) throws Exception {
		// 在magento上生成订单时是否发生异常
		boolean isException = false;
		
		// 订单号
		String orderCreateResult = "";
		
		String sessionId = "";
		try {
			// login
			sessionId = login();
			
			// login成功
			if (!StringUtils.isNullOrBlank2(sessionId)) {
			
				// 创建购物车参数准备
				ShoppingCartCreateRequestParam request = new ShoppingCartCreateRequestParam();
				request.setSessionId(sessionId);
				request.setStore(customer.getStore());
				
				// 创建购物车
				ShoppingCartCreateResponseParam cartCreateResponse = stub.shoppingCartCreate(request);
				if (cartCreateResponse != null) {
					// 新创建购物车ID
					int shoppingCartId = cartCreateResponse.getResult();
					
					// 购物车创建成功
					if (shoppingCartId > 0) {
						
						// 顾客对象
						ShoppingCartCustomerEntity customerEntity = new ShoppingCartCustomerEntity();
						customerEntity.setMode(customer.getMode());
						customerEntity.setStore_id(customer.getStoreId());
						customerEntity.setWebsite_id(customer.getWebsiteId());
						customerEntity.setCustomer_id(customer.getCustomerId());
						
						// 购物车中顾客对象设置参数准备
						ShoppingCartCustomerSetRequestParam customer = new ShoppingCartCustomerSetRequestParam();
						customer.setSessionId(sessionId);
						customer.setCustomerData(customerEntity);
						customer.setQuoteId(shoppingCartId);
						// 购物车中顾客对象设置
						ShoppingCartCustomerSetResponseParam responseCustomer = stub.shoppingCartCustomerSet(customer);
						boolean isCustomerSuccess = responseCustomer.getResult();
						
						// 购物车中顾客对象设置成功
						if (isCustomerSuccess) {
							
							// 购物车中商品追加参数设置
							ShoppingCartProductAddRequestParam productAddRequest = new ShoppingCartProductAddRequestParam();
							productAddRequest.setQuoteId(shoppingCartId);
							productAddRequest.setSessionId(sessionId);
							productAddRequest.setStore(customer.getStore());
							
							ShoppingCartProductEntityArray productEntityArrays = new ShoppingCartProductEntityArray();
							
							List<OrderDetailBean> detailList = order.getOrderDetails();
							if (detailList != null && detailList.size() > 0) {
								for (OrderDetailBean orderDetail : detailList) {
									ShoppingCartProductEntity productEntity = new ShoppingCartProductEntity();
									productEntity.setSku(orderDetail.getSku());
									productEntity.setQty(orderDetail.getQty());
									productEntityArrays.addComplexObjectArray(productEntity);
								}
							}
							
							productAddRequest.setProductsData(productEntityArrays);
							
							ShoppingCartProductAddResponseParam productAddResponse = stub.shoppingCartProductAdd(productAddRequest);
							boolean isProductAddSuccess = productAddResponse.getResult();
							
							// 购物车中商品追加成功
							if (isProductAddSuccess) {
							
								// 购物车中顾客账单地址和收货地址设置参数准备
								ShoppingCartCustomerAddressesRequestParam addressRequest = new ShoppingCartCustomerAddressesRequestParam();
								addressRequest.setQuoteId(shoppingCartId);
								addressRequest.setSessionId(sessionId);
								addressRequest.setStore(customer.getStore());
								ShoppingCartCustomerAddressEntityArray addressEntity = new ShoppingCartCustomerAddressEntityArray();
								
								// 账单人信息
								ShoppingCartCustomerAddressEntity billingAddress = new ShoppingCartCustomerAddressEntity();
								billingAddress.setMode("billing");
								billingAddress.setFirstname(order.getBillingName());
								billingAddress.setCity(order.getBillingCity());
								billingAddress.setRegion(order.getBillingState());
								billingAddress.setStreet(order.getBillingAddress());
								billingAddress.setTelephone(order.getBillingTelephone());
								billingAddress.setPostcode(order.getBillingPostcode());
								billingAddress.setCountry_id(order.getBillingCountry());
								
								// 收货人信息
								ShoppingCartCustomerAddressEntity shippingAddress = new ShoppingCartCustomerAddressEntity();
								shippingAddress.setMode("shipping");
								shippingAddress.setFirstname(order.getShippingName());
								shippingAddress.setCity(order.getShippingCity());
								shippingAddress.setRegion(order.getShippingState());
								shippingAddress.setStreet(order.getShippingAddress());
								shippingAddress.setTelephone(order.getShippingTelephone());
								shippingAddress.setPostcode(order.getShippingPostcode());
								shippingAddress.setCountry_id(order.getShippingCountry());
								
								addressEntity.addComplexObjectArray(billingAddress);
								addressEntity.addComplexObjectArray(shippingAddress);
								addressRequest.setCustomerAddressData(addressEntity);
								
								// 购物车中顾客账单地址和收货地址设置
								ShoppingCartCustomerAddressesResponseParam addressResponse = stub.shoppingCartCustomerAddresses(addressRequest);
								boolean isAddressSuccess = addressResponse.getResult();
								
								// 购物车中顾客账单地址和收货地址设置成功
								if (isAddressSuccess) {
									
									// 发货方式设定参数准备
									ShoppingCartShippingMethodRequestParam shippingMethodRequest = new ShoppingCartShippingMethodRequestParam();
									shippingMethodRequest.setQuoteId(shoppingCartId);
									shippingMethodRequest.setSessionId(sessionId);
									shippingMethodRequest.setShippingMethod(order.getShippingMethod());
									shippingMethodRequest.setStore(customer.getStore());
									
									// 发货方式设定
									ShoppingCartShippingMethodResponseParam shippingMethodResponse = stub.shoppingCartShippingMethod(shippingMethodRequest);
									boolean isShippingMethodSuccess = shippingMethodResponse.getResult();
									
									// 发货方式设定成功
									if (isShippingMethodSuccess) {
										
										// 支付方式设定参数准备
										ShoppingCartPaymentMethodRequestParam paymentMethodRequest = new ShoppingCartPaymentMethodRequestParam();
										paymentMethodRequest.setQuoteId(shoppingCartId);
										paymentMethodRequest.setSessionId(sessionId);
										paymentMethodRequest.setStore(customer.getStore());
										ShoppingCartPaymentMethodEntity paymentMethodEntity = new ShoppingCartPaymentMethodEntity();
										paymentMethodEntity.setMethod(order.getPaymentMethod());
										paymentMethodRequest.setPaymentData(paymentMethodEntity);
										
										// 支付方式设定
										ShoppingCartPaymentMethodResponseParam paymentMethodResponse = stub.shoppingCartPaymentMethod(paymentMethodRequest);
										boolean isPaymentMethodSuccess = paymentMethodResponse.getResult();
										
										// 支付方式设定成功
										if (isPaymentMethodSuccess) {
											
											// 下订单参数准备
											ShoppingCartOrderRequestParam shoppingCartOrderRequestParam = new ShoppingCartOrderRequestParam();
											shoppingCartOrderRequestParam.setStore(customer.getStore());
											shoppingCartOrderRequestParam.setSessionId(sessionId);
											shoppingCartOrderRequestParam.setQuoteId(shoppingCartId);
											
											// 下订单
											ShoppingCartOrderResponseParam orderResponse = stub.shoppingCartOrder(shoppingCartOrderRequestParam);
											// 订单号
											orderCreateResult = orderResponse.getResult();
											
										} else {
//											logger.info("支付方式设定失败");
										}
									} else {
//										logger.info("发货方式设定失败");
									}
								} else {
//									logger.info("购物车中顾客账单地址和收货地址设置失败");
								}
							} else {
//								logger.info("购物车中商品追加失败");
							}
						} else {
//							logger.info("购物车中顾客对象设置失败");
						}
					} else {
//						logger.info("购物车创建失败");
					}
					
				} else {
//					logger.info("购物车创建失败");
				}
			} else {
//				logger.info("login is failure");
			}
		} catch (Exception ex) {
//			logger.error(ex, ex.getMessage());
			
			isException = true;
			
			throw ex;
		} finally {
			// end session
//			if (!StringUtils.isNullOrBlank2(sessionId)) {
				try {
					logout(sessionId);
				} catch (Exception ex) {
//					logger.error(ex, ex.getMessage());
					
					if (!isException) {
						throw ex;
					}
				}
//			}
		}
		
		// 返回订单号
		return orderCreateResult;
	}
	
	/**
	 * 库存API
	 * 
	 * @param skuList
	 * @return
	 * @throws Exception
	 */
	public List<InventoryStockItemBean> inventoryStockItemList(List<String> skuList) throws Exception {
		
		List<InventoryStockItemBean> inventoryList = new ArrayList<InventoryStockItemBean>();
		
		// 获得magento商品库存时是否发生异常
		boolean isException = false;
		
		if (skuList != null && skuList.size() > 0) {
		
			// login
			String sessionId = "";
			
			try {
				sessionId = login();
				
				// 入力参数初始化
				CatalogInventoryStockItemListRequestParam request = new CatalogInventoryStockItemListRequestParam();
				// 入力参数设置sessionId
				request.setSessionId(sessionId);
				
				ArrayOfString productIds = new ArrayOfString();
				for (String sku : skuList) {
		//			if (StringUtils.isNullOrBlank2(sku)) {
						// sku列表追加
						productIds.addComplexObjectArray(sku);
		//			}
					
				}
				
				// 请求的sku列表不为空
				if (productIds.getComplexObjectArray().length > 0) {
					// 入力参数设置sku列表
					request.setProductIds(productIds);
					
					// 向magento请求sku列表的库存
					CatalogInventoryStockItemListResponseParam inventoryParam = stub.catalogInventoryStockItemList(request);
					
					// 解析返回值
					CatalogInventoryStockItemEntity[] inventoryArray = inventoryParam.getResult().getComplexObjectArray();
					if (inventoryArray != null && inventoryArray.length > 0) {
						
						for (CatalogInventoryStockItemEntity inventory : inventoryArray) {
							InventoryStockItemBean item = new InventoryStockItemBean();
							// Defines whether the product is in stock
							item.setIsInStock(inventory.getIs_in_stock());
							// Product ID
							item.setProductId(inventory.getProduct_id());
							// Product SKU
							item.setSku(inventory.getSku());
							// Product quantity
							item.setQty(inventory.getQty());
							
							inventoryList.add(item);
						}
					}
					
				}
			} catch (Exception ex) {
//				logger.error(ex, ex.getMessage());
				
				isException = true;
				
				throw ex;
			} finally {
				// end session
//				if (!StringUtils.isNullOrBlank2(sessionId)) {
					try {
						logout(sessionId);
					} catch (Exception ex) {
//						logger.error(ex, ex.getMessage());
						
						if (!isException) {
							throw ex;
						}
					}
//				}
			}
		}
		
		// 返回库存列表信息
		return inventoryList;
	}
	
	/**
	 * 获得订单信息
	 * @param orderNumber
	 */
	public SalesOrderInfoResponseParam getSalesOrderInfo(String orderNumber) throws Exception {
		// 获得magento订单信息时是否发生异常
		boolean isException = false;
		
		String sessionId = "";
		try {
			// login
			sessionId = login();
			
			SalesOrderInfoRequestParam request = new SalesOrderInfoRequestParam();
			request.setOrderIncrementId(orderNumber);
			request.setSessionId(sessionId);
			
			SalesOrderInfoResponseParam response = stub.salesOrderInfo(request);
			
			return response;
			
		} catch (Exception ex) {
//			logger.error(ex, ex.getMessage());
			
			isException = true;
			
			throw ex;
		} finally {
			// end session
//			if (!StringUtils.isNullOrBlank2(sessionId)) {
				try {
					logout(sessionId);
				} catch (Exception ex) {
//					logger.error(ex, ex.getMessage());
					
					if (!isException) {
						throw ex;
					}
				}
//			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		MagentoApiServiceImpl service = new MagentoApiServiceImpl("004");
		service.login();

		List<String> skuList = new ArrayList<String>();
		skuList.add("0892179531");
		service.inventoryStockItemList(skuList);
//		OrderDataBean order = new OrderDataBean();
//		String orderNumber = service.pushOrder();
//		order.setShippingMethod("freeshipping_freeshipping");
//		order.setPaymentMethod("cashondelivery");
		service.getSalesOrderInfo("400000121");
		
//		System.out.println(orderNumber);
//		List<String> skuList= new ArrayList<String>();
//		skuList.add("wfwd32469-032-L");
//		List<CatalogInventoryStockItemEntity> inventoryList = service.catalogInventoryStockItemList(skuList);
//		System.out.print(inventoryList);
	}

}

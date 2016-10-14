package com.voyageone.task2.cms.service.product.batch;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceCalculateException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 高级检索业务的批量更新
 *
 * @author jiangjusheng on 2016/08/24
 * @version 2.0.0
 */
@Service
public class CmsBacthUpdateTask extends VOAbsLoggable {

    @Autowired
    private ProductService productService;
    @Autowired
    private PriceService priceService;

    public void onStartup(Map<String, Object> messageMap) {
        $debug("高级检索 批量更新 开始执行... param=" + messageMap.toString());
        String channleId = StringUtils.trimToNull((String) messageMap.get("_channleId"));
        String userName = StringUtils.trimToNull((String) messageMap.get("_userName"));
        List<String> codeList = (List<String>) messageMap.get("productIds");
        if (channleId == null || userName == null || codeList == null || codeList.isEmpty()) {
            $error("高级检索 批量更新 缺少参数");
            return;
        }

        Map<String, Object> prop = (Map<String, Object>) messageMap.get("property");
        if (prop == null || prop.isEmpty()) {
            $error("高级检索 批量更新 缺少property参数");
            return;
        }
        String prop_id = StringUtils.trimToEmpty((String) prop.get("id"));
        if ("hsCodePrivate".equals(prop_id) || "hsCodeCrop".equals(prop_id)) {
            // 税号更新
            String hsCode = null;
            Map<String, Object> valObj = (Map<String, Object>) prop.get("value");
            if (valObj != null) {
                hsCode = StringUtils.trimToEmpty((String) valObj.get("value"));
            }
            // 是否自动同步指导价到最终售价
            Boolean synPriceFlg = (Boolean) messageMap.get("synPrice");
            if (synPriceFlg == null) {
                synPriceFlg = false;
            }
            updateHsCode(prop_id, hsCode, codeList, channleId, userName, synPriceFlg);
        }
    }

    /*
     * 税号变更
     */
    private void updateHsCode(String propId, String propValue, List<String> codeList, String channelId, String userName, Boolean synPriceFlg) {
        String msg = "税号变更 " + propId + "=> " + propValue;
        // 未配置自动同步的店铺，显示同步状况
        if (synPriceFlg) {
            msg += " (同步价格)";
        } else {
            CmsChannelConfigBean autoApprovePrice = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_APPROVE_PRICE);
            if (autoApprovePrice == null || !"1".equals(autoApprovePrice.getConfigValue1())) {
                msg += " (未同步最终售价)";
            }
        }

        String msgTxt = msg;
        boolean isUpdFlg = false;
        JongoUpdate updObj = new JongoUpdate();
        WriteResult rs = null;

        for (String prodCode : codeList) {
            try {
                CmsBtProductModel newProduct = productService.getProductByCode(channelId, prodCode);
                String oldHsCode = StringUtils.trimToNull((String) newProduct.getCommonNotNull().getFieldsNotNull().get(propId));

                updObj.setQuery("{'common.fields.code':#}");
                updObj.setQueryParameters(prodCode);
                updObj.setUpdate("{$set:{'common.fields." + propId + "':#,'common.fields.hsCodeStatus':'1','common.fields.hsCodeSetter':#,'common.fields.hsCodeSetTime':#}}");
                updObj.setUpdateParameters(propValue, userName, DateTimeUtil.getNow());
                rs = productService.updateFirstProduct(updObj, channelId);
                $debug("高级检索 批量更新 更新税号结果 " + rs.toString());

                // 重新计算并保存价格
                priceService.setPrice(newProduct, synPriceFlg || oldHsCode == null);
                newProduct.getPlatforms().forEach((s, platform) -> {
                    if (platform.getCartId() != 0) {
                        productService.updateProductPlatform(channelId, newProduct.getProdId(), platform, userName, false, EnumProductOperationType.BatchUpdate, msgTxt);
                    }
                });

                // 确认指导价变更
                List<Integer> cartList = newProduct.getCartIdList();
                for (Integer cartVal : cartList) {
                    isUpdFlg = false;
                    List<BaseMongoMap<String, Object>> skuList = newProduct.getPlatform(cartVal).getSkus();
                    for (BaseMongoMap skuObj : skuList) {
                        Boolean isSaleFlg = (Boolean) skuObj.get("isSale");
                        String chgFlg = StringUtils.trimToEmpty(skuObj.getStringAttribute("priceChgFlg"));
                        if ((chgFlg.startsWith("U") || chgFlg.startsWith("D")) && isSaleFlg) {
                            // 指导价有变更
                            skuObj.put("priceChgFlg", "0");
                            skuObj.put("confPriceRetail", skuObj.getDoubleAttribute("priceRetail"));
                            isUpdFlg = true;
                        }
                    }

                    // 更新产品的信息
                    if (isUpdFlg) {
                        updObj.setQuery("{'common.fields.code':#}");
                        updObj.setQueryParameters(prodCode);
                        updObj.setUpdate("{$set:{'platforms.P" + cartVal + ".skus':#,'modified':#,'modifier':#}}");
                        updObj.setUpdateParameters(skuList, DateTimeUtil.getNowTimeStamp(), userName);

                        rs = productService.updateFirstProduct(updObj, channelId);
                        if (rs != null) {
                            $debug("高级检索 批量更新 指导价变更批量确认 code=%s, channelId=%s 执行结果=%s", prodCode, channelId, rs.toString());
                        }
                    }
                }
            } catch (PriceCalculateException e) {
                $error(String.format("高级检索 批量更新 价格计算错误 channleid=%s, prodcode=%s", channelId, prodCode), e);
                continue;
            } catch (IllegalPriceConfigException e) {
                $error(String.format("高级检索 批量更新 配置错误 channleid=%s, prodcode=%s", channelId, prodCode), e);
                continue;
            } catch (Throwable e) {
                $error(String.format("高级检索 批量更新 未知错误 channleid=%s, prodcode=%s", channelId, prodCode), e);
                continue;
            }
        }
    }
}
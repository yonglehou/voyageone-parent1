package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.CustomModuleUserParamGetAllImages;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.ims.rule_expression.CustomWordValueGetAllImages;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;

import java.util.*;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class CustomWordModuleGetAllImages extends CustomWordModule {

    public final static String moduleName = "GetAllImages";

    public CustomWordModuleGetAllImages() {
        super(moduleName);
    }

//    @Override
//    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user) {
//        return parse(customWord, expressionParser, sxData, shopBean, user, null);
//    }

    @Override
    public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception {
        //user param
        CustomModuleUserParamGetAllImages customModuleUserParamGetAllImages = ((CustomWordValueGetAllImages) customWord.getValue()).getUserParam();

        RuleExpression imageTemplateExpression = customModuleUserParamGetAllImages.getImageTemplate();
        RuleExpression htmlTemplateExpression = customModuleUserParamGetAllImages.getHtmlTemplate();
        RuleExpression imageTypeExpression = customModuleUserParamGetAllImages.getImageType();
        RuleExpression useOriUrlExpression = customModuleUserParamGetAllImages.getUseOriUrl();

        String imageTemplate = expressionParser.parse(imageTemplateExpression, shopBean, user, extParameter);
        String htmlTemplate= expressionParser.parse(htmlTemplateExpression, shopBean, user, extParameter);
        String imageTypeStr = expressionParser.parse(imageTypeExpression, shopBean, user, extParameter);
        CmsBtProductConstants.FieldImageType imageType = CmsBtProductConstants.FieldImageType.valueOf(imageTypeStr);
        String useOriUrlStr = expressionParser.parse(useOriUrlExpression, shopBean, user, extParameter);

        //system param
        List<CmsBtProductModel> sxProducts = sxData.getProductList();

        String parseResult = "";
        Set<String> imageSet = new HashSet<>();

        for (CmsBtProductModel sxProduct : sxProducts) {
            // modified by morse.lu 2016/06/02 start
//            List<CmsBtProductModel_Field_Image> cmsBtProductModelFieldImages = sxProduct.getFields().getImages(imageType);
            // 如果是PRODUCT，先看看image6有没有值，只要image6有一条，那么都从image6里取,否则还是去取image1
            List<CmsBtProductModel_Field_Image> cmsBtProductModelFieldImages = sxProductService.getProductImages(sxProduct, imageType);
            // modified by morse.lu 2016/06/02 end

            for (CmsBtProductModel_Field_Image cmsBtProductModelFieldImage : cmsBtProductModelFieldImages) {
                // 20160512 tom 有可能为空 add START
                if (StringUtils.isEmpty(cmsBtProductModelFieldImage.getName())) {
                    continue;
                }
                // 20160512 tom 有可能为空 add END
                // 20160513 tom 图片服务器切换 START
//                String completeImageUrl = String.format(imageTemplate, cmsBtProductModelFieldImage.getName());
                String completeImageUrl;
                if ("1".equals(useOriUrlStr)) {
                    // 使用原图
                    // start
                    try {
                        String url = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?req=imageprops", cmsBtProductModelFieldImage.getName());
                        $info("[CustomWordModuleGetAllImages]取得图片大小url:" + url);
                        String result = HttpUtils.get(url, null);
                        result = result.substring(result.indexOf("image"));
                        String[] args = result.split("image\\.");
                        Map<String, String> responseMap = new HashMap<>();
                        for (String param : args) {
                            if (param.indexOf("=") > 0) {
                                String[] keyVal = param.split("=");
                                if (keyVal.length > 1) {
                                    responseMap.put(keyVal[0], keyVal[1]);
                                } else {
                                    responseMap.put(keyVal[0], "");
                                }
                            }
                        }
                        completeImageUrl = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?fmt=jpg&scl=1&rgn=0,0,%s,%s", cmsBtProductModelFieldImage.getName(), responseMap.get("width"), responseMap.get("height"));
                        $info("[CustomWordModuleGetAllImages]取得原始图片url:" + completeImageUrl);
                    } catch (Exception e) {
                        throw new BusinessException("[CustomWordModuleGetAllImages]取得原始图片url失败!");
                    }
                    // end
                } else {
//                    completeImageUrl = sxProductService.getImageByTemplateId(sxData.getChannelId(), imageTemplate, cmsBtProductModelFieldImage.getName());
                    completeImageUrl = String.format(imageTemplate, cmsBtProductModelFieldImage.getName());
                }
                // 20160513 tom 图片服务器切换 END
//                completeImageUrl = sxProductService.encodeImageUrl(completeImageUrl);
                if (htmlTemplate != null) {
                    parseResult += String.format(htmlTemplate, completeImageUrl);
                }
                else {
                    parseResult += completeImageUrl;
                }
                imageSet.add(completeImageUrl);
            }
        }

        if (imageSet.size() > 0 && shopBean.getPlatform_id().equals(PlatFormEnums.PlatForm.TM)) {
            sxProductService.uploadImage(sxData.getChannelId(), sxData.getCartId(), String.valueOf(sxData.getGroupId()), shopBean, imageSet, user);
        }

        return parseResult;
    }
}

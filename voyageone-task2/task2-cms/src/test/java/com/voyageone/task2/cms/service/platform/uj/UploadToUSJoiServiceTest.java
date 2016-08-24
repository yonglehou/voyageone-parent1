package com.voyageone.task2.cms.service.platform.uj;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author james.li on 2016/4/7.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class UploadToUSJoiServiceTest {

    @Autowired
    UploadToUSJoiService uploadToUSJoiService;

    @Autowired
    ProductService productService;

    @Autowired
    CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;

    @Test
    public void testUpload() throws Exception {

        // 清除缓存（这样在synship.com_mt_value_channel表中刚追加的brand，productType，sizeType等初始化mapping信息就能立刻取得了）
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_TypeChannel.toString());
        // 清除缓存（这样在synship.tm_order_channel表中刚追加的cartIds信息就能立刻取得了）
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_OrderChannelConfigs.toString());

        String usjoiChannelId = "929";

        // --------------------------------------------------------------------------------------------
        // 品牌mapping表
        Map<String, String> mapBrandMapping = new HashMap<>();
        // 产品分类mapping表
        Map<String, String> mapProductTypeMapping = new HashMap<>();
        // 适用人群mapping表
        Map<String, String> mapSizeTypeMapping = new HashMap<>();

        // 品牌mapping作成
        List<TypeChannelBean> brandTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.BRAND_41, usjoiChannelId);
        if (ListUtils.notNull(brandTypeChannelBeanList)) {
            for (TypeChannelBean typeChannelBean : brandTypeChannelBeanList) {
                if (!StringUtils.isEmpty(typeChannelBean.getAdd_name1())
                        && !StringUtils.isEmpty(typeChannelBean.getName())
                        && Constants.LANGUAGE.EN.equals(typeChannelBean.getLang_id())
                        ) {
                    // 品牌mapping表中key,value都设为小写(feed进来的brand不区分大小写)
                    mapBrandMapping.put(typeChannelBean.getAdd_name1().toLowerCase().trim(), typeChannelBean.getName().toLowerCase().trim());
                }
            }
        }

        // 产品分类mapping作成
        List<TypeChannelBean> productTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.PROUDCT_TYPE_57, usjoiChannelId);
        if (ListUtils.notNull(productTypeChannelBeanList)) {
            for (TypeChannelBean typeChannelBean : productTypeChannelBeanList) {
                if (!StringUtils.isEmpty(typeChannelBean.getValue())
                        && !StringUtils.isEmpty(typeChannelBean.getName())
                        && Constants.LANGUAGE.EN.equals(typeChannelBean.getLang_id())
                        ) {
                    // 产品分类mapping表(value是key,name和add_name1是值)
                    mapProductTypeMapping.put(typeChannelBean.getValue().trim(), typeChannelBean.getName().trim());
                }
            }
        }

        // 适用人群mapping作成
        List<TypeChannelBean> sizeTypeChannelBeanList = TypeChannels.getTypeList(Constants.comMtTypeChannel.PROUDCT_TYPE_58, usjoiChannelId);
        if (ListUtils.notNull(sizeTypeChannelBeanList)) {
            for (TypeChannelBean typeChannelBean : sizeTypeChannelBeanList) {
                if (!StringUtils.isEmpty(typeChannelBean.getValue())
                        && !StringUtils.isEmpty(typeChannelBean.getName())
                        && Constants.LANGUAGE.EN.equals(typeChannelBean.getLang_id())
                        ) {
                    // 适用人群mapping作成(value是key,name和add_name1是值)
                    mapSizeTypeMapping.put(typeChannelBean.getValue().trim(), typeChannelBean.getName().trim());
                }
            }
        }
        // --------------------------------------------------------------------------------------------

        // 从synship.tm_order_channel表中取得USJOI店铺channel对应的cartId列表（一般只有一条cartId.如928对应28, 929对应29）
        // 用于product.PXX追加平台信息(group表里面用到的用于展示的cartId不是从这里取得的)
        final List<Integer> cartIds;
        OrderChannelBean usJoiBean = Channels.getChannel(usjoiChannelId);
        if (usJoiBean != null && !StringUtil.isEmpty(usJoiBean.getCart_ids())) {
            cartIds = Arrays.asList(usJoiBean.getCart_ids().split(",")).stream().map(Integer::parseInt).collect(toList());
        } else {
            cartIds = new ArrayList<>();
        }

        CmsBtSxWorkloadModel sxWorkLoadBean = new CmsBtSxWorkloadModel();
        sxWorkLoadBean.setChannelId("017");
        sxWorkLoadBean.setGroupId(12922L);
        sxWorkLoadBean.setModifier("james");
        sxWorkLoadBean.setCartId(Integer.parseInt(usjoiChannelId)); // "929"

        uploadToUSJoiService.upload(sxWorkLoadBean, mapBrandMapping, mapProductTypeMapping, mapSizeTypeMapping, cartIds);
    }

    @Test
    public void testProduct() {
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode("010", "16189");
        CmsBtProductModel_Platform_Cart cmsBtProductModel_platform_cart = new CmsBtProductModel_Platform_Cart();
        cmsBtProductModel_platform_cart.setpNumIId("1111");
        cmsBtProductModel_platform_cart.setpCatId("2222");

        cmsBtProductModel_platform_cart.setFields(new BaseMongoMap<>());
        cmsBtProductModel_platform_cart.getFields().put("8652", "aaa");

        cmsBtProductModel.setPlatform(26, cmsBtProductModel_platform_cart);
        ProductUpdateBean productUpdateBean = new ProductUpdateBean();
        productUpdateBean.setProductModel(cmsBtProductModel);
        productUpdateBean.setIsCheckModifed(false);

//        CmsMtPlatformCategorySchemaModel platformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel("1349", 26);
//        List<Field> fields = SchemaReader.readXmlForList(platformCategorySchemaModel.getPropsItem());
//
//        CmsBtProductModel_Platform_Cart platformCart = cmsBtProductModel.getPlatform(26);
//        if (platformCart != null) {
//            BaseMongoMap<String, Object> fieldsValue = platformCart.getFields();
//            FieldUtil.setFieldsValueFromMap(fields, fieldsValue);
//            FieldUtil.getFieldsValueToMap(fields);
//        }
//        productService.updateProduct("010", productUpdateBean);
    }

    @Test
    public void testOnStartup() throws Exception {

        uploadToUSJoiService.onStartup(new ArrayList<>());
    }

    @Test
    public void testOnStartup1() throws Exception {

    }

    @Test
    public void testUpload1() throws Exception {

    }
}
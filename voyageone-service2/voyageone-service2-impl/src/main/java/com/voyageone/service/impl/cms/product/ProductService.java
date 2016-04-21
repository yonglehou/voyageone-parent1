package com.voyageone.service.impl.cms.product;

import com.google.common.base.Joiner;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.JomgoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.BeanUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.*;
import com.voyageone.service.dao.cms.CmsBtChannelConfigDao;
import com.voyageone.service.dao.cms.CmsBtPriceLogDao;
import com.voyageone.service.dao.cms.CmsBtSxWorkloadDao;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductLogDao;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtChannelConfigModel;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.service.model.wms.WmsBtInventoryCenterLogicModel;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * product Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 */
@Service
public class ProductService extends BaseService {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    protected CmsBtProductLogDao cmsBtProductLogDao;

    @Autowired
    private CmsBtPriceLogDao cmsBtPriceLogDao;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private CmsBtSxWorkloadDao cmsBtSxWorkloadDao;

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Autowired
    private WmsBtInventoryCenterLogicDao wmsBtInventoryCenterLogicDao;

    @Autowired
    private CmsMtChannelConfigDao cmsMtChannelConfigDao;

    /**
     * 获取商品 根据ID获
     */
    public CmsBtProductModel getProductById(String channelId, long prodId) {
        String query = "{\"prodId\":" + prodId + "}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据Code
     */
    public CmsBtProductModel getProductByCode(String channelId, String code) {
        String query = "{\"fields.code\":\"" + code + "\"}";
        return cmsBtProductDao.selectOneWithQuery(query, channelId);
    }

    /**
     * 根据多个groupsIds获取产品列表
     * @param channelId
     * @param groupId
     * @param flag: true:检索主商品以外的商品,false:检索所有的商品
     * @return
     */
    public List<CmsBtProductModel> getProductByGroupId(String channelId, Long groupId, Boolean flag) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(String.format("{ \"groupId\":%d}", groupId));
        CmsBtProductGroupModel grpObj = cmsBtProductGroupDao.selectOneWithQuery(queryObject, channelId);
        if (grpObj == null) {
            return null;
        }
        List<String> codeList = grpObj.getProductCodes();
        if (codeList == null || codeList.isEmpty()) {
            return null;
        }
        if (flag) {
            // 检索主商品以外的商品时，若该组仅有一个主商品，没有子商品，则返回空
            if (codeList.size() <= 1) {
                return null;
            } else {
                codeList.remove(0);
            }
        }

        String[] codeArr = new String[codeList.size()];
        codeArr = codeList.toArray(codeArr);
        queryObject.setQuery("{" + MongoUtils.splicingValue("fields.code", codeArr, "$in") + "}");

        return cmsBtProductDao.select(queryObject, channelId);
    }

    // 查询指定平台下各商品组中包含的商品code
    // 返回的map中，key是group id，value中是商品code列表
    public Map<String, List<String>> getProductGroupIdCodesMapByCart(String channelId, int cartId, String orgChannelId) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"cartId\":" + cartId + "}");
        queryObject.setProjection("{'groupId':1,'productCodes':1,'_id':0}");
        List<CmsBtProductGroupModel> grpList = cmsBtProductGroupDao.select(queryObject, channelId);

        Map<String, List<String>> result = new LinkedHashMap<>();
        for (CmsBtProductGroupModel grpObj : grpList) {
              result.put(grpObj.getGroupId().toString(), grpObj.getProductCodes());
        }
        return result;
    }

    /**
     * getList
     */
    public List<CmsBtProductModel> getList(String channelId, Set<Long> pids, String[] projections) {
        JomgoQuery queryObject = new JomgoQuery();
        String pidsArrStr = Joiner.on(", ").skipNulls().join(pids);
        queryObject.setQuery(String.format("{ \"prodId\" : { $in : [ %s ] } }", pidsArrStr));
        queryObject.setProjection(projections);
        return getList(channelId, queryObject);
    }

    /**
     * getList
     */
    public List<CmsBtProductModel> getList(String channelId, String queryStr) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(queryStr);
        return cmsBtProductDao.select(queryObject, channelId);
    }

    /**
     * getList
     */
    public List<CmsBtProductModel> getList(String channelId, JomgoQuery queryObject) {
        return cmsBtProductDao.select(queryObject, channelId);
    }

    /**
     * getCnt
     */
    public long getCnt(String channelId, String queryStr) {
        return cmsBtProductDao.countByQuery(queryStr, channelId);
    }

    /**
     * get prices log list
     */
    public List<CmsBtPriceLogModel> getPriceLog(String channelId, Map<String, Object> params) {
        params.put("channelId", channelId);
        String flag = (String) params.get("flag");
        if ("sku".equals(flag)){
            params.put("sku", "0");
        }
        return cmsBtPriceLogDao.selectPriceLogByCode(params);
    }

    /**
     * get prices log list
     */
    public int getPriceLogCnt(String channelId, Map<String, Object> params) {
        params.put("channelId", channelId);
        String flag = (String) params.get("flag");
        if ("sku".equals(flag)){
            params.put("sku", "0");
        }
        return cmsBtPriceLogDao.selectPriceLogByCodeCnt(params);
    }

    public boolean checkProductDataIsReady(String channelId, Long productId) {
        return cmsBtProductDao.checkProductDataIsReady(channelId, productId);
    }

    /**
     * 插入商品
     */
    public WriteResult insert(CmsBtProductModel model) {
        return cmsBtProductDao.insert(model);
    }

    /**
     * 插入商品
     */
    public WriteResult insert(Collection<CmsBtProductModel> models) {
        return cmsBtProductDao.insertWithList(models);
    }

    /**
     * add products
     */
    public void createProduct(String channelId, final CmsBtProductModel product, String modifier) {
        /**
         * check row exist
         */
        String prodIdQuery = String.format("{ \"prodId\" : %s }", product.getProdId());
        long count = cmsBtProductDao.countByQuery(prodIdQuery, channelId);
        if (count > 0) {
            throw new RuntimeException("prodId has existed, not add!");
        }

        String prodCodeQuery = String.format("{ \"fields.code\" : \"%s\" }", product.getFields().getCode());
        count = cmsBtProductDao.countByQuery(prodCodeQuery, channelId);
        if (count > 0) {
            throw new RuntimeException("fields.code has existed, not add!");
        }

        //update channel and modifier
        CmsBtProductGroupModel grp = product.getGroups();
        product.setGroups(null);
        product.setChannelId(channelId);
        product.setCreater(modifier);
        product.setModifier(modifier);

        //save
        cmsBtProductDao.insert(product);
        product.setGroups(grp);
    }

    /**
     * update product
     */
    public void updateProduct(String channelId, ProductUpdateBean request) {
        CmsBtProductModel productModel = request.getProductModel();
        Long prodId = productModel.getProdId();
        String productCode = null;
        if (productModel.getFields() != null) {
            productCode = productModel.getFields().getCode();
        }

        String queryStr = null;
        HashMap<String, Object> queryMap = new HashMap<>();
        if (prodId != null) {
            queryStr = String.format("{\"prodId\" : %s}", prodId);
            queryMap.put("prodId", prodId);
        } else if (!StringUtils.isEmpty(productCode)) {
            queryStr = String.format("{\"fields.code\" : \"%s\" }", productCode);
            queryMap.put("fields.code", productCode);
        }

        if (StringUtils.isEmpty(queryStr)) {
            return;
        }

        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(queryStr);
        queryObject.setProjection("prodId", "modified", "fields.status", "groups");

        CmsBtProductModel findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        if (findModel == null) {
            throw new RuntimeException("product not found!");
        }

        if (request.getIsCheckModifed()) {
            if (findModel.getModified() != null && !findModel.getModified().equals(productModel.getModified())) {
                throw new RuntimeException("product has been update, not update!");
            }
        }

        List<BulkUpdateModel> bulkList = new ArrayList<>();

        HashMap<String, Object> updateMap = new HashMap<>();

        /**
         * common attribute
         */
        String catId = productModel.getCatId();
        if (catId != null) {
            updateMap.put("catId", catId);
        }
        String catPath = productModel.getCatPath();
        if (catPath != null) {
            updateMap.put("catPath", catPath);
        }

        String modified = DateTimeUtil.getNowTimeStamp();
        if (request.getModified() != null) {
            modified = request.getModified();
        }
        updateMap.put("modified", modified);
        updateMap.put("modifier", request.getModifier());

        /**
         * Fields
         */
        CmsBtProductModel_Field fields = productModel.getFields();
        if (fields != null && fields.size() > 0) {
            BasicDBObject fieldObj = fields.toUpdateBasicDBObject("fields.");
            updateMap.putAll(fieldObj);
        }

        /**
         * Feed
         */
        CmsBtProductModel_Feed feed = productModel.getFeed();
        if (feed != null) {
            if (feed.getOrgAtts() != null && feed.getOrgAtts().size() > 0) {
                BasicDBObject orgAttsObj = feed.getOrgAtts().toUpdateBasicDBObject("feed.orgAtts.");
                updateMap.putAll(orgAttsObj);
            }
            if (feed.getCnAtts() != null && feed.getCnAtts().size() > 0) {
                BasicDBObject cnAttsObj = feed.getCnAtts().toUpdateBasicDBObject("feed.cnAtts.");
                updateMap.putAll(cnAttsObj);
            }
            if (feed.getCustomIds() != null && feed.getCustomIds().size() > 0) {
                updateMap.put("feed.customIds", feed.getCustomIds());
            }
        }

        /**
         * set update model
         */
        if (updateMap.size() > 0) {
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        }

        /**
         * execute update
         */
        if (bulkList.size() > 0) {

            if (findModel.getFields() != null && findModel.getFields().getStatus() != null
                    && productModel.getFields() != null && productModel.getFields().getStatus() != null) {
                //insert　ProductHistory
                CmsConstants.ProductStatus befStatus = CmsConstants.ProductStatus.valueOf(findModel.getFields().getStatus());
                CmsConstants.ProductStatus aftStatus = CmsConstants.ProductStatus.valueOf(productModel.getFields().getStatus());
                insertProductHistory(befStatus, aftStatus, channelId, findModel.getProdId());

                //insert　SxWorkLoad
                String modifier = "0";
                if (!StringUtils.isEmpty(request.getModifier())) {
                    modifier = request.getModifier();
                }
                insertSxWorkLoad(befStatus, aftStatus, channelId, productCode, modifier);
            }

            /**
             * 更新产品数据
             */
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
        }

        /**
         * 更新产品sku数据
         */
        List<CmsBtProductModel_Sku> skus = productModel.getSkus();
        if (skus != null && skus.size() > 0) {

            // 如果sku价格发生变化更新product/model的price
            ProductPriceBean model = new ProductPriceBean();
            model.setProductId(findModel.getProdId());

            // 设置sku的价格.
            for (CmsBtProductModel_Sku sku : skus) {
                ProductSkuPriceBean skuPriceModel = new ProductSkuPriceBean();
                skuPriceModel.setSkuCode(sku.getSkuCode());
                skuPriceModel.setPriceMsrp(sku.getPriceMsrp());
                skuPriceModel.setPriceRetail(sku.getPriceRetail());
                skuPriceModel.setPriceSale(sku.getPriceSale());
                //vendor price update
                skuPriceModel.setClientNetPrice(sku.getClientNetPrice());
                skuPriceModel.setClientMsrpPrice(sku.getClientMsrpPrice());
                skuPriceModel.setClientRetailPrice(sku.getClientRetailPrice());
                model.addSkuPrice(skuPriceModel);
            }

            List<ProductPriceBean> productPriceBeanList = new ArrayList<>();

            productPriceBeanList.add(model);
            productSkuService.updatePrices(channelId, productPriceBeanList, request.getModifier());

            // 更新sku信息
            productSkuService.saveSkus(channelId, findModel.getProdId(), skus);
        }

    }

    private void insertProductHistory(CmsConstants.ProductStatus befStatus,
                                      CmsConstants.ProductStatus aftStatus,
                                      String channelId, Long productId) {
        if (befStatus != null && aftStatus != null && !befStatus.equals(aftStatus)) {
            if (productId != null) {
                CmsBtProductModel productModel = getProductById(channelId, productId);
                CmsBtProductLogModel logModel = new CmsBtProductLogModel();
                BeanUtil.copy(productModel, logModel);
                logModel.set_id(null);
                cmsBtProductLogDao.insert(logModel);
            }
        }
    }

    private void insertSxWorkLoad(CmsConstants.ProductStatus befStatus,
                                  CmsConstants.ProductStatus aftStatus,
                                  String channelId, String productCode, String modifier) {
        if (befStatus != null && aftStatus != null) {
            boolean isNeed = false;
            // 从其他状态转为Pending
            if (befStatus != CmsConstants.ProductStatus.Approved && aftStatus == CmsConstants.ProductStatus.Approved) {
                isNeed = true;
                // 从Pending转为其他状态
                // 在Pending下变更了
            } else if (befStatus == CmsConstants.ProductStatus.Approved) {
                isNeed = true;
            }

            // 获得该店铺的上新平台列表
            List<Integer> carts = new ArrayList<>();
            for(TypeChannelBean typeChannelBean : TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en")){
                carts.add(Integer.valueOf(typeChannelBean.getValue()));
            }

            // 根据商品code获取其所有group信息(所有平台)
            List<CmsBtProductGroupModel> grpList = cmsBtProductGroupDao.select("{'productCodes':{'$in':['" + productCode + "']}}", channelId);

            // 获取所有的可上新的平台group信息
            List<CmsBtSxWorkloadModel> models = new ArrayList<>();
            for(CmsBtProductGroupModel platform : grpList) {
                CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
                if (carts.contains(platform.getCartId()) && isNeed) {
                    model.setChannelId(channelId);
                    model.setGroupId(platform.getGroupId());
                    model.setCartId(platform.getCartId());
                    model.setPublishStatus(0);
                    model.setCreater(modifier);
                    model.setModifier(modifier);
                    models.add(model);
                }
            }

            if (models.size() > 0) {
                cmsBtSxWorkloadDao.insertSxWorkloadModels(models);
            }
        }
    }

    /**
     * confirm change category
     */
    public Map<String, Object> changeProductCategory(String channelId, String categoryId, String categoryPath, List<String> models, String modifier) {

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("catId", categoryId);
        updateMap.put("catPath", categoryPath);
        updateMap.put("batchField.switchCategory", 1);

        List<BulkUpdateModel> bulkList = new ArrayList<>();

        for (String modelCode : models) {
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("feed.orgAtts.modelCode", modelCode);
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        }

        // 批量更新product表
        BulkWriteResult result = null;
        if (bulkList.size() > 0) {
            result = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");
        }

        // 批量更新feed表
        int updateFeedInfoCount = cmsBtFeedInfoDao.updateFeedInfoUpdFlg(channelId, models.toArray(new String[models.size()]));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("updFeedInfoCount", updateFeedInfoCount);
        if (result != null) {
            resultMap.put("updProductCount", result.getModifiedCount());
            resultMap.put("modifiedCount", result.getModifiedCount() + updateFeedInfoCount);
        } else {
            resultMap.put("updProductCount", 0);
            resultMap.put("modifiedCount", 0);
        }

        return resultMap;
    }

    /**
     * 根据groupId批量更新产品的信息
     * @param channelId String
     * @param prodCode String
     * @param updateMap Map
     * @param modifier String
     */
    public int updateTranslation(String channelId, String prodCode, Map<String, Object> updateMap, String modifier) {
        // 先根据产品code找到其model
        CmsBtProductModel prodObj = cmsBtProductDao.selectOneWithQuery("{'fields.code':'" + prodCode + "'},{'fields.model':1,'_id':0}",channelId);
        String prodModel = prodObj.getFields().getModel();

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("fields.model", prodModel);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        bulkList.add(model);

        // 批量更新product表
        int result = 0;
        if (bulkList.size() > 0) {
            BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");
            result = bulkWriteResult.getModifiedCount();
        }
        return result;
    }

    /**
     * get the product info from wms's request
     */
    public ProductForWmsBean getWmsProductsInfo(String channelId, String productSku, String[] projection) {
        JomgoQuery queryObject = new JomgoQuery();
        // set fields
        if (projection != null && projection.length > 0) {
            queryObject.setProjection(projection);
        }

        if (!StringUtils.isEmpty(productSku)) {
            queryObject.setQuery(String.format("{\"skus.skuCode\" : \"%s\" }", productSku));
        }

        ProductForWmsBean resultInfo = null;
        if (queryObject.getQuery() != null) {
            resultInfo = new ProductForWmsBean();

            CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
            resultInfo.setChannelId(product.getChannelId());
            resultInfo.setCode(product.getFields().getCode());
            resultInfo.setName(product.getFields().getProductNameEn());
            resultInfo.setProductId(product.getProdId().toString());
            resultInfo.setShortDescription(product.getFields().getShortDesEn());
            resultInfo.setLongDescription(product.getFields().getLongDesEn());
            // TODO set productType(but now productType is not commen field)
            resultInfo.setDescription(product.getFields().getProductType());
            resultInfo.setBrandName(product.getFields().getBrand());
            resultInfo.setGender(product.getFields().getSizeType());
            // TODO 无法提供,属于主数据的非共通属性
            resultInfo.setMaterialFabricName("");
            resultInfo.setCountryName(product.getFields().getOrigin());

            // 设置人民币价格
            resultInfo.setMsrp(product.getSku(productSku).getPriceMsrp() != null ? product.getSku(productSku).getPriceMsrp().toString() : "0.00");
            resultInfo.setPrice(product.getSku(productSku).getPriceSale() != null ? product.getSku(productSku).getPriceSale().toString() : "0.00");
            resultInfo.setRetailPrice(product.getSku(productSku).getPriceRetail() != null ? product.getSku(productSku).getPriceRetail().toString() : "0.00");

            // 设置美元价格
            resultInfo.setClientMsrpPrice(String.valueOf(product.getSku(productSku).getClientMsrpPrice()));
            resultInfo.setClientRetailPrice(String.valueOf(product.getSku(productSku).getClientRetailPrice()));
            resultInfo.setClientNetPrice(String.valueOf(product.getSku(productSku).getClientNetPrice()));

            // 设置原始价格单位
            List<CmsMtChannelConfigModel> channelConfigs = cmsMtChannelConfigDao.selectByConfigKey(channelId, CmsConstants.channelConfig.CLIENT_PRICE_UNIT);
            resultInfo.setClientPriceUnit(channelConfigs.get(0).getConfigValue1());

            // TODO 无法提供,属于主数据的非共通属性
            resultInfo.setWeightkg("");
            // TODO 无法提供,属于主数据的非共通属性
            resultInfo.setWeightlb("");
            resultInfo.setModelName(product.getFields().getModel());
            // TODO 无法提供,属于主数据的非共通属性
            resultInfo.setUrlKey("");
            // TODO 写死,取得是S7图片显示的路径
            String imagePath = "";
            if (product.getFields().getImages1().size() > 0) {
                if (!StringUtils.isEmpty(product.getFields().getImages1().get(0).getName()))
                    imagePath = Constants.productForOtherSystemInfo.IMG_URL + product.getFields().getImages1().get(0).getName();
            }
            resultInfo.setShowName(imagePath);
            resultInfo.setCnName(product.getFields().getLongTitle());
            // 获取HsCodeCrop
            String hsCodeCrop = product.getFields().getHsCodeCrop();
            if (!StringUtils.isEmpty(hsCodeCrop)) {
                TypeChannelBean bean = TypeChannels.getTypeChannelByCode(Constants.productForOtherSystemInfo.HS_CODE_CROP, channelId, hsCodeCrop);
                if (bean != null) {
                    String[] hsCode = bean.getName().split(",");
                    resultInfo.setHsCodeId(hsCodeCrop);
                    resultInfo.setHsCode(hsCode[1]);
                    resultInfo.setHsDescription(hsCode[2]);
                    resultInfo.setUnit(hsCode[3]);
                }
            }
            // 获取HsCodePrivate
            String hsCodePrivate = product.getFields().getHsCodePrivate();
            if (!StringUtils.isEmpty(hsCodePrivate)) {
                TypeChannelBean bean = TypeChannels.getTypeChannelByCode(Constants.productForOtherSystemInfo.HS_CODE_PRIVATE, channelId, hsCodePrivate);
                if (bean != null) {
                    String[] hsCodePu = bean.getName().split(",");
                    resultInfo.setHsCodePuId(hsCodePrivate);
                    resultInfo.setHsCodePu(hsCodePu[0]);
                    resultInfo.setHsDescriptionPu(hsCodePu[1]);
                    resultInfo.setUnitPu(hsCodePu[2]);
                }
            }
        }
        return resultInfo;
    }

    /**
     * get the product list from oms's request
     */
    public List<ProductForOmsBean> getOmsProductsInfo(String channelId,
                                                       String skuIncludes, List<String> skuList,
                                                       String nameIncludes,
                                                       String descriptionIncludes,
                                                       String cartId,
                                                       String[] projection) {
        JomgoQuery queryObject = new JomgoQuery();
        // set fields
        if (projection != null && projection.length > 0) {
            queryObject.setProjection(projection);
        }

        StringBuilder sbQuery = new StringBuilder();

        if (!StringUtils.isEmpty(skuIncludes)) {
            sbQuery.append(MongoUtils.splicingValue("skus.skuCode", skuIncludes, "$regex"));
            sbQuery.append(",");
        } else if (skuList != null && skuList.size() > 0) {
            sbQuery.append(MongoUtils.splicingValue("skus.skuCode", skuList.toArray(new String[skuList.size()])));
            sbQuery.append(",");
        }

        // 设定name的模糊查询
        if (!StringUtils.isEmpty(nameIncludes)) {
            sbQuery.append(MongoUtils.splicingValue("fields.productNameEn", nameIncludes, "$regex"));
            sbQuery.append(",");
        }

        // 设定description的模糊查询
        if (!StringUtils.isEmpty(descriptionIncludes)) {
            sbQuery.append(MongoUtils.splicingValue("fields.longDesEn", descriptionIncludes, "$regex"));
            sbQuery.append(",");
        }

        if (!StringUtils.isEmpty(cartId)) {
            sbQuery.append(MongoUtils.splicingValue("fields.productCarts", Integer.valueOf(cartId)));
            sbQuery.append(",");
        }

        if (!StringUtils.isEmpty(sbQuery.toString())) {
            queryObject.setQuery("{" + sbQuery.toString().substring(0, sbQuery.toString().length() - 1) + "}");
        }

        queryObject.setLimit(50);
        List<CmsBtProductModel> products = cmsBtProductDao.select(queryObject, channelId);

        List<ProductForOmsBean> resultInfo = new ArrayList<>();
        for (CmsBtProductModel product : products) {
            for (CmsBtProductModel_Sku sku : product.getSkus()) {
                ProductForOmsBean bean = new ProductForOmsBean();
                // TODO 目前无法取得,先设置“”
                bean.setChannelId("");
                bean.setSku(sku.getSkuCode());
                bean.setProduct(product.getFields().getProductNameEn());
                bean.setDescription(product.getFields().getLongDesEn());
                bean.setPricePerUnit(sku.getPriceSale() != null ? sku.getPriceSale().toString() : "0.00");
                // TODO 目前无法取得库存值
                Map<String, Object> param = new HashMap<>();
                param.put("channelId", channelId);
                param.put("sku", sku.getSkuCode());
                WmsBtInventoryCenterLogicModel skuInfo = wmsBtInventoryCenterLogicDao.selectItemDetailBySku(param);
                bean.setInventory(String.valueOf(skuInfo.getQtyChina()));
                // TODO 写死,取得是S7图片显示的路径
                String imagePath = "";
                if (product.getFields().getImages1().size() > 0) {
                    if (!StringUtils.isEmpty(product.getFields().getImages1().get(0).getName()))
                        imagePath = Constants.productForOtherSystemInfo.IMG_URL + product.getFields().getImages1().get(0).getName();
                }
                bean.setImgPath(imagePath);

                // 取得该商品的组信息
                CmsBtProductGroupModel grpObj = cmsBtProductGroupDao.selectOneWithQuery("{'cartId':" + cartId + ",'productCodes':'" + product.getFields().getCode() + "'},{'numIid':1,'_id':0}", channelId);

                // TODO 目前写死,以后再想办法修改
                String numIid = "";
                switch (CartEnums.Cart.getValueByID(cartId)) {
                    case TG:
                        numIid = grpObj != null && !StringUtils.isEmpty(grpObj.getNumIId())
                                ? Constants.productForOtherSystemInfo.TMALL_NUM_IID + grpObj.getNumIId() : "";
                        break;
                }
                bean.setSkuTmallUrl(numIid);

                resultInfo.add(bean);
            }
        }

        return resultInfo;
    }


    /**
     * distributeTranslation 分配翻译商品
     */
    public List<CmsBtProductModel> translateDistribute(String channelId, ProductTransDistrBean param) {
        /**
         * lock data
         */
        String nowStr = DateTimeUtil.getNow();
        int getCount = param.getLimit();
        String translator = param.getTranslator();
        int translateTimeHDiff = param.getTranslateTimeHDiff();
        int distributeRule = param.getDistributeRule();

        String queryStrTmp;
        switch (distributeRule){
            case 0:
                // add translateTime condition
                queryStrTmp = "{\"$or\":" +
                        "[{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translateStatus\":{\"$in\":[null,\"\", \"0\"]},\"fields.translator\":{\"$in\":[null,\"\"]}}," +
                        "{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translator\":{\"$nin\":[null,\"\"]},\"fields.translateTime\":{\"$lt\":\"%s\"}}]}";
                break;
            case 1:
                queryStrTmp = "{\"$or\":" +
                        "[{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translateStatus\":{\"$in\":[null,\"\",\"0\"]},\"fields.translator\":{\"$in\":[null,\"\"]},\"fields.isMasterMain\":1}," +
                        "{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translator\":{\"$nin\":[null,\"\"]},\"fields.translateTime\":{\"$lt\":\"%s\"},\"fields.isMasterMain\":1}]}";
                break;
            default:
                // add translateTime condition
                queryStrTmp = "{\"$or\":" +
                        "[{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translateStatus\":{\"$in\":[null,\"\", \"0\"]},\"fields.translator\":{\"$in\":[null,\"\"]}}," +
                        "{\"fields.status\":{\"$nin\":[\"New\"]},\"fields.translator\":{\"$nin\":[null,\"\"]},\"fields.translateTime\":{\"$lt\":\"%s\"}}]}";
                break;
        }

        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), -translateTimeHDiff);
        String translateTimeStr = DateTimeUtil.format(date, null);

        JomgoUpdate updateObject = new JomgoUpdate();
        // create query string
        String queryStr = String.format(queryStrTmp, translateTimeStr);
        updateObject.setQuery(queryStr);

        // create Projection
        updateObject.setProjection(param.getProjectionArr());

        // create sort String
        updateObject.setSort(param.getSortStr());

        // create Update string
        String strUpdateTmp = "{\"$set\":{\"fields.translateStatus\":\"0\", \"fields.translator\":\"%s\", \"fields.translateTime\":\"%s\"}}";
        String updateStr = String.format(strUpdateTmp, translator, nowStr);
        updateObject.setUpdate(updateStr);

        List<CmsBtProductModel> products = new ArrayList<>();
        //update translator translateTime
        for (int i=0; i<getCount; i++) {
            CmsBtProductModel productModel = cmsBtProductDao.findAndModify(updateObject, channelId);
            if (productModel != null) {
                products.add(productModel);
            } else {
                break;
            }
        }

        return products;
    }

    /**
     * 获取Sku的库存信息
     */
    public Map<String, Integer> getProductSkuQty(String channelId, String productCode) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("channelId", channelId);
        queryMap.put("code", productCode);

        List<WmsBtInventoryCenterLogicModel> inventoryList = wmsBtInventoryCenterLogicDao.selectItemDetailByCode(queryMap);

        Map<String, Integer> result = new HashMap<>();
        for (WmsBtInventoryCenterLogicModel inventory : inventoryList) {
            result.put(inventory.getSku(), inventory.getQtyChina());
        }
        return result;
    }

    /**
     * 批量更新上新结果 根据CodeList
     */
    public BulkWriteResult bathUpdateWithSXResult(String channelId, int cartId,
                                                  long groupId, List<String> codeList,
                                                  String numIId, String productId,
                                                  String publishTime, String onSalesTime, String instockTime,
                                                  CmsConstants.PlatformStatus status) {

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        for (String code : codeList) {
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("productCodes", code);
            queryMap.put("cartId", cartId);

            HashMap<String, Object> updateMap = new HashMap<>();
            if (numIId != null) {
                updateMap.put("numIId", numIId);
            }
            if (productId != null) {
                updateMap.put("platformPid", productId);
            }
            if (publishTime != null) {
                updateMap.put("publishTime", publishTime);
            }
            if (onSalesTime != null) {
                updateMap.put("onSaleTime", onSalesTime);
            }
            if (instockTime != null) {
                updateMap.put("instockTime", instockTime);
            }
            if (status != null) {
                updateMap.put("platformStatus", status.toString());
            }

            if (updateMap.size() > 0) {
                BulkUpdateModel model = new BulkUpdateModel();
                model.setUpdateMap(updateMap);
                model.setQueryMap(queryMap);
                bulkList.add(model);
            }
        }

        BulkWriteResult result = null;
        if (bulkList.size()>0) {
            result = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
        }
        return result;
    }

    /**
     * 取得逻辑库存
     *
     * @param channelId 渠道id
     * @param skuList   待取得逻辑库存的sku对象
     * @return 逻辑库存Map<sku, logicQty>
     */
    public Map<String, Integer> getLogicQty(String channelId, List<String> skuList) {
        // 逻辑库存Map
        Map<String, Integer> skuLogicQty = new HashMap<>();
        skuList.forEach(sku -> skuLogicQty.put(sku, 0)); // 初始化

        List<WmsBtInventoryCenterLogicModel> listLogicInventory = wmsBtInventoryCenterLogicDao.selectItemDetailBySkuList(channelId, skuList);

        if (listLogicInventory != null && listLogicInventory.size() > 0) {
            for (WmsBtInventoryCenterLogicModel logicInventory : listLogicInventory) {
                String sku = logicInventory.getSku();
                Integer logicQty = logicInventory.getQtyChina();
                skuLogicQty.merge(sku, logicQty, (val, newVal) -> val + newVal);
            }
        }

        return skuLogicQty;
    }

}

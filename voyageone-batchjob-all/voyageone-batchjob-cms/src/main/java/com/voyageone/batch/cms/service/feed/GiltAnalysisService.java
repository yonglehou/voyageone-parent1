package com.voyageone.batch.cms.service.feed;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.bean.*;
import com.voyageone.batch.cms.dao.feed.GiltFeedDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.components.gilt.GiltSkuService;
import com.voyageone.common.components.gilt.bean.GiltCategory;
import com.voyageone.common.components.gilt.bean.GiltImage;
import com.voyageone.common.components.gilt.bean.GiltPageGetSkusRequest;
import com.voyageone.common.components.gilt.bean.GiltSku;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.GILT;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

/**
 * @author Jonas, 2/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class GiltAnalysisService extends BaseTaskService {

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "GiltAnalysis";
    }

    @Autowired
    private GiltFeedDao giltFeedDao;

    @Autowired
    private GiltSkuService giltSkuService;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        int pageIndex = 0;

        while(true) {

            List<GiltSku> skuList = getSkus(pageIndex);

            $info("取得 SKU: %s", skuList.size());

            if (skuList.isEmpty())
                break;

            doFeedData(skuList);

            if (skuList.size() < 100)
                break;

            $info("阶段结束等待 10 秒");
            Thread.sleep(30000);
        }
    }

    private void doFeedData(List<GiltSku> skuList) throws Exception {

        prepareData(skuList);

        boolean inserted = insert();

        boolean updated = update();

        if (inserted || updated)
            attribute();
    }

    private void attribute() {
        $info("attribute 暂未实现");
    }

    private boolean update() throws Exception {

        $info("进入更新");

        List<SuperFeedGiltBean> newData = giltFeedDao.selectUpdatingProducts();

        $info("\t新数据取得 -> %s", newData.size());

        GiltAnalysisContext context = new GiltAnalysisContext();

        for (SuperFeedGiltBean newItem: newData) {
            SuperFeedGiltBean oldItem = giltFeedDao.selectFullUpdatingProduct(newItem.getProduct_look_id());
            context.put(newItem, oldItem);
        }

        if (context.isNoNeedUpdate()) {
            $info("\t无需要更新的数据");
            setUpdateFlag(null);
            return false;
        }

        return callUpdate(context);
    }

    private boolean callUpdate(GiltAnalysisContext context) throws Exception {

        WsdlProductService service = new WsdlProductService(GILT);

        List<String> failCodes = new ArrayList<>();

        List<String> successCodes = new ArrayList<>();

        for (ProductsFeedUpdate feedUpdate: context.getFeedUpdateList()) {

            WsdlProductUpdateResponse response = service.update(feedUpdate);

            ProductUpdateResponseBean productUpdateResponseBean = response.getResultInfo();

            if (response.getResult().equals("NG")) {

                $info("\t更新产品处理失败 -> MessageCode = %s ,Message = %s", response.getMessageCode(), response.getMessage());

                failCodes.add(feedUpdate.getCode());
            } else {

                List<ProductUpdateDetailBean> failure = productUpdateResponseBean.getFailure();

                if (!failure.isEmpty()) {

                    String failureMessage = failure.stream().map(ProductUpdateDetailBean::getResultMessage).collect(joining(";"));

                    $info("\t更新产品处理失败 -> " + failureMessage);

                    logIssue("cms 数据导入处理", "更新产品处理失败 -> " + failureMessage);

                    failCodes.add(feedUpdate.getCode());
                } else {

                    successCodes.add(feedUpdate.getCode());
                }
            }
        }

        $info("\t更新产品结束 -> 成功 %s, 失败 %s", successCodes.size(), failCodes.size());

        return setUpdateFlag(successCodes);
    }

    private boolean setUpdateFlag(List<String> successCodes) {

        int count0 = giltFeedDao.deleteUpdating();
        int count1 = giltFeedDao.selectInsertUpdating();
        int count2 = giltFeedDao.updateUpdated(successCodes);

        $info("\t更新产品标识结束 -> DELETE %s, INSERT %s, UPDATE %s", count0, count1, count2);

        return count2 > 0;
    }

    private Map<String, SuperFeedGiltBean> getUpdatingMap(List<SuperFeedGiltBean> feedGiltBeanList) {
        return feedGiltBeanList.stream().collect(toMap(SuperFeedGiltBean::getId, i -> i));
    }

    private void prepareData(List<GiltSku> skuList) {

        List<SuperFeedGiltBean> feedGiltBeanList = new ArrayList<>();

        for (GiltSku giltSku : skuList) feedGiltBeanList.add(toMySqlBean(giltSku));

        $info("转换 SKU: %s", feedGiltBeanList.size());

        giltFeedDao.clearTemp();

        int count = giltFeedDao.insertListTemp(feedGiltBeanList);

        $info("插入 TEMP SKU: %s", count);

        int[] counts = giltFeedDao.updateFlg();

        $info("更新 SKU 标识位: INSERT -> %s, UPDATE -> %s", counts[0], counts[1]);

        counts = giltFeedDao.appendInserting();

        $info("追加插入的 SKU 数据: DELETE -> %s, INSERT -> %s", counts[0], counts[1]);
    }

    private List<GiltSku> getSkus(int index) throws Exception {

        GiltPageGetSkusRequest request = new GiltPageGetSkusRequest();

        request.setOffset(index * 100);

        request.setLimit(100);

        return giltSkuService.pageGetSkus(request);
    }

    private boolean insert() throws Exception {

        List<SuperFeedGiltBean> inserting = giltFeedDao.selectFullByUpdateFlg(SuperFeedGiltBean.INSERTING);

        GiltAnalysisContext context = new GiltAnalysisContext();

        for (SuperFeedGiltBean feedGiltBean : inserting)
            context.put(feedGiltBean);

        List<List<CategoryBean>> categoryTreeList = context.getCategoriesList();

        $info("\t构建的 Category 树: %s", categoryTreeList.size());

        return callInsert(context);
    }

    private boolean callInsert(GiltAnalysisContext context) throws Exception {

        WsdlProductService service = new WsdlProductService(GILT);

        List<String> modelFailList = new ArrayList<>();

        List<String> productFailList = new ArrayList<>();

        for (List<CategoryBean> categoryTree: context.getCategoriesList()) {

            $info("\t调用 Insert -> %s", categoryTree.get(categoryTree.size() - 1).getUrl_key());

            ProductsFeedInsert feedInsert = new ProductsFeedInsert();

            feedInsert.setChannel_id(GILT.getId());
            feedInsert.setCategorybeans(categoryTree);

            WsdlProductInsertResponse response = service.insert(feedInsert);

            $info("\t接口结果: %s ; 返回: %s", response.getResult(), response.getMessage());

            ProductFeedResponseBean productFeedResponseBean = response.getResultInfo();

            if (response.getResult().equals("OK") && productFeedResponseBean.getSuccess().size() > 0) {

                List<ProductFeedDetailBean> productFeedDetailBeans = productFeedResponseBean.getFailure();

                for (ProductFeedDetailBean productFeedDetailBean : productFeedDetailBeans) {
                    //  处理类型 1:category 无; 2:model
                    if (productFeedDetailBean.getBeanType() == 2)
                        modelFailList.add(productFeedDetailBean.getDealObject().getModel());
                    //  处理类型 3:product; 4:item
                    if (productFeedDetailBean.getBeanType() == 3 || productFeedDetailBean.getBeanType() == 4)
                        productFailList.add(productFeedDetailBean.getDealObject().getCode());
                }
            }
        }

        $info("\t总共~ 失败的 Model: %s ; 失败的 Product: %s", modelFailList.size(), productFailList.size());

        int[] count = giltFeedDao.updateInsertSuccess(modelFailList, productFailList);

        $info("\t新商品 INSERT 处理全部完成 { Feed: %s, Fail(M): %s, Fail(C): %s }", count[0], count[1], count[2]);

        return count[0] - count[1] - count[2] > 0;
    }

    private SuperFeedGiltBean toMySqlBean(GiltSku giltSku) {

        SuperFeedGiltBean superFeedGiltBean = new SuperFeedGiltBean();

        // TODO UPC 貌似没处理

        superFeedGiltBean.setId(String.valueOf(giltSku.getId()));
        superFeedGiltBean.setProduct_id(String.valueOf(giltSku.getProduct_id()));
        superFeedGiltBean.setProduct_look_id(String.valueOf(giltSku.getProduct_look_id()));
        superFeedGiltBean.setLocale(String.valueOf(giltSku.getLocale()));
        superFeedGiltBean.setName(String.valueOf(giltSku.getName()));
        superFeedGiltBean.setDescription(String.valueOf(giltSku.getDescription()));
        superFeedGiltBean.setCountry_code(String.valueOf(giltSku.getCountry_code()));
        superFeedGiltBean.setBrand_id(String.valueOf(giltSku.getBrand().getId()));
        superFeedGiltBean.setBrand_name(String.valueOf(giltSku.getBrand().getName()));
        superFeedGiltBean.setImages_url(giltSku.getImages().stream().map(GiltImage::getUrl).collect(joining(",")));

        List<Object> attributes = giltSku.getAttributes();

        for (Object object : attributes) {

            Map<String, Map<String, Object>> map1 = (Map<String, Map<String, Object>>) object;
            Map<String, Object> map2;

            if (map1.containsKey("color")) {
                map2 = map1.get("color");
                superFeedGiltBean.setAttributes_color_nfr_code(String.valueOf(map2.get("nrf_code")));
                superFeedGiltBean.setAttributes_color_name(String.valueOf(map2.get("name")));
            } else if (map1.containsKey("style")) {
                map2 = map1.get("style");
                superFeedGiltBean.setAttributes_style_name(String.valueOf(map2.get("name")));
            } else if (map1.containsKey("material")) {
                map2 = map1.get("material");
                superFeedGiltBean.setAttributes_material_value(String.valueOf(map2.get("value")));
            } else if (map1.containsKey("size")) {
                map2 = map1.get("size");
                superFeedGiltBean.setAttributes_size_size_chart_id(String.valueOf(map2.get("size_chart_id")));
                superFeedGiltBean.setAttributes_size_type(String.valueOf(map2.get("type")));
                superFeedGiltBean.setAttributes_size_value(String.valueOf(map2.get("value")));
            }
        }

        superFeedGiltBean.setPrices_retail_currency(String.valueOf(giltSku.getPrices().getRetail().getCurrency()));
        superFeedGiltBean.setPrices_retail_value(String.valueOf(giltSku.getPrices().getRetail().getValue()));
        superFeedGiltBean.setPrices_sale_currencty(String.valueOf(giltSku.getPrices().getSale().getCurrency()));
        superFeedGiltBean.setPrices_sale_value(String.valueOf(giltSku.getPrices().getSale().getValue()));
        superFeedGiltBean.setPrices_cost_currency(String.valueOf(giltSku.getPrices().getCost().getCurrency()));
        superFeedGiltBean.setPrices_cost_value(String.valueOf(giltSku.getPrices().getCost().getValue()));
        superFeedGiltBean.setProduct_codes(StringUtils.join(giltSku.getProduct_codes(), ","));
        superFeedGiltBean.setProduct_codes_first(String.valueOf(giltSku.getProduct_codes().get(0)));

        List<GiltCategory> categories = giltSku.getCategories();
        GiltCategory giltCategory = categories.get(categories.size() - 1);

        superFeedGiltBean.setCategories_id(String.valueOf(giltCategory.getId()));
        superFeedGiltBean.setCategories_key(giltCategory.getKey().replaceAll("_", "-"));
        superFeedGiltBean.setCategories_name(giltCategory.getName());

        return superFeedGiltBean;
    }
}

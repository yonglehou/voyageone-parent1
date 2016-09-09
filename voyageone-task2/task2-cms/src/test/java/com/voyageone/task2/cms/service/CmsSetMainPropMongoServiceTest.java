package com.voyageone.task2.cms.service;

import com.voyageone.service.impl.cms.CategoryTreeAllService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujiaye on 15/12/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSetMainPropMongoServiceTest {

    @Autowired
    private CmsSetMainPropMongoService cmsSetMainPropMongoService;

    @Autowired
    private FeedInfoService feedInfoService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryTreeAllService categoryTreeAllService;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean taskControlBean = new TaskControlBean();
        taskControlBean.setCfg_name("order_channel_id");
        taskControlBean.setCfg_val1("019");
        taskControlList.add(taskControlBean);


//        TaskControlBean taskControlBean = new TaskControlBean();
//        taskControlBean.setCfg_name("order_channel_id");
//        taskControlBean.setCfg_val1("010");
//        taskControlList.add(taskControlBean);

//        TaskControlBean taskControlBean2 = new TaskControlBean();
//        taskControlBean2.setCfg_name("order_channel_id");
//        taskControlBean2.setCfg_val1("019");
//        taskControlList.add(taskControlBean2);

//        TaskControlBean taskControlBean3 = new TaskControlBean();
//        taskControlBean3.setCfg_name("order_channel_id");
//        taskControlBean3.setCfg_val1("020");
//        taskControlList.add(taskControlBean3);

        cmsSetMainPropMongoService.onStartup(taskControlList);
    }


    @Test
    public void testOnStartup2() throws Exception {

        String orderChannelID = "010";

        // 保存每个channel最终导入结果(成功失败件数信息)
        Map<String, String> resultMap = new HashMap<>();
        // 获取是否跳过mapping check
        boolean bln_skip_mapping_check = true;
        // 主逻辑
        CmsSetMainPropMongoService.setMainProp mainProp = cmsSetMainPropMongoService.new setMainProp(orderChannelID, bln_skip_mapping_check);
        mainProp.doRun(resultMap);

        System.out.println("=================feed->master导入  最终结果=====================");
        resultMap.entrySet().stream()
                .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .forEach(p ->  System.out.println(p.getValue()));
        System.out.println("=================feed->master导入  主线程结束====================");
    }


    @Test
    public void testSetSellerCats() throws Exception {

        String channelId = "010";
        String feedCode = "36/G05";

        // 查找当前渠道,所有等待反映到主数据的商品
        CmsBtFeedInfoModel feedInfo = feedInfoService.getProductByCode(channelId, feedCode);
//        String query = String.format("{ channelId: '%s', updFlg: %s}", channelId, 0);
//        String sort = "{modified:1}";
//        JongoQuery queryObject = new JongoQuery();
//        queryObject.setQuery(query);
//        queryObject.setSort(sort);
//        queryObject.setLimit(5);   // 默认为每次最大5件
//        List<CmsBtFeedInfoModel> feedList = feedInfoService.getList(channelId, queryObject);

        CmsBtProductModel cmsProduct = null;
        if (feedInfo != null) {
            cmsProduct = productService.getProductByCode(channelId, feedInfo.getCode());
        }

        if (feedInfo != null && cmsProduct != null) {
            // 设置产品各个平台的店铺内分类信息
            cmsSetMainPropMongoService.setSellerCats(feedInfo, cmsProduct);
        }

    }

}
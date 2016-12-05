package com.voyageone.task2.cms.service.product;

import com.voyageone.task2.cms.service.promotion.PromotionProductStockSyncService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jason.jiang on 2016/10/18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class PromotionProductStockSyncServiceTest {

    @Autowired
    PromotionProductStockSyncService targetService;

    @Test
    public void testTMPlatform() {
        try {
            targetService.onStartup(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
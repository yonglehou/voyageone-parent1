package com.voyageone.components.gilt.service;

import com.voyageone.components.gilt.bean.GiltRealTimeInventory;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class GiltRealTimeInventoryServiceTest {

    @Autowired
    private GiltRealTimeInventoryService giltRealTimeInventoryService;


    @Test
    public void testGetRealTimeInventoryBySkuId() throws Exception {
        GiltRealTimeInventory giltRealTimeInventory= giltRealTimeInventoryService.getRealTimeInventoryBySkuId("4099260");
        System.out.println(JsonUtil.getJsonString(giltRealTimeInventory));
    }

}
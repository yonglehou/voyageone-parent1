package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdatePriceMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by dell on 2017/7/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBtProductUpdatePriceMQJobTest {

    @Autowired
    CmsBtProductUpdatePriceMQJob cmsBtProductUpdatePriceMQJob;
    @Test
    public void testOnStartup() throws Exception {
        CmsBtProductUpdatePriceMQMessageBody map = new CmsBtProductUpdatePriceMQMessageBody();
        map.setChannelId("001");
        map.setCartId(5);
        map.setProductCodes(Collections.singletonList("8125115S.RED"));
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("changedPriceType","clientRetailPrice");
        paraMap.put("basePriceType","clientRetailPrice");
        paraMap.put("optionType","/");
        paraMap.put("value","100");
        paraMap.put("flag","1");
        map.setParams(paraMap);
        cmsBtProductUpdatePriceMQJob.onStartup(map);

    }
}
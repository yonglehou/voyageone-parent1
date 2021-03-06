package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchLockProductsMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 123 on 2017/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchLockProductsMQJobTest {

    @Autowired
    private CmsAdvSearchLockProductsMQJob cmsAdvSearchLockProductsMQJob;

    @Test
    public void onStartup() throws Exception {
        String json = "{\n" +
                "\t\"channelId\": \"010\",\n" +
                "\t\"cartId\": 23,\n" +
                "\t\"productCodes\": [\"51A0HC13E1-00LCNB0\"],\n" +
                "\t\"comment\": \"批量lock平台\",\n" +
                "\t\"lock\": \"0\",\n" +
                "\t\"consumerRetryTimes\": 0,\n" +
                "\t\"mqId\": 0,\n" +
                "\t\"delaySecond\": 0,\n" +
                "\t\"sender\": \"edward\"\n" +
                "}";

        AdvSearchLockProductsMQMessageBody messageBody = JacksonUtil.json2Bean(json, AdvSearchLockProductsMQMessageBody.class);
        cmsAdvSearchLockProductsMQJob.onStartup(messageBody);

    }
}

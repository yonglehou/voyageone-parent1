package com.voyageone.task2.vms.service;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeff.duan on 16/06/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-vms-test.xml")
public class VmsGetHomeInfoServiceTest {

    @Autowired
    private VmsGetHomeInfoService vmsGetHomeInfoService;
    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean taskControlBean = new TaskControlBean();
        taskControlBean.setCfg_name("order_channel_id");
        taskControlBean.setCfg_val1("090");
        taskControlList.add(taskControlBean);
        TaskControlBean taskControlBean1 = new TaskControlBean();
        taskControlBean1.setCfg_name("order_channel_id");
        taskControlBean1.setCfg_val1("091");
        taskControlList.add(taskControlBean1);
        vmsGetHomeInfoService.onStartup(taskControlList);
    }
}
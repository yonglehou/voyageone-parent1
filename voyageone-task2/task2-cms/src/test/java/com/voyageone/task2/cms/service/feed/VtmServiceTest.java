package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dell on 2016/3/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class VtmServiceTest {

    @Autowired
    VtmService vtmService;

    @Test
    public void testOnStartup() throws Exception {

        List<TaskControlBean> taskControlList = new ArrayList<>();
        vtmService.onStartup(taskControlList);
    }
}
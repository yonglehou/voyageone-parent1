package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsMtPlatFormDictModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Ethan Shi on 2016/4/22.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsMtPlatformDictDaoExtTest {

    @Autowired
    private CmsMtPlatformDictDaoExt cmsMtPlatformDictDaoExt;

    @Test
    public void testSelectById() throws Exception {

        CmsMtPlatFormDictModel cmsMtPlatFormDictModel = new CmsMtPlatFormDictModel();

        cmsMtPlatFormDictModel.setOrderChannelId("11");


        cmsMtPlatformDictDaoExt.selectById(cmsMtPlatFormDictModel);

    }
}
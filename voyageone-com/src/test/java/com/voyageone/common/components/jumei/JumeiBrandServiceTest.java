package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.JmBrandBean;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by DELL on 2016/1/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class JumeiBrandServiceTest {

    @Autowired
    JumeiBrandService brandService;

    @Test
    public void testGet() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://182.138.102.82:8868/");

        List<JmBrandBean> list = brandService.getBrands(shopBean);
        for (JmBrandBean bean : list) {
            System.out.println(bean.getId() + ";" + bean.getName());
        }
    }
}
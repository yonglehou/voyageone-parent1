package com.voyageone.service.impl.com.log;


import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.service.model.user.ComLoginLogModel;
import com.voyageone.service.daoext.core.AdminLoginLogDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.bean.com.PaginationBean;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-23.
 */
@Service
public class AdminLoginLogService extends BaseService {

    @Autowired
    AdminLoginLogDaoExt adminLoginLogDaoExt;


    public PaginationBean<ComLoginLogModel> searchLog(Integer pageNum, Integer pageSize) {
        return  searchLog(new ComLoginLogModel(), null, null, pageNum,  pageSize);
    }

    public PaginationBean<ComLoginLogModel> searchLog(ComLoginLogModel params, Long startTime, Long endTime, Integer pageNum, Integer pageSize) {

        PaginationBean<ComLoginLogModel> paginationBean = new PaginationBean<>();

        // 判断查询结果是否分页
        boolean needPage = false;

        Map<String, Object> beanMap = new BeanMap(params);
        Map<String, Object> newMap = new HashMap<>();

        Date start = null;
        Date end = null;

        if(startTime != null)
        {
            start = new Date(startTime);
        }

        if(endTime != null)
        {
            end = new Date(endTime);
        }

        newMap.put("startTime", start);
        newMap.put("endTime", end);
        newMap.putAll(beanMap);



        if (pageNum != null && pageSize != null) {
            needPage = true;
            paginationBean.setCount(adminLoginLogDaoExt.selectCount(newMap));
            newMap = MySqlPageHelper.build(newMap).page(pageNum).limit(pageSize).addSort("created", Order.Direction.DESC).toMap();
        }
        else
        {
            newMap = MySqlPageHelper.build(newMap).addSort("created", Order.Direction.DESC).toMap();
        }



        List<ComLoginLogModel> list = adminLoginLogDaoExt.selectList(newMap);
        if (!needPage) {
            paginationBean.setCount(list.size());
        }

        paginationBean.setResult(list);
        return paginationBean;
    }

}

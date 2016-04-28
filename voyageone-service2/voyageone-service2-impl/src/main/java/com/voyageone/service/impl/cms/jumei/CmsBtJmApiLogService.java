package com.voyageone.service.impl.cms.jumei;
import com.voyageone.service.dao.cms.CmsBtJmApiLogDao;
import com.voyageone.service.model.cms.CmsBtJmApiLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmApiLogService {
@Autowired
CmsBtJmApiLogDao dao;

    public CmsBtJmApiLogModel select(int id)
    {
       return dao.select(id);
    }

    public int update(CmsBtJmApiLogModel entity)
    {
   return dao.update(entity);
    }
    public int create(CmsBtJmApiLogModel entity)
    {
                   return dao.insert(entity);
    }
    }


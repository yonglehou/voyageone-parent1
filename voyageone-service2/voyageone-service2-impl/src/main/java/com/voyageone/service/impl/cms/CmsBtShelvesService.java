package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtShelvesDao;
import com.voyageone.service.fields.cms.CmsBtShelvesModelActive;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/11/11.
 *
 * @version 2.10.0
 * @since 2.10.0
 */
@Service
public class CmsBtShelvesService extends BaseService {
    private final CmsBtShelvesDao cmsBtShelvesDao;

    @Autowired
    public CmsBtShelvesService(CmsBtShelvesDao cmsBtShelvesDao) {
        this.cmsBtShelvesDao = cmsBtShelvesDao;
    }

    public CmsBtShelvesModel getId(Integer id){
        return cmsBtShelvesDao.select(id);
    }

    public int update(CmsBtShelvesModel cmsBtShelvesModel){
        return cmsBtShelvesDao.update(cmsBtShelvesModel);
    }

    public Integer insert(CmsBtShelvesModel cmsBtShelvesModel){
        cmsBtShelvesDao.insert(cmsBtShelvesModel);
        return cmsBtShelvesModel.getId();
    }

    public List<CmsBtShelvesModel>selectList(Map map){
        return cmsBtShelvesDao.selectList(map);
    }

    public List<CmsBtShelvesModel>selectByChannelId(String channelId){
        Map<String, Object> params = new HashedMap();
        params.put("channelId",channelId);
        params.put("active", CmsBtShelvesModelActive.ACTIVATE);
        return cmsBtShelvesDao.selectList(params);
    }
    public List<CmsBtShelvesModel>selectByChannelIdCart(String channelId, Integer cartId){
        Map<String, Object> params = new HashedMap();
        params.put("channelId",channelId);
        params.put("cartId",cartId);
        params.put("active", CmsBtShelvesModelActive.ACTIVATE);
        return cmsBtShelvesDao.selectList(params);
    }
}

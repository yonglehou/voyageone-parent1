package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.mongo.CmsBtTranslateDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel.CustomPropType;
import com.voyageone.service.model.cms.mongo.CmsBtTranslateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by james on 2017/2/27.
 */
@Service
public class CmsBtTranslateService extends BaseService {

    private final CmsBtTranslateDao cmsBtTranslateDao;

    @Autowired
    public CmsBtTranslateService(CmsBtTranslateDao cmsBtTranslateDao) {
        this.cmsBtTranslateDao = cmsBtTranslateDao;
    }

    public CmsBtTranslateModel get(String channelId, Integer customPropType, String name, String valueEn){
        valueEn = valueEn == null?"":valueEn.toLowerCase();
        return cmsBtTranslateDao.get(channelId,customPropType,name,valueEn.toLowerCase());
    }

    public void insertOrUpdate(CmsBtTranslateModel model){
        CmsBtTranslateModel cmsBtTranslateModel = cmsBtTranslateDao.get(model.getChannelId(),model.getType(), model.getName(), model.getValueEn().toLowerCase());
        if(cmsBtTranslateModel == null){
            model.set_id(null);
            cmsBtTranslateDao.insert(model);
        }else{
            model.set_id(cmsBtTranslateModel.get_id());
            cmsBtTranslateDao.save(model);
        }
    }

    public void create(String channelId, Integer customPropType, String name, String valueEn, String valueCn){
        valueEn = valueEn == null?"":valueEn.toLowerCase();
        CmsBtTranslateModel cmsBtTranslateModel = get(channelId, customPropType, name, valueEn);
        if(cmsBtTranslateModel == null) {
            cmsBtTranslateModel = new CmsBtTranslateModel();
        }
        cmsBtTranslateModel.setChannelId(channelId);
        cmsBtTranslateModel.setType(customPropType);
        cmsBtTranslateModel.setName(name);
        cmsBtTranslateModel.setValueEn(valueEn.toLowerCase());
        cmsBtTranslateModel.setValueCn(valueCn);
        insertOrUpdate(cmsBtTranslateModel);
    }

    public List<CmsBtTranslateModel> select(String channelId, Integer customPropType, String name, String valueEn, Integer skip, Integer limit){
        valueEn = valueEn == null?"":valueEn.toLowerCase();
        return cmsBtTranslateDao.select(channelId,customPropType,name,valueEn.toLowerCase(),skip,limit);
    }

    public Long selectCnt(String channelId, Integer customPropType, String name, String valueEn){
        valueEn = valueEn == null?"":valueEn.toLowerCase();
        return cmsBtTranslateDao.selectCnt(channelId,customPropType,name,valueEn.toLowerCase());
    }
}

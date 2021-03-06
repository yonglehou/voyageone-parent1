package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.service.model.cms.mongo.CmsMtCommonPropDefModel;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */
@Repository
public class CmsMtCommonPropDefDao extends BaseMongoDao<CmsMtCommonPropDefModel> {


    @Override
    public List<CmsMtCommonPropDefModel> selectAll() {
        List<JSONObject> jsonList = mongoTemplate.findAll(collectionName);
        List<CmsMtCommonPropDefModel> resultList = null;
        if (jsonList != null) {
            resultList = new ArrayList<>();
            for (JSONObject jsonObject : jsonList) {
                CmsMtCommonPropDefModel model = new CmsMtCommonPropDefModel();
                Object jsonField = jsonObject.get("field");
                Field field = SchemaJsonReader.mapToField(jsonField);
                model.setField(field);
                resultList.add(model);
            }
        }
        return resultList;
    }

    @Override
    public WriteResult insertWithList(Collection<? extends CmsMtCommonPropDefModel> models) {
        if (models != null && models.size() > 0) {
            return mongoTemplate.insert(models, collectionName);
        }
        return null;
    }

}

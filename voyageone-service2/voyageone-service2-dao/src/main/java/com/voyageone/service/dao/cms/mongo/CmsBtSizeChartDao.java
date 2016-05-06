package com.voyageone.service.dao.cms.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/5/5.
 */
@Repository
public class CmsBtSizeChartDao extends BaseMongoChannelDao<CmsBtSizeChartModel> {
    /**
     * 尺码关系一览初始化画面
     * @param channelId
     * @return List<CmsBtSizeChartModel>
     */
    public List<CmsBtSizeChartModel> selectInitSizeChartInfo(String channelId) {
        return selectAll(channelId);
    }

    /**
     * 尺码关系一览检索画面
     * @param channelId
     * @param cmsBtSizeChartModel
     * @return List<CmsBtSizeChartModel>
     */
    public List<CmsBtSizeChartModel> selectSearchSizeChartInfo(String channelId, CmsBtSizeChartModel cmsBtSizeChartModel) {

        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("{");
        //sizeChartName
        if(StringUtils.isEmpty(cmsBtSizeChartModel.getSizeChartName())){
            sbQuery.append("sizeChartName:"+cmsBtSizeChartModel.getSizeChartName()+",");
        }
        //finish
        if(StringUtils.isEmpty(cmsBtSizeChartModel.getFinish())){
            sbQuery.append("finish:"+cmsBtSizeChartModel.getFinish()+",");
        }
        //UpdateTimeStr
        if(StringUtils.isEmpty(cmsBtSizeChartModel.getCreated())){
            sbQuery.append("modified:"+cmsBtSizeChartModel.getCreated() + ",");
        }
        //UpdateTimeEnd
        if(StringUtils.isEmpty(cmsBtSizeChartModel.getModified())){
            sbQuery.append("modified:"+cmsBtSizeChartModel.getModified()+",");
        }
        //BrandName
        if(cmsBtSizeChartModel.getBrandName().size()>0){
            sbQuery.append(MongoUtils.splicingValue("brandName", cmsBtSizeChartModel.getBrandName()));
        }
        //ProductType
        if(cmsBtSizeChartModel.getProductType().size()>0){
            sbQuery.append(MongoUtils.splicingValue("productType", cmsBtSizeChartModel.getProductType()));
        }
        //SizeType
        if(cmsBtSizeChartModel.getSizeType().size()>0){
            sbQuery.append(MongoUtils.splicingValue("sizeType", cmsBtSizeChartModel.getSizeType()));
        }
        sbQuery.append("}");
        return select(sbQuery.toString(),channelId);
    }

    /**
     * gen
     * @param channelId
     * @param cmsBtSizeChartModel
     * @return WriteResult
     */
    public WriteResult sizeChartUpdate(String channelId, CmsBtSizeChartModel cmsBtSizeChartModel) {
        cmsBtSizeChartModel.setActive("0");
        //获取集合名
        DBCollection coll = getDBCollection(channelId);
        BasicDBObject params = new BasicDBObject();
        //条件
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("sizeChartId", cmsBtSizeChartModel.getSizeChartId());
        params.putAll(queryMap);
        //设置值
        BasicDBObject result = new BasicDBObject();
        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("active", "1");
        result.putAll(rsMap);
        return coll.update(params, new BasicDBObject("$set", result), false, true);
    }
}

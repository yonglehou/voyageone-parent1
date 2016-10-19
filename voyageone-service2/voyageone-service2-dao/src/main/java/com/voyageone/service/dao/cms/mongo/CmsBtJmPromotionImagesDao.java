package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmPromotionImagesModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 聚美专场图片 DAO
 *
 * Created by jonas on 2016/10/14.
 *
 * @author jonas
 * @version 2.8.0
 * @since 2.8.0
 */
@Repository
public class CmsBtJmPromotionImagesDao extends BaseMongoDao<CmsBtJmPromotionImagesModel> {

    /**
     * 下载专场图片
     * @param promotionId
     * @return List<CmsBtJmPromotionImagesModel>
     */
    public List<CmsBtJmPromotionImagesModel> selectPromotionImagesList(Integer promotionId) {
        JongoQuery query = new JongoQuery();
        query.setQuery(String.format("{\"promotionId\":\"" + promotionId + "\"}"));
        return select(query);
    }

    public void saveJmPromotionImages(CmsBtJmPromotionImagesModel model){
        mongoTemplate.save(model);
    }

    /**
     * 通过promotionid和jmpromotionid选出聚美活动图片集合
     * @param promotionId
     * @param jmPromotionId
     * @return
     */
    public CmsBtJmPromotionImagesModel selectJmPromotionImage(int promotionId,int jmPromotionId) {
        String query = "{\"promotionId\":" + promotionId + ",\"jmPromotionId\":" + jmPromotionId + "}";
        return selectOneWithQuery(query);
    }
}

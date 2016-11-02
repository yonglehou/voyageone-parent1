package com.voyageone.service.daoext.cms;

import org.springframework.stereotype.Repository;

/**
 * Created by dell on 2016/6/27.
 */
@Repository
public interface CmsBtJmPromotionTagProductDaoExt {
    int deleteByCmsBtJmPromotionProductId(int cmsBtJmPromotionProductId);
    int batchDeleteTag(@Param("listPromotionProductId") List<Long> listPromotionProductId);


    int deleteByTagId(int tagId);

}

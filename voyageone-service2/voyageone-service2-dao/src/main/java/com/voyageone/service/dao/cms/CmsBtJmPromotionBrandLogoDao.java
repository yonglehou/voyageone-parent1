/*
 * CmsBtJmPromotionBrandLogoDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtJmPromotionBrandLogoModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtJmPromotionBrandLogoDao {
    List<CmsBtJmPromotionBrandLogoModel> selectList(Object map);

    CmsBtJmPromotionBrandLogoModel selectOne(Object map);

    int selectCount(Object map);

    CmsBtJmPromotionBrandLogoModel select(Integer id);

    int insert(CmsBtJmPromotionBrandLogoModel record);

    int update(CmsBtJmPromotionBrandLogoModel record);

    int delete(Integer id);
}
/*
 * CmsBtPromotionGroupsDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtPromotionGroupsModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtPromotionGroupsDao {
    List<CmsBtPromotionGroupsModel> selectList(Map<String, Object> map);

    CmsBtPromotionGroupsModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    CmsBtPromotionGroupsModel select(Integer id);

    int insert(CmsBtPromotionGroupsModel record);

    int update(CmsBtPromotionGroupsModel record);

    int delete(Integer id);
}
/*
 * CmsBtPromotionCodesDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtPromotionCodesModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtPromotionCodesDao {
    List<CmsBtPromotionCodesModel> selectList(Map<String, Object> map);

    CmsBtPromotionCodesModel selectOne(Map<String, Object> map);

    CmsBtPromotionCodesModel select(Integer id);

    int insert(CmsBtPromotionCodesModel record);

    int update(CmsBtPromotionCodesModel record);

    int delete(Integer id);
}
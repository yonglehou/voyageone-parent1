/*
 * CmsBtBrandBlockDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtBrandBlockModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtBrandBlockDao {
    List<CmsBtBrandBlockModel> selectList(Object map);

    CmsBtBrandBlockModel selectOne(Object map);

    int selectCount(Object map);

    CmsBtBrandBlockModel select(Integer id);

    int insert(CmsBtBrandBlockModel record);

    int update(CmsBtBrandBlockModel record);

    int delete(Integer id);
}
/*
 * CmsMtPlatformSkusDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtPlatformSkusModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtPlatformSkusDao {
    List<CmsMtPlatformSkusModel> selectList(Object map);

    CmsMtPlatformSkusModel selectOne(Object map);

    int selectCount(Object map);

    CmsMtPlatformSkusModel select(Integer id);

    int insert(CmsMtPlatformSkusModel record);

    int update(CmsMtPlatformSkusModel record);

    int delete(Integer id);
}
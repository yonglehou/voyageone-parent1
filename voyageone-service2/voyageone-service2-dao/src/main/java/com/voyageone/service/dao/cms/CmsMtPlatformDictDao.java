/*
 * CmsMtPlatformDictDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtPlatformDictModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtPlatformDictDao {
    List<CmsMtPlatformDictModel> selectList(Object map);

    CmsMtPlatformDictModel selectOne(Object map);

    int selectCount(Object map);

    CmsMtPlatformDictModel select(Integer id);

    int insert(CmsMtPlatformDictModel record);

    int update(CmsMtPlatformDictModel record);

    int delete(Integer id);
}
/*
 * CmsMtPlatformBrandsDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtPlatformBrandsModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtPlatformBrandsDao {
    List<CmsMtPlatformBrandsModel> selectList(Object map);

    CmsMtPlatformBrandsModel selectOne(Object map);

    int selectCount(Object map);

    CmsMtPlatformBrandsModel select(Integer id);

    int insert(CmsMtPlatformBrandsModel record);

    int update(CmsMtPlatformBrandsModel record);

    int delete(Integer id);
}
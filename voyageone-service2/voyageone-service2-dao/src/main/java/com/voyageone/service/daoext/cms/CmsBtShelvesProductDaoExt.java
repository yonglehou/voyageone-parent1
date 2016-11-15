/*
 * CmsBtShelvesProductDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsBtShelvesProductDaoExt {

    List<CmsBtShelvesProductModel> selectByShelvesId(@Param("shelvesId") Integer shelvesId);

    int updateSort(CmsBtShelvesProductModel record);

    int updatePlatformStatus(CmsBtShelvesProductModel record);

    int updatePlatformImage(CmsBtShelvesProductModel record);

}
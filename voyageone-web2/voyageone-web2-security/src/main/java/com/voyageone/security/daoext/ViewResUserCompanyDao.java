/*
 * ViewResUserCompanyDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.security.daoext;

import com.voyageone.security.model.ViewResUserCompanyModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewResUserCompanyDao {

    List<ViewResUserCompanyModel> selectList(Object map);
}
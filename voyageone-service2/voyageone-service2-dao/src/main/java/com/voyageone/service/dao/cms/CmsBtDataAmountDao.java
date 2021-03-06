/*
 * CmsBtDataAmountDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtDataAmountModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtDataAmountDao {
    List<CmsBtDataAmountModel> selectList(Object map);

    CmsBtDataAmountModel selectOne(Object map);

    int selectCount(Object map);

    CmsBtDataAmountModel select(Integer id);

    int insert(CmsBtDataAmountModel record);

    int update(CmsBtDataAmountModel record);

    int delete(Integer id);
}
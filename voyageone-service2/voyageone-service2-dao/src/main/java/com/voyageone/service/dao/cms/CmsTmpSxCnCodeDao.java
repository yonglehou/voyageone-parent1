/*
 * CmsTmpSxCnCodeDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsTmpSxCnCodeModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsTmpSxCnCodeDao {
    List<CmsTmpSxCnCodeModel> selectList(Object map);

    CmsTmpSxCnCodeModel selectOne(Object map);

    int selectCount(Object map);

    CmsTmpSxCnCodeModel select(Integer id);

    int insert(CmsTmpSxCnCodeModel record);

    int update(CmsTmpSxCnCodeModel record);

    int delete(Integer id);
}
/*
 * CmsTmpSxCnSkuDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsTmpSxCnSkuModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsTmpSxCnSkuDao {
    List<CmsTmpSxCnSkuModel> selectList(Object map);

    CmsTmpSxCnSkuModel selectOne(Object map);

    int selectCount(Object map);

    CmsTmpSxCnSkuModel select(Integer id);

    int insert(CmsTmpSxCnSkuModel record);

    int update(CmsTmpSxCnSkuModel record);

    int delete(Integer id);
}
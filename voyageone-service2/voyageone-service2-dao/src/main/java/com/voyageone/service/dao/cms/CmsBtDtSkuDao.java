/*
 * CmsBtDtSkuDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtDtSkuModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtDtSkuDao {
    List<CmsBtDtSkuModel> selectList(Object map);

    CmsBtDtSkuModel selectOne(Object map);

    int selectCount(Object map);

    CmsBtDtSkuModel select(Integer id);

    int insert(CmsBtDtSkuModel record);

    int update(CmsBtDtSkuModel record);

    int delete(Integer id);
}
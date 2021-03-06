/*
 * CmsBtPriceConfirmLogDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtPriceConfirmLogModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtPriceConfirmLogDao {
    List<CmsBtPriceConfirmLogModel> selectList(Object map);

    CmsBtPriceConfirmLogModel selectOne(Object map);

    int selectCount(Object map);

    CmsBtPriceConfirmLogModel select(Integer id);

    int insert(CmsBtPriceConfirmLogModel record);

    int update(CmsBtPriceConfirmLogModel record);

    int delete(Integer id);
}
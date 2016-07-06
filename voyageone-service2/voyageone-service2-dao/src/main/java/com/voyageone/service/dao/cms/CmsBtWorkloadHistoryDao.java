/*
 * CmsBtWorkloadHistoryDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtWorkloadHistoryModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtWorkloadHistoryDao {
    List<CmsBtWorkloadHistoryModel> selectList(Map<String, Object> map);

    CmsBtWorkloadHistoryModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    CmsBtWorkloadHistoryModel select(Integer id);

    int insert(CmsBtWorkloadHistoryModel record);

    int update(CmsBtWorkloadHistoryModel record);

    int delete(Integer id);
}
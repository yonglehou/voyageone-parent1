/*
 * CmsMtPlatformProductIdListDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtPlatformProductIdListModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtPlatformProductIdListDao {
    List<CmsMtPlatformProductIdListModel> selectList(Map<String, Object> map);

    CmsMtPlatformProductIdListModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    CmsMtPlatformProductIdListModel select(Integer id);

    int insert(CmsMtPlatformProductIdListModel record);

    int update(CmsMtPlatformProductIdListModel record);

    int delete(Integer id);
}
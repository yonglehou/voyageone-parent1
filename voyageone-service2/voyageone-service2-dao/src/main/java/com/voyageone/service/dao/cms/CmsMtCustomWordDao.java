/*
 * CmsMtCustomWordDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtCustomWordModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtCustomWordDao {
    List<CmsMtCustomWordModel> selectList(Map<String, Object> map);

    CmsMtCustomWordModel selectOne(Map<String, Object> map);

    CmsMtCustomWordModel select(Integer id);

    int insert(CmsMtCustomWordModel record);

    int update(CmsMtCustomWordModel record);

    int delete(Integer id);
}
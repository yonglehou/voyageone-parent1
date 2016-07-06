/*
 * CmsBtSizeMapDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtSizeMapModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtSizeMapDao {
    List<CmsBtSizeMapModel> selectList(Map<String, Object> map);

    CmsBtSizeMapModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    CmsBtSizeMapModel select(Integer id);

    int insert(CmsBtSizeMapModel record);

    int update(CmsBtSizeMapModel record);

    int delete(Integer id);
}
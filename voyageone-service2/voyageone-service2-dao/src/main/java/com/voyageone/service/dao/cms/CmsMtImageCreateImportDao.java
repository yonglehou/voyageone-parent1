/*
 * CmsMtImageCreateImportMapper.java
 * Copyright(C) 20xx-2015 Voyageonone Group Inc.
 * All rights reserved.
 * -----------------------------------------------
 * 2016-05-04 Created
 */
package com.voyageone.service.dao.cms;


import com.voyageone.service.model.cms.CmsMtImageCreateImportModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtImageCreateImportDao {
    List<CmsMtImageCreateImportModel> selectList(Map<String, Object> map);

    CmsMtImageCreateImportModel selectOne(Map<String, Object> map);

    CmsMtImageCreateImportModel select(Integer id);

    int insert(CmsMtImageCreateImportModel record);

    int update(CmsMtImageCreateImportModel record);

    int delete(Integer id);
}
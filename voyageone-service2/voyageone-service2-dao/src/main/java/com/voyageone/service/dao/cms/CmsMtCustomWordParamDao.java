/*
 * CmsMtCustomWordParamDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtCustomWordParamModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtCustomWordParamDao {
    List<CmsMtCustomWordParamModel> selectList(Object map);

    CmsMtCustomWordParamModel selectOne(Object map);

    int selectCount(Object map);

    CmsMtCustomWordParamModel select(Integer id);

    int insert(CmsMtCustomWordParamModel record);

    int update(CmsMtCustomWordParamModel record);

    int delete(Integer id);
}
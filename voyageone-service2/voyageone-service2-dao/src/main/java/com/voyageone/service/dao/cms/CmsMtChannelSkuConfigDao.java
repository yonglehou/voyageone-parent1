/*
 * CmsMtChannelSkuConfigDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtChannelSkuConfigModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtChannelSkuConfigDao {
    List<CmsMtChannelSkuConfigModel> selectList(Map<String, Object> map);

    CmsMtChannelSkuConfigModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    CmsMtChannelSkuConfigModel select(Integer id);

    int insert(CmsMtChannelSkuConfigModel record);

    int update(CmsMtChannelSkuConfigModel record);

    int delete(Integer id);
}
/*
 * ComMtTrackingInfoConfigDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.admin;

import com.voyageone.service.model.admin.ComMtTrackingInfoConfigModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ComMtTrackingInfoConfigDao {
    List<ComMtTrackingInfoConfigModel> selectList(Map<String, Object> map);

    ComMtTrackingInfoConfigModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    ComMtTrackingInfoConfigModel select(Integer seq);

    int insert(ComMtTrackingInfoConfigModel record);

    int update(ComMtTrackingInfoConfigModel record);

    int delete(Integer seq);
}
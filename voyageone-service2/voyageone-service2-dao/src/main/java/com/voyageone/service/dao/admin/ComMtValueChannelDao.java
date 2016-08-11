/*
 * ComMtValueChannelDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.admin;

import com.voyageone.service.model.admin.ComMtValueChannelModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ComMtValueChannelDao {
    List<ComMtValueChannelModel> selectList(Map<String, Object> map);

    ComMtValueChannelModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    ComMtValueChannelModel select(Integer id);

    int insert(ComMtValueChannelModel record);

    int update(ComMtValueChannelModel record);

    int delete(Integer id);
}
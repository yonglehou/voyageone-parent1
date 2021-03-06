/*
 * TmSmsConfigDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.com;

import com.voyageone.service.model.com.TmSmsConfigModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface TmSmsConfigDao {
    List<TmSmsConfigModel> selectList(Map<String, Object> map);

    TmSmsConfigModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    TmSmsConfigModel select(Integer seq);

    int insert(TmSmsConfigModel record);

    int update(TmSmsConfigModel record);

    int delete(Integer seq);
}
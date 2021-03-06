/*
 * ComUserDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.user;

import java.util.List;

import com.voyageone.service.model.user.ComUserModel;
import org.springframework.stereotype.Repository;

@Repository
public interface ComUserDao {
    List<ComUserModel> selectList(Object map);

    ComUserModel selectOne(Object map);

    int selectCount(Object map);

    ComUserModel select(Integer id);

    int insert(ComUserModel record);

    int update(ComUserModel record);

    int delete(Integer id);
}
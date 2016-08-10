/*
 * ComUserRoleDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.security.dao;

import com.voyageone.security.model.ComUserRoleModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ComUserRoleDao {
    List<ComUserRoleModel> selectList(Map<String, Object> map);

    ComUserRoleModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    ComUserRoleModel select(Integer id);

    int insert(ComUserRoleModel record);

    int update(ComUserRoleModel record);

    int delete(Integer id);
}
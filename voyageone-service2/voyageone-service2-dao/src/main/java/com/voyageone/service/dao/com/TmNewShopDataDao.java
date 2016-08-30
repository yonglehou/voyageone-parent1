/*
 * TmNewShopDataDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.com;

import com.voyageone.service.model.com.TmNewShopDataModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TmNewShopDataDao {
    List<TmNewShopDataModel> selectList(Object map);

    TmNewShopDataModel selectOne(Object map);

    int selectCount(Object map);

    TmNewShopDataModel select(Long id);

    int insert(TmNewShopDataModel record);

    int update(TmNewShopDataModel record);

    int delete(Long id);
}
/*
 * ComMtThirdPartyConfigDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.com;

import com.voyageone.service.model.com.ComMtThirdPartyConfigModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ComMtThirdPartyConfigDao {
    List<ComMtThirdPartyConfigModel> selectList(Map<String, Object> map);

    ComMtThirdPartyConfigModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    ComMtThirdPartyConfigModel select(Integer seq);

    int insert(ComMtThirdPartyConfigModel record);

    int update(ComMtThirdPartyConfigModel record);

    int delete(Integer seq);
}
/*
 * VmsBtOrderDetailDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.vms;

import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface VmsBtOrderDetailDao {
    List<VmsBtOrderDetailModel> selectList(Object map);

    VmsBtOrderDetailModel selectOne(Object map);

    int selectCount(Object map);

    VmsBtOrderDetailModel select(Integer id);

    int insert(VmsBtOrderDetailModel record);

    int update(VmsBtOrderDetailModel record);

    int delete(Integer id);
}
package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmMasterBrandDao {
    public List<CmsBtJmMasterBrandModel>  selectList();
    public  CmsBtJmMasterBrandModel select(long id);
    public int insert(CmsBtJmMasterBrandModel entity);
    public  int update(CmsBtJmMasterBrandModel entity);

    public  int delete(long id);
    }

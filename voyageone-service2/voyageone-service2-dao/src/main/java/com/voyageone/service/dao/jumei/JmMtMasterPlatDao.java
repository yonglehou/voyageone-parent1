package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmMtMasterPlatDao {
    public List<JmMtMasterPlatModel>  getList();
    public  JmMtMasterPlatModel get(long id);
    public int create(JmMtMasterPlatModel entity);
    public  int update(JmMtMasterPlatModel entity);

    public  int delete(long id);
    }

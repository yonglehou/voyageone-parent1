package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;
import java.util.Map;
import java.util.List;

@Repository
public interface CmsMtMasterInfoDao {
     public List<CmsMtMasterInfoModel> selectList(Map<String, Object> map);
    public CmsMtMasterInfoModel selectOne(Map<String, Object> map);
    public CmsMtMasterInfoModel select(long id);
    public int insert(CmsMtMasterInfoModel entity);
    public int update(CmsMtMasterInfoModel entity);
    public int delete(long id);
    }
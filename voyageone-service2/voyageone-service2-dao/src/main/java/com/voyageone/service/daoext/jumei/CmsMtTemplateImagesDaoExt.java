package com.voyageone.service.daoext.jumei;

import com.voyageone.service.model.jumei.CmsMtTemplateImagesModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsMtTemplateImagesDaoExt {
    public List<CmsMtTemplateImagesModel> getListByPlatformChannelTemplateType(@Param("platformId") int platformId,@Param("channelId") String channelId,@Param("templateTypeList") List<Integer> templateTypeList);
}

package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.CmsSetMainPropMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 将产品导入到主数据的值表
 *
 * @author tom
 */
@Component("cmsSetMainPropTask")
public class CmsSetMainPropJob extends BaseTaskJob {

    @Autowired
    private CmsSetMainPropMongoService cmsSetMainPropMongoService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsSetMainPropMongoService;
    }
}


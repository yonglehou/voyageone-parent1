package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.WmfAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gjl on 2016/12/5.
 */
@Component("CmsWmfAnalysisJob")
public class CmsWmfAnalysisJob extends BaseTaskJob {
    @Autowired
    private WmfAnalysisService wmfAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return wmfAnalysisService;
    }
}

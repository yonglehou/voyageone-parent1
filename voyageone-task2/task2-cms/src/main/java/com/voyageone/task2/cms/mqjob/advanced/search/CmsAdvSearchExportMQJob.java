package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.CmsBtExportTaskService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchExportMQMessageBody;
import com.voyageone.service.model.cms.CmsBtExportTaskModel;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import com.voyageone.task2.cms.service.product.batch.CmsAdvSearchExportFileService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 高级检索文件导出job
 *
 * @Author rex
 * @Create 2016-12-30 15:25
 */
@Service
@RabbitListener()
public class CmsAdvSearchExportMQJob extends TBaseMQCmsService<AdvSearchExportMQMessageBody> {

    @Autowired
    private CmsBtExportTaskService cmsBtExportTaskService;
    @Autowired
    private CmsAdvSearchExportFileService cmsAdvSearchExportFileService;

    @Override
    public void onStartup(AdvSearchExportMQMessageBody messageBody) throws Exception {
        Integer cmsBtExportTaskId = messageBody.getCmsBtExportTaskId();
        CmsBtExportTaskModel exportTaskModel = cmsBtExportTaskService.getExportById(cmsBtExportTaskId);
        if (exportTaskModel == null) {
            cmsBusinessExLog(messageBody, String.format("cms.bt.export.task(id=%s)不存在", cmsBtExportTaskId));
            return;
        }

        List<Map<String, String>> failList = cmsAdvSearchExportFileService.export(messageBody);
        if (CollectionUtils.isNotEmpty(failList)) {
            cmsSuccessIncludeFailLog(messageBody, JacksonUtil.bean2Json(failList));
        }

    }
}
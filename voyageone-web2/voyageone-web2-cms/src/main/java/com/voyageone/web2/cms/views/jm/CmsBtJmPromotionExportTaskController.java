package com.voyageone.web2.cms.views.jm;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionExportTaskService;
import com.voyageone.service.model.jumei.CmsBtJmPromotionExportTaskModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
//@RestController
@RequestMapping(
        value = CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsBtJmPromotionExportTaskController extends CmsController {
    @Autowired
    private CmsBtJmPromotionExportTaskService service;
///cms/CmsBtJmPromotionExportTask/index/getByPromotionId
  //  @RequestMapping( value = "/cms/CmsBtJmPromotionExportTask/index/getByPromotionId", method = RequestMethod.POST)//CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.GET_BY_PROMOTIONID)
   // @ResponseBody
  @RequestMapping(CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.GET_BY_PROMOTIONID)
  @ResponseBody
    public AjaxResponse getByPromotionId(@RequestBody int promotionId) {
        return success(service.getByPromotionId(promotionId));
    }
    @RequestMapping("downloadExcel")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String source = request.getParameter("source");
        HashMap<String, Object> hm = JacksonUtil.ToObjectFromJson(source, HashMap.class);

        CmsBtJmPromotionExportTaskModel model=service.get(Integer.parseInt(hm.get("id").toString()));
        String path = Properties.readValue(CmsConstants.Props.CMS_JM_EXPORT_PATH);
        String fileName = model.getFileName().trim();
        String filePath = path + "/" + fileName;
        File excelFile = new File(filePath);
      com.voyageone.common.util.FileUtils.downloadFile(response, fileName, filePath);
    }


}

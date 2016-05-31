package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.CmsBtTasksBean;
import com.voyageone.service.bean.cms.task.beat.TaskBean;
import com.voyageone.service.impl.cms.BeatInfoService;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.impl.cms.TaskService;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.impl.cms.promotion.PromotionModelService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;
import com.voyageone.service.model.cms.enums.jiagepilu.ImageStatus;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.voyageone.common.util.ExcelUtils.getString;

/**
 * 价格披露的数据服务
 * Created by jonasvlag on 16/2/29.
 *
 * @version 2.0.0
 */
@Service
class CmsTaskPictureService extends BaseAppService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private BeatInfoService beatInfoService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private PromotionModelService promotionModelService;

    @Autowired
    private PromotionCodeService promotionCodeService;

    @Autowired
    private ImageTemplateService imageTemplateService;

    /**
     * 创建一个价格披露任务
     *
     * @param taskBean 输入的创建参数
     * @param user     当前用户
     * @return 创建完成后的模型
     */
    public TaskBean create(TaskBean taskBean, UserSessionBean user) {

        CmsBtPromotionModel promotion = promotionService.getPromotion(taskBean.getPromotionId());

        if (promotion == null)
            throw new BusinessException("7000001");

        if (!taskBean.getConfig().isValid())
            throw new BusinessException("7000002");

        List<CmsBtTasksBean> taskModels;
        boolean insert = (taskBean.getUpdate() == null || !taskBean.getUpdate());

        // 如果不是更新, 而是创建
        if (insert) {

            // 尝试检查任务的名称, 是否已经存在
            taskModels = taskService.getTasks(
                    taskBean.getPromotionId(),
                    taskBean.getTaskName(),
                    user.getSelChannelId(),
                    PromotionTypeEnums.Type.JIAGEPILU.getTypeId());

            if (!taskModels.isEmpty())
                throw new BusinessException("7000003");

            taskBean.setTaskType(PromotionTypeEnums.Type.JIAGEPILU);
            taskBean.setChannelId(user.getSelChannelId());
            taskBean.setCreater(user.getUserName());
        }

        // 如果没提交刷图时间, 则使用 Promotion 的预热时间
        if (StringUtils.isEmpty(taskBean.getActivityStart()))
            taskBean.setActivityStart(taskBean.getPromotion().getPrePeriodStart());

        // 如果没提交还原时间, 则使用 Promotion 的结束时间
        if (StringUtils.isEmpty(taskBean.getActivityEnd()))
            taskBean.setActivityEnd(taskBean.getPromotion().getActivityEnd());

        taskBean.setModifier(user.getUserName());

        int count = insert ? taskService.addTask(taskBean.toModel()) : taskService.updateConfig(taskBean.toModel());

        if (count < 1)
            return null;

        taskModels = taskService.getTasks(
                taskBean.getPromotionId(),
                taskBean.getTaskName(),
                user.getSelChannelId(),
                taskBean.getTaskType().getTypeId());

        return new TaskBean(taskModels.get(0));
    }

    /**
     * 获取 task_id 任务下的所有价格披露信息
     *
     * @param task_id 任务 ID
     * @param flag    指定的任务状态
     * @param offset  分页偏移量
     * @param size    分页数
     * @return 数据集合
     */
    List<CmsBtBeatInfoBean> getAllBeat(int task_id, BeatFlag flag, String searchKey, int offset, int size) {

        return beatInfoService.getBeatInfoListByTaskId(task_id, flag, searchKey, offset, size);
    }

    /**
     * 获取 task_id 任务下的所有价格披露信息的总数, 参看方法 getAllBeat
     *
     * @param task_id 任务 ID
     * @param flag    指定的任务状态
     * @return 数据集合
     */
    int getAllBeatCount(int task_id, BeatFlag flag, String searchKey) {

        return beatInfoService.getBeatInfoCountByTaskId(task_id, flag, searchKey);
    }

    /**
     * 获取价格披露的统计信息
     *
     * @param task_id 任务 ID
     * @return 统计信息, List[Map{flag&count}]
     */
    List<Map<String, Object>> getBeatSummary(int task_id) {
        List<Map<String, Object>> result = beatInfoService.getBeatSummary(task_id);
        // 数据查询出来的是整数, 转换为枚举名
        for (Map<String, Object> map : result) {
            Object flag = map.get("flag");
            Integer flagId = Integer.valueOf(String.valueOf(flag));
            BeatFlag flagEnum = BeatFlag.valueOf(flagId);
            map.put("flag", flagEnum.name());
        }
        return result;
    }

    List<CmsBtBeatInfoBean> importBeatInfo(int task_id, int size, MultipartFile file, UserSessionBean user) {

        // 如果存在以下标识数据, 就不能重新导入
        int count = beatInfoService.getCountInFlags(task_id, BeatFlag.BEATING, BeatFlag.RE_FAIL, BeatFlag.REVERT, BeatFlag.SUCCESS);

        if (count > 0)
            throw new BusinessException("7000004");

        try (InputStream a = file.getInputStream();
             Workbook wb = WorkbookFactory.create(a)) {

            Sheet sheet = wb.getSheetAt(0);

            List<CmsBtBeatInfoBean> models = new ArrayList<>();

            for (Row row : sheet) {

                String value = getString(row, 0, "#");

                if (StringUtils.isEmpty(value))
                    break;

                if (!StringUtils.isNumeric(value))
                    throw new BusinessException("7000006", row.getRowNum());

                CmsBtBeatInfoBean model = new CmsBtBeatInfoBean();

                model.setNumIid(Long.valueOf(value));
                model.setProductCode(getString(row, 1));
                model.setSynFlag(BeatFlag.STOP); // syn_flag
                model.setTaskId(task_id);
                model.setCreater(user.getUserName());
                model.setModifier(user.getUserName());
                model.setMessage("");
                model.setImageStatus(ImageStatus.None);
                model.setImageTaskId(0);

                Date now = DateTimeUtil.getDate();
                model.setCreated(now);
                model.setModified(now);

                models.add(model);
            }

            beatInfoService.importBeatInfo(task_id, models);

        } catch (IOException | InvalidFormatException e) {
            throw new BusinessException("7000005");
        }

        return getAllBeat(task_id, null, null, 0, size);
    }

    byte[] downloadBeatInfo(int task_id) {

        List<CmsBtBeatInfoBean> beatInfoModels = beatInfoService.getBeatInfByTaskId(task_id);

        // 注意: HSSFWorkbook 为 2003 的 xls 格式
        try (Workbook book = new HSSFWorkbook()) {

            Sheet sheet = book.createSheet();

            Row row = row(sheet, 0);

            cell(row, 0, null).setCellValue("num_iid");

            cell(row, 1, null).setCellValue("商品 Code");

            cell(row, 2, null).setCellValue("状态");

            cell(row, 3, null).setCellValue("结果信息");

            // 设置查询类型
            for (int i = 0; i < beatInfoModels.size(); i++) {

                CmsBtBeatInfoBean model = beatInfoModels.get(i);

                row = row(sheet, i + 1);

                cell(row, 0, null).setCellValue(model.getNumIid());

                cell(row, 1, null).setCellValue(model.getProductCode());

                cell(row, 2, null).setCellValue(model.getSynFlagEnum().name());

                cell(row, 3, null).setCellValue(model.getMessage());
            }

            // 打开保存
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                book.write(outputStream);
                return outputStream.toByteArray();
            }

        } catch (IOException e) {
            throw new BusinessException("7000007", e);
        }
    }

    int control(Integer beat_id, Integer task_id, BeatFlag flag, Boolean force, UserSessionBean user) {
        if (beat_id != null)
            return setFlag(beat_id, flag, user);
        else if (task_id != null)
            return setFlags(task_id, flag, force, user);
        else
            return 0;
    }

    List<Map<String, Object>> getNewNumiid(Integer task_id) {
        if (task_id == null) return null;
        CmsBtTasksBean taskModel = taskService.getTaskWithPromotion(task_id);
        if (taskModel == null) return null;
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId", taskModel.getPromotionId());
        return promotionModelService.getPromotionModelDetailList(map);
    }

    List<CmsBtPromotionCodesBean> getCodes(int promotionId, String productModel) {
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId", promotionId);
        map.put("productModel", productModel);
        return promotionCodeService.getPromotionCodeList(map);
    }

    List<CmsBtBeatInfoBean> addCheck(int task_id, String num_iid) {
        CmsBtTasksBean taskModel = taskService.getTaskWithPromotion(task_id);
        if (taskModel == null)
            throw new BusinessException("没找到 Promotion");
        return beatInfoService.getBeatInfByNumiidInOtherTask(taskModel.getPromotionId(), task_id, num_iid);
    }

    List<CmsBtImageTemplateModel> getTemplatesByPromotion(int promotionId) {

        CmsBtPromotionModel promotionModel = promotionService.getPromotion(promotionId);

        if (promotionModel == null)
            throw new BusinessException("没找到 Promotion");

        return imageTemplateService.getAllTemplate(promotionModel.getChannelId(), promotionModel.getCartId());
    }

    public Integer add(int task_id, String num_iid, String code, UserSessionBean user) {

        CmsBtTasksBean taskModel = taskService.getTaskWithPromotion(task_id);

        if (taskModel == null) return null;

        CmsBtBeatInfoBean model = beatInfoService.getBeatInfByNumiid(task_id, num_iid);

        if (model != null) {

            if (model.getProductCode().equals(code))
                return 0;

            model.setImageStatus(ImageStatus.None);
            model.setProductCode(code);
            model.setModifier(user.getUserName());
            return beatInfoService.updateCode(model);
        }

        model = new CmsBtBeatInfoBean();
        model.setNumIid(Long.valueOf(num_iid));
        model.setProductCode(code);
        model.setSynFlag(BeatFlag.STOP);
        model.setTaskId(task_id);
        model.setCreater(user.getUserName());
        model.setModifier(user.getUserName());
        model.setImageStatus(ImageStatus.None);
        model.setImageTaskId(0);
        model.setMessage("");

        Date now = DateTimeUtil.getDate();
        model.setCreated(now);
        model.setModified(now);

        List<CmsBtBeatInfoBean> list = new ArrayList<>();
        list.add(model);
        return beatInfoService.addTasks(list);
    }

    private Row row(Sheet sheet, int rowIndex) {

        Row row = sheet.getRow(rowIndex);

        if (row == null) row = sheet.createRow(rowIndex);

        return row;
    }

    private Cell cell(Row row, int index, CellStyle cellStyle) {

        Cell cell = row.getCell(index);

        if (cell == null) cell = row.createCell(index);

        if (cellStyle != null) cell.setCellStyle(cellStyle);

        return cell;
    }

    /**
     * 设定任务到指定状态, 并重置任务的图片状态
     */
    private int setFlag(int beat_id, BeatFlag flag, UserSessionBean user) {

        if (flag == null)
            throw new BusinessException("7000002");

        CmsBtBeatInfoBean beatInfoModel = beatInfoService.getBeatInfById(beat_id);

        if (beatInfoModel == null)
            return 0;

        beatInfoModel.setImageStatus(ImageStatus.None);
        beatInfoModel.setSynFlag(flag);
        beatInfoModel.setModifier(user.getUserName());
        return beatInfoService.updateBeatInfoFlag(beatInfoModel);
    }

    /**
     * 设定所有任务到指定状态, 并重置所有任务的图片状态
     */
    private int setFlags(int task_id, BeatFlag flag, Boolean force, UserSessionBean user) {

        if (flag == null)
            throw new BusinessException("7000002");

        return beatInfoService.updateBeatInfoFlag(task_id, flag, ImageStatus.None, force, user.getUserName());
    }
}

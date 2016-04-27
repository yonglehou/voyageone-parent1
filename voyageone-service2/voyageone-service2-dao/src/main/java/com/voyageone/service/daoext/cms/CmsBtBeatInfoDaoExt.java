package com.voyageone.service.daoext.cms;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.enums.BeatFlag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jonasvlag on 16/2/29.
 *
 * @version 2.0.0
 */
@Repository
public class CmsBtBeatInfoDaoExt extends ServiceBaseDao {

    public int insertList(List<CmsBtBeatInfoBean> modelList) {
        return insert("cms_bt_beat_info_insertList", parameters("modelList", modelList));
    }

    /**
     * 更新 BeatInfo Message, 为那些在 Beat 中但不在 Promotion 中的 Code/Numiid
     */
    public int updateDiffPromotionMessage(int task_id, String message) {
        Map params = parameters(
                "task_id", task_id,
                "message", message,
                "syn_flag", BeatFlag.CANT_BEAT.getFlag());
        int count0 = update("cms_bt_beat_info_updateNoCodeMessage", params);
        int count1 = update("cms_bt_beat_info_updateNoNumiidMessage", params);
        return count0 + count1;
    }

    public List<CmsBtBeatInfoBean> selectListByTask(int task_id) {
        return selectList("cms_bt_beat_info_selectListByTask", parameters(
                "task_id", task_id));
    }

    public List<CmsBtBeatInfoBean> selectListByTask(int task_id, BeatFlag flag, int offset, int size) {
        return selectList("cms_bt_beat_info_selectListByTask_page", parameters(
                "task_id", task_id,
                "flag", flag,
                "offset", offset,
                "size", size));
    }

    public int selectListByTaskCount(int task_id, BeatFlag flag) {
        return selectOne("cms_bt_beat_info_selectListByTask_count", parameters(
                "task_id", task_id,
                "flag", flag));
    }

    public int deleteByTask(int task_id) {
        return delete("cms_bt_beat_info_deleteByTask", parameters("task_id", task_id));
    }

    public int selectCountInFlags(int task_id, BeatFlag... flags) {
        return selectOne("cms_bt_beat_info_selectCountInFlags", parameters("task_id", task_id, "flags", flags));
    }

    public CmsBtBeatInfoBean selectOneById(int beat_id) {
        return selectOne("cms_bt_beat_info_selectOneById", parameters("beat_id", beat_id));
    }

    public int updateFlag(CmsBtBeatInfoBean beatInfoModel) {
        return update("cms_bt_beat_info_updateFlag", beatInfoModel);
    }

    public int updateFlags(Integer task_id, BeatFlag flag, String userName) {
        return update("cms_bt_beat_info_updateFlags", parameters(
                "task_id", task_id,
                "modifier", userName,
                "syn_flag", flag.getFlag()));
    }

    public List<Map> selectSummary(int task_id) {
        return selectList("cms_bt_beat_info_selectSummary", parameters("task_id", task_id));
    }

    public List<CmsBtBeatInfoBean> selectListByNumiidInOtherTask(int promotion_id, int task_id, String num_iid) {
        return selectList("cms_bt_beat_info_selectListByNumiidInOtherTask", parameters(
                "promotion_id", promotion_id,
                "task_id", task_id,
                "num_iid", num_iid
        ));
    }

    public CmsBtBeatInfoBean selectOneByNumiid(int task_id, String num_iid) {
        return selectOne("cms_bt_beat_info_selectOneByNumiid", parameters(
                "task_id", task_id,
                "num_iid", num_iid
        ));
    }

    public int updateCode(CmsBtBeatInfoBean model) {
        return update("cms_bt_beat_info_updateCode", model);
    }

    /**
     * 查询在特定条件下的, 需要处理的价格披露信息
     *
     * @param limit TOP 行数
     * @return 带有 Promotion_Code 和 Task 信息 CmsBtBeatInfoModel
     */
    public List<CmsBtBeatInfoBean> selectListNeedBeatFullData(int limit) {
        return selectList("cms_bt_beat_info_selectListNeedBeatFullData", parameters(
                "upFlag", BeatFlag.BEATING.getFlag(),
                "revertFlag", BeatFlag.REVERT.getFlag(),
                "downFlag", BeatFlag.SUCCESS.getFlag(),
                "now", DateTimeUtil.getNow(),
                "limit", limit
        ));
    }

    public int updateFlagAndMessage(CmsBtBeatInfoBean beatInfoModel) {
        return update("cms_bt_beat_info_updateFlagAndMessage", beatInfoModel);
    }
}
package com.voyageone.batch.synship.service;

import com.voyageone.batch.SynshipConstants;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.synship.dao.IdCardDao;
import com.voyageone.batch.synship.dao.IdCardHistoryDao;
import com.voyageone.batch.synship.dao.ShortUrlDao;
import com.voyageone.batch.synship.dao.SmsHistoryDao;
import com.voyageone.batch.synship.modelbean.*;
import com.voyageone.common.components.ems.B2COrderServiceStub;
import com.voyageone.common.components.ems.EmsService;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import org.apache.axis2.AxisFault;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.voyageone.batch.SynshipConstants.AuditResult.FAIL;
import static com.voyageone.batch.SynshipConstants.AuditResult.PASS;
import static com.voyageone.batch.SynshipConstants.IdCardStatus.*;
import static com.voyageone.batch.SynshipConstants.Reason.*;
import static com.voyageone.batch.SynshipConstants.*;

/**
 * 从 Synship CloudClient 迁移的身份证验证任务
 * <p>
 * Created by Jonas on 9/22/15.
 */
@Service
public class ValidIdCardByEmsService extends BaseTaskService {

    private final static String REJECTED_TEMPLATE =
            "顾客提交的下记资料没有通过跨境易的审核，请联系顾客重新提交资料。\r\n" +
                    "身份证号码：%s \r\n" +
                    "姓名：%s \r\n" +
                    "拒绝原因：%s";

    private final static int THREAD_COUNT = 1;

    private final static int EVERY_THREAD_COUNT = 30;

    @Autowired
    private IdCardDao idCardDao;

    @Autowired
    private ShortUrlDao shortUrlDao;

    @Autowired
    private IdCardHistoryDao idCardHistoryDao;

    @Autowired
    private SmsHistoryDao smsHistoryDao;

    @Autowired
    private SmsConfigService smsConfigService;

    /**
     * 获取子系统
     */
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.SYNSHIP;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "ValidIdCardByEms";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        int limit = THREAD_COUNT * EVERY_THREAD_COUNT;

        // 获取等待验证的数据
        List<IdCardBean> idCardBeans = idCardDao.selectNewestByApproved(WAITING_AUTO, limit);

        // 如果木有数据，那自然就结束掉
        if (idCardBeans == null || idCardBeans.size() == 0) {
            $info("没有找到需要验证的记录");
            return;
        }

        List<Runnable> runnable = new ArrayList<>();

        // 将集合拆分到线程
        for (int i = 0; i < THREAD_COUNT; i++) {

            int start = i * EVERY_THREAD_COUNT;
            int end = start + EVERY_THREAD_COUNT;

            List<IdCardBean> subList = idCardBeans.subList(start, end);

            runnable.add(() -> {
                try {
                    validOnThread(subList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        runWithThreadPool(runnable, taskControlList);
    }

    private void validOnThread(List<IdCardBean> idCardBeans) throws JSONException {
        // 有数据的话，那么开始加载一些固定的配置

        // 短信花费计算的配置
        Map<String, String> smsInfoMap = Codes.getCodeMap(SMS_INFO);
        String strSmsWords = smsInfoMap.get(SMS_WORDS);
        String smsCost = smsInfoMap.get(SMS_COST);
        double smsWords = Double.valueOf(strSmsWords);
        double eachFee = Double.valueOf(smsCost);

        // 短信内容的配置
        SmsConfig smsConfig = smsConfigService.getSmsConfigs(SynshipConstants.SMS_TYPE_CLOUD_CLIENT);

        $info("准备开始");

        for (IdCardBean idCardBean : idCardBeans) {
            // 打印分割
            $info("---------------------------------------------------------");
            // 打印属性
            $prop("idCardBean", "id_no", idCardBean.getId_no());
            $prop("idCardBean", "source_order_id", idCardBean.getSource_order_id());

            // 根据收货人和手机，查询之前的验证记录
            // 如果已经过了，那么继续下一个
            if (isAlreadyApproved(idCardBean)) continue;

            // 调用跨境易的接口，验证身份证信息的有效性
            IdCardHistory idCardHistory = callEmsValid(idCardBean);

            // 获取 ShortUrl，尝试从其中获取 channel
            // 后续多处使用
            ShortUrlBean shortUrlBean = shortUrlDao.getInfosBySourceOrderId(idCardHistory.getSource_order_id());

            trySetChannel(idCardHistory, shortUrlBean);

            idCardHistoryDao.insert(idCardHistory);

            // 如果通过了，继续执行一些后续逻辑，然后继续下一个
            if (isPass(idCardHistory)) {
                afterPass(idCardHistory, idCardBean, shortUrlBean);
                continue;
            }

            // 超时错误时，下次继续验证
            if (isTimeout(idCardHistory)) {
                $info("超时错误，等待下次验证");
                continue;
            }

            // 如果信息错误，直接转人工
            // 并在备注表明
            $info("身份证信息验证失败，直接转人工");

            // 暂时屏蔽数据
            updateApprovedWithMsg(idCardBean, REJECTED, getRejectedMsg(idCardHistory), idCardHistory.getReason());

            if (shortUrlBean == null || StringUtils.isEmpty(shortUrlBean.getComment())) {
                continue;
            }

            // 如果短链接是有内容的，则尝试发送短信通知客户
            sendSmsToCustomer(idCardHistory, smsConfig, idCardBean, shortUrlBean, smsWords, eachFee);
        }
    }

    private void sendSmsToCustomer(IdCardHistory idCardHistory, SmsConfig smsConfig, IdCardBean idCardBean, ShortUrlBean shortUrlBean, double smsWords, double eachFee ) {

        String targetCode1;

        if (idCardHistory.getMessage().contains("不匹配") ||
                idCardHistory.getMessage().contains("不正确")) {
            targetCode1 = SynshipConstants.SMS_CONTENT_VALID_NO_MATCH;
        } else {
            targetCode1 = SynshipConstants.SMS_CONTENT_VALID_NO_IMAGE;
        }

        Channel channel = Channel.valueOfId(idCardHistory.getOrder_channel_id());

        if (channel == null) {
            logIssue(String.format("发送短信前，获取渠道失败。参数 [ %s ]", idCardHistory.getOrder_channel_id()));
            // 渠道的数据不对，无法继续
            return;
        }

        SmsConfigBean configBean = smsConfig.get(channel, targetCode1);

        if (configBean == null || StringUtils.isEmpty(configBean.getContent())) {
            // 如果短信内容的配置，完全没有获取到的话，说明配置错误，不能继续
            logIssue(String.format("短信内容没有配置，渠道 [ %s ]，Code1 [ %s ]", channel, targetCode1));
            return;
        }

        // del flg 为 0，放弃发送
        if (!configBean.getDel_flg().equals("0"))
            return;

        $info("发送短信");

        String smsContent = configBean.getContent();

        // 短信内容中各参数的设定
        smsContent = String.format(smsContent, channel.getFullName(), idCardBean.getReceive_name(), shortUrlBean.getShort_key());

        $info("短消息发送成功的场合，更新发送履历 ");

        // 发送履历的做成
        SmsHistoryBean smsHistoryBean = new SmsHistoryBean();

        String dateTime = DateTimeUtil.getNow();

        double count = Math.ceil((double) smsContent.length() / smsWords);
        double smsFee = count * eachFee;

        smsHistoryBean.setSource_order_id(shortUrlBean.getSource_order_id());
        smsHistoryBean.setShip_phone(idCardBean.getPhone());
        smsHistoryBean.setShip_name(idCardBean.getReceive_name());
        smsHistoryBean.setSent_type(SynshipConstants.SMS_SNET_TYPE_CLIENT);
        smsHistoryBean.setSent_person(getTaskName());
        smsHistoryBean.setSent_time(dateTime);
        smsHistoryBean.setSent_conent(smsContent);
        smsHistoryBean.setSent_cost(String.valueOf(smsFee));
        smsHistoryBean.setStatus("00");
        smsHistoryBean.setSms_flg(SynshipConstants.SMS_STATUS_NOT_SENT);
        smsHistoryBean.setOrder_channel_id(idCardHistory.getOrder_channel_id());
        smsHistoryBean.setCreate_time(dateTime);
        smsHistoryBean.setUpdate_time(dateTime);
        smsHistoryBean.setCreate_person(getTaskName());
        smsHistoryBean.setUpdate_person(getTaskName());

        smsHistoryDao.insertSmsHistory(smsHistoryBean);
    }

    private String getRejectedMsg(IdCardHistory idCardHistory) {
        return String.format(REJECTED_TEMPLATE, idCardHistory.getId_card(), idCardHistory.getShip_name(), idCardHistory.getMessage());
    }

    private boolean isTimeout(IdCardHistory idCardHistory) {
        return idCardHistory.getMessage().contains("timed out") || idCardHistory.getMessage().contains("timeout");
    }

    private void afterPass(IdCardHistory idCardHistory, IdCardBean idCardBean, ShortUrlBean shortUrlBean) {

        if (idCardBean.getPhone().equals(shortUrlBean.getShip_phone()) && idCardBean.getReceive_name().equals(shortUrlBean.getShip_name())) {
            $info("信息验证成功");
            updateApprovedWithMsg(idCardBean, APPROVED, "跨境易审核结果：" + idCardHistory.getMessage(), idCardHistory.getReason());
            return;
        }

        // 通过检查后，继续检查订单信息
        // 如果订单信息不匹配，转人工

        String message = "跨境易审核结果：" + idCardHistory.getMessage() + getAlertCommentContent(idCardBean, shortUrlBean);
        $info("订单信息不一致，将转人工");
        $prop("shortUrlBean", "short_key", shortUrlBean.getShort_key());

        idCardHistory.setAudit_result(FAIL);
        idCardHistory.setReason(DIFF_FROM_ORDER);
        idCardHistory.setFunction("");
        idCardHistory.setIsSuccess(FAIL);
        idCardHistory.setMessage("");
        idCardHistory.setResult("");

        idCardHistoryDao.insert(idCardHistory);

        updateApprovedWithMsg(idCardBean, UNAUDITED, message, idCardHistory.getReason());
    }

    private void trySetChannel(IdCardHistory idCardHistory, ShortUrlBean shortUrlBean) throws JSONException {
        if (shortUrlBean == null || StringUtils.isEmpty(shortUrlBean.getComment()))
            return;

        JSONObject jsonObj = new JSONObject(shortUrlBean.getComment());

        idCardHistory.setOrder_channel_id(jsonObj.getString("order_channel_id"));
    }

    /**
     * 获取，在提交的信息与原订单信息不符时，用于保存提示客服的备注信息
     */
    private String getAlertCommentContent(IdCardBean bean, ShortUrlBean shortUrlBean) {

        StringBuilder str = new StringBuilder("\r\n");

        if (SHORTURL_PRE_SALE.equals(shortUrlBean.getType())) {

            str.append("预售订单：").append(shortUrlBean.getSource_order_id());
            str.append("\r\n");
            str.append("上传的收件人信息与预售时的不一致，请确保客户在正式下单时会使用此信息！");
            str.append("\r\n");
            str.append("预售时的手机号：").append(shortUrlBean.getShip_phone()).append("、姓名：").append(shortUrlBean.getShip_name());

        } else {

            str.append("上传的收件人信息与订单号（").append(shortUrlBean.getSource_order_id()).append("）的不一致，请及时确认同步OMS中的数据！");
            str.append("\r\n");
            str.append("下单时的手机号：").append(shortUrlBean.getShip_phone()).append("、姓名：").append(shortUrlBean.getShip_name());

        }

        str.append("\r\n");
        str.append("上传时的手机号：").append(bean.getPhone()).append("、姓名：").append(bean.getReceive_name());

        return str.toString();
    }

    /**
     * 根据 Reason 检查是否验证成功了
     */
    private boolean isPass(IdCardHistory idCardHistory) {
        String r = idCardHistory.getReason();
        return r.equals(EMS_EXISTS) || r.equals(EMS_PASS);
    }

    /**
     * 调用跨境易
     */
    private IdCardHistory callEmsValid(IdCardBean idCardBean) {

        $info("开始调用验证");

        final String name = idCardBean.getReceive_name(), idCode = idCardBean.getId_card();

        String function = "";

        try {
            B2COrderServiceStub.ServiceResult serviceResult;

            // 2. 检查身份证号格式及有效性

            // 先检查格式
            function = "validCardNoFormat";
            serviceResult = EmsService.validCardNoFormat(idCode);

            $info("callEmsValid : 通过 : validCardNoFormat");
            $info("通过结果 : " + serviceResult.getMessage());

            if (!serviceResult.getIsSuccess()) {
                // 验证格式不通过，写历史后，退出
                return getHistory(idCardBean, ID_CARD_FORMAT_ERROR, function, serviceResult.getMessage());
            }

            function = "validCardNo";
            serviceResult = EmsService.validCardNo(name, idCode);

            $info("callEmsValid : 通过 : validCardNo");
            $info("通过结果 : " + serviceResult.getMessage());

            if (!serviceResult.getIsSuccess()) {

                return getHistory(idCardBean, EMS_FAIL, function, serviceResult.getMessage());
            }
            // 验证成功，但是没有图片的场合，也作为失败
            else if (StringUtils.isEmpty(serviceResult.getData())) {
                $info("身份证图片未取得，无法通过");

                return getHistory(idCardBean, IMAGE_NOT_EXISTS, function, serviceResult.getMessage() + "(身份证图片未取得，无法清关。)");
            }

            return getHistory(idCardBean, EMS_PASS, function, serviceResult.getMessage());

        } catch (AxisFault a) {
            String msg = CommonUtil.getExceptionContent(a);
            $info(msg);
            return getHistory(idCardBean, CALL_EMS_FAIL, function, msg);
        } catch (Exception e) {
            String msg = CommonUtil.getExceptionContent(e);
            $info(msg);
            return getHistory(idCardBean, CALL_ERROR, function, msg);
        }
    }

    private int updateApprovedWithMsg(IdCardBean idCardBean, String approved, String msg, String auto_type) {

        if (!StringUtils.isEmpty(auto_type))
            idCardBean.setAutomatic_type(auto_type);

        idCardBean.setApproved(approved);
        idCardBean.setComments(msg);
        idCardBean.setUpdate_person(getTaskName());
        int count = idCardDao.updateApprovedWithMsg(idCardBean);

        $info("{ updateApprovedWithMsg } 保存结果: " + count);

        return count;
    }

    private boolean isAlreadyApproved(IdCardBean idCardBean) {
        // 根据收货人和手机，查询之前的验证记录
        int countExists = idCardDao.selectCountByPhoneName(idCardBean.getPhone(), idCardBean.getReceive_name(), APPROVED);

        // 无记录
        if (countExists < 1)
            return false;

        // 同一手机号码、姓名已验证通过的场合，直接删除
        $info("已存在同一手机号码、姓名的审核记录");

        // 暂时屏蔽数据
        updateApprovedWithMsg(idCardBean, DELETE, "已存在同一手机号码、姓名的审核记录", PHONE_NAME_EXISTS);

        return true;
    }

    private IdCardHistory getHistory(IdCardBean idCardBean, String reason, String function, String message) {

        IdCardHistory idCardHistory = new IdCardHistory();

        idCardHistory.setSource_order_id(idCardBean.getSource_order_id());
        idCardHistory.setShip_name(idCardBean.getReceive_name());
        idCardHistory.setShip_phone(idCardBean.getPhone());
        idCardHistory.setId_card(idCardBean.getId_card());

        idCardHistory.setReason(reason);
        idCardHistory.setFunction(function);
        idCardHistory.setMessage(message);

        // 通过 reason 判断结果
        String result = isPass(idCardHistory) ? PASS : FAIL;

        idCardHistory.setAudit_result(result);
        idCardHistory.setIsSuccess(result);

        return idCardHistory;
    }
}

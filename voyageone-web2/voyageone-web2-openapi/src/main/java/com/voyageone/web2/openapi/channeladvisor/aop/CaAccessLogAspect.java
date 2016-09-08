package com.voyageone.web2.openapi.channeladvisor.aop;

import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.vms.ApiClientAccessLogService;
import com.voyageone.service.model.vms.VmsBtClientAccessLogModel;
import com.voyageone.web2.openapi.channeladvisor.exception.CAApiException;
import com.voyageone.web2.openapi.channeladvisor.util.ResponseUtils;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
@Aspect
public class CaAccessLogAspect {

    private static final String SELLER_ID = "SellerID";

    private static final String POINT_CUT = "execution(public * com.voyageone.web2.openapi.channeladvisor.control.*Controller.*(..))";

    private static final Logger log = LoggerFactory.getLogger(CaAccessLogAspect.class);

    @Autowired
    private ApiClientAccessLogService apiClientAccessLogService;

    @Autowired
    protected IssueLog issueLog;

    @Around(POINT_CUT)
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        log.info("CaAccessLogAspect_doAround start:" + pjp.getSignature().getName());
        Object responseObj = pjp.proceed();
        saveLogToDB(pjp, responseObj, null);
        log.info("CaAccessLogAspect_doAround end:" + pjp.getSignature().getName());
        return responseObj;
    }

    @AfterThrowing(value = POINT_CUT, throwing = "e")
    public void doAfterThrow(JoinPoint jp, Exception e) {
        log.error("发生异常：" + jp.getSignature().getName(), e);
        saveLogToDB(jp, null, e);
    }

    private void saveLogToDB(JoinPoint jp, Object responseObj, Exception exception) {
        VmsBtClientAccessLogModel model = new VmsBtClientAccessLogModel();
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            // sellerID
            String sellerID = request.getHeader(SELLER_ID);
            if (sellerID == null) {
                sellerID = "";
            }
            model.setSellerId(sellerID);

            // accessUri
            String accessUri = request.getRequestURI();
            model.setAccessUri(accessUri);

            // access_param
            String accessParam = "";
            Object[] inputParams = jp.getArgs();
            if (inputParams != null && inputParams.length > 0) {
                List<Object> paramList = new ArrayList<>();
                for (Object inputParam : inputParams) {
                    if (!HttpServletRequest.class.isInstance(inputParam)) {
                        paramList.add(inputParam);
                    }
                }
                accessParam = JacksonUtil.bean2Json(paramList);
            }
            model.setAccessParam(accessParam);

            // res_status
            String resStatus = "unknown";
            // res_result
            String resResult = null;
            // res_exception
            String resException = null;

            if (responseObj != null) {
                if (ActionResponse.class.isInstance(responseObj)) {
                    ActionResponse actionResponse = (ActionResponse)responseObj;
                    resStatus = actionResponse.getStatus().name();
                }
                resResult = JacksonUtil.bean2Json(responseObj);
            }

            if (exception != null) {
                if (exception instanceof CAApiException) {
                    ActionResponse actionResponse = ResponseUtils.buildError(exception);
                    resStatus = actionResponse.getStatus().name();
                    resResult = JacksonUtil.bean2Json(actionResponse);
                } else {
                    resStatus = "exception";
                    resException = CommonUtil.getExceptionSimpleContent(exception);
                    // Insert issueLog
                    issueLog.log(exception, ErrorType.WSDL, SubSystem.VMS, format("<p>出现未处理异常: [ %s ]</p>%s", accessUri, resException));
                }
            }

            // response
            model.setResStatus(resStatus);
            model.setResResult(resResult);
            model.setResException(resException);

            apiClientAccessLogService.saveLog(model);

        } catch (Exception e) {
            log.error("CaAccessLogAspect_saveLogToDB:", e);
        }
    }
}

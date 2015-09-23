package com.voyageone.wms.controller;

import com.voyageone.common.configs.Type;
import com.voyageone.common.configs.beans.MasterInfoBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.base.BaseController;
import com.voyageone.wms.WmsUrlConstants.ReturnUrls;
import com.voyageone.wms.formbean.FormReturn;
import com.voyageone.wms.service.WmsReturnService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.controller]  
 * @ClassName    [WmsReturnController]   
 *  [订单return处理的控制类]   
 * @Author       [sky]   
 * @CreateDate   [20150427]   
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
@Controller
@RequestMapping( method = RequestMethod.POST)
public class WmsReturnController extends BaseController{

	private static Log logger = LogFactory.getLog(WmsReturnController.class);
	@Autowired
	private WmsReturnService wmsReturnService;
	
	/**
	 * 【returnList页面】初始化，获取下拉框内容
	 * @param response 发送数据给页面
	 * @param paramMap 需要获取的下拉框控件的option名称和对应的typeId {name: typeId}
	 * @create 20150427
	 */
	@RequestMapping(ReturnUrls.ReturnList.INIT)
	public void doInit(HttpServletResponse response, @RequestBody Map<String,Integer> paramMap) {
		Map<String, Object> resultMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();
		for(String strkey : paramMap.keySet()){
			List<MasterInfoBean> list = Type.getMasterInfoFromId(paramMap.get(strkey), true);
			resultMap.put(strkey, list);
		}
		// 获取开始日期（当前日期的一个月前）
		String date_from = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.addMonths(DateTimeUtil.getDate(), -1), getUser().getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
		resultMap.put("fromDate", date_from);
		// 获取结束日期（当前日期）
		String date_to = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), getUser().getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
		resultMap.put("toDate", date_to);
		result.setResult(true);
		result.setResultInfo(resultMap);
		result.writeTo(getRequest(), response);
		logger.info(result.toString());
	}
	
	/**
	 * 【returnList页面】检索returnList页面訂單的信息
	 * @param response 发送数据给页面
	 * @param formReturn bean对象
	 * @create 20150427
	 */
	@RequestMapping(ReturnUrls.ReturnList.SEARCH)
	public void doReturnListSearch(HttpServletResponse response, @RequestBody FormReturn formReturn) {
		wmsReturnService.doReturnListSearch(getRequest(), response, formReturn, getUser());
	}

//	/**
//	 * 【returnList页面】点击列表的Operations列的【Refunded】按钮修改return状态【Received to Refunded】
//	 * @param response 发送数据给页面
//	 * @param returnId returnId
//	 * @create 20150428
//	 */
//	@RequestMapping(ReturnUrls.ReturnList.CHANGESTATUS)
//	public void doChangeStatus(HttpServletResponse response, @RequestBody String returnId) {
//		wmsReturnService.changeStatus(getRequest(), response, returnId);
//	}
	
	/**
	 * 【newSession页面】初始化，获取下拉框内容
	 * @param response 发送数据给页面
	 * @param paramMap 参数Map
	 * @create 20150429
	 */
	@RequestMapping(ReturnUrls.newSession.INIT)
	public void doNewSessionInit(HttpServletResponse response, @RequestBody Map<String,String> paramMap) {
		wmsReturnService.doNewSessionInit(getRequest(), response, paramMap, getUser());
	}

	/**
	 * 【newSession页面】根据returnSessionId获取sessionInfo
	 * @param response 发送数据给页面
	 * @param paramMap returnSessionId
	 * @create 20150429
	 */
	@RequestMapping(ReturnUrls.newSession.GETSESSIONINFO)
	public void doGetSessionInfo(HttpServletResponse response,  @RequestBody Map<String,String> paramMap) {
		String returnSessionId = paramMap.get("id");
		wmsReturnService.getSessionInfo(getRequest(), response, returnSessionId, getSession(), getUser());
	}
	
	/**
	 * 【newSession页面】根据order_number获取对应的信息
	 * @param response 发送数据给页面
	 * @param orderNumber 订单编号
	 * @create 20150430
	 */
	@RequestMapping(ReturnUrls.newSession.GETORDERINFOBYORDNO)
	public void getOrderInfoByOrdNo(HttpServletResponse response, @RequestBody String orderNumber) {
		wmsReturnService.getOrderInfoByOrdNo(getRequest(), response, orderNumber, getUser());
	}
	
	/**
	 * 【newSession页面popUp】将扫描相关产品记录插入wms_bt_return表
	 * @param response 发送数据给页面
	 * @param object 参数对象
	 * @create 20150505
	 */
	@RequestMapping(ReturnUrls.newSession.INSERTRETURNINFO)
	public void insertReturnInfo(HttpServletResponse response, @RequestBody Object object) {
		wmsReturnService.insertReturnInfo(getRequest(), response, object, getUser());
	}
	
	/**
	 * 【newSession页面】点击Scan按钮的时候检测到没有选择returnSession则创建session
	 * @param  response 发送数据给页面
	 * @param object 参数对象
	 * @create 20150505
	 */
	@RequestMapping(ReturnUrls.newSession.CREATERETURNSESSION)
	public void createReturnSession(HttpServletResponse response, @RequestBody Object object) {
		wmsReturnService.createReturnSession(getRequest(), response, object, getUser());
	}
	
	/**
	 * 【newSession页面】关闭Session
	 * @param response HttpServletResponse
	 * @param sessionId sessionId
	 * @create 20150505
	 */
	@RequestMapping(ReturnUrls.newSession.CLOSERETURNSESSION)
	public void closeReturnSession(HttpServletResponse response, @RequestBody String sessionId) {
		wmsReturnService.closeReturnSession(getRequest(), response, sessionId, getUser(), getSession());
	}

	/**
	 * 【newSessionPoPUp 页面】根据storeID找到对应的快递公司项
	 * @param response HttpServletResponse
	 * @param storeId 仓库Id
	 * @create 20150527
	 */
	@RequestMapping(ReturnUrls.newSession.GETRECEIVEDFROMITEMBYSTOREID)
	public void doGetReceivedFromItemByStoreId(HttpServletResponse response, @RequestBody int storeId) {
		wmsReturnService.doGetReceivedFromItemByStoreId(getRequest(), response, storeId, getUser());
	}
	
	/**
	 * 【itemEdit页面】初始化，获取下拉框内容
	 * @param response HttpServletResponse
	 * @param paramMap 需要获取的下拉框控件的option名称和对应的typeId {name: typeId}
	 * @create 20150507
	 */
	@RequestMapping(ReturnUrls.ItemEdit.INIT)
	public void doItemEditInit(HttpServletResponse response, @RequestBody Map<String,Integer> paramMap) {
		Map<String, Object> resultMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();
		for(String strkey : paramMap.keySet()){
			List<MasterInfoBean> list = Type.getMasterInfoFromId(paramMap.get(strkey), false);
			resultMap.put(strkey, list);
		}
		result.setResult(true);
		result.setResultInfo(resultMap);
		result.writeTo(getRequest(), response);
		logger.info(result.toString());
	}
	
	/**
	 * 【itemEdit页面】编辑内容保存
	 * @param response HttpServletResponse
	 * @param object 参数对象
	 * @create 20150507
	 */
	@RequestMapping(ReturnUrls.ItemEdit.SAVE)
	public void doItemEditSave(HttpServletResponse response, @RequestBody Object object) {
		wmsReturnService.saveItemEdit(getRequest(), response, object, getUser());
	}
	
	/**
	 * 【newSession页面】删除returnInfo记录
	 * @param response HttpServletResponse
	 * @param resId reservationId
	 * @create 20150507
	 */
	@RequestMapping(ReturnUrls.ItemEdit.REMOVE)
	public void doReturnInfoRemove(HttpServletResponse response, @RequestBody String resId) {
		wmsReturnService.removeReturnInfo(getRequest(), response, resId);
	}
	
	/**
	 * 【RetnSessionList 页面】初始化
	 * @param response HttpServletResponse
	 * @param paramMap 需要获取的下拉框控件的option名称和对应的typeId {name: typeId;hasAllOption}
	 * @create 20150508
	 */
	@RequestMapping(ReturnUrls.SessionList.INIT)
	public void doSessionListInit( HttpServletResponse response, @RequestBody Map<String, String> paramMap) {
		wmsReturnService.SessionListInit(getRequest(), response, paramMap, getSession(), getUser());
	}
	
	/**
	 * 【RetnSessionList 页面】查询数据
	 * @param response HttpServletResponse
	 * @param object 参数对象
	 * @create 20150508
	 */
	@RequestMapping(ReturnUrls.SessionList.SEARCH)
	public void doSessionListSearch( HttpServletResponse response, @RequestBody Object object) {
		wmsReturnService.doSessionListSearch(getRequest(), response, object, getSession(), getUser());
	}
	
	/**
	 * 【SessionDetail 页面】初始化，查询sessionId对应的session内容
	 * @param response HttpServletResponse
	 * @param sessionId returnSessionId
	 * @create 20150508
	 */
	@RequestMapping(ReturnUrls.SessionDetail.INIT)
	public void doSessionDetailInit( HttpServletResponse response, @RequestBody String sessionId) {
		wmsReturnService.getSessionInfo(getRequest(), response, sessionId, getSession(), getUser());
	}

	/**
	 *  获取returnSession对应的属性（session级别）
	 * @param response HttpServletResponse
	 * @create 20150623
	 */
	@RequestMapping(ReturnUrls.newSession.GETRETURNTYPE)
	public void doGetReturnType( HttpServletResponse response) {
		wmsReturnService.getReturnType(getRequest(), response, getUser());
	}
}

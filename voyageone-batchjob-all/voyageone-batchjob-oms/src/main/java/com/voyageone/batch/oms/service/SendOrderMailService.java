package com.voyageone.batch.oms.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.core.Constants;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.dao.OrderDao;
import com.voyageone.batch.oms.formbean.InFormServiceSearchSKU;
import com.voyageone.batch.oms.formbean.OutFormSendOrderDetailMail;
import com.voyageone.batch.oms.formbean.OutFormSendOrderMail;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;

/**
 * 日报邮件
 * 
 * @author eric
 *
 */
@Service
public class SendOrderMailService {

	private static Log logger = LogFactory.getLog(SendOrderMailService.class);

	@Autowired
	private OrderDao orderDao;

	private static final String APPENDIX = ".csv";

	private static final String SUBJECT = "Order List";

	// private static final String RECEIVER = "maggie.yu@voyageone.cn;"
	// + "kobe.xin@voyageone.cn;"
	// + "tom.zhu@voyageone.cn;"
	// + "weijianhuaxp@163.com;"
	// + "dennis.zhang@voyageone.com;"
	// + "owen.sun@voyageone.cn;"
	// + "helena.han@voyageone.com;"
	// + "robert.gao@voyageone.com;"
	// + "catherine.gao@voyageone.cn;"
	// + "summer.hu@voyageone.cn;"
	// + "zero.zeng@voyageone.cn;"
	// + "cathy.liu@voyageone.cn";

	public boolean sendOrderMail() {

		boolean isSuccess = true;

		// 从配置文件获取CSV配置信息
		String orderfolder = Properties.readValue(OmsConstants.SEND_ORDER_FOLDER);
		File file = new File(orderfolder);
		if (!file.exists()) {
			try {
				file.mkdir();
				logger.info("文件夹创建成功！");
			} catch (Exception e) {

				logger.info("文件夹创建失败！" + e);
			}
		}
		// TODO 删除 模拟ITOMS对应的渠道 等邮件的渠道权限方法出来再更改,现在是先写死
		List<String> channelIdSet = new ArrayList<String>();
		channelIdSet.add(OmsConstants.CHANNEL_SNEAKERHEAD);
		channelIdSet.add(OmsConstants.CHANNEL_JC);
		channelIdSet.add(OmsConstants.CHANNEL_PA);
		channelIdSet.add(OmsConstants.CHANNEL_SPALDING);
		channelIdSet.add(OmsConstants.CHANNEL_BHFO);
		channelIdSet.add(OmsConstants.CHANNEL_RM);
		channelIdSet.add(OmsConstants.CHANNEL_CP);
		// 生成CSV文件
		// 拼接文件名
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		Calendar now = DateTimeUtil.getCustomCalendar(OmsConstants.SEND_ORDER_ZONE);
		now.add(Calendar.DAY_OF_YEAR, -1);
		Date checkdate = now.getTime();
		// 文件名拼接的时间
		String date_ymd = sdf1.format(checkdate);

		String strdate = sdf.format(checkdate);
		// 由于统计北京时间的当天订单，所以要默认时区为8
		String fromTime = DateTimeUtil.getGMTTimeFrom(strdate, OmsConstants.SEND_ORDER_ZONE);
		String endTime = DateTimeUtil.getGMTTimeTo(strdate, OmsConstants.SEND_ORDER_ZONE);
		// String fromTime = strdate+OmsConstants.SENDORDER_FORMTIME;
		// String endTime = strdate+OmsConstants.SENDORDER_ENDTIME;
		String output_file = "";

		for (String channelId : channelIdSet) {

			OrderChannelBean channelBean = ChannelConfigs.getChannel(channelId);
			String channelName = channelBean.getName();

			// 获取channelId对应的cartId
			List<ShopBean> shopBean = ShopConfigs.getChannelShopList(channelId);
			// List<String> cartId = new ArrayList<String>();

			// 存放邮件附件
			List<String> fileAffix = new ArrayList<String>();

			if (null != shopBean && shopBean.size() > 0) {
				for (ShopBean shopBeanObj : shopBean) {

					FileWriter fw = null;
					// cartId.add(shopBeanObj.getCart_id());
					List<OutFormSendOrderMail> result = new ArrayList<OutFormSendOrderMail>();
					try {
						// 获取订单信息
						result = orderDao.sendOrderMail(fromTime, endTime, channelId, shopBeanObj.getCart_id());
					} catch (Exception e) {
						logger.info(e);
						
					}
					// 生成的文件名
					String filename = ShopConfigs.getVal1(channelId, shopBeanObj.getCart_id(), ShopConfigEnums.Name.filename);
					output_file = orderfolder + date_ymd + channelName + filename;
					if (result != null && result.size() > 0) {
						if (channelIdResult(result, fw, output_file)) {
							fileAffix.add(output_file);
						}
					}
				}
			}

			// 获取统计结果
			String Totalresult = sendOrderMailTotal(channelId, channelName);
			// 拼接附件名字
			String subject = channelName + SUBJECT;

			// String filename_tg = orderfolder + channelName +
			// OmsConstants.SENDORDER_FILENAME_TG + date_ymd;

			try {

				Mail.send("eric.chen@voyageone.cn", subject, Totalresult, fileAffix, false);
				Mail.send("kobe.xin@voyageone.cn", subject, Totalresult, fileAffix, false);
				Mail.send("dennis.zhang@voyageone.com", subject, Totalresult, fileAffix, false);
				Mail.send("owen.sun@voyageone.cn", subject, Totalresult, fileAffix, false);
				Mail.send("cathy.liu@voyageone.cn", subject, Totalresult, fileAffix, false);
				Mail.send("zero.zeng@voyageone.cn", subject, Totalresult, fileAffix, false);
				Mail.send("will.wei@voyageone.cn", subject, Totalresult, fileAffix, false);
				Mail.send("zero.zeng@voyageone.cn", subject, Totalresult, fileAffix, false);
				Mail.send("tom.zhu@voyageone.cn", subject, Totalresult, fileAffix, false);
				Mail.send("gary.li@voyageone.cn", subject, Totalresult, fileAffix, false);
				Mail.send("veronica.sha@voyageone.cn", subject, Totalresult, fileAffix, false);
				Mail.send("aaron.deng@voyageone.cn", subject, Totalresult, fileAffix, false);
				Mail.send("shasha.yao@voyageone.com", subject, Totalresult, fileAffix, false);
				Mail.send("maggie.yu@voyageone.cn", subject, Totalresult, fileAffix, false);
				Mail.send("robert.gao@voyageone.com", subject, Totalresult, fileAffix, false);
				Mail.send("helena.han@voyageone.com", subject, Totalresult, fileAffix, false);
				logger.info("日报邮件发送成功！");

			} catch (MessagingException e) {

				logger.info("日报邮件发送失败！", e);

				isSuccess = false;

			}
		}

		return isSuccess;
	}

	/**
	 * 根据渠道生成结果
	 * 
	 * @param result
	 * @param fw
	 * @param output_file
	 * @return
	 */
	public boolean channelIdResult(List<OutFormSendOrderMail> result, FileWriter fw, String output_file) {
		boolean isSuccess = true;
		try {
			String title = "PayTime,SourceOrderId,Name,Sku,Product,Num,Type,Price,GrandTotal,Stock";
			fw = new FileWriter(output_file);
			fw.write(title);
			// 获取订单结果
			if (null != result && result.size() > 0) {
				for (int i = 0; i < result.size(); i++) {
					OutFormSendOrderMail sendOrder = new OutFormSendOrderMail();
					sendOrder = result.get(i);

					List<OutFormSendOrderDetailMail> orderDetailList = sendOrder.getOrderDetailList();
					for (int j = 0; j < orderDetailList.size(); j++) {
						StringBuffer line = new StringBuffer();
						fw.write("\r\n");
						String orderDateTime = StringUtils.null2Space2(sendOrder.getPaytime());
						if (orderDateTime.length() > 19) {
							orderDateTime = DateTimeUtil.getLocalTime(orderDateTime, OmsConstants.SEND_ORDER_ZONE).substring(0, 19);
						}
						line.append(orderDateTime).append(",");
						line.append(StringUtils.null2Space2(sendOrder.getSourceId())).append(",");
						line.append(StringUtils.null2Space2(sendOrder.getName())).append(",");
						// 从OrderDetaile中获取每一个订单的订单详情
						line.append(StringUtils.null2Space2(orderDetailList.get(j).getSku())).append(",");
						line.append(StringUtils.null2Space2(orderDetailList.get(j).getProduct())).append(",");
						line.append(StringUtils.null2Space2(orderDetailList.get(j).getNum())).append(",");
						// 调用WebService 接口获取库存数据
						InFormServiceSearchSKU inFormServiceSearchSKU = new InFormServiceSearchSKU();
						inFormServiceSearchSKU.setSku(orderDetailList.get(j).getSku());
						inFormServiceSearchSKU.setChannelId(sendOrder.getOrderChannelId());
						String param = JsonUtil.getJsonString(inFormServiceSearchSKU);
						String searchskuPath = Properties.readValue(OmsConstants.SEND_ORDER_SERVICE);
						String skuresult = HttpUtils.post(searchskuPath, param);
						List<Map<String, Object>> listMap = null;
						if (skuresult != null) {
							listMap = JsonUtil.jsonToMapList(skuresult);
						}
						if (listMap != null && listMap.size() > 0) {
							String type = String.valueOf(listMap.get(0).get("typename"));
							line.append(StringUtils.null2Space2(type)).append(",");

						} else {
							line.append("").append(",");

						}
						line.append(StringUtils.null2Space2(orderDetailList.get(j).getPrice())).append(",");

						// 获取每一个订单的支付总价格
						if (j == 0) {

							line.append(StringUtils.null2Space2(sendOrder.getGrandTotal()));
						} else {
							line.append(OmsConstants.SEND_ORDER_GRAND_TOTAL_ZREO);
						}
						if (listMap != null && listMap.size() > 0) {

							String stock = String.valueOf(listMap.get(0).get("quantity"));
							if (Integer.parseInt(stock) > 0) {
								line.append("stock in china").append(",");
							} else if (orderDetailList.get(j).getSku() == null || "".equals(orderDetailList.get(j).getSku())) {
								line.append("not send usa").append(",");
							} else {
								line.append("").append(",");
							}
						} else {

							line.append("").append(",");
						}
						fw.write(line.toString());
					}
				}
			}
		} catch (IOException e) {
			isSuccess = false;
			logger.error(e.getMessage());
		} finally {

			try {
				fw.close();
			} catch (IOException e) {
				isSuccess = false;
				logger.error(e.getMessage());
			}

		}
		return isSuccess;
	}

	/**
	 * 获取统计结果
	 */
	public String sendOrderMailTotal(String channelId, String channelName) {

		// 时间参数处理
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar now = DateTimeUtil.getCustomCalendar(OmsConstants.SEND_ORDER_ZONE);
		now.add(Calendar.DAY_OF_YEAR, -1);
		Date checkdate = now.getTime();
		String strdate = sdf.format(checkdate);
		// 由于统计北京时间的当天订单，所以要默认时区为8
		String fromTime = DateTimeUtil.getGMTTimeFrom(strdate, OmsConstants.SEND_ORDER_ZONE);
		String endTime = DateTimeUtil.getGMTTimeTo(strdate, OmsConstants.SEND_ORDER_ZONE);
		// String fromTime = strdate+OmsConstants.SENDORDER_FORMTIME;
		// String endTime = strdate+OmsConstants.SENDORDER_ENDTIME;
		// 获取查询结果
		List<Map<String, Object>> list = orderDao.sendOrderMailTotal(fromTime, endTime, channelId);
		List<Map<String, Object>> listOrder = orderDao.sendOrderMailOrderTotal(fromTime, endTime, channelId);
		StringBuffer result = new StringBuffer();
		// String nameCount = OmsConstants.SEND_ORDER_GRAND_TOTAL_ZREO;
		// String orderCount = OmsConstants.SEND_ORDER_GRAND_TOTAL_ZREO;
		// String skuCount = OmsConstants.SEND_ORDER_GRAND_TOTAL_ZREO;
		// String skutypeCount = OmsConstants.SEND_ORDER_GRAND_TOTAL_ZREO;
		// String totalPrice = OmsConstants.SEND_ORDER_GRAND_TOTAL_ZREO;
		String row = null;
		StringBuffer buf = new StringBuffer();
		String head = null;
		String tablecontent = null;
		// 统计详情遍历
		if (null != list && list.size() > 0 && null != listOrder && listOrder.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> skumap = list.get(i);
				Map<String, Object> ordermap = listOrder.get(i);
				String cartId = String.valueOf(skumap.get("cartId"));
				ShopBean shopBean = ShopConfigs.getShop(channelId, cartId);
				String name = shopBean.getShop_name() + "(" + shopBean.getComment() + ")";
				String skuCount = String.valueOf(skumap.get("skuCount"));
				String skutypeCount = String.valueOf(skumap.get("skutypeCount"));
				String nameCount = String.valueOf(ordermap.get("nameCount"));
				String orderCount = String.valueOf(ordermap.get("orderCount"));
				String totalPrice = String.valueOf(ordermap.get("totalPrice"));
				// 邮件发送格式
				row = String.format(OmsConstants.SEND_ORDER_ROW, name, orderCount, skuCount, skutypeCount, nameCount, totalPrice);
				buf.append(row);
				head = OmsConstants.SEND_ORDER_HEAD;
				tablecontent = OmsConstants.SEND_ORDER_TABLE;
			}
		} else {
			tablecontent = OmsConstants.SEND_ORDER_TABLE_NULL;
			head = channelName + OmsConstants.SEND_ORDER_HEAD_NULL;
		}
		// 拼接Table
		String table = String.format(tablecontent, head, buf.toString());
		result.append(OmsConstants.EMAIL_STYLE_STRING).append(table);
		return result.toString();
	}

	/*
	 * public String RenameOrderList(String filepath, String filename, String
	 * date) {
	 * 
	 * File file = new File(filepath + filename + APPENDIX); File dest = new
	 * File(filepath + filename + date + APPENDIX); if(file.isFile()) {
	 * if(file.renameTo(dest)) { logger.info("File rename sucess : " +
	 * dest.getName()); try { File file2 = new File(filepath + filename +
	 * APPENDIX); if(file2.createNewFile()) { FileWriter writer = new
	 * FileWriter(file2, true); String title =
	 * "payTime,SourceId,Name,Product,Sku,Price,GrandTotal";
	 * writer.write(title); writer.close(); logger.info("File create sucess : "
	 * + file2.getName()); } } catch (IOException e) {
	 * logger.error(e.getMessage()); } } } return filepath + filename + date +
	 * APPENDIX; }
	 */
	
	/**
	 * sneakerhead 88店庆每小时统计消费前10顾客
	 * 
	 * @return
	 */
	public boolean sendSneakerhead88Top10Mail() {
		
		List<Map<String, String>> top10MapList = orderDao.getSendSneakerhead88Top10();
		
		if (top10MapList != null && top10MapList.size() > 0) {
			
			StringBuilder tbody = new StringBuilder();
			
			for (int i = 0; i < top10MapList.size(); i++) {
				Map<String, String> top10Map = top10MapList.get(i);
				
				String wangwangId = String.valueOf(top10Map.get("wangwangId"));
				String consumeMoney = String.valueOf(top10Map.get("consumeMoney"));
				
				// 邮件每行正文
				String mailTextLine = 
					String.format(OmsConstants.SNEAKERHEAD_TOP10_CHECK_ROW, "第" + (i+1) + "名", wangwangId, consumeMoney);
				tbody.append(mailTextLine);
			}
			
			// 需要发警告邮件
			if (tbody.length() > 0) {
				// 拼接table
				String body = String.format(OmsConstants.SNEAKERHEAD_TOP10_CHECK_TABLE, OmsConstants.SNEAKERHEAD_TOP10_CHECK_HEAD, tbody.toString());
				
				// 拼接邮件正文
				StringBuilder emailContent = new StringBuilder();
				emailContent.append(Constants.EMAIL_STYLE_STRING).append(body);
				try {
					String receiver = "SNEAKERHEAD_TOP10";
					Mail.send2(receiver, OmsConstants.SNEAKERHEAD_TOP10_CHECK_SUBJECT, emailContent.toString());
					logger.info("邮件发送成功!");
					
				} catch (MessagingException e) {
					logger.info("邮件发送失败！" + e);
					
					return false;
				}
			}
		}
		
		return true;
	}
}

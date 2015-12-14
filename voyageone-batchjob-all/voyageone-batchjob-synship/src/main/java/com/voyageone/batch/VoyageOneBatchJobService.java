/**
 * 
 */
package com.voyageone.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;


/**
 * @author jacky.zhu
 *
 */
public class VoyageOneBatchJobService {

	private static final Log log = LogFactory.getLog(com.voyageone.batch.VoyageOneBatchJobService.class);
	
	private static final String BATCH_JOB_NAME = "VoyageOne_BatchJob";
	
	public static void main(String[] args) {
//		// 判断同名实例是否已经启动  TODO
//		String pid = CommonUtils.getPid();
//		boolean isExistSameNameJobRunning = CommonUtils.isExistJavaBatchJobRunning4Linux(BATCH_JOB_NAME, pid);
//		if (isExistSameNameJobRunning) {
//			log.info(BATCH_JOB_NAME + "另一个实例已经启动，本次实例不启动！");
//		} else {
			com.voyageone.batch.VoyageOneBatchJobService service = new com.voyageone.batch.VoyageOneBatchJobService();
			try {
				log.info(BATCH_JOB_NAME + "启动中......");
				service.startServer();
				log.info(BATCH_JOB_NAME + "启动完成......");
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error(ex.getMessage());
			}
//		}
	}
	
	private void startServer() {
		// 数据库初始化
//		DBInitConfig.getInstance();
		
		// 上下文获得
		Context conext = Context.getContext();
		ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
		conext.putAttribute("springContext", ctx);
		
		// 库存同步服务类初始化
//		SynInventoryFromUsaService.getInstance();
	}
	


}

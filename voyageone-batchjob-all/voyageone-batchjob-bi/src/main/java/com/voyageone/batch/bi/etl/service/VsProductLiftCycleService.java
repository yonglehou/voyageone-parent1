package com.voyageone.batch.bi.etl.service;

import org.springframework.stereotype.Service;

@Service
public class VsProductLiftCycleService extends BaseBiKettleService {
	
	private final static String TASK_NAME = "vs_product_lift_cycle";
	private final static String KBJ_FILE_NAME = "vs_product_lift_cycle";
	
	public VsProductLiftCycleService() {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
	}

}

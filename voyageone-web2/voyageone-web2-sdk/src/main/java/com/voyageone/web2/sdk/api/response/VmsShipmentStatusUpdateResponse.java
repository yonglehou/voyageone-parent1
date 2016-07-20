package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiResponse;


/**
 * /vms/order/updateOrderStatus
 *
 * Created on 16/07/18.
 * @author jeff.duan
 * @version 1.0
 */
public class VmsShipmentStatusUpdateResponse extends VoApiResponse {

	// true/false
	private boolean result;

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}

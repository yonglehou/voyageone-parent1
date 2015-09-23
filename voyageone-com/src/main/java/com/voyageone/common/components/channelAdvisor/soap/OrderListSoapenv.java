package com.voyageone.common.components.channelAdvisor.soap;

import com.voyageone.common.components.channelAdvisor.webservices.*;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
		name = "",
		propOrder = {"header", "body"}
)
@XmlRootElement(
		name = "Envelope"
)
public class OrderListSoapenv {
	@XmlElement(
			name = "Header"
	)
	private OrderListHeader header;
	@XmlElement(
			name = "Body"
	)
	private OrderListBody body;

	public OrderListSoapenv() {
	}

	public OrderListSoapenv(APICredentials api, GetOrderList getOrderList) {
		this.body = new OrderListBody();
		this.body.setSubmitParam(getOrderList);
		this.header = new OrderListHeader();
		this.header.setaPICredentials(api);
	}

	public OrderListHeader getHeader() {
		return this.header;
	}

	public void setHeader(OrderListHeader header) {
		this.header = header;
	}

	public OrderListBody getBody() {
		return this.body;
	}

	public void setBody(OrderListBody body) {
		this.body = body;
	}
}

package com.voyageone.common.components.channelAdvisor.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetOrderListResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfArrayOfOrderResponseItem" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getOrderListResult" })
@XmlRootElement(name = "GetOrderListResponse")
public class GetOrderListResponse {

	@XmlElement(name = "GetOrderListResult")
	protected APIResultOfArrayOfOrderResponseItem getOrderListResult;

	/**
	 * Gets the value of the getOrderListResult property.
	 * 
	 * @return possible object is {@link APIResultOfArrayOfOrderResponseItem }
	 * 
	 */
	public APIResultOfArrayOfOrderResponseItem getGetOrderListResult() {
		return getOrderListResult;
	}

	/**
	 * Sets the value of the getOrderListResult property.
	 * 
	 * @param value
	 *            allowed object is {@link APIResultOfArrayOfOrderResponseItem }
	 * 
	 */
	public void setGetOrderListResult(APIResultOfArrayOfOrderResponseItem value) {
		this.getOrderListResult = value;
	}

}

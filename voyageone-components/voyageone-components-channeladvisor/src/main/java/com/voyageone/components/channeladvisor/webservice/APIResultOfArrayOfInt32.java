package com.voyageone.components.channeladvisor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for APIResultOfArrayOfInt32 complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="APIResultOfArrayOfInt32">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Status" type="{http://api.channeladvisor.com/webservices/}ResultStatus"/>
 *         &lt;element name="MessageCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResultData" type="{http://api.channeladvisor.com/webservices/}ArrayOfInt" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "APIResultOfArrayOfInt32", propOrder = { "status", "messageCode", "message", "data", "resultData" })
public class APIResultOfArrayOfInt32 {

	@XmlElement(name = "Status", required = true)
	protected com.voyageone.components.channeladvisor.webservice.ResultStatus status;
	@XmlElement(name = "MessageCode")
	protected int messageCode;
	@XmlElement(name = "Message")
	protected String message;
	@XmlElement(name = "Data")
	protected String data;
	@XmlElement(name = "ResultData")
	protected com.voyageone.components.channeladvisor.webservice.ArrayOfInt resultData;

	/**
	 * Gets the value of the status property.
	 *
	 * @return possible object is {@link com.voyageone.components.channeladvisor.webservice.ResultStatus }
	 *
	 */
	public com.voyageone.components.channeladvisor.webservice.ResultStatus getStatus() {
		return status;
	}

	/**
	 * Sets the value of the status property.
	 *
	 * @param value
	 *            allowed object is {@link com.voyageone.components.channeladvisor.webservice.ResultStatus }
	 *
	 */
	public void setStatus(com.voyageone.components.channeladvisor.webservice.ResultStatus value) {
		this.status = value;
	}

	/**
	 * Gets the value of the messageCode property.
	 *
	 */
	public int getMessageCode() {
		return messageCode;
	}

	/**
	 * Sets the value of the messageCode property.
	 *
	 */
	public void setMessageCode(int value) {
		this.messageCode = value;
	}

	/**
	 * Gets the value of the message property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the value of the message property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setMessage(String value) {
		this.message = value;
	}

	/**
	 * Gets the value of the data property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getData() {
		return data;
	}

	/**
	 * Sets the value of the data property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setData(String value) {
		this.data = value;
	}

	/**
	 * Gets the value of the resultData property.
	 *
	 * @return possible object is {@link com.voyageone.components.channeladvisor.webservice.ArrayOfInt }
	 *
	 */
	public com.voyageone.components.channeladvisor.webservice.ArrayOfInt getResultData() {
		return resultData;
	}

	/**
	 * Sets the value of the resultData property.
	 *
	 * @param value
	 *            allowed object is {@link com.voyageone.components.channeladvisor.webservice.ArrayOfInt }
	 * 
	 */
	public void setResultData(com.voyageone.components.channeladvisor.webservice.ArrayOfInt value) {
		this.resultData = value;
	}

}

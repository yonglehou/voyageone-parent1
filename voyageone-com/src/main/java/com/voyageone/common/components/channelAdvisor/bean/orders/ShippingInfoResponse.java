package com.voyageone.common.components.channelAdvisor.bean.orders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ShippingInfoResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ShippingInfoResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://api.channeladvisor.com/datacontracts/orders}ShippingInfo">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShippingInfoResponse")
public class ShippingInfoResponse extends ShippingInfo {

}

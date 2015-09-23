package com.voyageone.common.components.channelAdvisor.bean.orders;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ArrayOfOrderLineItemInvoice complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfOrderLineItemInvoice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrderLineItemInvoice" type="{http://api.channeladvisor.com/datacontracts/orders}OrderLineItemInvoice" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfOrderLineItemInvoice", propOrder = { "orderLineItemInvoice" })
public class ArrayOfOrderLineItemInvoice {

	@XmlElement(name = "OrderLineItemInvoice", nillable = true)
	protected List<OrderLineItemInvoice> orderLineItemInvoice;

	/**
	 * Gets the value of the orderLineItemInvoice property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the orderLineItemInvoice property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getOrderLineItemInvoice().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OrderLineItemInvoice }
	 * 
	 * 
	 */
	public List<OrderLineItemInvoice> getOrderLineItemInvoice() {
		if (orderLineItemInvoice == null) {
			orderLineItemInvoice = new ArrayList<OrderLineItemInvoice>();
		}
		return this.orderLineItemInvoice;
	}

}

/**
 * ShoppingCartPaymentEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  ShoppingCartPaymentEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class ShoppingCartPaymentEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = shoppingCartPaymentEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Payment_id
     */
    protected String localPayment_id;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPayment_idTracker = false;

    /**
     * field for Created_at
     */
    protected String localCreated_at;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCreated_atTracker = false;

    /**
     * field for Updated_at
     */
    protected String localUpdated_at;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localUpdated_atTracker = false;

    /**
     * field for Method
     */
    protected String localMethod;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localMethodTracker = false;

    /**
     * field for Cc_type
     */
    protected String localCc_type;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_typeTracker = false;

    /**
     * field for Cc_number_enc
     */
    protected String localCc_number_enc;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_number_encTracker = false;

    /**
     * field for Cc_last4
     */
    protected String localCc_last4;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_last4Tracker = false;

    /**
     * field for Cc_cid_enc
     */
    protected String localCc_cid_enc;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_cid_encTracker = false;

    /**
     * field for Cc_owner
     */
    protected String localCc_owner;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_ownerTracker = false;

    /**
     * field for Cc_exp_month
     */
    protected String localCc_exp_month;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_exp_monthTracker = false;

    /**
     * field for Cc_exp_year
     */
    protected String localCc_exp_year;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_exp_yearTracker = false;

    /**
     * field for Cc_ss_owner
     */
    protected String localCc_ss_owner;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_ss_ownerTracker = false;

    /**
     * field for Cc_ss_start_month
     */
    protected String localCc_ss_start_month;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_ss_start_monthTracker = false;

    /**
     * field for Cc_ss_start_year
     */
    protected String localCc_ss_start_year;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_ss_start_yearTracker = false;

    /**
     * field for Cc_ss_issue
     */
    protected String localCc_ss_issue;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCc_ss_issueTracker = false;

    /**
     * field for Po_number
     */
    protected String localPo_number;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPo_numberTracker = false;

    /**
     * field for Additional_data
     */
    protected String localAdditional_data;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localAdditional_dataTracker = false;

    /**
     * field for Additional_information
     */
    protected String localAdditional_information;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localAdditional_informationTracker = false;

    public boolean isPayment_idSpecified() {
        return localPayment_idTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getPayment_id() {
        return localPayment_id;
    }

    /**
     * Auto generated setter method
     * @param param Payment_id
     */
    public void setPayment_id(String param) {
        localPayment_idTracker = param != null;

        this.localPayment_id = param;
    }

    public boolean isCreated_atSpecified() {
        return localCreated_atTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCreated_at() {
        return localCreated_at;
    }

    /**
     * Auto generated setter method
     * @param param Created_at
     */
    public void setCreated_at(String param) {
        localCreated_atTracker = param != null;

        this.localCreated_at = param;
    }

    public boolean isUpdated_atSpecified() {
        return localUpdated_atTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getUpdated_at() {
        return localUpdated_at;
    }

    /**
     * Auto generated setter method
     * @param param Updated_at
     */
    public void setUpdated_at(String param) {
        localUpdated_atTracker = param != null;

        this.localUpdated_at = param;
    }

    public boolean isMethodSpecified() {
        return localMethodTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getMethod() {
        return localMethod;
    }

    /**
     * Auto generated setter method
     * @param param Method
     */
    public void setMethod(String param) {
        localMethodTracker = param != null;

        this.localMethod = param;
    }

    public boolean isCc_typeSpecified() {
        return localCc_typeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_type() {
        return localCc_type;
    }

    /**
     * Auto generated setter method
     * @param param Cc_type
     */
    public void setCc_type(String param) {
        localCc_typeTracker = param != null;

        this.localCc_type = param;
    }

    public boolean isCc_number_encSpecified() {
        return localCc_number_encTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_number_enc() {
        return localCc_number_enc;
    }

    /**
     * Auto generated setter method
     * @param param Cc_number_enc
     */
    public void setCc_number_enc(String param) {
        localCc_number_encTracker = param != null;

        this.localCc_number_enc = param;
    }

    public boolean isCc_last4Specified() {
        return localCc_last4Tracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_last4() {
        return localCc_last4;
    }

    /**
     * Auto generated setter method
     * @param param Cc_last4
     */
    public void setCc_last4(String param) {
        localCc_last4Tracker = param != null;

        this.localCc_last4 = param;
    }

    public boolean isCc_cid_encSpecified() {
        return localCc_cid_encTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_cid_enc() {
        return localCc_cid_enc;
    }

    /**
     * Auto generated setter method
     * @param param Cc_cid_enc
     */
    public void setCc_cid_enc(String param) {
        localCc_cid_encTracker = param != null;

        this.localCc_cid_enc = param;
    }

    public boolean isCc_ownerSpecified() {
        return localCc_ownerTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_owner() {
        return localCc_owner;
    }

    /**
     * Auto generated setter method
     * @param param Cc_owner
     */
    public void setCc_owner(String param) {
        localCc_ownerTracker = param != null;

        this.localCc_owner = param;
    }

    public boolean isCc_exp_monthSpecified() {
        return localCc_exp_monthTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_exp_month() {
        return localCc_exp_month;
    }

    /**
     * Auto generated setter method
     * @param param Cc_exp_month
     */
    public void setCc_exp_month(String param) {
        localCc_exp_monthTracker = param != null;

        this.localCc_exp_month = param;
    }

    public boolean isCc_exp_yearSpecified() {
        return localCc_exp_yearTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_exp_year() {
        return localCc_exp_year;
    }

    /**
     * Auto generated setter method
     * @param param Cc_exp_year
     */
    public void setCc_exp_year(String param) {
        localCc_exp_yearTracker = param != null;

        this.localCc_exp_year = param;
    }

    public boolean isCc_ss_ownerSpecified() {
        return localCc_ss_ownerTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_ss_owner() {
        return localCc_ss_owner;
    }

    /**
     * Auto generated setter method
     * @param param Cc_ss_owner
     */
    public void setCc_ss_owner(String param) {
        localCc_ss_ownerTracker = param != null;

        this.localCc_ss_owner = param;
    }

    public boolean isCc_ss_start_monthSpecified() {
        return localCc_ss_start_monthTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_ss_start_month() {
        return localCc_ss_start_month;
    }

    /**
     * Auto generated setter method
     * @param param Cc_ss_start_month
     */
    public void setCc_ss_start_month(String param) {
        localCc_ss_start_monthTracker = param != null;

        this.localCc_ss_start_month = param;
    }

    public boolean isCc_ss_start_yearSpecified() {
        return localCc_ss_start_yearTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_ss_start_year() {
        return localCc_ss_start_year;
    }

    /**
     * Auto generated setter method
     * @param param Cc_ss_start_year
     */
    public void setCc_ss_start_year(String param) {
        localCc_ss_start_yearTracker = param != null;

        this.localCc_ss_start_year = param;
    }

    public boolean isCc_ss_issueSpecified() {
        return localCc_ss_issueTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getCc_ss_issue() {
        return localCc_ss_issue;
    }

    /**
     * Auto generated setter method
     * @param param Cc_ss_issue
     */
    public void setCc_ss_issue(String param) {
        localCc_ss_issueTracker = param != null;

        this.localCc_ss_issue = param;
    }

    public boolean isPo_numberSpecified() {
        return localPo_numberTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getPo_number() {
        return localPo_number;
    }

    /**
     * Auto generated setter method
     * @param param Po_number
     */
    public void setPo_number(String param) {
        localPo_numberTracker = param != null;

        this.localPo_number = param;
    }

    public boolean isAdditional_dataSpecified() {
        return localAdditional_dataTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getAdditional_data() {
        return localAdditional_data;
    }

    /**
     * Auto generated setter method
     * @param param Additional_data
     */
    public void setAdditional_data(String param) {
        localAdditional_dataTracker = param != null;

        this.localAdditional_data = param;
    }

    public boolean isAdditional_informationSpecified() {
        return localAdditional_informationTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public String getAdditional_information() {
        return localAdditional_information;
    }

    /**
     * Auto generated setter method
     * @param param Additional_information
     */
    public void setAdditional_information(String param) {
        localAdditional_informationTracker = param != null;

        this.localAdditional_information = param;
    }

    /**
     *
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(
        final javax.xml.namespace.QName parentQName,
        final org.apache.axiom.om.OMFactory factory)
        throws org.apache.axis2.databinding.ADBException {
        org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this,
                parentQName);

        return factory.createOMElement(dataSource, parentQName);
    }

    public void serialize(final javax.xml.namespace.QName parentQName,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException {
        serialize(parentQName, xmlWriter, false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName,
        javax.xml.stream.XMLStreamWriter xmlWriter, boolean serializeType)
        throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException {
        String prefix = null;
        String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();
        writeStartElement(prefix, namespace, parentQName.getLocalPart(),
            xmlWriter);

        if (serializeType) {
            String namespacePrefix = registerPrefix(xmlWriter,
                    "urn:Magento");

            if ((namespacePrefix != null) &&
                    (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    namespacePrefix + ":shoppingCartPaymentEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "shoppingCartPaymentEntity", xmlWriter);
            }
        }

        if (localPayment_idTracker) {
            namespace = "";
            writeStartElement(null, namespace, "payment_id", xmlWriter);

            if (localPayment_id == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "payment_id cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPayment_id);
            }

            xmlWriter.writeEndElement();
        }

        if (localCreated_atTracker) {
            namespace = "";
            writeStartElement(null, namespace, "created_at", xmlWriter);

            if (localCreated_at == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "created_at cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCreated_at);
            }

            xmlWriter.writeEndElement();
        }

        if (localUpdated_atTracker) {
            namespace = "";
            writeStartElement(null, namespace, "updated_at", xmlWriter);

            if (localUpdated_at == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "updated_at cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localUpdated_at);
            }

            xmlWriter.writeEndElement();
        }

        if (localMethodTracker) {
            namespace = "";
            writeStartElement(null, namespace, "method", xmlWriter);

            if (localMethod == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "method cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localMethod);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_typeTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_type", xmlWriter);

            if (localCc_type == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_type cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_type);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_number_encTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_number_enc", xmlWriter);

            if (localCc_number_enc == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_number_enc cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_number_enc);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_last4Tracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_last4", xmlWriter);

            if (localCc_last4 == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_last4 cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_last4);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_cid_encTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_cid_enc", xmlWriter);

            if (localCc_cid_enc == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_cid_enc cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_cid_enc);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_ownerTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_owner", xmlWriter);

            if (localCc_owner == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_owner cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_owner);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_exp_monthTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_exp_month", xmlWriter);

            if (localCc_exp_month == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_exp_month cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_exp_month);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_exp_yearTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_exp_year", xmlWriter);

            if (localCc_exp_year == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_exp_year cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_exp_year);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_ss_ownerTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_ss_owner", xmlWriter);

            if (localCc_ss_owner == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_ss_owner cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_ss_owner);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_ss_start_monthTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_ss_start_month", xmlWriter);

            if (localCc_ss_start_month == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_ss_start_month cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_ss_start_month);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_ss_start_yearTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_ss_start_year", xmlWriter);

            if (localCc_ss_start_year == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_ss_start_year cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_ss_start_year);
            }

            xmlWriter.writeEndElement();
        }

        if (localCc_ss_issueTracker) {
            namespace = "";
            writeStartElement(null, namespace, "cc_ss_issue", xmlWriter);

            if (localCc_ss_issue == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_ss_issue cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localCc_ss_issue);
            }

            xmlWriter.writeEndElement();
        }

        if (localPo_numberTracker) {
            namespace = "";
            writeStartElement(null, namespace, "po_number", xmlWriter);

            if (localPo_number == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "po_number cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localPo_number);
            }

            xmlWriter.writeEndElement();
        }

        if (localAdditional_dataTracker) {
            namespace = "";
            writeStartElement(null, namespace, "additional_data", xmlWriter);

            if (localAdditional_data == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "additional_data cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localAdditional_data);
            }

            xmlWriter.writeEndElement();
        }

        if (localAdditional_informationTracker) {
            namespace = "";
            writeStartElement(null, namespace, "additional_information",
                xmlWriter);

            if (localAdditional_information == null) {
                // write the nil attribute
                throw new org.apache.axis2.databinding.ADBException(
                    "additional_information cannot be null!!");
            } else {
                xmlWriter.writeCharacters(localAdditional_information);
            }

            xmlWriter.writeEndElement();
        }

        xmlWriter.writeEndElement();
    }

    private static String generatePrefix(String namespace) {
        if (namespace.equals("urn:Magento")) {
            return "ns1";
        }

        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Utility method to write an element start tag.
     */
    private void writeStartElement(String prefix,
        String namespace, String localPart,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        String writerPrefix = xmlWriter.getPrefix(namespace);

        if (writerPrefix != null) {
            xmlWriter.writeStartElement(namespace, localPart);
        } else {
            if (namespace.length() == 0) {
                prefix = "";
            } else if (prefix == null) {
                prefix = generatePrefix(namespace);
            }

            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
    }

    /**
     * Util method to write an attribute with the ns prefix
     */
    private void writeAttribute(String prefix,
        String namespace, String attName,
        String attValue, javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        if (xmlWriter.getPrefix(namespace) == null) {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        xmlWriter.writeAttribute(namespace, attName, attValue);
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeAttribute(String namespace,
        String attName, String attValue,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attValue);
        }
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeQNameAttribute(String namespace,
        String attName, javax.xml.namespace.QName qname,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        String attributeNamespace = qname.getNamespaceURI();
        String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

        if (attributePrefix == null) {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
        }

        String attributeValue;

        if (attributePrefix.trim().length() > 0) {
            attributeValue = attributePrefix + ":" + qname.getLocalPart();
        } else {
            attributeValue = qname.getLocalPart();
        }

        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attributeValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attributeValue);
        }
    }

    /**
     *  method to handle Qnames
     */
    private void writeQName(javax.xml.namespace.QName qname,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        String namespaceURI = qname.getNamespaceURI();

        if (namespaceURI != null) {
            String prefix = xmlWriter.getPrefix(namespaceURI);

            if (prefix == null) {
                prefix = generatePrefix(namespaceURI);
                xmlWriter.writeNamespace(prefix, namespaceURI);
                xmlWriter.setPrefix(prefix, namespaceURI);
            }

            if (prefix.trim().length() > 0) {
                xmlWriter.writeCharacters(prefix + ":" +
                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        qname));
            } else {
                // i.e this is the default namespace
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        qname));
            }
        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    qname));
        }
    }

    private void writeQNames(javax.xml.namespace.QName[] qnames,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        if (qnames != null) {
            // we have to store this data until last moment since it is not possible to write any
            // namespace data after writing the charactor data
            StringBuffer stringToWrite = new StringBuffer();
            String namespaceURI = null;
            String prefix = null;

            for (int i = 0; i < qnames.length; i++) {
                if (i > 0) {
                    stringToWrite.append(" ");
                }

                namespaceURI = qnames[i].getNamespaceURI();

                if (namespaceURI != null) {
                    prefix = xmlWriter.getPrefix(namespaceURI);

                    if ((prefix == null) || (prefix.length() == 0)) {
                        prefix = generatePrefix(namespaceURI);
                        xmlWriter.writeNamespace(prefix, namespaceURI);
                        xmlWriter.setPrefix(prefix, namespaceURI);
                    }

                    if (prefix.trim().length() > 0) {
                        stringToWrite.append(prefix).append(":")
                                     .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                qnames[i]));
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                qnames[i]));
                    }
                } else {
                    stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            qnames[i]));
                }
            }

            xmlWriter.writeCharacters(stringToWrite.toString());
        }
    }

    /**
     * Register a namespace prefix
     */
    private String registerPrefix(
        javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
        throws javax.xml.stream.XMLStreamException {
        String prefix = xmlWriter.getPrefix(namespace);

        if (prefix == null) {
            prefix = generatePrefix(namespace);

            javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

            while (true) {
                String uri = nsContext.getNamespaceURI(prefix);

                if ((uri == null) || (uri.length() == 0)) {
                    break;
                }

                prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
            }

            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        return prefix;
    }

    /**
     * databinding method to get an XML representation of this object
     *
     */
    public javax.xml.stream.XMLStreamReader getPullParser(
        javax.xml.namespace.QName qName)
        throws org.apache.axis2.databinding.ADBException {
        java.util.ArrayList elementList = new java.util.ArrayList();
        java.util.ArrayList attribList = new java.util.ArrayList();

        if (localPayment_idTracker) {
            elementList.add(new javax.xml.namespace.QName("", "payment_id"));

            if (localPayment_id != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPayment_id));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "payment_id cannot be null!!");
            }
        }

        if (localCreated_atTracker) {
            elementList.add(new javax.xml.namespace.QName("", "created_at"));

            if (localCreated_at != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCreated_at));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "created_at cannot be null!!");
            }
        }

        if (localUpdated_atTracker) {
            elementList.add(new javax.xml.namespace.QName("", "updated_at"));

            if (localUpdated_at != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localUpdated_at));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "updated_at cannot be null!!");
            }
        }

        if (localMethodTracker) {
            elementList.add(new javax.xml.namespace.QName("", "method"));

            if (localMethod != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localMethod));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "method cannot be null!!");
            }
        }

        if (localCc_typeTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_type"));

            if (localCc_type != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_type));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_type cannot be null!!");
            }
        }

        if (localCc_number_encTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_number_enc"));

            if (localCc_number_enc != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_number_enc));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_number_enc cannot be null!!");
            }
        }

        if (localCc_last4Tracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_last4"));

            if (localCc_last4 != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_last4));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_last4 cannot be null!!");
            }
        }

        if (localCc_cid_encTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_cid_enc"));

            if (localCc_cid_enc != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_cid_enc));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_cid_enc cannot be null!!");
            }
        }

        if (localCc_ownerTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_owner"));

            if (localCc_owner != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_owner));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_owner cannot be null!!");
            }
        }

        if (localCc_exp_monthTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_exp_month"));

            if (localCc_exp_month != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_exp_month));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_exp_month cannot be null!!");
            }
        }

        if (localCc_exp_yearTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_exp_year"));

            if (localCc_exp_year != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_exp_year));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_exp_year cannot be null!!");
            }
        }

        if (localCc_ss_ownerTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_ss_owner"));

            if (localCc_ss_owner != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_ss_owner));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_ss_owner cannot be null!!");
            }
        }

        if (localCc_ss_start_monthTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "cc_ss_start_month"));

            if (localCc_ss_start_month != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_ss_start_month));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_ss_start_month cannot be null!!");
            }
        }

        if (localCc_ss_start_yearTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_ss_start_year"));

            if (localCc_ss_start_year != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_ss_start_year));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_ss_start_year cannot be null!!");
            }
        }

        if (localCc_ss_issueTracker) {
            elementList.add(new javax.xml.namespace.QName("", "cc_ss_issue"));

            if (localCc_ss_issue != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localCc_ss_issue));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "cc_ss_issue cannot be null!!");
            }
        }

        if (localPo_numberTracker) {
            elementList.add(new javax.xml.namespace.QName("", "po_number"));

            if (localPo_number != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPo_number));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "po_number cannot be null!!");
            }
        }

        if (localAdditional_dataTracker) {
            elementList.add(new javax.xml.namespace.QName("", "additional_data"));

            if (localAdditional_data != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localAdditional_data));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "additional_data cannot be null!!");
            }
        }

        if (localAdditional_informationTracker) {
            elementList.add(new javax.xml.namespace.QName("",
                    "additional_information"));

            if (localAdditional_information != null) {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localAdditional_information));
            } else {
                throw new org.apache.axis2.databinding.ADBException(
                    "additional_information cannot be null!!");
            }
        }

        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName,
            elementList.toArray(), attribList.toArray());
    }

    /**
     *  Factory class that keeps the parse method
     */
    public static class Factory {
        /**
         * static method to create the object
         * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
         *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
         * Postcondition: If this object is an element, the reader is positioned at its end element
         *                If this object is a complex type, the reader is positioned at the end element of its outer element
         */
        public static ShoppingCartPaymentEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws Exception {
            ShoppingCartPaymentEntity object = new ShoppingCartPaymentEntity();

            int event;
            String nillableValue = null;
            String prefix = "";
            String namespaceuri = "";

            try {
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.getAttributeValue(
                            "http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                    String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "type");

                    if (fullTypeName != null) {
                        String nsPrefix = null;

                        if (fullTypeName.indexOf(":") > -1) {
                            nsPrefix = fullTypeName.substring(0,
                                    fullTypeName.indexOf(":"));
                        }

                        nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                        String type = fullTypeName.substring(fullTypeName.indexOf(
                                    ":") + 1);

                        if (!"shoppingCartPaymentEntity".equals(type)) {
                            //find namespace for the prefix
                            String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (ShoppingCartPaymentEntity) magento.ExtensionMapper.getTypeObject(nsUri,
                                type, reader);
                        }
                    }
                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "payment_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "payment_id" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setPayment_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "created_at").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "created_at" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCreated_at(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "updated_at").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "updated_at" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setUpdated_at(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "method").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "method" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setMethod(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_type").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_type" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_type(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_number_enc").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_number_enc" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_number_enc(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_last4").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_last4" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_last4(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_cid_enc").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_cid_enc" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_cid_enc(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_owner").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_owner" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_owner(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_exp_month").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_exp_month" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_exp_month(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_exp_year").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_exp_year" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_exp_year(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_ss_owner").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_ss_owner" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_ss_owner(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_ss_start_month").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_ss_start_month" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_ss_start_month(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_ss_start_year").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_ss_start_year" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_ss_start_year(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "cc_ss_issue").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "cc_ss_issue" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setCc_ss_issue(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "po_number").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "po_number" + "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setPo_number(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "additional_data").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "additional_data" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setAdditional_data(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("",
                            "additional_information").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "additional_information" +
                            "  cannot be null");
                    }

                    String content = reader.getElementText();

                    object.setAdditional_information(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()) {
                    // A start element we are not expecting indicates a trailing invalid property
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }
            } catch (javax.xml.stream.XMLStreamException e) {
                throw new Exception(e);
            }

            return object;
        }
    } //end of factory class
}

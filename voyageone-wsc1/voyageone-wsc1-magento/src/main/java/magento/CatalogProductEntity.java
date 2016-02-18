/**
 * CatalogProductEntity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:18:31 BST)
 */
package magento;


/**
 *  CatalogProductEntity bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class CatalogProductEntity implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = catalogProductEntity
       Namespace URI = urn:Magento
       Namespace Prefix = ns1
     */

    /**
     * field for Product_id
     */
    protected java.lang.String localProduct_id;

    /**
     * field for Sku
     */
    protected java.lang.String localSku;

    /**
     * field for Name
     */
    protected java.lang.String localName;

    /**
     * field for Set
     */
    protected java.lang.String localSet;

    /**
     * field for Type
     */
    protected java.lang.String localType;

    /**
     * field for Category_ids
     */
    protected magento.ArrayOfString localCategory_ids;

    /**
     * field for Website_ids
     */
    protected magento.ArrayOfString localWebsite_ids;

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getProduct_id() {
        return localProduct_id;
    }

    /**
     * Auto generated setter method
     * @param param Product_id
     */
    public void setProduct_id(java.lang.String param) {
        this.localProduct_id = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSku() {
        return localSku;
    }

    /**
     * Auto generated setter method
     * @param param Sku
     */
    public void setSku(java.lang.String param) {
        this.localSku = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getName() {
        return localName;
    }

    /**
     * Auto generated setter method
     * @param param Name
     */
    public void setName(java.lang.String param) {
        this.localName = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSet() {
        return localSet;
    }

    /**
     * Auto generated setter method
     * @param param Set
     */
    public void setSet(java.lang.String param) {
        this.localSet = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getType() {
        return localType;
    }

    /**
     * Auto generated setter method
     * @param param Type
     */
    public void setType(java.lang.String param) {
        this.localType = param;
    }

    /**
     * Auto generated getter method
     * @return magento.ArrayOfString
     */
    public magento.ArrayOfString getCategory_ids() {
        return localCategory_ids;
    }

    /**
     * Auto generated setter method
     * @param param Category_ids
     */
    public void setCategory_ids(magento.ArrayOfString param) {
        this.localCategory_ids = param;
    }

    /**
     * Auto generated getter method
     * @return magento.ArrayOfString
     */
    public magento.ArrayOfString getWebsite_ids() {
        return localWebsite_ids;
    }

    /**
     * Auto generated setter method
     * @param param Website_ids
     */
    public void setWebsite_ids(magento.ArrayOfString param) {
        this.localWebsite_ids = param;
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
        java.lang.String prefix = null;
        java.lang.String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();
        writeStartElement(prefix, namespace, parentQName.getLocalPart(),
            xmlWriter);

        if (serializeType) {
            java.lang.String namespacePrefix = registerPrefix(xmlWriter,
                    "urn:Magento");

            if ((namespacePrefix != null) &&
                    (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    namespacePrefix + ":catalogProductEntity", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "catalogProductEntity", xmlWriter);
            }
        }

        namespace = "";
        writeStartElement(null, namespace, "product_id", xmlWriter);

        if (localProduct_id == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "product_id cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localProduct_id);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "sku", xmlWriter);

        if (localSku == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "sku cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localSku);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "name", xmlWriter);

        if (localName == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "name cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localName);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "set", xmlWriter);

        if (localSet == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "set cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localSet);
        }

        xmlWriter.writeEndElement();

        namespace = "";
        writeStartElement(null, namespace, "type", xmlWriter);

        if (localType == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "type cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localType);
        }

        xmlWriter.writeEndElement();

        if (localCategory_ids == null) {
            throw new org.apache.axis2.databinding.ADBException(
                "category_ids cannot be null!!");
        }

        localCategory_ids.serialize(new javax.xml.namespace.QName("",
                "category_ids"), xmlWriter);

        if (localWebsite_ids == null) {
            throw new org.apache.axis2.databinding.ADBException(
                "website_ids cannot be null!!");
        }

        localWebsite_ids.serialize(new javax.xml.namespace.QName("",
                "website_ids"), xmlWriter);

        xmlWriter.writeEndElement();
    }

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("urn:Magento")) {
            return "ns1";
        }

        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Utility method to write an element start tag.
     */
    private void writeStartElement(java.lang.String prefix,
        java.lang.String namespace, java.lang.String localPart,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);

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
    private void writeAttribute(java.lang.String prefix,
        java.lang.String namespace, java.lang.String attName,
        java.lang.String attValue, javax.xml.stream.XMLStreamWriter xmlWriter)
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
    private void writeAttribute(java.lang.String namespace,
        java.lang.String attName, java.lang.String attValue,
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
    private void writeQNameAttribute(java.lang.String namespace,
        java.lang.String attName, javax.xml.namespace.QName qname,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String attributeNamespace = qname.getNamespaceURI();
        java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

        if (attributePrefix == null) {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
        }

        java.lang.String attributeValue;

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
        java.lang.String namespaceURI = qname.getNamespaceURI();

        if (namespaceURI != null) {
            java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);

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
            java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
            java.lang.String namespaceURI = null;
            java.lang.String prefix = null;

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
    private java.lang.String registerPrefix(
        javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String prefix = xmlWriter.getPrefix(namespace);

        if (prefix == null) {
            prefix = generatePrefix(namespace);

            javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

            while (true) {
                java.lang.String uri = nsContext.getNamespaceURI(prefix);

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

        elementList.add(new javax.xml.namespace.QName("", "product_id"));

        if (localProduct_id != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localProduct_id));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "product_id cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "sku"));

        if (localSku != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localSku));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "sku cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "name"));

        if (localName != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localName));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "name cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "set"));

        if (localSet != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localSet));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "set cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "type"));

        if (localType != null) {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localType));
        } else {
            throw new org.apache.axis2.databinding.ADBException(
                "type cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "category_ids"));

        if (localCategory_ids == null) {
            throw new org.apache.axis2.databinding.ADBException(
                "category_ids cannot be null!!");
        }

        elementList.add(localCategory_ids);

        elementList.add(new javax.xml.namespace.QName("", "website_ids"));

        if (localWebsite_ids == null) {
            throw new org.apache.axis2.databinding.ADBException(
                "website_ids cannot be null!!");
        }

        elementList.add(localWebsite_ids);

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
        public static CatalogProductEntity parse(
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            CatalogProductEntity object = new CatalogProductEntity();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";

            try {
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.getAttributeValue(
                            "http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                    java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "type");

                    if (fullTypeName != null) {
                        java.lang.String nsPrefix = null;

                        if (fullTypeName.indexOf(":") > -1) {
                            nsPrefix = fullTypeName.substring(0,
                                    fullTypeName.indexOf(":"));
                        }

                        nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                        java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(
                                    ":") + 1);

                        if (!"catalogProductEntity".equals(type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (CatalogProductEntity) magento.ExtensionMapper.getTypeObject(nsUri,
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
                        new javax.xml.namespace.QName("", "product_id").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "product_id" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setProduct_id(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "sku").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "sku" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSku(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "name").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "name" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "set").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "set" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setSet(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "type").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "type" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "category_ids").equals(
                            reader.getName())) {
                    object.setCategory_ids(magento.ArrayOfString.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName("", "website_ids").equals(
                            reader.getName())) {
                    object.setWebsite_ids(magento.ArrayOfString.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()) {
                    // A start element we are not expecting indicates a trailing invalid property
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }
            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }
    } //end of factory class
}
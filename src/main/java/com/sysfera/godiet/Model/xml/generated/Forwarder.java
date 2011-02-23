//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.23 at 03:33:47 PM CET 
//


package com.sysfera.godiet.Model.xml.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * A Diet Forwarder is a way to encapsulate all
 * 				communication between two domains. You need to specify on which node
 * 				(could be a gateway, fronted) you want to instantiate.
 * 			
 * 
 * <p>Java class for forwarder complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="forwarder">
 *   &lt;complexContent>
 *     &lt;extension base="{}agent">
 *       &lt;attribute name="client" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="server" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "forwarder")
public class Forwarder
    extends Agent
{

    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Node client;
    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Node server;

    /**
     * Gets the value of the client property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Node getClient() {
        return client;
    }

    /**
     * Sets the value of the client property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setClient(Node value) {
        this.client = value;
    }

    /**
     * Gets the value of the server property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Node getServer() {
        return server;
    }

    /**
     * Sets the value of the server property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setServer(Node value) {
        this.server = value;
    }

}

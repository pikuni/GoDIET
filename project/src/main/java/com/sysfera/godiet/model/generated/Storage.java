//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.03.24 at 05:04:27 PM CET 
//


package com.sysfera.godiet.model.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Remote storage access. Typically to upload agent's
 * 				configuration files.
 * 
 * <p>Java class for storage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="storage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="scratch" type="{}scratch"/>
 *         &lt;element name="scp" type="{}ssh"/>
 *       &lt;/sequence>
 *       &lt;attribute name="label" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "storage", propOrder = {
    "scratch",
    "scp"
})
public class Storage {

    @XmlElement(required = true)
    protected Scratch scratch;
    @XmlElement(required = true)
    protected Ssh scp;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String label;

    /**
     * Gets the value of the scratch property.
     * 
     * @return
     *     possible object is
     *     {@link Scratch }
     *     
     */
    public Scratch getScratch() {
        return scratch;
    }

    /**
     * Sets the value of the scratch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Scratch }
     *     
     */
    public void setScratch(Scratch value) {
        this.scratch = value;
    }

    /**
     * Gets the value of the scp property.
     * 
     * @return
     *     possible object is
     *     {@link Ssh }
     *     
     */
    public Ssh getScp() {
        return scp;
    }

    /**
     * Sets the value of the scp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ssh }
     *     
     */
    public void setScp(Ssh value) {
        this.scp = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

}

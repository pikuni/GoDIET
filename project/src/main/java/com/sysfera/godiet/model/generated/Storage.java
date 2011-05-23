//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.23 at 06:20:43 PM CEST 
//


package com.sysfera.godiet.model.generated;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="scratch" type="{http://www.sysfera.com}scratch"/>
 *         &lt;element name="scp" type="{http://www.sysfera.com}ssh" maxOccurs="unbounded"/>
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
    protected List<Ssh> scp;
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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scp property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScp().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ssh }
     * 
     * 
     */
    public List<Ssh> getScp() {
        if (scp == null) {
            scp = new ArrayList<Ssh>();
        }
        return this.scp;
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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.28 at 05:19:40 PM CET 
//


package com.sysfera.godiet.Model.xml.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="goDietConfiguration" type="{}goDietConfiguration" minOccurs="0"/>
 *         &lt;element name="infrastructure" type="{}infrastructure"/>
 *         &lt;element name="dietServices" type="{}dietServices"/>
 *         &lt;element name="dietInfrastructure" type="{}dietInfrastructure"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "goDietConfiguration",
    "infrastructure",
    "dietServices",
    "dietInfrastructure"
})
@XmlRootElement(name = "dietDescription")
public class DietDescription {

    protected GoDietConfiguration goDietConfiguration;
    @XmlElement(required = true)
    protected Infrastructure infrastructure;
    @XmlElement(required = true)
    protected DietServices dietServices;
    @XmlElement(required = true)
    protected DietInfrastructure dietInfrastructure;

    /**
     * Gets the value of the goDietConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link GoDietConfiguration }
     *     
     */
    public GoDietConfiguration getGoDietConfiguration() {
        return goDietConfiguration;
    }

    /**
     * Sets the value of the goDietConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link GoDietConfiguration }
     *     
     */
    public void setGoDietConfiguration(GoDietConfiguration value) {
        this.goDietConfiguration = value;
    }

    /**
     * Gets the value of the infrastructure property.
     * 
     * @return
     *     possible object is
     *     {@link Infrastructure }
     *     
     */
    public Infrastructure getInfrastructure() {
        return infrastructure;
    }

    /**
     * Sets the value of the infrastructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Infrastructure }
     *     
     */
    public void setInfrastructure(Infrastructure value) {
        this.infrastructure = value;
    }

    /**
     * Gets the value of the dietServices property.
     * 
     * @return
     *     possible object is
     *     {@link DietServices }
     *     
     */
    public DietServices getDietServices() {
        return dietServices;
    }

    /**
     * Sets the value of the dietServices property.
     * 
     * @param value
     *     allowed object is
     *     {@link DietServices }
     *     
     */
    public void setDietServices(DietServices value) {
        this.dietServices = value;
    }

    /**
     * Gets the value of the dietInfrastructure property.
     * 
     * @return
     *     possible object is
     *     {@link DietInfrastructure }
     *     
     */
    public DietInfrastructure getDietInfrastructure() {
        return dietInfrastructure;
    }

    /**
     * Sets the value of the dietInfrastructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link DietInfrastructure }
     *     
     */
    public void setDietInfrastructure(DietInfrastructure value) {
        this.dietInfrastructure = value;
    }

}

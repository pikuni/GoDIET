//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.21 at 06:28:56 PM CET 
//


package com.sysfera.godiet.Model.xml.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for diet_services complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="diet_services">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="omni_names" type="{}omni_names" maxOccurs="unbounded"/>
 *         &lt;element name="log_central" type="{}log_central" minOccurs="0"/>
 *         &lt;element name="log_tool" type="{}log_tool" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="statistics" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "diet_services", propOrder = {
    "omniNames",
    "logCentral",
    "logTool"
})
public class DietServices {

    @XmlElement(name = "omni_names", required = true)
    protected List<OmniNames> omniNames;
    @XmlElement(name = "log_central")
    protected LogCentral logCentral;
    @XmlElement(name = "log_tool")
    protected LogTool logTool;
    @XmlAttribute
    protected Boolean statistics;

    /**
     * Gets the value of the omniNames property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the omniNames property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOmniNames().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OmniNames }
     * 
     * 
     */
    public List<OmniNames> getOmniNames() {
        if (omniNames == null) {
            omniNames = new ArrayList<OmniNames>();
        }
        return this.omniNames;
    }

    /**
     * Gets the value of the logCentral property.
     * 
     * @return
     *     possible object is
     *     {@link LogCentral }
     *     
     */
    public LogCentral getLogCentral() {
        return logCentral;
    }

    /**
     * Sets the value of the logCentral property.
     * 
     * @param value
     *     allowed object is
     *     {@link LogCentral }
     *     
     */
    public void setLogCentral(LogCentral value) {
        this.logCentral = value;
    }

    /**
     * Gets the value of the logTool property.
     * 
     * @return
     *     possible object is
     *     {@link LogTool }
     *     
     */
    public LogTool getLogTool() {
        return logTool;
    }

    /**
     * Sets the value of the logTool property.
     * 
     * @param value
     *     allowed object is
     *     {@link LogTool }
     *     
     */
    public void setLogTool(LogTool value) {
        this.logTool = value;
    }

    /**
     * Gets the value of the statistics property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isStatistics() {
        if (statistics == null) {
            return false;
        } else {
            return statistics;
        }
    }

    /**
     * Sets the value of the statistics property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStatistics(Boolean value) {
        this.statistics = value;
    }

}

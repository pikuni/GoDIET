//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.25 at 02:15:44 PM CET 
//


package com.sysfera.godiet.Model.xml.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for goDietConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="goDietConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="localscratch" type="{}scratch"/>
 *       &lt;/sequence>
 *       &lt;attribute name="debug" type="{http://www.w3.org/2001/XMLSchema}int" default="1" />
 *       &lt;attribute name="saveStdOut" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="saveStdErr" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="log" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="watcherPeriod" type="{http://www.w3.org/2001/XMLSchema}int" default="30000" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "goDietConfiguration", propOrder = {
    "localscratch"
})
public class GoDietConfiguration {

    @XmlElement(required = true)
    protected Scratch localscratch;
    @XmlAttribute
    protected Integer debug;
    @XmlAttribute
    protected Boolean saveStdOut;
    @XmlAttribute
    protected Boolean saveStdErr;
    @XmlAttribute
    protected Boolean log;
    @XmlAttribute
    protected Integer watcherPeriod;

    /**
     * Gets the value of the localscratch property.
     * 
     * @return
     *     possible object is
     *     {@link Scratch }
     *     
     */
    public Scratch getLocalscratch() {
        return localscratch;
    }

    /**
     * Sets the value of the localscratch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Scratch }
     *     
     */
    public void setLocalscratch(Scratch value) {
        this.localscratch = value;
    }

    /**
     * Gets the value of the debug property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getDebug() {
        if (debug == null) {
            return  1;
        } else {
            return debug;
        }
    }

    /**
     * Sets the value of the debug property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDebug(Integer value) {
        this.debug = value;
    }

    /**
     * Gets the value of the saveStdOut property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSaveStdOut() {
        if (saveStdOut == null) {
            return true;
        } else {
            return saveStdOut;
        }
    }

    /**
     * Sets the value of the saveStdOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSaveStdOut(Boolean value) {
        this.saveStdOut = value;
    }

    /**
     * Gets the value of the saveStdErr property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSaveStdErr() {
        if (saveStdErr == null) {
            return true;
        } else {
            return saveStdErr;
        }
    }

    /**
     * Sets the value of the saveStdErr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSaveStdErr(Boolean value) {
        this.saveStdErr = value;
    }

    /**
     * Gets the value of the log property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLog() {
        return log;
    }

    /**
     * Sets the value of the log property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLog(Boolean value) {
        this.log = value;
    }

    /**
     * Gets the value of the watcherPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getWatcherPeriod() {
        if (watcherPeriod == null) {
            return  30000;
        } else {
            return watcherPeriod;
        }
    }

    /**
     * Sets the value of the watcherPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWatcherPeriod(Integer value) {
        this.watcherPeriod = value;
    }

}
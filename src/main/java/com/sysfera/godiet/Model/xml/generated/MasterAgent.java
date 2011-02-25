//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.25 at 02:15:44 PM CET 
//


package com.sysfera.godiet.Model.xml.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * An MA receives computation requests from
 * 				clients.
 * 
 * <p>Java class for masterAgent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="masterAgent">
 *   &lt;complexContent>
 *     &lt;extension base="{}agent">
 *       &lt;sequence>
 *         &lt;element name="madag" type="{}maDag" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="localAgent" type="{}localAgent" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sed" type="{}sed" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "masterAgent", propOrder = {
    "madag",
    "localAgent",
    "sed"
})
public class MasterAgent
    extends Agent
{

    protected List<MaDag> madag;
    protected List<LocalAgent> localAgent;
    protected List<Sed> sed;

    /**
     * Gets the value of the madag property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the madag property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMadag().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MaDag }
     * 
     * 
     */
    public List<MaDag> getMadag() {
        if (madag == null) {
            madag = new ArrayList<MaDag>();
        }
        return this.madag;
    }

    /**
     * Gets the value of the localAgent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the localAgent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocalAgent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LocalAgent }
     * 
     * 
     */
    public List<LocalAgent> getLocalAgent() {
        if (localAgent == null) {
            localAgent = new ArrayList<LocalAgent>();
        }
        return this.localAgent;
    }

    /**
     * Gets the value of the sed property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sed property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSed().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Sed }
     * 
     * 
     */
    public List<Sed> getSed() {
        if (sed == null) {
            sed = new ArrayList<Sed>();
        }
        return this.sed;
    }

}
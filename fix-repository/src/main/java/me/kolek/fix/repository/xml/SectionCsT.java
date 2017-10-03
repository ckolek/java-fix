//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.25 at 07:33:34 PM EDT 
//


package me.kolek.fix.repository.xml;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Section_cs_t complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Section_cs_t">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SectionID" type="{}SectionID_t"/>
 *         &lt;element name="Name" type="{}Name_t"/>
 *         &lt;element name="DisplayOrder" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Volume" type="{}Volume_t"/>
 *         &lt;element name="NotReqXML" type="{}BOOL_t"/>
 *         &lt;element name="FIXMLFileName" type="{}Name_t" minOccurs="0"/>
 *         &lt;element name="Description" type="{}Description_t" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{}EntityLevelRevisionAttribGrp"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Section_cs_t", propOrder = {
    "sectionID",
    "name",
    "displayOrder",
    "volume",
    "notReqXML",
    "fixmlFileName",
    "description"
})
public class SectionCsT {

    @XmlElement(name = "SectionID", required = true)
    @XmlSchemaType(name = "string")
    protected SectionIDT sectionID;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "DisplayOrder")
    protected int displayOrder;
    @XmlElement(name = "Volume", required = true)
    protected String volume;
    @XmlElement(name = "NotReqXML")
    protected short notReqXML;
    @XmlElement(name = "FIXMLFileName")
    protected String fixmlFileName;
    @XmlElement(name = "Description")
    protected String description;
    @XmlAttribute(name = "added")
    protected String added;
    @XmlAttribute(name = "addedEP")
    protected BigInteger addedEP;
    @XmlAttribute(name = "deprecated")
    protected String deprecated;
    @XmlAttribute(name = "deprecatedEP")
    protected BigInteger deprecatedEP;
    @XmlAttribute(name = "issue")
    protected String issue;
    @XmlAttribute(name = "updated")
    protected String updated;
    @XmlAttribute(name = "updatedEP")
    protected BigInteger updatedEP;

    /**
     * Gets the value of the sectionID property.
     * 
     * @return
     *     possible object is
     *     {@link SectionIDT }
     *     
     */
    public SectionIDT getSectionID() {
        return sectionID;
    }

    /**
     * Sets the value of the sectionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link SectionIDT }
     *     
     */
    public void setSectionID(SectionIDT value) {
        this.sectionID = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the displayOrder property.
     * 
     */
    public int getDisplayOrder() {
        return displayOrder;
    }

    /**
     * Sets the value of the displayOrder property.
     * 
     */
    public void setDisplayOrder(int value) {
        this.displayOrder = value;
    }

    /**
     * Gets the value of the volume property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVolume() {
        return volume;
    }

    /**
     * Sets the value of the volume property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVolume(String value) {
        this.volume = value;
    }

    /**
     * Gets the value of the notReqXML property.
     * 
     */
    public short getNotReqXML() {
        return notReqXML;
    }

    /**
     * Sets the value of the notReqXML property.
     * 
     */
    public void setNotReqXML(short value) {
        this.notReqXML = value;
    }

    /**
     * Gets the value of the fixmlFileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFIXMLFileName() {
        return fixmlFileName;
    }

    /**
     * Sets the value of the fixmlFileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFIXMLFileName(String value) {
        this.fixmlFileName = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the added property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdded() {
        return added;
    }

    /**
     * Sets the value of the added property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdded(String value) {
        this.added = value;
    }

    /**
     * Gets the value of the addedEP property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAddedEP() {
        return addedEP;
    }

    /**
     * Sets the value of the addedEP property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAddedEP(BigInteger value) {
        this.addedEP = value;
    }

    /**
     * Gets the value of the deprecated property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeprecated() {
        return deprecated;
    }

    /**
     * Sets the value of the deprecated property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeprecated(String value) {
        this.deprecated = value;
    }

    /**
     * Gets the value of the deprecatedEP property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDeprecatedEP() {
        return deprecatedEP;
    }

    /**
     * Sets the value of the deprecatedEP property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDeprecatedEP(BigInteger value) {
        this.deprecatedEP = value;
    }

    /**
     * Gets the value of the issue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssue() {
        return issue;
    }

    /**
     * Sets the value of the issue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssue(String value) {
        this.issue = value;
    }

    /**
     * Gets the value of the updated property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdated() {
        return updated;
    }

    /**
     * Sets the value of the updated property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdated(String value) {
        this.updated = value;
    }

    /**
     * Gets the value of the updatedEP property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getUpdatedEP() {
        return updatedEP;
    }

    /**
     * Sets the value of the updatedEP property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setUpdatedEP(BigInteger value) {
        this.updatedEP = value;
    }

}

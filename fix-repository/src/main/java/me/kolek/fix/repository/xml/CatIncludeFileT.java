//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.25 at 07:33:34 PM EDT 
//


package me.kolek.fix.repository.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CatIncludeFile_t.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CatIncludeFile_t"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="components"/&gt;
 *     &lt;enumeration value="fields"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CatIncludeFile_t")
@XmlEnum
public enum CatIncludeFileT {

    @XmlEnumValue("components")
    COMPONENTS("components"),
    @XmlEnumValue("fields")
    FIELDS("fields");
    private final String value;

    CatIncludeFileT(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CatIncludeFileT fromValue(String v) {
        for (CatIncludeFileT c: CatIncludeFileT.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

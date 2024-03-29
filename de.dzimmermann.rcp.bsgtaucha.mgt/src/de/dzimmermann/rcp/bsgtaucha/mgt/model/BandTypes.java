//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.10.21 at 04:16:13 PM MESZ 
//

package de.dzimmermann.rcp.bsgtaucha.mgt.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for bandTypes.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="bandTypes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="continuous"/>
 *     &lt;enumeration value="lateral"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "bandTypes")
@XmlEnum
public enum BandTypes {

	//
	@XmlEnumValue("continuous")
	CONTINUOUS("continuous"),
	//
	@XmlEnumValue("lateral")
	LATERAL("lateral");
	private final String value;

	BandTypes(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static BandTypes fromValue(String v) {
		for (BandTypes c : BandTypes.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.10.21 at 04:16:13 PM MESZ 
//

package de.dzimmermann.rcp.bsgtaucha.mgt.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for workType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="workType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="affect-band" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "workType", propOrder = { "name", "description" })
public class WorkType {

	@XmlElement(required = true)
	@XmlJavaTypeAdapter(EncryptionDecryptionAdapter.class)
	protected String name;
	@XmlJavaTypeAdapter(EncryptionDecryptionAdapter.class)
	protected String description;

	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(EncryptionDecryptionAdapter.class)
	protected String id;
	@XmlAttribute(name = "affect-band")
	protected Boolean affectBand;

	@XmlAttribute(required = false)
	protected boolean removed;

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Gets the value of the description property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the value of the description property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDescription(String value) {
		this.description = value;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Gets the value of the affectBand property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isAffectBand() {
		return affectBand;
	}

	/**
	 * Sets the value of the affectBand property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setAffectBand(Boolean value) {
		this.affectBand = value;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public WorkType getClone() {
		WorkType wt = new WorkType();
		wt.affectBand = affectBand == null ? null : new Boolean(affectBand);
		wt.description = description == null ? null : new String(description);
		wt.id = id == null ? null : new String(id);
		wt.name = name == null ? null : new String(name);
		wt.removed = removed;
		return wt;
	}

	@Override
	public String toString() {
		return String.format("id=[%s]: affect-band=%b, name='%s', descr='%s'",
				id, (affectBand != null ? affectBand : false), name,
				(description != null ? description : ""));
	}
}

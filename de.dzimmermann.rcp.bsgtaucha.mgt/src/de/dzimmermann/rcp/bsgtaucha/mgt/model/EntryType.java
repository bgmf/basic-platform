//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.10.21 at 04:16:13 PM MESZ 
//

package de.dzimmermann.rcp.bsgtaucha.mgt.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for entryType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="entryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="band-action" type="{http://de.dezimmermann.rcp.bsgtaucha.mgt/bsgtaucha-model}bandActionType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="person-id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="work-id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="date" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="time" type="{http://www.w3.org/2001/XMLSchema}float" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "entryType", propOrder = { "comment", "bandAction" })
public class EntryType {

	@XmlJavaTypeAdapter(EncryptionDecryptionAdapter.class)
	protected String comment;
	@XmlElement(name = "band-action")
	protected BandActionType bandAction;

	@XmlAttribute(name = "person-id", required = true)
	@XmlJavaTypeAdapter(EncryptionDecryptionAdapter.class)
	protected String personId;
	@XmlAttribute(name = "work-id", required = true)
	@XmlJavaTypeAdapter(EncryptionDecryptionAdapter.class)
	protected String workId;
	@XmlAttribute
	@XmlSchemaType(name = "date")
	@XmlJavaTypeAdapter(EncryptionDateAdapter.class)
	protected Date date;
	@XmlAttribute(required = true)
	protected Float time;

	/**
	 * Gets the value of the comment property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the value of the comment property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setComment(String value) {
		this.comment = value;
	}

	/**
	 * Gets the value of the bandAction property.
	 * 
	 * @return possible object is {@link BandActionType }
	 * 
	 */
	public BandActionType getBandAction() {
		return bandAction;
	}

	/**
	 * Sets the value of the bandAction property.
	 * 
	 * @param value
	 *            allowed object is {@link BandActionType }
	 * 
	 */
	public void setBandAction(BandActionType value) {
		this.bandAction = value;
	}

	/**
	 * Gets the value of the personId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPersonId() {
		return personId;
	}

	/**
	 * Sets the value of the personId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPersonId(String value) {
		this.personId = value;
	}

	/**
	 * Gets the value of the workId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getWorkId() {
		return workId;
	}

	/**
	 * Sets the value of the workId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setWorkId(String value) {
		this.workId = value;
	}

	/**
	 * Gets the value of the date property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the value of the date property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDate(Date value) {
		this.date = value;
	}

	/**
	 * Gets the value of the time property.
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getTime() {
		return time;
	}

	/**
	 * Sets the value of the time property.
	 * 
	 * @param value
	 *            allowed object is {@link Float }
	 * 
	 */
	public void setTime(Float value) {
		this.time = value;
	}

	public EntryType getClone() {
		EntryType et = new EntryType();
		et.date = date != null ? new Date(date.getTime()) : null;
		et.personId = personId != null ? new String(personId) : null;
		et.workId = workId != null ? new String(workId) : null;
		et.time = time != null ? new Float(time) : null;
		et.comment = comment != null ? new String(comment) : null;
		et.bandAction = bandAction != null ? bandAction.getClone() : null;
		return et;
	}

	@Override
	public String toString() {
		return String
				.format("date='%s', person-id=[%s], work-id=[%s], time=%d, is-band-entry=%b",
						date.toString(), personId, workId, (time != null ? time
								: 0), (bandAction != null));
	}
}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.10.21 at 04:16:13 PM MESZ 
//

package de.dzimmermann.rcp.bsgtaucha.mgt.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for worksType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="worksType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="work" type="{http://de.dezimmermann.rcp.bsgtaucha.mgt/bsgtaucha-model}workType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="entry" type="{http://de.dezimmermann.rcp.bsgtaucha.mgt/bsgtaucha-model}entryType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "worksType", propOrder = { "work", "entry" })
public class WorksType {

	protected List<WorkType> work;
	protected List<EntryType> entry;

	/**
	 * Gets the value of the work property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the work property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getWork().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link WorkType }
	 * 
	 * 
	 */
	public List<WorkType> getWork() {
		if (work == null) {
			work = new ArrayList<WorkType>();
		}
		return this.work;
	}

	/**
	 * Gets the value of the entry property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the entry property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getEntry().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link EntryType }
	 * 
	 * 
	 */
	public List<EntryType> getEntry() {
		if (entry == null) {
			entry = new ArrayList<EntryType>();
		}
		return this.entry;
	}

	public WorksType getClone() {
		WorksType wt = new WorksType();
		if (work != null) {
			for (WorkType w : work) {
				wt.getWork().add(w.getClone());
			}
		}
		if (entry != null) {
			for (EntryType et : entry) {
				wt.getEntry().add(et.getClone());
			}
		}
		return wt;
	}

	@Override
	public String toString() {
		StringBuilder sbW = new StringBuilder();
		if (work != null && !work.isEmpty())
			for (WorkType wt : work)
				sbW.append("  ").append(wt.toString()).append("\n");
		StringBuilder sbE = new StringBuilder();
		if (entry != null && !entry.isEmpty())
			for (EntryType et : entry)
				sbW.append("  ").append(et.toString()).append("\n");
		return String.format(" Works%n -----%n%s%n%n Entries%n -------%n%s%n",
				(sbW.length() == 0 ? "no works available" : sbW.toString()),
				(sbE.length() == 0 ? "no entries available" : sbE.toString()));
	}
}

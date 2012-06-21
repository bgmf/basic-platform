package de.dzimmermann.rcp.pwm.model.content;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "groupContent", propOrder = { "description", "entries" })
@XmlRootElement(name = "group-content")
public class PWMGroup {

	@XmlAttribute(name = "id", required = true)
	protected String id;

	@XmlAttribute(name = "name", required = true)
	protected String name;

	@XmlElement(name = "description", required = false)
	protected String description;
	@XmlElement(name = "entry", required = false)
	protected List<PWMGroupEntry> entries;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<PWMGroupEntry> getEntries() {
		if (entries == null)
			entries = new ArrayList<PWMGroupEntry>();
		return entries;
	}

	private final static QName _QNAME = new QName(null, "group-content");

	@XmlElementDecl(name = "group-content")
	public JAXBElement<PWMGroup> createJAXBElement(PWMGroup value) {
		return new JAXBElement<PWMGroup>(_QNAME, PWMGroup.class, null, value);
	}
}

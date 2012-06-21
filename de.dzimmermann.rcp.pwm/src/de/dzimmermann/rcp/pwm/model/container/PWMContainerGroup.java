package de.dzimmermann.rcp.pwm.model.container;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "group", propOrder = { "content", "groups" })
public class PWMContainerGroup {

	@XmlAttribute(name = "id", required = true)
	protected String id;

	@XmlAttribute(name = "name", required = true)
	protected String name;

	@XmlElement(name = "content", required = false)
	protected PWMContainerGroupContent content;
	@XmlElement(name = "group", required = false)
	protected List<PWMContainerGroup> groups;

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

	public PWMContainerGroupContent getContent() {
		return content;
	}

	public void setContent(PWMContainerGroupContent content) {
		this.content = content;
	}

	public List<PWMContainerGroup> getGroups() {
		if (groups == null)
			groups = new ArrayList<PWMContainerGroup>();
		return groups;
	}
}

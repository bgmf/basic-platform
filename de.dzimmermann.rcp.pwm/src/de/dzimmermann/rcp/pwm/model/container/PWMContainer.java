package de.dzimmermann.rcp.pwm.model.container;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pwm", propOrder = { "groups" })
@XmlRootElement(name = "pwm")
public class PWMContainer {

	@XmlElement(name = "group", required = false)
	protected List<PWMContainerGroup> groups;

	public List<PWMContainerGroup> getGroups() {
		if (groups == null)
			groups = new ArrayList<PWMContainerGroup>();
		return groups;
	}

	private final static QName _QNAME = new QName(null, "pwm");

	@XmlElementDecl(name = "pwm")
	public JAXBElement<PWMContainer> createJAXBElement(PWMContainer value) {
		return new JAXBElement<PWMContainer>(_QNAME, PWMContainer.class, null,
				value);
	}
}

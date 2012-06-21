package de.dzimmermann.rcp.bsgtaucha.mgt.model.intermediate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlType(name = "person-list", propOrder = { "persons" })
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PersonListRoot {

	@XmlElement(name = "person")
	private List<Person> persons;

	public List<Person> getPersons() {
		if (persons == null) {
			persons = new ArrayList<PersonListRoot.Person>();
		}
		return persons;
	}

	@XmlType(name = "person-list-entry")
	@XmlAccessorType(value = XmlAccessType.FIELD)
	public static class Person {

		@XmlAttribute(name = "first-name", required = true)
		private String firstName;
		@XmlAttribute(name = "name", required = true)
		private String name;
		@XmlAttribute(name = "street", required = true)
		private String street;
		@XmlAttribute(name = "postal-code", required = true)
		private String postalCode;
		@XmlAttribute(name = "city", required = true)
		private String city;
		@XmlAttribute(name = "active-member")
		private boolean activeMember;
		@XmlAttribute(name = "phone")
		private String phone;
		@XmlAttribute(name = "mobile")
		private String mobile;
		@XmlAttribute(name = "e-mail")
		private String email;
		@XmlAttribute(name = "fax")
		private String fax;
		@XmlAttribute(name = "birthday", required = true)
		@XmlJavaTypeAdapter(SimpleDateAdapter.class)
		private Date birthday;

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getPostalCode() {
			return postalCode;
		}

		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public boolean isActiveMember() {
			return activeMember;
		}

		public void setActiveMember(boolean activeMember) {
			this.activeMember = activeMember;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getFax() {
			return fax;
		}

		public void setFax(String fax) {
			this.fax = fax;
		}

		public Date getBirthday() {
			return birthday;
		}

		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}
	}

	public static class SimpleDateAdapter extends XmlAdapter<String, Date> {

		private static final DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

		@Override
		public Date unmarshal(String v) throws Exception {
			return new Date(df.parse(v).getTime());
		}

		@Override
		public String marshal(Date v) throws Exception {
			return df.format(v);
		}
	}
}

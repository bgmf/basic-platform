package de.dzimmermann.rcp.pwm.model.content;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "entry", propOrder = { "username", "password", "url",
		"description" })
public class PWMGroupEntry {

	@XmlAttribute(name = "id", required = true)
	protected String id;
	@XmlAttribute(name = "name", required = true)
	protected String name;
	@XmlAttribute(name = "date-added", required = true)
	protected Calendar dateAdded;
	@XmlAttribute(name = "date-modified", required = true)
	protected Calendar dateModified;
	@XmlAttribute(name = "date-expiration", required = false)
	protected Calendar dateExpiration;

	@XmlElement(name = "username", required = true)
	protected String username;
	@XmlElement(name = "password", required = false)
	protected String password;
	@XmlElement(name = "url", required = false)
	protected String url;
	@XmlElement(name = "description", required = false)
	protected String description;

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

	public Calendar getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Calendar dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Calendar getDateModified() {
		return dateModified;
	}

	public void setDateModified(Calendar dateModified) {
		this.dateModified = dateModified;
	}

	public Calendar getDateExpiration() {
		return dateExpiration;
	}

	public void setDateExpiration(Calendar dateExpiration) {
		this.dateExpiration = dateExpiration;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

package org.openstack.model.identity.swift;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.openstack.model.identity.Tenant;
import org.openstack.model.identity.Token;
import org.openstack.model.identity.keystone.KeystoneTenant;

@XmlAccessorType(XmlAccessType.NONE)
public class SwiftKeystoneToken implements Serializable, Token {

	@XmlAttribute
	private String id;

	@XmlAttribute
	private String expires;

	@XmlAttribute(name = "issued_at")
	@JsonProperty("issued_at")
	private String issuedAt;

	@XmlElement(type = KeystoneTenant.class)
	@JsonDeserialize(as = KeystoneTenant.class)
	private Tenant tenant;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openstack.model.identity.keystone.Token#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openstack.model.identity.keystone.Token#getExpires()
	 */
	@Override
	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	public String getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(String issuedAt) {
		this.issuedAt = issuedAt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openstack.model.identity.keystone.Token#getTenant()
	 */
	@Override
	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = (KeystoneTenant) tenant;
	}

	@Override
	public String toString() {
		return "KeyStoneToken [id=" + id + ", expires=" + expires + ", tenant="
				+ tenant + "]";
	}

}

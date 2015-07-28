package org.openstack.model.identity.swift;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

public class SwiftKeystoneServiceCatalogEntry implements Serializable {

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String type;

	@XmlElement(nillable = true, name = "endpoint", type = SwiftKeystoneEndpoint.class)
	@JsonProperty("endpoints")
	@JsonDeserialize(as = List.class, contentAs = SwiftKeystoneEndpoint.class)
	private List<SwiftKeystoneEndpoint> endpoints;

	@JsonProperty("endpoints_links")
	private List<String> endpointsLinks;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openstack.model.identity.keystone.ServiceCatalogEntry#getName()
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openstack.model.identity.keystone.ServiceCatalogEntry#getType()
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openstack.model.identity.keystone.ServiceCatalogEntry#getEndpoints()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openstack.model.identity.keystone.ServiceCatalogEntry#getEndpointsLinks
	 * ()
	 */
	public List<String> getEndpointsLinks() {
		return endpointsLinks;
	}

	public List<SwiftKeystoneEndpoint> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<SwiftKeystoneEndpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public void setEndpointsLinks(List<String> endpointsLinks) {
		this.endpointsLinks = endpointsLinks;
	}

}

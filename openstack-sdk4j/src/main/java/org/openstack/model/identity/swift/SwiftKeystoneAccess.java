package org.openstack.model.identity.swift;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.api.Namespaces;
import org.openstack.model.exceptions.OpenstackException;
import org.openstack.model.identity.Token;
import org.openstack.model.identity.User;
import org.openstack.model.identity.keystone.KeystoneUser;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@XmlType(namespace = Namespaces.NS_OPENSTACK_IDENTITY_2_0)
@XmlRootElement(name = "access")
@XmlAccessorType(XmlAccessType.NONE)
@JsonRootName("access")
public class SwiftKeystoneAccess implements Serializable {

	@XmlElement(type = SwiftKeystoneToken.class)
	private SwiftKeystoneToken token;

	@XmlElementWrapper(name = "serviceCatalog")
	@XmlElement(name = "service", type = SwiftKeystoneServiceCatalogEntry.class)
	@JsonProperty("serviceCatalog")
	@JsonDeserialize(as = List.class, contentAs = SwiftKeystoneServiceCatalogEntry.class)
	private List<SwiftKeystoneServiceCatalogEntry> services = new ArrayList<SwiftKeystoneServiceCatalogEntry>();

	@XmlElement(type = KeystoneUser.class)
	private KeystoneUser user;

	private Map metadata;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openstack.model.identity.glance.Access#getToken()
	 */
	public Token getToken() {
		return token;
	}

	public void setToken(SwiftKeystoneToken token) {
		this.token = (SwiftKeystoneToken) token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openstack.model.identity.glance.Access#getUser()
	 */
	public User getUser() {
		return user;
	}

	public List<SwiftKeystoneServiceCatalogEntry> getServices() {
		return services;
	}

	public void setServices(List<SwiftKeystoneServiceCatalogEntry> services) {
		this.services = services;
	}

	public void setUser(KeystoneUser user) {
		this.user = user;
	}

	public Map getMetadata() {
		return metadata;
	}

	public void setMetadata(Map metadata) {
		this.metadata = metadata;
	}

	@Override
	public String toString() {
		return "KeyStoneAccess [token=" + token + ", services=" + services
				+ ", user=" + user + "]";
	}


	public SwiftKeystoneEndpoint getEndpoint(final String type, final String region) {
		try {
			SwiftKeystoneServiceCatalogEntry service = Iterables.find(getServices(), new Predicate<SwiftKeystoneServiceCatalogEntry>() {

						@Override
						public boolean apply(SwiftKeystoneServiceCatalogEntry service) {
							return type.equals(service.getType());
						}

					});
			List<SwiftKeystoneEndpoint> endpoints = service.getEndpoints();
			if (region != null) {
				return  Iterables.find(endpoints, new Predicate<SwiftKeystoneEndpoint>() {

							@Override
							public boolean apply(SwiftKeystoneEndpoint endpoint) {
								return region.equals(endpoint.getRegion());
							}
						});
			} else {
				return endpoints.get(0);
			}
		} catch (NoSuchElementException e) {
			throw new OpenstackException("Service " + type + " not found, you can try openstack.target(<endpoint>, <resource class>) method instead", e);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}

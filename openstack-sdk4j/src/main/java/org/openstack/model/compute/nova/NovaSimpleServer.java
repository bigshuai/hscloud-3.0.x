package org.openstack.model.compute.nova;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.atom.Link;
import org.openstack.model.compute.Fault;
import org.openstack.model.compute.Flavor;
import org.openstack.model.compute.Image;
import org.openstack.model.compute.Server;
import org.openstack.model.compute.SimpleServer;
import org.openstack.model.compute.nova.NovaAddressList.Network;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@XmlRootElement(name = "server")
@XmlAccessorType(XmlAccessType.NONE)
@JsonRootName("server")
public class NovaSimpleServer implements Serializable, SimpleServer {

	@XmlAttribute
	private String id;

	@XmlElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
	private List<Link> links;
	
	public NovaSimpleServer() {
		// TODO Auto-generated constructor stub
	}

	public NovaSimpleServer(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getLinks()
	 */
	@Override
	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "Server [id=" + id + ", links=" + links + "]";
	}
	
}

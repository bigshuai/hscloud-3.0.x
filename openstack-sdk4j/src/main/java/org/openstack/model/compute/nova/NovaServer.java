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
import org.openstack.model.compute.nova.NovaAddressList.Network;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@XmlRootElement(name = "server")
@XmlAccessorType(XmlAccessType.NONE)
@JsonRootName("server")
public class NovaServer implements Serializable, Server {

	@XmlAttribute
	private String id;

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String status;

	@XmlAttribute
	private Date updated;

	@XmlAttribute
	private Date created;

	@XmlAttribute
	private String hostId;

	@XmlAttribute
	@JsonProperty("user_id")
	private String userId;

	@XmlAttribute
	@JsonProperty("tenant_id")
	private String tenantId;

	@XmlAttribute
	private String accessIPv4;

	@XmlAttribute
	private String accessIPv6;

	@XmlAttribute
	private String adminPass;

	@XmlAttribute
	private String progress;

	@XmlAttribute(name = "config_drive")
	@JsonProperty("config_drive")
	private String configDrive;

	@XmlAttribute(name = "key_name")
	@JsonProperty("key_name")
	private String keyName;

	@XmlElement(name = "image")
	@JsonDeserialize(as=NovaImage.class)
	private Image image;

	@XmlElement(name = "flavor")
	@JsonDeserialize(as=NovaFlavor.class)
	private Flavor flavor;

	@XmlElement(name = "fault")
	@JsonDeserialize(as=NovaFault.class)
	private Fault fault;

	@XmlElement(name = "metadata")
	private Map<String, String> metadata;

	@XmlElement(name = "addresses")
	private Map<String, List<Network.Ip>> addresses;
	
	private Map<String, String> extensions = new HashMap<String, String>();

	@JsonAnyGetter
	public Map<String, String> getExtensions() {
		return extensions;
	}

	@JsonAnySetter 
	public void put(String key, String value) {
	      extensions.put(key, value);
	}

	@XmlElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
	private List<Link> links;
	
	@XmlAttribute(name = "OS-EXT-STS:task_state")
	@JsonProperty("OS-EXT-STS:task_state")
	private String taskStatus;
	
	@XmlAttribute(name = "OS-EXT-SRV-ATTR:host")
	@JsonProperty("OS-EXT-SRV-ATTR:host")
	private String hostName;
	
	/**    
	 * hostName    
	 *    
	 * @return  the hostName    
	 * @since   CodingExample Ver(编码范例查看) 1.0    
	 */
	
	public String getHostName() {
		return hostName;
	}

	/**    
	 * @param hostName the hostName to set    
	 */
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public NovaServer() {
		// TODO Auto-generated constructor stub
	}

	public NovaServer(String id, String name) {
		this.id = id;
		this.name = name;
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
	 * @see org.openstack.model.compute.Server#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getStatus()
	 */
	@Override
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getUpdated()
	 */
	@Override
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getCreated()
	 */
	@Override
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getHostId()
	 */
	@Override
	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getUserId()
	 */
	@Override
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getTenantId()
	 */
	@Override
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getAccessIpV4()
	 */
	@Override
	public String getAccessIPv4() {
		return accessIPv4;
	}

	public void setAccessIPv4(String accessIPv4) {
		this.accessIPv4 = accessIPv4;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getAccessIpV6()
	 */
	@Override
	public String getAccessIPv6() {
		return accessIPv6;
	}

	public void setAccessIPv6(String accessIPv6) {
		this.accessIPv6 = accessIPv6;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getAdminPass()
	 */
	@Override
	public String getAdminPass() {
		return adminPass;
	}

	public void setAdminPass(String adminPass) {
		this.adminPass = adminPass;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getProgress()
	 */
	@Override
	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getConfigDrive()
	 */
	@Override
	public String getConfigDrive() {
		return configDrive;
	}

	public void setConfigDrive(String configDrive) {
		this.configDrive = configDrive;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getKeyName()
	 */
	@Override
	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getImage()
	 */
	@Override
	public Image getImage() {
		return image;
	}

	public void setImage(NovaImage image) {
		this.image = image;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getFlavor()
	 */
	@Override
	public Flavor getFlavor() {
		return flavor;
	}

	public void setFlavor(NovaFlavor flavor) {
		this.flavor = flavor;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getFault()
	 */
	@Override
	public Fault getFault() {
		return fault;
	}

	public void setFault(NovaFault fault) {
		this.fault = fault;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getMetadata()
	 */
	@Override
	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getAddresses()
	 */
	@Override
	public Map<String, List<Network.Ip>> getAddresses() {
		return addresses;
	}

	public void setAddresses(Map<String, List<Network.Ip>> addresses) {
		this.addresses = addresses;
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
	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	@Override
	public String toString() {
		return "NovaServer [id=" + id + ", name=" + name + ", status=" + status
				+ ", updated=" + updated + ", created=" + created + ", hostId="
				+ hostId + ", userId=" + userId + ", tenantId=" + tenantId
				+ ", accessIPv4=" + accessIPv4 + ", accessIPv6=" + accessIPv6
				+ ", adminPass=" + adminPass + ", progress=" + progress
				+ ", configDrive=" + configDrive + ", keyName=" + keyName
				+ ", image=" + image + ", flavor=" + flavor + ", fault="
				+ fault + ", metadata=" + metadata + ", addresses=" + addresses
				+ ", extensions=" + extensions + ", links=" + links
				+ ", taskStatus=" + taskStatus + ", hostName=" + hostName + "]";
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.Server#getLink(java.lang.String)
	 */
	@Override
	public Link getLink(final String rel) {
		return Iterables.find(links, new Predicate<Link>() {

			@Override
			public boolean apply(Link link) {
				return rel.equals(link.getRel());
			}
		});
	}

}

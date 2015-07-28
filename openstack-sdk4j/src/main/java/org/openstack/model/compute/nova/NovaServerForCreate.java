package org.openstack.model.compute.nova;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.compute.ServerForCreate;
import org.openstack.model.compute.nova.server.actions.SecurityRules;

import com.google.common.collect.Lists;

@XmlRootElement(name = "server")
@XmlAccessorType(XmlAccessType.NONE)
@JsonRootName("server")
public class NovaServerForCreate implements Serializable, ServerForCreate {

	@XmlType
	@XmlAccessorType(XmlAccessType.NONE)
	public static final class SecurityGroup implements Serializable {

		@XmlElement(name = "name")
		private String name;

		public SecurityGroup() {

		}

		public SecurityGroup(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "CreateSecurityGroupRequest [name=" + name + "]";
		}

	}

	@XmlType
	@XmlAccessorType(XmlAccessType.NONE)
	public static final class File implements Serializable {

		@XmlAttribute
		private String path;

		@XmlValue
		private String contents;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getContents() {
			return this.contents;
		}

		public void setContents(String contents) {
			this.contents = Base64.encodeBase64String(contents.getBytes());
//			this.contents = contents;
		}

	}

	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private Integer min;
	
	@XmlAttribute
	private Integer max;

	@XmlAttribute
	private String imageRef;

	@XmlAttribute
	private String flavorRef;

	@XmlAttribute
	private String accessIPv4;

	@XmlAttribute()
	private String accessIPv6;

	@XmlAttribute(name="availability_zone")
	@JsonProperty("availability_zone")
	private String zone;
	
	@XmlAttribute(name="job_id")
	@JsonProperty("job_id")
	private String jobId;
	
	@XmlAttribute(name="floating_ip")
	@JsonProperty("floating_ip")
	private String floatingIp;
	
	@XmlAttribute(name="disk_size")
	@JsonProperty("disk_size")	
	private String disksize;
	
	@XmlAttribute(name="reference_id")
	@JsonProperty("reference_id")
	private String referenceId;
	
	@XmlAttribute(name="obj_instance_id")
	@JsonProperty("obj_instance_id")
	private String objInstanceId;

	@XmlAttribute(name="key_name")
	@JsonProperty("key_name")
	private String keyName;
	
	@XmlAttribute(name="adminPass")
	@JsonProperty("adminPass")
	private String adminPassword;
	
	@XmlAttribute(name="job_ext")
	@JsonProperty("job_ext")
	private String jobExt;
	
	@JsonProperty("max_count")
	private int maxCount=1;
	@JsonProperty("min_count")
	private int minCount=1;
	@JsonProperty("networks")
	private List<NetworkLan> networks;
	public static final class NetworkLan implements Serializable {
		@JsonProperty("uuid")
		private String networkId;
		@JsonProperty("security_lan")
		private Integer lanId;
		@JsonProperty("fixed_ip")
		private String fixedIp;
		public String getNetworkId() {
			return networkId;
		}
		public void setNetworkId(String networkId) {
			this.networkId = networkId;
		}
		public Integer getLanId() {
			return lanId;
		}
		public void setLanId(Integer lanId) {
			this.lanId = lanId;
		}
		public String getFixedIp() {
			return fixedIp;
		}
		public void setFixedIp(String fixedIp) {
			this.fixedIp = fixedIp;
		}
	}
	

	// We have a problem here - config_drive can be both a boolean and an image ref...
	// But booleans can't be quoted!
	@XmlAttribute(name="config_drive")
	@JsonProperty("config_drive")
	private boolean configDrive;

	@XmlElementWrapper(name="metatadata")
	@XmlElement(name="meta")
	//private List<NovaMetadata.Item> metadata;
	private Map<String, String> metadata = new HashMap<String, String>();

	@XmlElementWrapper(name = "personality")
	@XmlElement(name="file")
	private List<File> personality;

	/**
	 * This security groups are not created on the fly. They must be exist in
	 * the tenant.
	 */
	@XmlElementWrapper(name = "security_groups")
	@XmlElement(name = "security_group")
	private List<SecurityGroup> securityGroups;
	
	@JsonProperty("security_rules")
	private SecurityRules securityRules;
	
	@JsonProperty("security_lan")
	private int securityLan;

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAdminPassword(String passwd){
		this.adminPassword = passwd;
	}
	
	public String getAdminPassword(){
		return this.adminPassword;
	}
	
	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#getMin()
	 */
	@Override
	public Integer getMin() {
		return min;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#setMin(java.lang.Integer)
	 */
	@Override
	public void setMin(Integer min) {
		this.min = min;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#getMax()
	 */
	@Override
	public Integer getMax() {
		return max;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#setMax(java.lang.Integer)
	 */
	@Override
	public void setMax(Integer max) {
		this.max = max;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#getImageRef()
	 */
	@Override
	public String getImageRef() {
		return imageRef;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#setImageRef(java.lang.String)
	 */
	@Override
	public void setImageRef(String imageRef) {
		this.imageRef = imageRef;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#getFlavorRef()
	 */
	@Override
	public String getFlavorRef() {
		return flavorRef;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#setFlavorRef(java.lang.String)
	 */
	@Override
	public void setFlavorRef(String flavorRef) {
		this.flavorRef = flavorRef;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#getZone()
	 */
	@Override
	public String getZone() {
		return zone;
	}

	public void setFloatingIp(String ip){
		this.floatingIp = ip;
	}
	
	public String getFloatingIp(){
		return this.floatingIp;
	}
	
	public void setReferenceId(String id){
		this.referenceId = id;
	}
	
	public String getReferenceId(){
		return this.referenceId;
	}
	
	public String getObjInstanceId() {
		return objInstanceId;
	}
	public void setObjInstanceId(String objInstanceId) {
		this.objInstanceId = objInstanceId;
	}
	
	public String getJobId(){
		return this.jobId;
	}
	
	public void setJobId(String id){
		this.jobId = id;
	}
	
	public void setJobExt(String ext){
		this.jobExt = ext;
	}
	
	public String getJobExt(){
		return this.jobExt;
	}
	
	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#setZone(java.lang.String)
	 */
	@Override
	public void setZone(String zone) {
		this.zone = zone;
	}
	
	public void setHost(String zone, String host, boolean force){
		if(zone != null){
			if(force){
				if(host == null){
					this.zone = zone;
				}else{
					this.zone = zone + ":" + host;
				}
			}else{
				if(host == null){
					this.zone = zone;
				}else{
					this.zone = zone + "$" + host;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#getKeyName()
	 */
	@Override
	public String getKeyName() {
		return keyName;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#setKeyName(java.lang.String)
	 */
	@Override
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#getAccessIPv4()
	 */
	@Override
	public String getAccessIPv4() {
		return accessIPv4;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#setAccessIPv4(java.lang.String)
	 */
	@Override
	public void setAccessIPv4(String accessIPv4) {
		this.accessIPv4 = accessIPv4;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#getAccessIPv6()
	 */
	@Override
	public String getAccessIPv6() {
		return accessIPv6;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#setAccessIpV6(java.lang.String)
	 */
	@Override
	public void setAccessIPv6(String accessIPv6) {
		this.accessIPv6 = accessIPv6;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#getMetadata()
	 */
	@Override
	public Map<String, String> getMetadata() {
		return metadata;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#setMetadata(java.util.List)
	 */
	@Override
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#getPersonality()
	 */
	@Override
	public List<File> getPersonality() {
		if (personality == null) {
			personality = Lists.newArrayList();
		}
		return personality;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#setPersonality(java.util.List)
	 */
	@Override
	public void setPersonality(List<File> personality) {
		this.personality = personality;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#getSecurityGroups()
	 */
	@Override
	public List<SecurityGroup> getSecurityGroups() {
		if (securityGroups == null) {
			securityGroups = Lists.newArrayList();
		}
		return securityGroups;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#setSecurityGroups(java.util.List)
	 */
	@Override
	public void setSecurityGroups(List<SecurityGroup> securityGroups) {
		this.securityGroups = securityGroups;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#addUploadFile(java.lang.String, java.lang.String)
	 */
	@Override
	public void addUploadFile(String path, String contents) {
		File item = new File();
		item.path = path;
		item.contents = contents;
		getPersonality().add(item);
	}

	//public void addUploadFile(String path, String contents) {
	//	addUploadFile(path, contents.getBytes(Charsets.UTF_8));
	//}

	
	public void setDisksize(String size){
		this.disksize = size;
	}
	
	public String getDisksize(){
		return this.disksize;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#getConfigDrive()
	 */
	@Override
	public boolean getConfigDrive() {
		return configDrive;
	}

	/* (non-Javadoc)
	 * @see org.openstack.model.compute.nova.ServerForCreate#setConfigDrive(boolean)
	 */
	@Override
	public void setConfigDrive(boolean configDrive) {
		this.configDrive = configDrive;
	}

	public void setSecurityLan(int secrityLan){
		this.securityLan = secrityLan;
	}
	
	public int getSecurityLan(){
		return this.securityLan;
	}
	
	public void setSecurityRules(SecurityRules sr){
		this.securityRules = sr;
	}
	
	public SecurityRules getSecurityRules(){
		return this.securityRules;
	}
	
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public int getMinCount() {
		return minCount;
	}
	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}
	public List<NetworkLan> getNetworks() {
		return networks;
	}

	public void setNetworks(List<NetworkLan> networks) {
		this.networks = networks;
	}
	
	@Override
	public String toString() {
		return "NovaServerForCreate [name=" + name + ", min=" + min + ", max="
				+ max + ", imageRef=" + imageRef + ", flavorRef=" + flavorRef
				+ ", accessIpV4=" + accessIPv4 + ", accessIpV6=" + accessIPv6
				+ ", zone=" + zone + ", keyName=" + keyName + ", configDrive="
				+ configDrive + ", metadata=" + metadata + ", personality="
				+ personality + ", securityGroups=" + securityGroups + "]";
	}

}

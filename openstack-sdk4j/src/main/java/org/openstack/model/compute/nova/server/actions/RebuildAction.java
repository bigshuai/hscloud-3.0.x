package org.openstack.model.compute.nova.server.actions;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.openstack.model.compute.ServerAction;
import org.openstack.model.compute.nova.NovaServer;
import org.openstack.model.compute.nova.NovaServerForCreate;
import org.openstack.model.compute.nova.NovaServerForCreate.File;

/**
 * The rebuild operation removes all data on the server and replaces it with the
 * specified image.
 * 
 * The serverRef and all IP addresses will remain the same.
 * 
 * If name, metadata, accessIPv4, or accessIPv6 are specified, they will replace
 * existing values, otherwise they do not change.
 * 
 * A rebuild operation always removes data injected into the file system through
 * server personality.
 * 
 * You can reinsert data into the file system during the rebuild.
 * 
 * @author luis@woorea.es
 * 
 */
@XmlRootElement(name = "rebuild")
@XmlAccessorType(XmlAccessType.NONE)
@JsonRootName("rebuild")
public class RebuildAction implements Serializable {

	@XmlAttribute
	private String name;

	// @XmlAttribute(name="auto_disk_config")
	// private boolean autoDiskConfig;
	//
	// @XmlElementWrapper(name="metatadata")
	// @XmlElement(name="meta")
	// private Map<String, String> metadata = new HashMap<String, String>();
	//
	// @XmlElementWrapper(name = "personality")
	// @XmlElement(name = "file")
	// private List<NovaServerForCreate.File> personality = new
	// ArrayList<NovaServerForCreate.File>();

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
			// this.contents = contents;
		}

	}

	@XmlAttribute
	private String imageRef;

	@JsonProperty("osId")
	@XmlAttribute
	private int osId;

	@JsonProperty("adminPass")
	@XmlAttribute
	private String adminPass;

	@XmlElementWrapper(name = "personality")
	@XmlElement(name = "file")
	private List<File> personality;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// public boolean getAutoDiskConfig() {
	// return autoDiskConfig;
	// }
	//
	// public void setAutoDiskConfig(boolean autoDiskConfig) {
	// this.autoDiskConfig = autoDiskConfig;
	// }
	//
	// public Map<String, String> getMetadata() {
	// return metadata;
	// }
	//
	// public void setMetadata(HashMap<String, String> metadata) {
	// this.metadata = metadata;
	// }
	//
	// public List<NovaServerForCreate.File> getPersonality() {
	// return personality;
	// }
	//
	// public void setPersonality(List<NovaServerForCreate.File> personality) {
	// this.personality = personality;
	// }

	public String getImageRef() {
		return imageRef;
	}

	public void setImageRef(String imageRef) {
		this.imageRef = imageRef;
	}

	public String getAdminPass() {
		return adminPass;
	}

	public void setAdminPass(String adminPass) {
		this.adminPass = adminPass;
	}

	public List<File> getPersonality() {
		return personality;
	}

	public void setPersonality(List<File> personality) {
		this.personality = personality;
	}

	public int getOsId() {
		return osId;
	}

	public void setOsId(int osId) {
		this.osId = osId;
	}

}

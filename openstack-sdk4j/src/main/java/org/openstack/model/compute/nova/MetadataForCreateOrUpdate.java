package org.openstack.model.compute.nova;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("metadata")
public class MetadataForCreateOrUpdate implements Serializable {

	private String passwd;
	@JsonProperty("os_type")
	private String OsType;
	private String desc;
	private String username;
	private String distro;

	public String getDistro() {
		return distro;
	}

	public void setDistro(String distro) {
		this.distro = distro;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getOsType() {
		return OsType;
	}

	public void setOsType(String osType) {
		OsType = osType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "MetadataForCreateOrUpdate [passwd=" + passwd + ", OsType="
				+ OsType + ", desc=" + desc + ", username=" + username
				+ ", distro=" + distro + "]";
	}

}

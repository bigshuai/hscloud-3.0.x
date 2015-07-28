package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("image")
public class ImageForCreate implements Serializable {
	private String file;

	@JsonProperty("disk_format")
	private String diskFormat;//必填项

	private String distro;

	@JsonProperty("os_type")
	private String osType;

	@JsonProperty("desc")
	private String desc;

	private String name;//必填项

	private String passwd;

	@JsonProperty("default_size")
	private String defaultSize;

	private String username;

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getFile() {
		return file;
	}

	public String getDefaultSize() {
		return defaultSize;
	}

	public void setDefaultSize(String defaultSize) {
		this.defaultSize = defaultSize;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getDiskFormat() {
		return diskFormat;
	}

	public void setDiskFormat(String diskFormat) {
		this.diskFormat = diskFormat;
	}

	public String getDistro() {
		return distro;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDistro(String distro) {
		this.distro = distro;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "ImageForCreate [file=" + file + ", diskFormat=" + diskFormat
				+ ", distro=" + distro + ", osType=" + osType + ", desc="
				+ desc + ", passwd=" + passwd + ", username=" + username + "]";
	}

}

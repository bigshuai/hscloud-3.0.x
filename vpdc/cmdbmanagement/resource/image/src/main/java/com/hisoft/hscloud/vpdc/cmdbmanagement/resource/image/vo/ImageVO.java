package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo; 


public class ImageVO {
	private String id;
	private String fileName;
	private String diskFormat;
	private String distro;
	private String osType;
	private String desc;
	private String passwd;
	private String username;
	private String size;
	private String virtualSize;
	private String name;

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	public void setDistro(String distro) {
		this.distro = distro;
	}
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getVirtualSize() {
		return virtualSize;
	}
	public void setVirtualSize(String virtualSize) {
		this.virtualSize = virtualSize;
	}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

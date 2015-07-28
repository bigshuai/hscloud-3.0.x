package com.pactera.hscloud.openstackhandler.vo;


public class VMVO {
	
	private String uuid;
	
	private String user;
	
	private String password;
	
	private String imageId;
	
	private Integer osId;
	
	private Long instanceId;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public Integer getOsId() {
		return osId;
	}

	public void setOsId(Integer osId) {
		this.osId = osId;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}
	
	
}

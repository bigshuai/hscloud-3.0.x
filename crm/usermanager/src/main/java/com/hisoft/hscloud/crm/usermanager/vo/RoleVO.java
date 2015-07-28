package com.hisoft.hscloud.crm.usermanager.vo;


public class RoleVO	{
	private Long id;
	private String name;
	private String privilegesStr;
	private String noCheckStr;
	private String type;


	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPrivilegesStr() {
		return privilegesStr;
	}
	public void setPrivilegesStr(String privilegesStr) {
		this.privilegesStr = privilegesStr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNoCheckStr() {
		return noCheckStr;
	}
	public void setNoCheckStr(String noCheckStr) {
		this.noCheckStr = noCheckStr;
	}
	
	
}

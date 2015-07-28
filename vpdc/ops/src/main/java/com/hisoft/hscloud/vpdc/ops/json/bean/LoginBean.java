package com.hisoft.hscloud.vpdc.ops.json.bean;
 
public class LoginBean {
	private String auth_url;
	private String name;
	private String password;
	private String tenant;
	public String getAuth_url() {
		return auth_url;
	}
	public void setAuth_url(String auth_url) {
		this.auth_url = auth_url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	
	
} 

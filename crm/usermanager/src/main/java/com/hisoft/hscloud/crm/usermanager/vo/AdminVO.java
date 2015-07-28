package com.hisoft.hscloud.crm.usermanager.vo;

import java.util.Date;

public class AdminVO {
	private long id;
	private String name;
	private String password;
	private String email;
	private short enable;
	private boolean isSuper;
	private String telephone;
	private Date lastLoginDate;
	private String roleName;
	private Date createDate;
	private Long roleId;
	private Long adminRoleId;
	private Integer type;
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getAdminRoleId() {
		return adminRoleId;
	}
	public void setAdminRoleId(Long adminRoleId) {
		this.adminRoleId = adminRoleId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public short getEnable() {
		return enable;
	}
	public void setEnable(short enable) {
		this.enable = enable;
	}
	public boolean isSuper() {
		return isSuper;
	}
	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}

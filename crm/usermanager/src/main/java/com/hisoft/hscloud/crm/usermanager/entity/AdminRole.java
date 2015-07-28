package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hc_admin_role")
public class AdminRole {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	long adminId;
	long roleId;
	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAdminId() {
		return adminId;
	}
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
	

}

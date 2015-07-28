package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hc_role_permission")
public class RolePermission {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	@Column(name = "role_id")
	long roleId;
	@Column(name = "permission_id")
	long permissionId;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	
	public long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}

	@Override
	public String toString() {
		return "RolePermission[id=\"" + this.id + "\",permissionId=\""
				+ this.permissionId + "\",roleId=" + this.roleId + "\"]";
	}

}

package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hc_usergroup_permission")
public class UserGroupPermission {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	@Column(name = "usergroup_id")
	long userGroupId;
	@Column(name = "permission_id")
	long permissionId;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	public long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(long userGroupId) {
		this.userGroupId = userGroupId;
	}

	
	public long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}

	@Override
	public String toString() {
		return "UserGroupPermission[userGroupId=\"" + this.userGroupId
				+ "\",permissionId=\"" + this.permissionId + "\",id=\""
				+ this.id + "\"]";
	}

}

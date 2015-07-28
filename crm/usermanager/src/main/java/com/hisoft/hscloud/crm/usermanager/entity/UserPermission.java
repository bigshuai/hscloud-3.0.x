package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hc_user_permission")
public class UserPermission {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;

	long userId;

	long permissionId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}

	@Override
	public String toString() {
		return "UserPermission[id=\"" + this.id + "\",userId=\"" + this.userId
				+ "\",permissionId=\"" + this.permissionId + "\"]";
	}

}

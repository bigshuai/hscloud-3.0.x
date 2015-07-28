package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hc_user_usergroup")
public class UserUserGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	long userId;
	long userGroupId;

	
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

	public long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(long userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Override
	public String toString() {
		return "UserUserGroup[userId=\"" + this.userId + "\",userGroupId=\""
				+ this.userGroupId + "\",id=\"" + this.id + "\"]";
	}

}

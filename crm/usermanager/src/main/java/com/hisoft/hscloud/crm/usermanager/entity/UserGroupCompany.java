package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hc_usergroup_company")
public class UserGroupCompany {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	@Column(name = "usergroup_id")
	long userGroupId;
	@Column(name = "company_id")
	long companyId;
	
	
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

	
	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "UserGroupCompany[userGroupId=\"" + this.userGroupId
				+ "\",companyId=\"" + this.companyId +"\",id="+this.id+ "\"]";
	}

}

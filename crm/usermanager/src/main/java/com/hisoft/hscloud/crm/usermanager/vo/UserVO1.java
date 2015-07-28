package com.hisoft.hscloud.crm.usermanager.vo;

import java.util.Date;

import com.hisoft.hscloud.common.entity.AbstractVO;

public class UserVO1 extends AbstractVO {
	
	public String email;
	
	public String name;
	
	public long domainId;
	
	private Short enable ;
	
	private Date lastLoginDate;
	
	private String userType;
	
	private String telephone;
	
	private long profileId;
	
	private ProfileVo profileVo;
	
	private String level;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDomainId() {
		return domainId;
	}

	public void setDomainId(long domainId) {
		this.domainId = domainId;
	}

	public Short getEnable() {
		return enable;
	}

	public void setEnable(Short enable) {
		this.enable = enable;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public ProfileVo getProfileVo() {
		return profileVo;
	}

	public void setProfileVo(ProfileVo profileVo) {
		this.profileVo = profileVo;
	}

	public long getProfileId() {
		return profileId;
	}

	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	
	

}

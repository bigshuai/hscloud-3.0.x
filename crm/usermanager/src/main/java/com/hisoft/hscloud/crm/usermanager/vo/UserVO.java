package com.hisoft.hscloud.crm.usermanager.vo;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.hisoft.hscloud.common.entity.AbstractVO;
import com.hisoft.hscloud.crm.usermanager.constant.UserState;
import com.hisoft.hscloud.crm.usermanager.entity.UserProfile;

public class UserVO extends AbstractVO {

	private String name;
	private String password;
	private String email;
	private short enable = UserState.CREATE.getIndex();
	private Date lastLoginDate;
	private String userType;
	private String telephone;
	private Integer domainId;
	private UserProfile userProfile;
	private List<UserGroupVO> userGroup = new LinkedList<UserGroupVO>();

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

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public List<UserGroupVO> getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(List<UserGroupVO> userGroup) {
		this.userGroup = userGroup;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}
	

}

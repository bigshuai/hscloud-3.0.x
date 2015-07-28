package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.UserProfile;

/**
 * @description user entity
 * @version 1.0
 * @author guole.liang
 * @update 2012-3-29 下午1:38:59
 */
@Entity
@Table(name = "hc_user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User extends AbstractUser {

	@Column(name = "user_type")
	private String userType;//用户类型
	private String telephone;//手机号码
	@Column(name = "online_status")
	private short onlineStatus = (short) 0;
	@OneToOne(cascade = CascadeType.ALL)
	private UserProfile userProfile;
	@Column(name = "user_source")
	private String user_source;//用户来源
	
	private String level=Constants.DEFULT_BRAND_CODE;// 用户级别
	@Column(columnDefinition="int default 0",nullable=false)
	private int specialFlag;//特殊用户标识：0-普通；1-特殊
//	@Column(name = "domain_id", nullable = false)
//	private Long domainId;//平台id;
	@OneToOne(optional=false)
	private Domain domain;

	public short getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(short onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	public int getSpecialFlag() {
		return specialFlag;
	}

	public void setSpecialFlag(int specialFlag) {
		this.specialFlag = specialFlag;
	}

	public User(long id, String name) {
		super();
		setId(id);
		setName(name);
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public User() {

	}

	public User(long id) {
		super();
		setId(id);
	}

	@Transient
	public long getCurrentUserId() {
		return getId();
	}

	public String getUser_source() {
		return user_source;
	}

	public void setUser_source(String user_source) {
		this.user_source = user_source;
	}

	@Override
	public String toString() {
		return "User [userType=" + userType + ", telephone=" + telephone
				+ ", onlineStatus=" + onlineStatus + ", userProfile="
				+ userProfile + ", user_source=" + user_source + ", level="
				+ level + ", specialFlag=" + specialFlag + ", domain=" + domain
				+ "]";
	}

}
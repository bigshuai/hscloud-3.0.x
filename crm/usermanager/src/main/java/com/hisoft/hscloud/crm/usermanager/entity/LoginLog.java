package com.hisoft.hscloud.crm.usermanager.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hc_login_log")
public class LoginLog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Date loginDate = new Date();
	//user web-site User;admin web-admin User
	@Column(name="user_type",nullable=false)
	private String userType;
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	@Override
	public String toString() {

		return "LoginLog=[" + "id=\"" + this.id + "\",userId=\"" + this.userId
				+ "\",loginDate=\"" + this.loginDate + "\"]";

	}

}

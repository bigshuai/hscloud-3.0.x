package com.hisoft.hscloud.crm.usermanager.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @className: MaintenanceLog
 * @package: com.hisoft.hscloud.crm.usermanager.entity
 * @description: TODO
 * @author: liyunhui
 * @createTime: Sep 6, 2013 9:37:34 AM
 */
@Entity
@Table(name = "hc_maintenance_log")
public class MaintenanceLog {

	// id主键
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	// 用户名
	@Column
	private String name;
	// 邮箱
	@Column
	private String email;
	@Column(name="user_id")
	private Long userId;
	// 登录时间
	@Column(name = "login_date", nullable = false)
	private Date loginDate;
	// 用户类型: 1为前台用户 2为控制面板用户
	@Column(name = "user_type", nullable = false)
	private short userType;
	// 虚拟机的uuid,用于控制面板的虚拟机
	private String uuid;
	// 操作人
	private String operator;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public short getUserType() {
		return userType;
	}

	public void setUserType(short userType) {
		this.userType = userType;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "MaintenanceLog [id=" + id + ", name=" + name + ", email="
				+ email + ", loginDate=" + loginDate + ", userType=" + userType
				+ ", uuid=" + uuid + ", operator=" + operator + "]";
	}

}
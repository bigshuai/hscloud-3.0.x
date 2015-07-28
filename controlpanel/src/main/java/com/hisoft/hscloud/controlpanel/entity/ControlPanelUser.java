/* 
 * 文 件 名:  ControlPanelUser.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  dinghb 
 * 修改时间:  2013-1-4 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.controlpanel.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author dinghb
 * @version [版本号, 2013-1-4]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Entity
@Table(name = "hc_controlpanel_user")
public class ControlPanelUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	// 控制面板VM IP
	@Column(name = "vmIP",nullable = false, length=2000)
	private String vmIP;
	// 控制面板登录密码
	@Column(name = "userPassword",columnDefinition="varchar(255) binary")
	private String userPassword;
	// 创建人
	@Column(name = "create_Id", nullable = false)
	private long create_Id;
	// 创建时间
	@Column(name = "create_date", nullable = false)
	private Date createDate = new Date();
	// 修改时间
	@Column(name = "update_date", nullable = false)
	private Date updateDate = new Date();
	// 版本
	@Version
	@Column(name = "version", nullable = false)
	private long version = 0;
	// 删除状态
	@Column(name = "status", nullable = false)
	private int status = 0;
	//对应的Instance
	@Column(name = "vmId")
	private String vmId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVmIP() {
		return vmIP;
	}

	public void setVmIP(String vmIP) {
		this.vmIP = vmIP;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public long getCreate_Id() {
		return create_Id;
	}

	public void setCreate_Id(long create_Id) {
		this.create_Id = create_Id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

}

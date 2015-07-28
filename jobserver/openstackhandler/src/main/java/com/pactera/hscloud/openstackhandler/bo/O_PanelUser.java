package com.pactera.hscloud.openstackhandler.bo;

import java.util.Date;

public class O_PanelUser {
	
	private Date create_date;
	
	private Date update_date;
	
	private Long create_Id;
	
	private Integer status;
	
	private String userPassword;
	
	private String vmIP;
	
	private String vmId;
	
	private Integer version;

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Long getCreate_Id() {
		return create_Id;
	}

	public void setCreate_Id(Long create_Id) {
		this.create_Id = create_Id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getVmIP() {
		return vmIP;
	}

	public void setVmIP(String vmIP) {
		this.vmIP = vmIP;
	}

	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	
}

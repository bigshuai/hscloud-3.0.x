package com.hisoft.hscloud.vpdc.ops.json.bean;

public class GetVmsBean {
	  private String vmName;
	  private String UserName;
	  private String status;
	  private String Ip;
	  private String userId;
	  public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	//vm所在主機的IP
	  private String hostId;
	public String getVmName() {
		return vmName;
	}
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIp() {
		return Ip;
	}
	public void setIp(String ip) {
		Ip = ip;
	}
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	@Override
	public String toString() {
		return "GetVmsBean [vmName=" + vmName + ", UserName=" + UserName
				+ ", status=" + status + ", Ip=" + Ip + ", userId=" + userId
				+ ", hostId=" + hostId + "]";
	}
 
	  
} 

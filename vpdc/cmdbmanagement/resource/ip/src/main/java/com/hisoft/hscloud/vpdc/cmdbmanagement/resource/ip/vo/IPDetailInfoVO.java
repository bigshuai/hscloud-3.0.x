package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo;

public class IPDetailInfoVO {
	private long id;	
	private String ip;		
	private int status;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}

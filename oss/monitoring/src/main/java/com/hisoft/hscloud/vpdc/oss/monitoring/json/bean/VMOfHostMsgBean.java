package com.hisoft.hscloud.vpdc.oss.monitoring.json.bean;

import java.util.Date;
 public class VMOfHostMsgBean {
	
	 private Date timestamp;
	 private String type;
	 private String format;
	 private VMOfHostBean[] vmList;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public VMOfHostBean[] getVmList() {
		return vmList;
	}

	public void setVmList(VMOfHostBean[] vmList) {
		this.vmList = vmList;
	}
	
	public String toString() {
		return "VMOfHostMsgBean [timestamp=" + timestamp + ", type=" + type + ", format=" + format
				+ ", vmList=" + vmList + "]";
	}
}

/**
 * @title InstanceAdminVo.java
 * @package com.hisoft.hscloud.vpdc.ops.vo
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-4-1 下午4:59:44
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.oss.monitoring.vo;

import java.util.Date;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.4
 * @author ljg
 * @update 2012-4-1 下午4:59:44
 */
public class InstancesOfAdminVo {
	private String id;
	private String vmName;	
	private String nodeName;
	private String vmStatus;	
	private String vmIp;
	private String vmImage;
	private double cpuUsage;
	private double memoryUsage;
	private double diskUsage;
	private double networkUsage;	
	private Date createTime;
	private String remark;
	
	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getVmName() {
		return vmName;
	}



	public void setVmName(String vmName) {
		this.vmName = vmName;
	}



	public String getNodeName() {
		return nodeName;
	}



	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}



	public String getVmStatus() {
		return vmStatus;
	}



	public void setVmStatus(String vmStatus) {
		this.vmStatus = vmStatus;
	}



	public String getVmIp() {
		return vmIp;
	}



	public void setVmIp(String vmIp) {
		this.vmIp = vmIp;
	}



	public String getVmImage() {
		return vmImage;
	}



	public void setVmImage(String vmImage) {
		this.vmImage = vmImage;
	}



	public double getCpuUsage() {
		return cpuUsage;
	}



	public void setCpuUsage(double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}



	public double getMemoryUsage() {
		return memoryUsage;
	}



	public void setMemoryUsage(double memoryUsage) {
		this.memoryUsage = memoryUsage;
	}



	public double getDiskUsage() {
		return diskUsage;
	}



	public void setDiskUsage(double diskUsage) {
		this.diskUsage = diskUsage;
	}



	public double getNetworkUsage() {
		return networkUsage;
	}



	public void setNetworkUsage(double networkUsage) {
		this.networkUsage = networkUsage;
	}



	public Date getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}




	@Override
	public String toString() {
		return "InstanceAdminVo [id=" + id + ", vmName=" + vmName
				+ ", nodeName=" + nodeName + ", vmStatus=" + vmStatus
				+ ", vmIp=" + vmIp + ", vmImage=" + vmImage + ", cpuUsage="
				+ cpuUsage + ", memoryUsage=" + memoryUsage + ", diskUsage=" + diskUsage
				+ ", networkUsage=" + networkUsage 
				+ ", createTime=" + createTime +", remark="+remark+"]";
	}
	
	
}

/* 
* 文 件 名:  ServerAcquisitionBean.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-5-8 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean; 

/** 
 * <虚拟机监控信息> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ServerAcquisitionBean {

	private String vmUUID;//虚拟机ID
	private Integer vcpus;//虚拟机CPU数量
	private Integer vmemory;//虚拟机内存大小
	private Integer vdisk;//虚拟机磁盘大小
	private String vmStatus;//虚拟机状态
	private Double vCPUUsage;//虚拟CPU使用率
	private Double vMemoryUsage;//虚拟内存使用率
	private Double vDiskUsage;//虚拟磁盘使用率
	private Integer cpuWorkload;//cpu工作量
	private Integer iopsRead;//IOPS读
	private Integer iopsWrite;//IOPS写
	private Integer rxSpeed;//网络读
	private Integer txSpeed;//网络写
	public String getVmUUID() {
		return vmUUID;
	}
	public void setVmUUID(String vmUUID) {
		this.vmUUID = vmUUID;
	}
	public Integer getVcpus() {
		return vcpus;
	}
	public void setVcpus(Integer vcpus) {
		this.vcpus = vcpus;
	}
	public Integer getVmemory() {
		return vmemory;
	}
	public void setVmemory(Integer vmemory) {
		this.vmemory = vmemory;
	}
	public Integer getVdisk() {
		return vdisk;
	}
	public void setVdisk(Integer vdisk) {
		this.vdisk = vdisk;
	}
	public String getVmStatus() {
		return vmStatus;
	}
	public void setVmStatus(String vmStatus) {
		this.vmStatus = vmStatus;
	}
	public Double getvCPUUsage() {
		return vCPUUsage;
	}
	public void setvCPUUsage(Double vCPUUsage) {
		this.vCPUUsage = vCPUUsage;
	}
	public Double getvMemoryUsage() {
		return vMemoryUsage;
	}
	public void setvMemoryUsage(Double vMemoryUsage) {
		this.vMemoryUsage = vMemoryUsage;
	}
	public Double getvDiskUsage() {
		return vDiskUsage;
	}
	public void setvDiskUsage(Double vDiskUsage) {
		this.vDiskUsage = vDiskUsage;
	}
	public Integer getCpuWorkload() {
		return cpuWorkload;
	}
	public void setCpuWorkload(Integer cpuWorkload) {
		this.cpuWorkload = cpuWorkload;
	}
	public Integer getIopsRead() {
		return iopsRead;
	}
	public void setIopsRead(Integer iopsRead) {
		this.iopsRead = iopsRead;
	}
	public Integer getIopsWrite() {
		return iopsWrite;
	}
	public void setIopsWrite(Integer iopsWrite) {
		this.iopsWrite = iopsWrite;
	}
	public Integer getRxSpeed() {
		return rxSpeed;
	}
	public void setRxSpeed(Integer rxSpeed) {
		this.rxSpeed = rxSpeed;
	}
	public Integer getTxSpeed() {
		return txSpeed;
	}
	public void setTxSpeed(Integer txSpeed) {
		this.txSpeed = txSpeed;
	}
}

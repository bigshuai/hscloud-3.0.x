/* 
* 文 件 名:  HostAcquisitionBean.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-5-20 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.jsonBean; 

/** 
 * <发现节点数据集> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-20] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class HostAcquisitionBean {

	private String hostName;//节点名称
	private String hostStatus;//节点状态
	private String hostIP;//节点外网IP
	private String hostInnerIP;//节点内网IP
	private String zoneCode;//资源域编码
	private String physicalCPUConfiguration;//物理CPU配置
	private String physicalCPUFrequency;//物理CPU频率
	private Integer physicalCPUCore;//物理CPU核数
	private Integer physicalCPUTotal;//物理CPU总数量
	private Integer physicalMemoryTotal;//物理内存总大小
	private Integer physicalDiskTotal;//物理磁盘总大小	
	private Double vgTotal;//扩展盘总大小
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getHostStatus() {
		return hostStatus;
	}
	public void setHostStatus(String hostStatus) {
		this.hostStatus = hostStatus;
	}
	public String getHostIP() {
		return hostIP;
	}
	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}
	public String getHostInnerIP() {
		return hostInnerIP;
	}
	public void setHostInnerIP(String hostInnerIP) {
		this.hostInnerIP = hostInnerIP;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public String getPhysicalCPUConfiguration() {
		return physicalCPUConfiguration;
	}
	public void setPhysicalCPUConfiguration(String physicalCPUConfiguration) {
		this.physicalCPUConfiguration = physicalCPUConfiguration;
	}
	public String getPhysicalCPUFrequency() {
		return physicalCPUFrequency;
	}
	public void setPhysicalCPUFrequency(String physicalCPUFrequency) {
		this.physicalCPUFrequency = physicalCPUFrequency;
	}
	public Integer getPhysicalCPUCore() {
		return physicalCPUCore;
	}
	public void setPhysicalCPUCore(Integer physicalCPUCore) {
		this.physicalCPUCore = physicalCPUCore;
	}
	public Integer getPhysicalCPUTotal() {
		return physicalCPUTotal;
	}
	public void setPhysicalCPUTotal(Integer physicalCPUTotal) {
		this.physicalCPUTotal = physicalCPUTotal;
	}
	public Integer getPhysicalMemoryTotal() {
		return physicalMemoryTotal;
	}
	public void setPhysicalMemoryTotal(Integer physicalMemoryTotal) {
		this.physicalMemoryTotal = physicalMemoryTotal;
	}
	public Integer getPhysicalDiskTotal() {
		return physicalDiskTotal;
	}
	public void setPhysicalDiskTotal(Integer physicalDiskTotal) {
		this.physicalDiskTotal = physicalDiskTotal;
	}
	public Double getVgTotal() {
		return vgTotal;
	}
	public void setVgTotal(Double vgTotal) {
		this.vgTotal = vgTotal;
	}
}

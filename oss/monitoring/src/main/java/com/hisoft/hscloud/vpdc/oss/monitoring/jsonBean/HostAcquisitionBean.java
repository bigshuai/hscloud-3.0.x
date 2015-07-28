/* 
* 文 件 名:  HostAcquisitionBean.java 
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
 * <节点监控信息> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class HostAcquisitionBean {

	private String hostName;//节点名称
	private String hostStatus;//节点状态
	private String hostIP;//节点外网IP
	private String hostInnerIP;//节点内网IP
	private String physicalCPUConfiguration;//物理CPU配置
	private String physicalCPUFrequency;//物理CPU频率
	private Integer physicalCPUCore;//物理CPU核数
	private Integer physicalCPUTotal;//物理CPU总数量
	private Integer physicalMemoryTotal;//物理内存总大小
	private Integer physicalDiskTotal;//物理磁盘总大小
	private Double CPUUsage;//CPU使用率
	private Double memoryUsage;//内存使用率
	private Double diskUsage;//磁盘使用率
	private Integer CPUTotal;//虚拟CPU总数
	private Integer CPUApply;//虚拟CPU申请数
	private Integer CPUUsed;//虚拟CPU使用数
	private Integer memoryTotal;//虚拟内存总大小
	private Integer memoryApply;//虚拟内存申请大小
	private Integer memoryUsed;//虚拟内存使用大小
	private Integer diskTotal;//虚拟磁盘总大小
	private Integer diskApply;//虚拟磁盘申请大小
	private Integer diskUsed;//虚拟磁盘使用大小
	private Double vgTotal;//扩展盘总大小
	private Double vgUsed;//扩展盘使用大小
	private Integer CPURatio;//CPU配比
	private Integer memoryRatio;//内存配比
	private Integer diskRatio;//磁盘配比
	private Integer vmTotal;//虚拟机总数
	private Integer vmActive;//虚拟机运行数
	private Integer cpuWorkload;//cpu工作量
	private Integer iopsRead;//IOPS读
	private Integer iopsWrite;//IOPS写
	private Integer rxSpeed;//网络读
	private Integer txSpeed;//网络写
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
	public Double getCPUUsage() {
		return CPUUsage;
	}
	public void setCPUUsage(Double cPUUsage) {
		CPUUsage = cPUUsage;
	}
	public Double getMemoryUsage() {
		return memoryUsage;
	}
	public void setMemoryUsage(Double memoryUsage) {
		this.memoryUsage = memoryUsage;
	}
	public Double getDiskUsage() {
		return diskUsage;
	}
	public void setDiskUsage(Double diskUsage) {
		this.diskUsage = diskUsage;
	}
	public Integer getCPUTotal() {
		return CPUTotal;
	}
	public void setCPUTotal(Integer cPUTotal) {
		CPUTotal = cPUTotal;
	}
	public Integer getCPUApply() {
		return CPUApply;
	}
	public void setCPUApply(Integer cPUApply) {
		CPUApply = cPUApply;
	}
	public Integer getCPUUsed() {
		return CPUUsed;
	}
	public void setCPUUsed(Integer cPUUsed) {
		CPUUsed = cPUUsed;
	}
	public Integer getMemoryTotal() {
		return memoryTotal;
	}
	public void setMemoryTotal(Integer memoryTotal) {
		this.memoryTotal = memoryTotal;
	}
	public Integer getMemoryApply() {
		return memoryApply;
	}
	public void setMemoryApply(Integer memoryApply) {
		this.memoryApply = memoryApply;
	}
	public Integer getMemoryUsed() {
		return memoryUsed;
	}
	public void setMemoryUsed(Integer memoryUsed) {
		this.memoryUsed = memoryUsed;
	}
	public Integer getDiskTotal() {
		return diskTotal;
	}
	public void setDiskTotal(Integer diskTotal) {
		this.diskTotal = diskTotal;
	}
	public Integer getDiskApply() {
		return diskApply;
	}
	public void setDiskApply(Integer diskApply) {
		this.diskApply = diskApply;
	}
	public Integer getDiskUsed() {
		return diskUsed;
	}
	public void setDiskUsed(Integer diskUsed) {
		this.diskUsed = diskUsed;
	}
	public Double getVgTotal() {
		return vgTotal;
	}
	public void setVgTotal(Double vgTotal) {
		this.vgTotal = vgTotal;
	}
	public Double getVgUsed() {
		return vgUsed;
	}
	public void setVgUsed(Double vgUsed) {
		this.vgUsed = vgUsed;
	}
	public Integer getCPURatio() {
		return CPURatio;
	}
	public void setCPURatio(Integer cPURatio) {
		CPURatio = cPURatio;
	}
	public Integer getMemoryRatio() {
		return memoryRatio;
	}
	public void setMemoryRatio(Integer memoryRatio) {
		this.memoryRatio = memoryRatio;
	}
	public Integer getDiskRatio() {
		return diskRatio;
	}
	public void setDiskRatio(Integer diskRatio) {
		this.diskRatio = diskRatio;
	}
	public Integer getVmTotal() {
		return vmTotal;
	}
	public void setVmTotal(Integer vmTotal) {
		this.vmTotal = vmTotal;
	}
	public Integer getVmActive() {
		return vmActive;
	}
	public void setVmActive(Integer vmActive) {
		this.vmActive = vmActive;
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

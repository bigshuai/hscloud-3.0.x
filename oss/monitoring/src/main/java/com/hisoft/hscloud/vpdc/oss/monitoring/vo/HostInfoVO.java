package com.hisoft.hscloud.vpdc.oss.monitoring.vo;

import org.openstack.model.common.HostIpmiInfo;

import com.hisoft.hscloud.bss.sla.sc.entity.IPMIConfig;
import com.hisoft.hscloud.bss.sla.sc.entity.NodeIsolationConfig;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.CPUMonitorDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.IOPSMonitorDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.NETMonitorDetailBean;

/**
 * 
* @description 节点信息
* @version 1.3
* @author ljg
* @update 2012-9-21 下午5:15:19
 */
 public class HostInfoVO extends HostVO {
	 
	 /** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -5274151440880149823L;
	private int vmActive;
	 private int vmTotal;
	 private String ipInner;
	 private String hostStatus;	 
	 private int cpuRate;
	 private int memoryRate;
	 private int diskRate;	 
	 private double cpuUsage;
	 private double memoryUsage;
	 private double diskUsage;
	 private String cpuType;
	 private int cpuCore;
	 private double memory;
	 private double disk;
	 private String hostZone;
	 private long zoneId;
	 private HostIpmiInfo hostIpmiInfo;
	 private CPUMonitorDetailBean cpuMonitorDetailBean;
	 private IPMIConfig ipmiConfig;
	 private NodeIsolationConfig nodeIsolationConfig;
	 private IOPSMonitorDetailBean iopsMonitorDetailBean;
	 private NETMonitorDetailBean netMonitorDetailBean;
	 private int isEnable;
	
	public int getVmActive() {
		return vmActive;
	}
	public void setVmActive(int vmActive) {
		this.vmActive = vmActive;
	}
	public int getVmTotal() {
		return vmTotal;
	}
	public void setVmTotal(int vmTotal) {
		this.vmTotal = vmTotal;
	}
	public String getIpInner() {
		return ipInner;
	}
	public void setIpInner(String ipInner) {
		this.ipInner = ipInner;
	}	
	public String getHostStatus() {
		return hostStatus;
	}
	public void setHostStatus(String hostStatus) {
		this.hostStatus = hostStatus;
	}
	public int getCpuRate() {
		return cpuRate;
	}
	public void setCpuRate(int cpuRate) {
		this.cpuRate = cpuRate;
	}
	public int getMemoryRate() {
		return memoryRate;
	}
	public void setMemoryRate(int memoryRate) {
		this.memoryRate = memoryRate;
	}
	public int getDiskRate() {
		return diskRate;
	}
	public void setDiskRate(int diskRate) {
		this.diskRate = diskRate;
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
	public String getCpuType() {
		return cpuType;
	}
	public void setCpuType(String cpuType) {
		this.cpuType = cpuType;
	}
	public int getCpuCore() {
		return cpuCore;
	}
	public void setCpuCore(int cpuCore) {
		this.cpuCore = cpuCore;
	}
	public double getMemory() {
		return memory;
	}
	public void setMemory(double memory) {
		this.memory = memory;
	}
	public double getDisk() {
		return disk;
	}
	public void setDisk(double disk) {
		this.disk = disk;
	}
	public String getHostZone() {
		return hostZone;
	}
	public void setHostZone(String hostZone) {
		this.hostZone = hostZone;
	}	
	public long getZoneId() {
		return zoneId;
	}
	public void setZoneId(long zoneId) {
		this.zoneId = zoneId;
	}
	public HostIpmiInfo getHostIpmiInfo() {
		return hostIpmiInfo;
	}
	public void setHostIpmiInfo(HostIpmiInfo hostIpmiInfo) {
		this.hostIpmiInfo = hostIpmiInfo;
	}	
	public CPUMonitorDetailBean getCpuMonitorDetailBean() {
		return cpuMonitorDetailBean;
	}
	public void setCpuMonitorDetailBean(CPUMonitorDetailBean cpuMonitorDetailBean) {
		this.cpuMonitorDetailBean = cpuMonitorDetailBean;
	}
	public IPMIConfig getIpmiConfig() {
		return ipmiConfig;
	}
	public void setIpmiConfig(IPMIConfig ipmiConfig) {
		this.ipmiConfig = ipmiConfig;
	}
	public NodeIsolationConfig getNodeIsolationConfig() {
		return nodeIsolationConfig;
	}
	public void setNodeIsolationConfig(NodeIsolationConfig nodeIsolationConfig) {
		this.nodeIsolationConfig = nodeIsolationConfig;
	}
	public IOPSMonitorDetailBean getIopsMonitorDetailBean() {
		return iopsMonitorDetailBean;
	}
	public void setIopsMonitorDetailBean(IOPSMonitorDetailBean iopsMonitorDetailBean) {
		this.iopsMonitorDetailBean = iopsMonitorDetailBean;
	}
	public NETMonitorDetailBean getNetMonitorDetailBean() {
		return netMonitorDetailBean;
	}
	public void setNetMonitorDetailBean(NETMonitorDetailBean netMonitorDetailBean) {
		this.netMonitorDetailBean = netMonitorDetailBean;
	}
	public int getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}
		
}

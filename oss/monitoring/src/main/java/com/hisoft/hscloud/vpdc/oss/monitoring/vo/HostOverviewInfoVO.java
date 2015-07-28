package com.hisoft.hscloud.vpdc.oss.monitoring.vo;

import java.util.List;

/**
 * 
* @description 节点概述数据集
* @version 1.3
* @author ljg
* @update 2012-9-21 下午3:54:57
 */
 public class HostOverviewInfoVO {

	 private String hostId;
	 private String hostName;
	 private String cpuType;
	 private int cpuCore;
	 private double memory;
	 private double disk;
	 private String ipInner;
	 private String ipOuter;
	 private int cpuRate;
	 private int memoryRate;
	 private int diskRate;	 
	 private double cpuUsage;
	 private double memoryUsage;
	 private List<DiskDetailVO> diskDetail;
	 private List<NetworkDetailVO> networkDetail;
	 private int virtualCPUUsed;
	 private int virtualCPUApply;
	 private int virtualCPUTotal;
	 private int virtualMemoryUsed;
	 private int virtualMemoryApply;
	 private int virtualMemoryTotal;
	 private int virtualDiskUsed;
	 private int virtualDiskApply;
	 private int virtualDiskTotal;
	 private long zoneId;
	 private String zoneName;
	 private String zoneCode;
	 
	
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
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
	
	public String getIpInner() {
		return ipInner;
	}
	public void setIpInner(String ipInner) {
		this.ipInner = ipInner;
	}
	public String getIpOuter() {
		return ipOuter;
	}
	public void setIpOuter(String ipOuter) {
		this.ipOuter = ipOuter;
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
	
	public List<DiskDetailVO> getDiskDetail() {
		return diskDetail;
	}
	public void setDiskDetail(List<DiskDetailVO> diskDetail) {
		this.diskDetail = diskDetail;
	}
	public List<NetworkDetailVO> getNetworkDetail() {
		return networkDetail;
	}
	public void setNetworkDetail(List<NetworkDetailVO> networkDetail) {
		this.networkDetail = networkDetail;
	}
	public int getVirtualCPUUsed() {
		return virtualCPUUsed;
	}
	public void setVirtualCPUUsed(int virtualCPUUsed) {
		this.virtualCPUUsed = virtualCPUUsed;
	}
	public int getVirtualCPUApply() {
		return virtualCPUApply;
	}
	public void setVirtualCPUApply(int virtualCPUApply) {
		this.virtualCPUApply = virtualCPUApply;
	}
	public int getVirtualCPUTotal() {
		return virtualCPUTotal;
	}
	public void setVirtualCPUTotal(int virtualCPUTotal) {
		this.virtualCPUTotal = virtualCPUTotal;
	}
	public int getVirtualMemoryUsed() {
		return virtualMemoryUsed;
	}
	public void setVirtualMemoryUsed(int virtualMemoryUsed) {
		this.virtualMemoryUsed = virtualMemoryUsed;
	}
	public int getVirtualMemoryApply() {
		return virtualMemoryApply;
	}
	public void setVirtualMemoryApply(int virtualMemoryApply) {
		this.virtualMemoryApply = virtualMemoryApply;
	}
	public int getVirtualMemoryTotal() {
		return virtualMemoryTotal;
	}
	public void setVirtualMemoryTotal(int virtualMemoryTotal) {
		this.virtualMemoryTotal = virtualMemoryTotal;
	}
	public int getVirtualDiskUsed() {
		return virtualDiskUsed;
	}
	public void setVirtualDiskUsed(int virtualDiskUsed) {
		this.virtualDiskUsed = virtualDiskUsed;
	}
	public int getVirtualDiskApply() {
		return virtualDiskApply;
	}
	public void setVirtualDiskApply(int virtualDiskApply) {
		this.virtualDiskApply = virtualDiskApply;
	}
	public int getVirtualDiskTotal() {
		return virtualDiskTotal;
	}
	public void setVirtualDiskTotal(int virtualDiskTotal) {
		this.virtualDiskTotal = virtualDiskTotal;
	}
	public long getZoneId() {
		return zoneId;
	}
	public void setZoneId(long zoneId) {
		this.zoneId = zoneId;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	@Override
	public String toString() {
		return "HostOverviewInfoVO [hostId=" + hostId
				+ ", hostName=" +hostName
				+ ", cpuType=" +cpuType
				+ ", cpuCore=" +cpuCore
				+ ", memory=" +memory
				+ ", disk=" +disk
				+ ", ipInner=" +ipInner
				+ ", ipOuter=" +ipOuter
				+ ", cpuRate=" +cpuRate
				+ ", memoryRate=" +memoryRate
				+ ", diskRate=" +diskRate
				+ ", cpuUsage=" +cpuUsage
				+ ", memoryUsage=" +memoryUsage
				+ ", diskDetail=" +diskDetail
				+ ", networkDetail=" +networkDetail
				+ ", virtualCPUUsed=" +virtualCPUUsed
				+ ", virtualCPUApply=" +virtualCPUApply
				+ ", virtualCPUTotal=" +virtualCPUTotal
				+ ", virtualMemoryUsed=" +virtualMemoryUsed
				+ ", virtualMemoryApply=" +virtualMemoryApply
				+ ", virtualMemoryTotal=" +virtualMemoryTotal
				+ ", virtualDiskUsed=" +virtualDiskUsed
				+ ", virtualDiskApply=" +virtualDiskApply
				+ ", virtualDiskTotal=" +virtualDiskTotal	
				+ ", zoneId=" +zoneId
				+ ", zoneName=" +zoneName
				+ ", zoneCode=" +zoneCode
				+"]";
	}
}

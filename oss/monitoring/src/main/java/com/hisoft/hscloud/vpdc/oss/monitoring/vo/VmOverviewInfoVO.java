package com.hisoft.hscloud.vpdc.oss.monitoring.vo;

import java.util.List;

/**
 * 
* @description 虚拟机概述数据集
* @version 1.3
* @author ljg
* @update 2012-9-21 下午3:56:20
 */
public class VmOverviewInfoVO {

	private String vmId;
	private String vmName;
	private String cpuType;
	private int cpuCore;
	private double memory;
	private double disk;
	private String ipInner;
	private String ipOuter;
	private String image;
	private String catalog;
	private double cpuUsage;
	private double memoryUsage;
	private List<DiskDetailVO> diskDetail;
	private List<NetworkDetailVO> networkDetail;

	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
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

	@Override
	public String toString() {
		return "VmOverviewInfoVO [vmId=" + vmId
				+ ", vmName=" +vmName
				+ ", cpuType=" +cpuType
				+ ", cpuCore=" +cpuCore
				+ ", memory=" +memory
				+ ", disk=" +disk
				+ ", ipInner=" +ipInner
				+ ", ipOuter=" +ipOuter
				+ ", image=" +image
				+ ", catalog=" +catalog				
				+ ", cpuUsage=" +cpuUsage
				+ ", memoryUsage=" +memoryUsage
				+ ", diskDetail=" +diskDetail
				+ ", networkDetail=" +networkDetail							
				+"]";
	}
}

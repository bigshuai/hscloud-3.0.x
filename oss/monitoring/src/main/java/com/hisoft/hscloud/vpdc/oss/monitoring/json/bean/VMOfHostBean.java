package com.hisoft.hscloud.vpdc.oss.monitoring.json.bean;
 public class VMOfHostBean {

	 private String hostname;
	 private String hostStatus;
	 private int physicalCpus;
	 private double physicalMemory;
	 private double physicalDisk;
	 private int usedVcpus;//主机
	 private double usedVmemory;//主机
	 private double usedVdisk;//主机
	 
	 private String vmUUID;
	 private int vcpus;//单个vm
	 private double vmemory;//单个vm
	 private double vdisk;//单个vm
	 private String vmStatus;
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getHostStatus() {
		return hostStatus;
	}
	public void setHostStatus(String hostStatus) {
		this.hostStatus = hostStatus;
	}
	public int getPhysicalCpus() {
		return physicalCpus;
	}
	public void setPhysicalCpus(int physicalCpus) {
		this.physicalCpus = physicalCpus;
	}
	public double getPhysicalMemory() {
		return physicalMemory;
	}
	public void setPhysicalMemory(double physicalMemory) {
		this.physicalMemory = physicalMemory;
	}
	public double getPhysicalDisk() {
		return physicalDisk;
	}
	public void setPhysicalDisk(double physicalDisk) {
		this.physicalDisk = physicalDisk;
	}
	public int getUsedVcpus() {
		return usedVcpus;
	}
	public void setUsedVcpus(int usedVcpus) {
		this.usedVcpus = usedVcpus;
	}
	public double getUsedVmemory() {
		return usedVmemory;
	}
	public void setUsedVmemory(double usedVmemory) {
		this.usedVmemory = usedVmemory;
	}
	public double getUsedVdisk() {
		return usedVdisk;
	}
	public void setUsedVdisk(double usedVdisk) {
		this.usedVdisk = usedVdisk;
	}
	public String getVmUUID() {
		return vmUUID;
	}
	public void setVmUUID(String vmUUID) {
		this.vmUUID = vmUUID;
	}
	public int getVcpus() {
		return vcpus;
	}
	public void setVcpus(int vcpus) {
		this.vcpus = vcpus;
	}
	public double getVmemory() {
		return vmemory;
	}
	public void setVmemory(double vmemory) {
		this.vmemory = vmemory;
	}
	public double getVdisk() {
		return vdisk;
	}
	public void setVdisk(double vdisk) {
		this.vdisk = vdisk;
	}
	public String getVmStatus() {
		return vmStatus;
	}
	public void setVmStatus(String vmStatus) {
		this.vmStatus = vmStatus;
	}
	
	public String toString() {
		return "VMOfHostBean [hostname=" + hostname + ", hostStatus=" + hostStatus + ", physicalCpus=" + physicalCpus
				+ ", physicalMemory=" + physicalMemory + ", physicalDisk=" + physicalDisk 
				+ ", usedVcpus=" + usedVcpus + ", usedVmemory=" + usedVmemory + ", usedVdisk=" + usedVdisk 
				+ ", vmUUID=" + vmUUID + ", vcpus=" + vcpus + ", vmemory=" + vmemory + ", vdisk=" + vdisk 
				+ ", vmStatus=" + vmStatus + "]";
	}
}

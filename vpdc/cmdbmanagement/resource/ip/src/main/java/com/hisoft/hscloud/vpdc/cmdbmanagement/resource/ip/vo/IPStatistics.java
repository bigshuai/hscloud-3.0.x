package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo;

public class IPStatistics {
	private int freeIPs;//空闲ip数
	private int assigningIPs;//待分配ip数
	private int assignedIPs;//已分配ip数
	private int disabledIPs;//已禁用ip数
	private int releasingIPs;//待释放ip数
	public IPStatistics(){
		
	}
	public IPStatistics(int freeIPs,int assigningIPs,int assignedIPs,int disabledIPs,int releasingIPs){
		this.freeIPs = freeIPs;
		this.assigningIPs = assigningIPs;
		this.assignedIPs = assignedIPs;
		this.disabledIPs = disabledIPs;
		this.releasingIPs = releasingIPs;
	}
	public int getFreeIPs() {
		return freeIPs;
	}
	public void setFreeIPs(int freeIPs) {
		this.freeIPs = freeIPs;
	}	
	public int getAssigningIPs() {
		return assigningIPs;
	}
	public void setAssigningIPs(int assigningIPs) {
		this.assigningIPs = assigningIPs;
	}
	public int getAssignedIPs() {
		return assignedIPs;
	}
	public void setAssignedIPs(int assignedIPs) {
		this.assignedIPs = assignedIPs;
	}
	public int getDisabledIPs() {
		return disabledIPs;
	}
	public void setDisabledIPs(int disabledIPs) {
		this.disabledIPs = disabledIPs;
	}
	public int getReleasingIPs() {
		return releasingIPs;
	}
	public void setReleasingIPs(int releasingIPs) {
		this.releasingIPs = releasingIPs;
	}
}

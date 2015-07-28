package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo;

import java.util.Date;

public class IPRangeVO {
	
	private long id;	
	private long startIP;	
	private long endIP;	
	private Date createTime;	
	private long createUid;
	private int totalIPs;//ip总数
	private int usedIPs;//已使用ip数	
	private int assignedIPs;//已分配ip数
	private int freeIPs;
	private String remark;
	private String gateWay;
	private long zoneId;//域Id
	private String zoneCode;//域code
	private String zoneName;//域名称
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getStartIP() {
		return startIP;
	}
	public void setStartIP(long startIP) {
		this.startIP = startIP;
	}
	
	public long getEndIP() {
		return endIP;
	}
	public void setEndIP(long endIP) {
		this.endIP = endIP;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public long getCreateUid() {
		return createUid;
	}
	public void setCreateUid(long createUid) {
		this.createUid = createUid;
	}
	public int getTotalIPs() {
		return totalIPs;
	}
	public void setTotalIPs(int totalIPs) {
		this.totalIPs = totalIPs;
	}
	public int getUsedIPs() {
		return usedIPs;
	}
	public void setUsedIPs(int usedIPs) {
		this.usedIPs = usedIPs;
	}	
	public int getAssignedIPs() {
		return assignedIPs;
	}
	public void setAssignedIPs(int assignedIPs) {
		this.assignedIPs = assignedIPs;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public long getZoneId() {
		return zoneId;
	}
	public void setZoneId(long zoneId) {
		this.zoneId = zoneId;
	}
	public String getGateWay() {
		return gateWay;
	}
	public void setGateWay(String gateWay) {
		this.gateWay = gateWay;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public int getFreeIPs() {
        return freeIPs;
    }
    public void setFreeIPs(int freeIPs) {
        this.freeIPs = freeIPs;
    }
    @Override
	public String toString() {
		return "IPRangeVO [id=" + id 
				+ ", startIP=" + startIP 
				+ ", endIP=" + endIP
				+ ", createTime=" + createTime 
				+ ", createUid=" + createUid
				+ ", totalIPs=" + totalIPs 
				+ ", usedIPs=" + usedIPs 
				+ ", assignedIPs=" + assignedIPs 
				+ ", remark=" + remark 
				+ ", zoneId=" + zoneId
				+ ", gateWay=" + gateWay
				+ ", zoneCode=" + zoneCode
				+ ", zoneName=" + zoneName
				+"]";
	}
}

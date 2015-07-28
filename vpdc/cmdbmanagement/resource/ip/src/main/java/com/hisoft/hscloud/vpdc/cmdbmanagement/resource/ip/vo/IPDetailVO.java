package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.vo;

public class IPDetailVO {

	private long id;	
	private long ip;
	private String nodeName;//节点名称
	private String vmName;//虚拟机名称
	private String userName;//用户名称
	private int totalIPs;//ip总数
	private int freeIPs;//已使用ip数
	private int assigningIPs;//待分ip数
	private int assignedIPs;//已分配ip数
	private int disabledIPs;//已禁用ip数
	private int releasingIPs;//待释放ip数
	
	private int status;
	private String email;
	private String remark;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIp() {
		return ip;
	}
	public void setIp(long ip) {
		this.ip = ip;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getVmName() {
		return vmName;
	}
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getTotalIPs() {
		return totalIPs;
	}
	public void setTotalIPs(int totalIPs) {
		this.totalIPs = totalIPs;
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
    public int getReleasingIPs() {
        return releasingIPs;
    }
    public void setReleasingIPs(int releasingIPs) {
        this.releasingIPs = releasingIPs;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}

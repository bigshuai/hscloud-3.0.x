package com.hisoft.hscloud.bss.sla.om.util;

import java.util.Arrays;

import com.hisoft.hscloud.crm.usermanager.entity.User;

public class SubmitOrderData {

	private User user;
	private int vmNum;
	private int ipNum;
	private long zoneGroupId;
	private int buyPeriod;
	private String planId;
	private int[] serviceItemIds;
	private String extDiskExtend;
	private String[] vlanParams;// 按顺序为name,dns1,dns2,vlan的数量

	public SubmitOrderData() {

	}

	public SubmitOrderData(User user, int vmNum, int ipNum, long zoneGroupId, int buyPeriod, int[] serviceItemIds) {
		this.user = user;
		this.ipNum = ipNum;
		this.vmNum = vmNum;
		this.zoneGroupId = zoneGroupId;
		this.buyPeriod = buyPeriod;
		this.serviceItemIds = serviceItemIds;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getVmNum() {
		return vmNum;
	}

	public void setVmNum(int vmNum) {
		this.vmNum = vmNum;
	}

	public int getIpNum() {
		return ipNum;
	}

	public void setIpNum(int ipNum) {
		this.ipNum = ipNum;
	}

	public long getZoneGroupId() {
		return zoneGroupId;
	}

	public void setZoneGroupId(long zoneGroupId) {
		this.zoneGroupId = zoneGroupId;
	}

	public int getBuyPeriod() {
		return buyPeriod;
	}

	public void setBuyPeriod(int buyPeriod) {
		this.buyPeriod = buyPeriod;
	}

	public int[] getServiceItemIds() {
		return serviceItemIds;
	}

	public void setServiceItemIds(int[] serviceItemIds) {
		this.serviceItemIds = serviceItemIds;
	}

	public String getExtDiskExtend() {
		return extDiskExtend;
	}

	public void setExtDiskExtend(String extDisk) {
		this.extDiskExtend = extDisk;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String[] getVlanParams() {
		return vlanParams;
	}

	public void setVlanParams(String[] vlanParams) {
		this.vlanParams = vlanParams;
	}

	@Override
	public String toString() {
		return "SubmitOrderData [user=" + user + ", vmNum=" + vmNum + ", ipNum=" + ipNum + ", zoneGroupId="
				+ zoneGroupId + ", buyPeriod=" + buyPeriod + ", planId=" + planId + ", serviceItemIds="
				+ Arrays.toString(serviceItemIds) + ", extDiskExtend=" + extDiskExtend + ", vlanParams="
				+ Arrays.toString(vlanParams) + "]";
	}

}
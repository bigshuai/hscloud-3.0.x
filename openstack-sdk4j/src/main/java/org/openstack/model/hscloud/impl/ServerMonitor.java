package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("monitor")
public class ServerMonitor implements Serializable {

	@JsonProperty("cpu_rate")
	private float cpuRate;

	@JsonProperty("cpu_num")
	private int cpuNum;

	@JsonProperty("cpu_detail")
	private CPUMonitorDetail cpuDetail;

	@JsonProperty("disks")
	private List<DISKMonitor> diskList = new ArrayList<DISKMonitor>();

	@JsonProperty("nets")
	private List<NETMonitor> netList = new ArrayList<NETMonitor>();

	@JsonProperty("ram_rate")
	private float ramRate;

	@JsonProperty("ram_total")
	private int ramTotal;

	public float getRamRate() {
		return this.ramRate;
	}

	public int getRamTotal() {
		return this.ramTotal;
	}

	/**
	 * @return the diskList
	 */
	public List<DISKMonitor> getDiskList() {
		return diskList;
	}

	/**
	 * @param diskList
	 *            the diskList to set
	 */
	public void setDiskList(List<DISKMonitor> diskList) {
		this.diskList = diskList;
	}

	/**
	 * @return the netList
	 */
	public List<NETMonitor> getNetList() {
		return netList;
	}

	/**
	 * @param netList
	 *            the netList to set
	 */
	public void setNetList(List<NETMonitor> netList) {
		this.netList = netList;
	}

	public float getCpuRate() {
		return this.cpuRate;
	}

	public int getCpuNum() {
		return this.cpuNum;
	}

	/**
	 * @param cpuRate
	 *            the cpuRate to set
	 */
	public void setCpuRate(float cpuRate) {
		this.cpuRate = cpuRate;
	}

	/**
	 * @param cpuNum
	 *            the cpuNum to set
	 */
	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}

	/**
	 * @param ramRate
	 *            the ramRate to set
	 */
	public void setRamRate(float ramRate) {
		this.ramRate = ramRate;
	}

	/**
	 * @param ramTotal
	 *            the ramTotal to set
	 */
	public void setRamTotal(int ramTotal) {
		this.ramTotal = ramTotal;
	}

	public CPUMonitorDetail getCpuDetail() {
		return cpuDetail;
	}

	public void setCpuDetail(CPUMonitorDetail cpuDetail) {
		this.cpuDetail = cpuDetail;
	}

	@Override
	public String toString() {
		return "ServerMonitor [cpuRate=" + cpuRate + ", cpuNum=" + cpuNum
				+ ", diskList=" + diskList + ", netList=" + netList
				+ ", ramRate=" + ramRate + ", ramTotal=" + ramTotal + "]";
	}

}

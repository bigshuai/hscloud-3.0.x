package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.hscloud.ICPUMonitor;

@JsonRootName("cpu_monitor")
public class CPUMonitor implements Serializable, ICPUMonitor {

	@JsonProperty("cpu_rate")
	private float cpuRate;

	@JsonProperty("cpu_num")
	private int cpuNum;

	@JsonProperty("cpu_detail")
	private CPUMonitorDetail detail;

	@Override
	public float getCpuRate() {
		return this.cpuRate;
	}

	@Override
	public int getCpuNum() {
		return this.cpuNum;
	}

	public void setCpuRate(float cpuRate) {
		this.cpuRate = cpuRate;
	}

	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}

	public CPUMonitorDetail getDetail() {
		return detail;
	}

	public void setDetail(CPUMonitorDetail detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "CPUMonitor [cpuRate=" + cpuRate + ", cpuNum=" + cpuNum
				+ ", detail=" + detail + "]";
	}

}

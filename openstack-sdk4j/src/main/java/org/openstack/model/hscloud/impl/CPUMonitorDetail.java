package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class CPUMonitorDetail implements Serializable {

	@JsonProperty("load_avg")
	private CPULoadAvg loadAvg;

	@JsonProperty("cpu_rate_list")
	private List<CPUSingleDetail> cpuList = new ArrayList<CPUSingleDetail>();

	public CPULoadAvg getLoadAvg() {
		return loadAvg;
	}

	public void setLoadAvg(CPULoadAvg loadAvg) {
		this.loadAvg = loadAvg;
	}

	public List<CPUSingleDetail> getCpuList() {
		return cpuList;
	}

	public void setCpuList(List<CPUSingleDetail> cpuList) {
		this.cpuList = cpuList;
	}

	@Override
	public String toString() {
		return "CPUMonitorDetail [loadAvg=" + loadAvg + ", cpuList=" + cpuList
				+ "]";
	}

}

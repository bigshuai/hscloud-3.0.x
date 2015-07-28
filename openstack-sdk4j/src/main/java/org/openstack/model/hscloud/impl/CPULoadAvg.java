package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

public class CPULoadAvg implements Serializable {

	@JsonProperty("load_avg1")
	private float loadAvg1;

	@JsonProperty("load_avg5")
	private float loadAvg5;

	@JsonProperty("load_avg15")
	private float loadAvg15;

	public float getLoadAvg1() {
		return loadAvg1;
	}

	public void setLoadAvg1(float loadAvg1) {
		this.loadAvg1 = loadAvg1;
	}

	public float getLoadAvg5() {
		return loadAvg5;
	}

	public void setLoadAvg5(float loadAvg5) {
		this.loadAvg5 = loadAvg5;
	}

	public float getLoadAvg15() {
		return loadAvg15;
	}

	public void setLoadAvg15(float loadAvg15) {
		this.loadAvg15 = loadAvg15;
	}

	@Override
	public String toString() {
		return "CPULoadAvg [loadAvg1=" + loadAvg1 + ", loadAvg5=" + loadAvg5
				+ ", loadAvg15=" + loadAvg15 + "]";
	}

}

package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.hscloud.IRAMMonitor;

@JsonRootName("ram_monitor")
public class RAMMonitor implements Serializable, IRAMMonitor {

	@JsonProperty("ram_rate")
	private float ramRate;

	@JsonProperty("ram_total")
	private int ramTotal;

	@Override
	public float getRamRate() {
		return this.ramRate;
	}

	@Override
	public int getRamTotal() {
		return this.ramTotal;
	}

	public void setRamRate(float ramRate) {
		this.ramRate = ramRate;
	}

	public void setRamTotal(int ramTotal) {
		this.ramTotal = ramTotal;
	}

	@Override
	public String toString() {
		return "CLASSNAME:RAMMonitor, " + "ram rate:" + this.ramRate
				+ ", ram total:" + this.ramTotal;
	}

}

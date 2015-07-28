package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class CPUSingleDetail implements Serializable {

	@JsonProperty("cpu_us_rate")
	private float cpuUsRate;

	@JsonProperty("cpu_sys_rate")
	private float cpuSysRate;

	@JsonProperty("cpu_nice_rate")
	private float cpuNiceRate;

	@JsonProperty("cpu_idle_rate")
	private float cpuIdleRate;

	@JsonProperty("cpu_iowait_rate")
	private float cpuIowaitRate;

	@JsonProperty("cpu_irq_rate")
	private float cpuIrqRate;

	@JsonProperty("cpu_softirq_rate")
	private float cpuSoftIrqRate;

	public float getCpuUsRate() {
		return cpuUsRate;
	}

	public void setCpuUsRate(float cpuUsRate) {
		this.cpuUsRate = cpuUsRate;
	}

	public float getCpuSysRate() {
		return cpuSysRate;
	}

	public void setCpuSysRate(float cpuSysRate) {
		this.cpuSysRate = cpuSysRate;
	}

	public float getCpuNiceRate() {
		return cpuNiceRate;
	}

	public void setCpuNiceRate(float cpuNiceRate) {
		this.cpuNiceRate = cpuNiceRate;
	}

	public float getCpuIdleRate() {
		return cpuIdleRate;
	}

	public void setCpuIdleRate(float cpuIdleRate) {
		this.cpuIdleRate = cpuIdleRate;
	}

	public float getCpuIowaitRate() {
		return cpuIowaitRate;
	}

	public void setCpuIowaitRate(float cpuIowaitRate) {
		this.cpuIowaitRate = cpuIowaitRate;
	}

	public float getCpuIrqRate() {
		return cpuIrqRate;
	}

	public void setCpuIrqRate(float cpuIrqRate) {
		this.cpuIrqRate = cpuIrqRate;
	}

	public float getCpuSoftIrqRate() {
		return cpuSoftIrqRate;
	}

	public void setCpuSoftIrqRate(float cpuSoftIrqRate) {
		this.cpuSoftIrqRate = cpuSoftIrqRate;
	}

	@Override
	public String toString() {
		return "CPUSingleDetail [cpuUsRate=" + cpuUsRate + ", cpuSysRate="
				+ cpuSysRate + ", cpuNiceRate=" + cpuNiceRate
				+ ", cpuIdleRate=" + cpuIdleRate + ", cpuIowaitRate="
				+ cpuIowaitRate + ", cpuIrqRate=" + cpuIrqRate
				+ ", cpuSoftIrqRate=" + cpuSoftIrqRate + "]";
	}

}

package org.openstack.model.common;

import java.io.Serializable;

public class HostIpmiInfo implements Serializable {

	private int hostPower;

	private int cpuPower;

	private int memPower;

	private int hostTemp;

	private int cpuTemp;

	private int memTemp;

	public int getHostPower() {
		return hostPower;
	}

	public void setHostPower(int hostPower) {
		this.hostPower = hostPower;
	}

	public int getCpuPower() {
		return cpuPower;
	}

	public void setCpuPower(int cpuPower) {
		this.cpuPower = cpuPower;
	}

	public int getMemPower() {
		return memPower;
	}

	public void setMemPower(int memPower) {
		this.memPower = memPower;
	}

	public int getHostTemp() {
		return hostTemp;
	}

	public void setHostTemp(int hostTemp) {
		this.hostTemp = hostTemp;
	}

	public int getCpuTemp() {
		return cpuTemp;
	}

	public void setCpuTemp(int cpuTemp) {
		this.cpuTemp = cpuTemp;
	}

	public int getMemTemp() {
		return memTemp;
	}

	public void setMemTemp(int memTemp) {
		this.memTemp = memTemp;
	}

	@Override
	public String toString() {
		return "HostIpmiInfo [hostPower=" + hostPower + ", cpuPower="
				+ cpuPower + ", memPower=" + memPower + ", hostTemp="
				+ hostTemp + ", cpuTemp=" + cpuTemp + ", memTemp=" + memTemp
				+ "]";
	}

}

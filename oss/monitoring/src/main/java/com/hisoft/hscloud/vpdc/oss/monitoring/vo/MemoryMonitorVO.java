package com.hisoft.hscloud.vpdc.oss.monitoring.vo;


public class MemoryMonitorVO  {

	
	private float ramRate;
	
	private int ramTotal;
	
	public float getRamRate() {
		return this.ramRate;
	}
	
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
		return "MemoryMonitorVO [" + "ramRate=" + this.ramRate
				+ ", ramTotal=" + this.ramTotal+"]";
	}

}

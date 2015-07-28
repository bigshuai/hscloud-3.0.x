package com.hisoft.hscloud.vpdc.oss.monitoring.vo;


public class CPUMonitorVO  {

	
	private float cpuRate;	
	private int cpuNum;	
	
	public float getCpuRate() {
		return this.cpuRate;
	}
	
	public int getCpuNum() {
		return this.cpuNum;
	}
	
	public void setCpuRate(float cpuRate) {
		this.cpuRate = cpuRate;
	}

	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}

	@Override
	public String toString() {
		return "CPUMonitorVO [" + "cpuRate=" + this.cpuRate + ", cpuNum=" + this.cpuNum+"]";
	}
	
	

}

package com.hisoft.hscloud.vpdc.oss.monitoring.vo;

import java.util.Date;

public class CPUHistoryVO  {

	private long timestamp;	
	private int cpuNum;	
	private float cpuRate;

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the cpuNum
	 */
	public int getCpuNum() {
		return cpuNum;
	}

	/**
	 * @param cpuNum
	 *            the cpuNum to set
	 */
	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}

	/**
	 * @return the cpuRate
	 */
	public float getCpuRate() {
		return cpuRate;
	}

	/**
	 * @param cpuRate
	 *            the cpuRate to set
	 */
	public void setCpuRate(float cpuRate) {
		this.cpuRate = cpuRate;
	}

	@Override
	public String toString() {
		return "CPUHistoryVO [" + "cpuRate=" + this.cpuRate
				+ " ,cpuNum=" + this.cpuNum + " ,timestamp="
				+ new Date(timestamp)+"]";
	}

}

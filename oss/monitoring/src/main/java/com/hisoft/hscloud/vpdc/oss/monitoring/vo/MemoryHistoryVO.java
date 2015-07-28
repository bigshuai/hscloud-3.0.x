package com.hisoft.hscloud.vpdc.oss.monitoring.vo;

import java.util.Date;


public class MemoryHistoryVO  {

	private long timestamp;

	
	private float ramRate;

	
	private int ramTotal;

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
	 * @return the ramRate
	 */
	public float getRamRate() {
		return ramRate;
	}

	/**
	 * @param ramRate
	 *            the ramRate to set
	 */
	public void setRamRate(float ramRate) {
		this.ramRate = ramRate;
	}

	/**
	 * @return the ramTotal
	 */
	public int getRamTotal() {
		return ramTotal;
	}

	/**
	 * @param ramTotal
	 *            the ramTotal to set
	 */
	public void setRamTotal(int ramTotal) {
		this.ramTotal = ramTotal;
	}

	@Override
	public String toString() {
		return "MemoryHistoryVO [" + "ramTotal=" + this.ramTotal
				+ ",ramRate=" + this.ramRate + ",timestamp="
				+ new Date(timestamp)+"]";
	}

}

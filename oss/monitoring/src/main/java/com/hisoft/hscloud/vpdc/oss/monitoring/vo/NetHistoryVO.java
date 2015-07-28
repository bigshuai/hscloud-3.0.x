package com.hisoft.hscloud.vpdc.oss.monitoring.vo;

import java.util.Date;


public class NetHistoryVO  {

	private long timestamp;

	
	private float rxSpeed;

	
	private float txSpeed;

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
	 * @return the rxSpeed
	 */
	public float getRxSpeed() {
		return rxSpeed;
	}

	/**
	 * @param rxSpeed
	 *            the rxSpeed to set
	 */
	public void setRxSpeed(float rxSpeed) {
		this.rxSpeed = rxSpeed;
	}

	/**
	 * @return the txSpeed
	 */
	public float getTxSpeed() {
		return txSpeed;
	}

	/**
	 * @param txSpeed
	 *            the txSpeed to set
	 */
	public void setTxSpeed(float txSpeed) {
		this.txSpeed = txSpeed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NetHistoryVO [" + ",rxSpeed=" + this.rxSpeed
				+ ",txSpeed=" + this.txSpeed + "timestamp="
				+ new Date(timestamp)+"]";
	}

}

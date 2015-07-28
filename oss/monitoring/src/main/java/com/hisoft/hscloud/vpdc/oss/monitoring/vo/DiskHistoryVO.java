package com.hisoft.hscloud.vpdc.oss.monitoring.vo;

import java.util.Date;


public class DiskHistoryVO  {

	private long timestamp;	
	private float diskTotal;	
	private float readSpeed;	
	private float writeSpeed;

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
	 * @return the diskTotal
	 */
	public float getDiskTotal() {
		return diskTotal;
	}

	/**
	 * @param diskTotal
	 *            the diskTotal to set
	 */
	public void setDiskTotal(float diskTotal) {
		this.diskTotal = diskTotal;
	}

	/**
	 * @return the readSpeed
	 */
	public float getReadSpeed() {
		return readSpeed;
	}

	/**
	 * @param readSpeed
	 *            the readSpeed to set
	 */
	public void setReadSpeed(float readSpeed) {
		this.readSpeed = readSpeed;
	}

	/**
	 * @return the writeSpeed
	 */
	public float getWriteSpeed() {
		return writeSpeed;
	}

	/**
	 * @param writeSpeed
	 *            the writeSpeed to set
	 */
	public void setWriteSpeed(float writeSpeed) {
		this.writeSpeed = writeSpeed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DiskHistoryVO [" + "diskTotal=" + this.diskTotal
				+ ",readSpeed=" + this.readSpeed + ",writeSpeed="
				+ this.writeSpeed + ",timestamp=" + new Date(timestamp)+"]";
	}

}

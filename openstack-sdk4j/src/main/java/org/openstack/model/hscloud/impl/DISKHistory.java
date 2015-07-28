package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("disk_history")
public class DISKHistory implements Serializable {

	private long timestamp;

	@JsonProperty("disktotal")
	private float diskTotal;

	@JsonProperty("readspeed")
	private float readSpeed;

	@JsonProperty("writespeed")
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
		return "CLASSNAME:DISKHistory, " + "disk total:" + this.diskTotal
				+ ", disk read speed:" + this.readSpeed + ", disk write speed:"
				+ this.writeSpeed + ", timestamp:" + new Date(timestamp);
	}

}

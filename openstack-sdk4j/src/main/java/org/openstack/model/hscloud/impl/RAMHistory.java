package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("ram_history")
public class RAMHistory implements Serializable {

	private long timestamp;

	@JsonProperty("ramrate")
	private float ramRate;

	@JsonProperty("ramtotal")
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
		return "CLASSNAME:RAMHistory, " + "ram total:" + this.ramTotal
				+ ", ram rate:" + this.ramRate + ", timestamp:"
				+ new Date(timestamp);
	}

}

package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("cpu_history")
public class CPUHistory implements Serializable {

	private long timestamp;

	@JsonProperty("cpunum")
	private int cpuNum;

	@JsonProperty("cpurate")
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
		return "CLASSNAME:CPUHistory, " + "cpu rate:" + this.cpuRate
				+ " ,cpu num:" + this.cpuNum + " ,timestamp:"
				+ new Date(timestamp * 1000);
	}

}

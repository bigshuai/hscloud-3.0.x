package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.hscloud.INETMonitor;

@JsonRootName("net_monitor")
public class NETMonitor implements Serializable, INETMonitor {

	@JsonProperty("rx_speed")
	private float rxSpeed;

	@JsonProperty("tx_speed")
	private float txSpeed;

	private String device;

	private String ip;

	/**
	 * @return the device
	 */
	public String getDevice() {
		return device;
	}

	/**
	 * @param device
	 *            the device to set
	 */
	public void setDevice(String device) {
		this.device = device;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public float getRxSpeed() {
		return this.rxSpeed;
	}

	@Override
	public float getTxSpeed() {
		return this.txSpeed;
	}

	public void setRxSpeed(float rxSpeed) {
		this.rxSpeed = rxSpeed;
	}

	public void setTxSpeed(float txSpeed) {
		this.txSpeed = txSpeed;
	}

	@Override
	public String toString() {
		return "NETMonitor [rxSpeed=" + rxSpeed + ", txSpeed=" + txSpeed
				+ ", device=" + device + ", ip=" + ip + "]";
	}

}

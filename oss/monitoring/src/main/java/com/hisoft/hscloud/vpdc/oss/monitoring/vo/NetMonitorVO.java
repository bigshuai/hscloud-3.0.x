package com.hisoft.hscloud.vpdc.oss.monitoring.vo;


public class NetMonitorVO  {

	
	private float rxSpeed;

	
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

	
	public float getRxSpeed() {
		return this.rxSpeed;
	}

	
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
		return "NetMonitorVO [" + "rxSpeed=" + this.rxSpeed
				+ ", txSpeed=" + this.txSpeed+"]";
	}

}

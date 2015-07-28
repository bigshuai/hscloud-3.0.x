package com.hisoft.hscloud.vpdc.oss.monitoring.vo;

public class DiskMonitorVO  {

	
	private float diskTotal;

	
	private float readSpeed;

	
	private float writeSpeed;

	private String device;

	
	private float diskRate;

	/**
	 * @return the diskRate
	 */
	public float getDiskRate() {
		return diskRate;
	}

	/**
	 * @param diskRate
	 *            the diskRate to set
	 */
	public void setDiskRate(float diskRate) {
		this.diskRate = diskRate;
	}

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

	
	public float getDiskTotal() {
		return diskTotal;
	}

	
	public float getReadSpeed() {
		return this.readSpeed;
	}

	
	public float getWriteSpeed() {
		return this.writeSpeed;
	}

	public void setDiskTotal(float diskTotal) {
		this.diskTotal = diskTotal;
	}

	public void setReadSpeed(float readSpeed) {
		this.readSpeed = readSpeed;
	}

	public void setWriteSpeed(float writeSpeed) {
		this.writeSpeed = writeSpeed;
	}

	@Override
	public String toString() {
		return "DiskMonitorVO [" + "total=" + this.diskTotal
				+ ", readSpeed=" + this.readSpeed + ", writeSpeed="
				+ this.writeSpeed+"]";
	}

}

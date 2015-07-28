package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.hscloud.IDISKMonitor;

@JsonRootName("disk_monitor")
public class DISKMonitor implements Serializable, IDISKMonitor {

	@JsonProperty("disk_total")
	private float diskTotal;

	@JsonProperty("read_speed")
	private float readSpeed;

	@JsonProperty("write_speed")
	private float writeSpeed;

	private String device;

	@JsonProperty("disk_rate")
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

	@Override
	public float getDiskTotal() {
		return diskTotal;
	}

	@Override
	public float getReadSpeed() {
		return this.readSpeed;
	}

	@Override
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
		return "DISKMonitor [diskTotal=" + diskTotal + ", readSpeed="
				+ readSpeed + ", writeSpeed=" + writeSpeed + ", device="
				+ device + ", diskRate=" + diskRate + "]";
	}

}

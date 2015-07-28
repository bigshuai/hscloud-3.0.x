package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class DISKMonitorList implements Serializable {

	@JsonProperty("disks")
	private List<DISKMonitor> list = new ArrayList<DISKMonitor>();

	@JsonProperty("device_iostat")
	private List<DeviceIOStat> deviceStat = new ArrayList<DeviceIOStat>();

	/**
	 * @return the list
	 */
	public List<DISKMonitor> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<DISKMonitor> list) {
		this.list = list;
	}

	public List<DeviceIOStat> getDeviceStat() {
		return deviceStat;
	}

	public void setDeviceStat(List<DeviceIOStat> deviceStat) {
		this.deviceStat = deviceStat;
	}

	@Override
	public String toString() {
		return "DISKMonitorList [list=" + list + ", deviceStat=" + deviceStat
				+ "]";
	}

}

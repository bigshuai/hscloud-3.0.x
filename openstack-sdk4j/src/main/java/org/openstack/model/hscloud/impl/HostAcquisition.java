package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("host")
public class HostAcquisition implements Serializable {
	private String hostname;

	@JsonProperty("physical_cpus")
	private int physicalCpus;

	@JsonProperty("used_vcpus")
	private int usedVcpus;

	@JsonProperty("physical_memory")
	private int physicalMemory;

	@JsonProperty("vg_total")
	private float vgTotal;

	@JsonProperty("vg_used")
	private float vgUsed;

	@JsonProperty("used_vmemory")
	private int usedVmemory;

	@JsonProperty("physical_disk")
	private int physicalDisk;

	@JsonProperty("used_vdisk")
	private int usedVdisk;

	@JsonProperty("host_status")
	private boolean hostStatus;

	@JsonProperty("servers")
	List<ServerAcquisition> list = new ArrayList<ServerAcquisition>();

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @param hostname
	 *            the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @return the physicalCpus
	 */
	public int getPhysicalCpus() {
		return physicalCpus;
	}

	/**
	 * @param physicalCpus
	 *            the physicalCpus to set
	 */
	public void setPhysicalCpus(int physicalCpus) {
		this.physicalCpus = physicalCpus;
	}

	/**
	 * @return the usedVcpus
	 */
	public int getUsedVcpus() {
		return usedVcpus;
	}

	/**
	 * @param usedVcpus
	 *            the usedVcpus to set
	 */
	public void setUsedVcpus(int usedVcpus) {
		this.usedVcpus = usedVcpus;
	}

	/**
	 * @return the physicalMemory
	 */
	public int getPhysicalMemory() {
		return physicalMemory;
	}

	/**
	 * @param physicalMemory
	 *            the physicalMemory to set
	 */
	public void setPhysicalMemory(int physicalMemory) {
		this.physicalMemory = physicalMemory;
	}

	/**
	 * @return the usedVmemory
	 */
	public int getUsedVmemory() {
		return usedVmemory;
	}

	/**
	 * @param usedVmemory
	 *            the usedVmemory to set
	 */
	public void setUsedVmemory(int usedVmemory) {
		this.usedVmemory = usedVmemory;
	}

	/**
	 * @return the physicalDisk
	 */
	public int getPhysicalDisk() {
		return physicalDisk;
	}

	/**
	 * @param physicalDisk
	 *            the physicalDisk to set
	 */
	public void setPhysicalDisk(int physicalDisk) {
		this.physicalDisk = physicalDisk;
	}

	/**
	 * @return the usedVdisk
	 */
	public int getUsedVdisk() {
		return usedVdisk;
	}

	/**
	 * @param usedVdisk
	 *            the usedVdisk to set
	 */
	public void setUsedVdisk(int usedVdisk) {
		this.usedVdisk = usedVdisk;
	}

	/**
	 * @return the hostStatus
	 */
	public boolean isHostStatus() {
		return hostStatus;
	}

	/**
	 * @param hostStatus
	 *            the hostStatus to set
	 */
	public void setHostStatus(boolean hostStatus) {
		this.hostStatus = hostStatus;
	}

	/**
	 * @return the list
	 */
	public List<ServerAcquisition> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<ServerAcquisition> list) {
		this.list = list;
	}

	public float getVgTotal() {
		return vgTotal;
	}

	public void setVgTotal(float vgTotal) {
		this.vgTotal = vgTotal;
	}

	public float getVgUsed() {
		return vgUsed;
	}

	public void setVgUsed(float vgUsed) {
		this.vgUsed = vgUsed;
	}

	@Override
	public String toString() {
		return "HostAcquisition [hostname=" + hostname + ", physicalCpus="
				+ physicalCpus + ", usedVcpus=" + usedVcpus
				+ ", physicalMemory=" + physicalMemory + ", vgTotal=" + vgTotal
				+ ", vgUsed=" + vgUsed + ", usedVmemory=" + usedVmemory
				+ ", physicalDisk=" + physicalDisk + ", usedVdisk=" + usedVdisk
				+ ", hostStatus=" + hostStatus + ", list=" + list + "]";
	}

}

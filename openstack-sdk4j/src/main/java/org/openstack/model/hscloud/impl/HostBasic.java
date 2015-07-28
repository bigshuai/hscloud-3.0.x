package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("host_basic")
public class HostBasic implements Serializable {

	private String hostname;

	private String ip;

	@JsonProperty("cpu_num")
	private int cpuNum;

	private int disk;

	private int ram;

	@JsonProperty("host_state")
	private boolean hostState;

	@JsonProperty("host_zone")
	private String availabilityZone;

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
	 * @return the disk
	 */
	public int getDisk() {
		return disk;
	}

	/**
	 * @param disk
	 *            the disk to set
	 */
	public void setDisk(int disk) {
		this.disk = disk;
	}

	/**
	 * @return the ram
	 */
	public int getRam() {
		return ram;
	}

	/**
	 * @param ram
	 *            the ram to set
	 */
	public void setRam(int ram) {
		this.ram = ram;
	}

	public boolean isHostState() {
		return hostState;
	}

	public void setHostState(boolean hostState) {
		this.hostState = hostState;
	}

	public String getAvailabilityZone() {
		return availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	@Override
	public String toString() {
		return "HostBasic [hostname=" + hostname + ", ip=" + ip + ", cpuNum="
				+ cpuNum + ", disk=" + disk + ", ram=" + ram + ", hostState="
				+ hostState + ", availabilityZone=" + availabilityZone + "]";
	}

}

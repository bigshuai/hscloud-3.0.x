package com.hisoft.hscloud.vpdc.oss.monitoring.vo;

import java.util.ArrayList;
import java.util.List;


public class HostAcquisitionVO  {
	private String hostname;	
	private int physicalCpus;

	private int usedVcpus;

	private int physicalMemory;

	private int usedVmemory;

	private int physicalDisk;

	private int usedVdisk;

	private boolean hostStatus;
	
	List<ServerAcquisitionVO> list = new ArrayList<ServerAcquisitionVO>();

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
	public List<ServerAcquisitionVO> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<ServerAcquisitionVO> list) {
		this.list = list;
	}

}

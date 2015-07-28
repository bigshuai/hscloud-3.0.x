package com.hisoft.hscloud.bss.sla.sc.vo;


public class HostBasicVO  {

	private String hostname;

	private String ip;
	
	private int cpuNum;

	private int disk;

	private int ram;

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

}

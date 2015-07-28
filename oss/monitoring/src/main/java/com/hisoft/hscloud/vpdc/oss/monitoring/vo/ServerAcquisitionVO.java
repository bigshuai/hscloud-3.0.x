package com.hisoft.hscloud.vpdc.oss.monitoring.vo;


public class ServerAcquisitionVO  {

	private int vcpus;

	private int vmemory;

	private int vdisk;

	private int vmStatus;

	private String vmUUID;

	/**
	 * @return the vcpus
	 */
	public int getVcpus() {
		return vcpus;
	}

	/**
	 * @param vcpus
	 *            the vcpus to set
	 */
	public void setVcpus(int vcpus) {
		this.vcpus = vcpus;
	}

	/**
	 * @return the vmemory
	 */
	public int getVmemory() {
		return vmemory;
	}

	/**
	 * @param vmemory
	 *            the vmemory to set
	 */
	public void setVmemory(int vmemory) {
		this.vmemory = vmemory;
	}

	/**
	 * @return the vdisk
	 */
	public int getVdisk() {
		return vdisk;
	}

	/**
	 * @param vdisk
	 *            the vdisk to set
	 */
	public void setVdisk(int vdisk) {
		this.vdisk = vdisk;
	}

	/**
	 * @return the vmStatus
	 */
	public int getVmStatus() {
		return vmStatus;
	}

	/**
	 * @param vmStatus
	 *            the vmStatus to set
	 */
	public void setVmStatus(int vmStatus) {
		this.vmStatus = vmStatus;
	}

	/**
	 * @return the vmUUID
	 */
	public String getVmUUID() {
		return vmUUID;
	}

	/**
	 * @param vmUUID
	 *            the vmUUID to set
	 */
	public void setVmUUID(String vmUUID) {
		this.vmUUID = vmUUID;
	}

}

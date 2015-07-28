package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("server")
public class ServerAcquisition implements Serializable {

	private int vcpus;

	private int vmemory;

	private int vdisk;

	@JsonProperty("vm_status")
	private int vmStatus;

	@JsonProperty("vm_uuid")
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

	@Override
	public String toString() {
		return "ServerAcquisition [vcpus=" + vcpus + ", vmemory=" + vmemory
				+ ", vdisk=" + vdisk + ", vmStatus=" + vmStatus + ", vmUUID="
				+ vmUUID + "]";
	}
	
}

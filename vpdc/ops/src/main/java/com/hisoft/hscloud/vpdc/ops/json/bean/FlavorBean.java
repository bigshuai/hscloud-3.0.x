package com.hisoft.hscloud.vpdc.ops.json.bean;


public class FlavorBean {

	private String vcpus;
	private String ram;
	private String disk;

	
	/**
	
	 * @return vcpus : return the property vcpus.
	 */
	public String getVcpus() {
		return vcpus;
	}


	/**
	 * @param vcpus : set the property vcpus.
	 */
	public void setVcpus(String vcpus) {
		this.vcpus = vcpus;
	}


	/**
	
	 * @return ram : return the property ram.
	 */
	public String getRam() {
		return ram;
	}


	/**
	 * @param ram : set the property ram.
	 */
	public void setRam(String ram) {
		this.ram = ram;
	}


	/**
	
	 * @return disk : return the property disk.
	 */
	public String getDisk() {
		return disk;
	}


	/**
	 * @param disk : set the property disk.
	 */
	public void setDisk(String disk) {
		this.disk = disk;
	}


	/* (非 Javadoc) 
	 * <p>Title: toString</p> 
	 * <p>Description: </p> 
	 * @return 
	 * @see java.lang.Object#toString() 
	 */
	@Override
	public String toString() {
		return "FlavorBean [vcpus=" + vcpus + ", vcpus=" + vcpus
				+ ", ram=" + ram + ", disk=" + disk
				+ "]";
	}
}

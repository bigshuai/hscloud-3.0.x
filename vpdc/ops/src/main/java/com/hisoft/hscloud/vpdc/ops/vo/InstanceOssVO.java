/**
 * @title InstanceOss.java
 * @package com.hisoft.hscloud.vpdc.ops.vo
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-4-11 上午11:36:09
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.vo;

/**
 * @description 虚拟机监控信息
 * @version 1.0
 * @author hongqin.li
 * @update 2012-4-11 上午11:36:09
 */
public class InstanceOssVO {
 
	private float ossCpu;//cpu的使用率
	private float ossMemory;//memory 的使用率
	private float ossDisk;
	private float ossNetwork;
	/**
	 * @return ossCpu : return the property ossCpu.
	 */
	public float getOssCpu() {
		return ossCpu;
	}
	/**
	 * @param ossCpu : set the property ossCpu.
	 */
	public void setOssCpu(float ossCpu) {
		this.ossCpu = ossCpu;
	}
	/**
	 * @return ossMemory : return the property ossMemory.
	 */
	public float getOssMemory() {
		return ossMemory;
	}
	/**
	 * @param ossMemory : set the property ossMemory.
	 */
	public void setOssMemory(float ossMemory) {
		this.ossMemory = ossMemory;
	}
	/**
	 * @return ossDisk : return the property ossDisk.
	 */
	public float getOssDisk() {
		return ossDisk;
	}
	/**
	 * @param ossDisk : set the property ossDisk.
	 */
	public void setOssDisk(float ossDisk) {
		this.ossDisk = ossDisk;
	}
	/**
	 * @return ossNetwork : return the property ossNetwork.
	 */
	public float getOssNetwork() {
		return ossNetwork;
	}
	/**
	 * @param ossNetwork : set the property ossNetwork.
	 */
	public void setOssNetwork(float ossNetwork) {
		this.ossNetwork = ossNetwork;
	}
	/* (非 Javadoc) 
	 * <p>Title: toString</p> 
	 * <p>Description: </p> 
	 * @return 
	 * @see java.lang.Object#toString() 
	 */
	@Override
	public String toString() {
		return "InstanceOssVO [ossCpu=" + ossCpu + ", ossMemory=" + ossMemory
				+ ", ossDisk=" + ossDisk + ", ossNetwork=" + ossNetwork + "]";
	}
	 
}

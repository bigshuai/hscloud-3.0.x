/**
 * @title InstanceAdminOssVO.java
 * @package com.hisoft.hscloud.vpdc.ops.vo
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-4-11 上午11:39:18
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.vo;

/**
 * @description 接点监控
 * @version 1.0
 * @author hongqin.li
 * @update 2012-4-11 上午11:39:18
 */
public class InstanceAdminOssVO {
	private int totalCpu;
	private double usedCpu;
	private double rateCpu;// cpu使用率

	private double totalMemory;
	private double usedMemory;
	private double rateMemory;

	private double totalDisk;
	private double usedDisk;
	private double rateDisk;
	private double freeDisk;
	private double receiveInmb;//
	private double receivekbSec;//每秒中接收数据
	/**
	 * @return totalCpu : return the property totalCpu.
	 */
	public int getTotalCpu() {
		return totalCpu;
	}
	/**
	 * @param totalCpu : set the property totalCpu.
	 */
	public void setTotalCpu(int totalCpu) {
		this.totalCpu = totalCpu;
	}
	/**
	 * @return usedCpu : return the property usedCpu.
	 */
	public double getUsedCpu() {
		return usedCpu;
	}
	/**
	 * @param usedCpu : set the property usedCpu.
	 */
	public void setUsedCpu(double usedCpu) {
		this.usedCpu = usedCpu;
	}
	/**
	 * @return rateCpu : return the property rateCpu.
	 */
	public double getRateCpu() {
		return rateCpu;
	}
	/**
	 * @param rateCpu : set the property rateCpu.
	 */
	public void setRateCpu(double rateCpu) {
		this.rateCpu = rateCpu;
	}
	/**
	 * @return totalMemory : return the property totalMemory.
	 */
	public double getTotalMemory() {
		return totalMemory;
	}
	/**
	 * @param totalMemory : set the property totalMemory.
	 */
	public void setTotalMemory(double totalMemory) {
		this.totalMemory = totalMemory;
	}
	/**
	 * @return usedMemory : return the property usedMemory.
	 */
	public double getUsedMemory() {
		return usedMemory;
	}
	/**
	 * @param usedMemory : set the property usedMemory.
	 */
	public void setUsedMemory(double usedMemory) {
		this.usedMemory = usedMemory;
	}
	/**
	 * @return rateMemory : return the property rateMemory.
	 */
	public double getRateMemory() {
		return rateMemory;
	}
	/**
	 * @param rateMemory : set the property rateMemory.
	 */
	public void setRateMemory(double rateMemory) {
		this.rateMemory = rateMemory;
	}
	/**
	 * @return totalDisk : return the property totalDisk.
	 */
	public double getTotalDisk() {
		return totalDisk;
	}
	/**
	 * @param totalDisk : set the property totalDisk.
	 */
	public void setTotalDisk(double totalDisk) {
		this.totalDisk = totalDisk;
	}
	/**
	 * @return usedDisk : return the property usedDisk.
	 */
	public double getUsedDisk() {
		return usedDisk;
	}
	/**
	 * @param usedDisk : set the property usedDisk.
	 */
	public void setUsedDisk(double usedDisk) {
		this.usedDisk = usedDisk;
	}
	/**
	 * @return rateDisk : return the property rateDisk.
	 */
	public double getRateDisk() {
		return rateDisk;
	}
	/**
	 * @param rateDisk : set the property rateDisk.
	 */
	public void setRateDisk(double rateDisk) {
		this.rateDisk = rateDisk;
	}
	/**
	 * @return freeDisk : return the property freeDisk.
	 */
	public double getFreeDisk() {
		return freeDisk;
	}
	/**
	 * @param freeDisk : set the property freeDisk.
	 */
	public void setFreeDisk(double freeDisk) {
		this.freeDisk = freeDisk;
	}
	/**
	 * @return receiveInmb : return the property receiveInmb.
	 */
	public double getReceiveInmb() {
		return receiveInmb;
	}
	/**
	 * @param receiveInmb : set the property receiveInmb.
	 */
	public void setReceiveInmb(double receiveInmb) {
		this.receiveInmb = receiveInmb;
	}
	/**
	 * @return receivekbSec : return the property receivekbSec.
	 */
	public double getReceivekbSec() {
		return receivekbSec;
	}
	/**
	 * @param receivekbSec : set the property receivekbSec.
	 */
	public void setReceivekbSec(double receivekbSec) {
		this.receivekbSec = receivekbSec;
	}

	@Override
	public String toString() {
		return "InstanceAdminOssVO [totalCpu=" + totalCpu + ", usedCpu="
				+ usedCpu + ", rateCpu=" + rateCpu + ", totalMemory="
				+ totalMemory + ", usedMemory=" + usedMemory + ", rateMemory="
				+ rateMemory + ", totalDisk=" + totalDisk + ", usedDisk="
				+ usedDisk + ", rateDisk=" + rateDisk + ", freeDisk="
				+ freeDisk + ", receiveInmb=" + receiveInmb + ", receivekbSec="
				+ receivekbSec + "]";
	}
}

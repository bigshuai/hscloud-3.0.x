package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class DeviceIOStat implements Serializable {

	/**
	 * 设备名
	 */
	@JsonProperty("device")
	private String device;

	/**
	 * 一秒中有百分之多少的时间用于 I/O 操作，或者说一秒中有多少时间 I/O 队列是非空的,已成100
	 */
	@JsonProperty("util")
	private String util;

	/**
	 * 每秒写K字节数
	 */
	@JsonProperty("wkb")
	private String writeKB;
 
	/**
	 * 每秒读K字节数
	 */
	@JsonProperty("rkb")
	private String readKB;

	/**
	 * 平均每次设备I/O操作的等待时间 (毫秒)
	 */
	@JsonProperty("await")
	private String await;

	/**
	 * 平均I/O队列长度。
	 */
	@JsonProperty("avgqu-sz")
	private String avgqu;

	@JsonProperty("r_await")
	private String rAwait;

	/**
	 * 平均每次设备I/O操作的服务 时间 (毫秒)
	 */
	@JsonProperty("svctm")
	private String svctm;

	/**
	 * 每秒进行 merge 的写操作数目。
	 */
	@JsonProperty("wrqm")
	private String wrqm;
	
	/**
	 * 平均每次设备I/O操作的数据 大小
	 */
	@JsonProperty("avgrq-sz")
	private String avgrq;

	/**
	 * 每秒进行 merge 的读操作数目
	 */
	@JsonProperty("rrqm")
	private String rrqm;

	@JsonProperty("w_await")
	private String wAwait;

	/**
	 * 每秒完成的写 I/O 设备次数
	 */
	@JsonProperty("w")
	private String wTimes;

	/**
	 * 每秒完成的读 I/O 设备次数
	 */
	@JsonProperty("r")
	private String rTimes;

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getUtil() {
		return util;
	}

	public void setUtil(String util) {
		this.util = util;
	}

	public String getWriteKB() {
		return writeKB;
	}

	public void setWriteKB(String writeKB) {
		this.writeKB = writeKB;
	}

	public String getReadKB() {
		return readKB;
	}

	public void setReadKB(String readKB) {
		this.readKB = readKB;
	}

	public String getAwait() {
		return await;
	}

	public void setAwait(String await) {
		this.await = await;
	}

	public String getAvgqu() {
		return avgqu;
	}

	public void setAvgqu(String avgqu) {
		this.avgqu = avgqu;
	}

	public String getrAwait() {
		return rAwait;
	}

	public void setrAwait(String rAwait) {
		this.rAwait = rAwait;
	}

	public String getSvctm() {
		return svctm;
	}

	public void setSvctm(String svctm) {
		this.svctm = svctm;
	}

	public String getWrqm() {
		return wrqm;
	}

	public void setWrqm(String wrqm) {
		this.wrqm = wrqm;
	}

	public String getAvgrq() {
		return avgrq;
	}

	public void setAvgrq(String avgrq) {
		this.avgrq = avgrq;
	}

	public String getRrqm() {
		return rrqm;
	}

	public void setRrqm(String rrqm) {
		this.rrqm = rrqm;
	}

	public String getwAwait() {
		return wAwait;
	}

	public void setwAwait(String wAwait) {
		this.wAwait = wAwait;
	}

	public String getwTimes() {
		return wTimes;
	}

	public void setwTimes(String wTimes) {
		this.wTimes = wTimes;
	}

	public String getrTimes() {
		return rTimes;
	}

	public void setrTimes(String rTimes) {
		this.rTimes = rTimes;
	}

	@Override
	public String toString() {
		return "DeviceIOStat [device=" + device + ", util=" + util
				+ ", writeKB=" + writeKB + ", readKB=" + readKB + ", await="
				+ await + ", avgqu=" + avgqu + ", rAwait=" + rAwait
				+ ", svctm=" + svctm + ", wrqm=" + wrqm + ", avgrq=" + avgrq
				+ ", rrqm=" + rrqm + ", wAwait=" + wAwait + ", wTimes="
				+ wTimes + ", rTimes=" + rTimes + "]";
	}

}

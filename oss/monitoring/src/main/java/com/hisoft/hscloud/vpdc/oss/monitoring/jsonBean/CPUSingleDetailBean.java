/* 
* 文 件 名:  CPUSingleDetailbean.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-5-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class CPUSingleDetailBean {

	private Float cpuUsRate;
	private Float cpuSysRate;
	private Float cpuNiceRate;
	private Float cpuIdleRate;
	private Float cpuIowaitRate;
	private Float cpuIrqRate;
	private Float cpuSoftIrqRate;
	public Float getCpuUsRate() {
		return cpuUsRate;
	}
	public void setCpuUsRate(Float cpuUsRate) {
		this.cpuUsRate = cpuUsRate;
	}
	public Float getCpuSysRate() {
		return cpuSysRate;
	}
	public void setCpuSysRate(Float cpuSysRate) {
		this.cpuSysRate = cpuSysRate;
	}
	public Float getCpuNiceRate() {
		return cpuNiceRate;
	}
	public void setCpuNiceRate(Float cpuNiceRate) {
		this.cpuNiceRate = cpuNiceRate;
	}
	public Float getCpuIdleRate() {
		return cpuIdleRate;
	}
	public void setCpuIdleRate(Float cpuIdleRate) {
		this.cpuIdleRate = cpuIdleRate;
	}
	public Float getCpuIowaitRate() {
		return cpuIowaitRate;
	}
	public void setCpuIowaitRate(Float cpuIowaitRate) {
		this.cpuIowaitRate = cpuIowaitRate;
	}
	public Float getCpuIrqRate() {
		return cpuIrqRate;
	}
	public void setCpuIrqRate(Float cpuIrqRate) {
		this.cpuIrqRate = cpuIrqRate;
	}
	public Float getCpuSoftIrqRate() {
		return cpuSoftIrqRate;
	}
	public void setCpuSoftIrqRate(Float cpuSoftIrqRate) {
		this.cpuSoftIrqRate = cpuSoftIrqRate;
	}	
}

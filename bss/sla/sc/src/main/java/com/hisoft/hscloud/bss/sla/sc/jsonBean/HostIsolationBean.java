/* 
* 文 件 名:  HostIsolationBean.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-7-30 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.jsonBean; 

import net.sf.json.JSONObject;

/** 
 * <资源隔离参数设置> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-7-30] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class HostIsolationBean {

	private String hostName;
	private Integer cpuRatio = 1;
	private Integer memoryRatio = 1;
	private Integer diskRatio = 1;
	private Integer hostNumber = 0;
	private Integer iopsRead = 0;
	private Integer iopsWrite = 0;
	private Integer cpuWorkload = 0;
	private Integer rxSpeed = 0;
	private Integer txSpeed = 0;
	private Integer limitStorageLeft = 300;
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public Integer getCpuRatio() {
		return cpuRatio;
	}
	public void setCpuRatio(Integer cpuRatio) {
		this.cpuRatio = cpuRatio;
	}
	public Integer getMemoryRatio() {
		return memoryRatio;
	}
	public void setMemoryRatio(Integer memoryRatio) {
		this.memoryRatio = memoryRatio;
	}
	public Integer getDiskRatio() {
		return diskRatio;
	}
	public void setDiskRatio(Integer diskRatio) {
		this.diskRatio = diskRatio;
	}
	public Integer getHostNumber() {
		return hostNumber;
	}
	public void setHostNumber(Integer hostNumber) {
		this.hostNumber = hostNumber;
	}	
	public Integer getIopsRead() {
		return iopsRead;
	}
	public void setIopsRead(Integer iopsRead) {
		this.iopsRead = iopsRead;
	}
	public Integer getIopsWrite() {
		return iopsWrite;
	}
	public void setIopsWrite(Integer iopsWrite) {
		this.iopsWrite = iopsWrite;
	}
	public Integer getCpuWorkload() {
		return cpuWorkload;
	}
	public void setCpuWorkload(Integer cpuWorkload) {
		this.cpuWorkload = cpuWorkload;
	}
	public Integer getRxSpeed() {
		return rxSpeed;
	}
	public void setRxSpeed(Integer rxSpeed) {
		this.rxSpeed = rxSpeed;
	}
	public Integer getTxSpeed() {
		return txSpeed;
	}
	public void setTxSpeed(Integer txSpeed) {
		this.txSpeed = txSpeed;
	}
	public Integer getLimitStorageLeft() {
		return limitStorageLeft;
	}
	public void setLimitStorageLeft(Integer limitStorageLeft) {
		this.limitStorageLeft = limitStorageLeft;
	}
	public String toJsonString() { 
		JSONObject object = JSONObject.fromObject(this);
		return object.toString();
	}	
}

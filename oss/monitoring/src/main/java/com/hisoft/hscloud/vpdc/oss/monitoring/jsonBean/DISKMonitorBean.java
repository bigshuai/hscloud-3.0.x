/* 
* 文 件 名:  DISKMonitorBean.java 
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
 * <磁盘读写监控信息> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class DISKMonitorBean {

	private Double diskTotal;//磁盘总大小
	private Double readSpeed;//磁盘读入速率
	private Double writeSpeed;//磁盘写入速率
	private String device;//磁盘名称
	private Double ROPS;//
	private Double WOPS;//
	private Double diskUsage;//磁盘使用率
	public Double getDiskTotal() {
		return diskTotal;
	}
	public void setDiskTotal(Double diskTotal) {
		this.diskTotal = diskTotal;
	}
	public Double getReadSpeed() {
		return readSpeed;
	}
	public void setReadSpeed(Double readSpeed) {
		this.readSpeed = readSpeed;
	}
	public Double getWriteSpeed() {
		return writeSpeed;
	}
	public void setWriteSpeed(Double writeSpeed) {
		this.writeSpeed = writeSpeed;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public Double getROPS() {
		return ROPS;
	}
	public void setROPS(Double rOPS) {
		ROPS = rOPS;
	}
	public Double getWOPS() {
		return WOPS;
	}
	public void setWOPS(Double wOPS) {
		WOPS = wOPS;
	}
	public Double getDiskUsage() {
		return diskUsage;
	}
	public void setDiskUsage(Double diskUsage) {
		this.diskUsage = diskUsage;
	}
}

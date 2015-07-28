/* 
* 文 件 名:  VolumeVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2012-11-12 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.vo; 

/** 
 * <扩展盘信息> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2012-11-12] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class VolumeVO {

	int id;//扩展盘id
	String status;//扩展盘状态
	int size;//扩展盘大小
	String name;//扩展盘名称
	String vmId;//扩展盘对应虚拟机ID
	String device;//扩展盘对应分区
	int mode;//扩展盘挂载方式：//0:无-1：isics方式扩展盘-2：SCIS方式扩展盘
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVmId() {
		return vmId;
	}
	public void setVmId(String vmId) {
		this.vmId = vmId;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
}

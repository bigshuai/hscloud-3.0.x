/* 
* 文 件 名:  VpdcInstanceVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-6-7 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.vo; 

import java.util.Date;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-6-7] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class VpdcInstanceVO {

	 private long referenceId;
	 private String vmId;	 
	 private Date startTime;//虚拟机创建时间
	 private Date endTime;//虚拟机到期时间
	 private int isEnable;//0:正常；1：手动禁用；2：到期禁用
	 private long userId;//用户Id
	 private int vmType;//虚拟机类型（0：试用；1：正式）
	 private long spareTime;//剩余时长（毫秒）
	public long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}
	public String getVmId() {
		return vmId;
	}
	public void setVmId(String vmId) {
		this.vmId = vmId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getVmType() {
		return vmType;
	}
	public void setVmType(int vmType) {
		this.vmType = vmType;
	}
	public long getSpareTime() {
		return spareTime;
	}
	public void setSpareTime(long spareTime) {
		this.spareTime = spareTime;
	}
}

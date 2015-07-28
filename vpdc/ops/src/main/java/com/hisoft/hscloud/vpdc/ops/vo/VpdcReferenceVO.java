/* 
* 文 件 名:  VpdcReferenceVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-4-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.vo; 

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-4-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class VpdcReferenceVO {

	private String vmId;
	private String hostName;//节点名称
	private String vmStatus;//虚拟机状态
	private int businessType;//业务类型
	private String createrType;//创建类型
	private String osType;//镜像类型
	public String getVmId() {
		return vmId;
	}
	public void setVmId(String vmId) {
		this.vmId = vmId;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getVmStatus() {
		return vmStatus;
	}
	public void setVmStatus(String vmStatus) {
		this.vmStatus = vmStatus;
	}
	public int getBusinessType() {
		return businessType;
	}
	public void setBusinessType(int businessType) {
		this.businessType = businessType;
	}
	public String getCreaterType() {
		return createrType;
	}
	public void setCreaterType(String createrType) {
		this.createrType = createrType;
	}
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
	}
}

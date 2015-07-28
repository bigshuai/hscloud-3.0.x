/* 
* 文 件 名:  ZoneAcquisitionBean.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-5-8 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.jsonBean; 

/** 
 * <资源域监控信息> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ZoneAcquisitionBean {

	private String zoneCode;//资源域编码
	private Integer hostTotal;//节点总数
	private Integer hostActive;//节点活动数
	private Integer hostError;//节点错误数
	private Integer CPUTotal;//虚拟CPU总数
	private Integer CPUApply;//虚拟CPU申请数
	private Integer CPUUsed;//虚拟CPU使用数
	private Integer memoryTotal;//虚拟内存总大小
	private Integer memoryUsed;//虚拟内存使用大小
	private Integer memoryApply;//虚拟内存申请大小
	private Integer diskTotal;//虚拟磁盘总大小
	private Integer diskUsed;//虚拟磁盘使用大小
	private Integer diskApply;//虚拟磁盘申请大小
	private Double vgUsed;//扩展盘使用大小
	private Double vgTotal;//扩展盘总大小
	private Integer theoreticalValue;//理论上还能创建的虚拟机数
	private String theoreticalInfo;//理论上还能创建的虚拟机数信息
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public Integer getHostTotal() {
		return hostTotal;
	}
	public void setHostTotal(Integer hostTotal) {
		this.hostTotal = hostTotal;
	}
	public Integer getHostActive() {
		return hostActive;
	}
	public void setHostActive(Integer hostActive) {
		this.hostActive = hostActive;
	}
	public Integer getHostError() {
		return hostError;
	}
	public void setHostError(Integer hostError) {
		this.hostError = hostError;
	}
	public Integer getCPUTotal() {
		return CPUTotal;
	}
	public void setCPUTotal(Integer cPUTotal) {
		CPUTotal = cPUTotal;
	}
	public Integer getCPUApply() {
		return CPUApply;
	}
	public void setCPUApply(Integer cPUApply) {
		CPUApply = cPUApply;
	}
	public Integer getCPUUsed() {
		return CPUUsed;
	}
	public void setCPUUsed(Integer cPUUsed) {
		CPUUsed = cPUUsed;
	}
	public Integer getMemoryTotal() {
		return memoryTotal;
	}
	public void setMemoryTotal(Integer memoryTotal) {
		this.memoryTotal = memoryTotal;
	}
	public Integer getMemoryUsed() {
		return memoryUsed;
	}
	public void setMemoryUsed(Integer memoryUsed) {
		this.memoryUsed = memoryUsed;
	}
	public Integer getMemoryApply() {
		return memoryApply;
	}
	public void setMemoryApply(Integer memoryApply) {
		this.memoryApply = memoryApply;
	}
	public Integer getDiskTotal() {
		return diskTotal;
	}
	public void setDiskTotal(Integer diskTotal) {
		this.diskTotal = diskTotal;
	}
	public Integer getDiskUsed() {
		return diskUsed;
	}
	public void setDiskUsed(Integer diskUsed) {
		this.diskUsed = diskUsed;
	}
	public Integer getDiskApply() {
		return diskApply;
	}
	public void setDiskApply(Integer diskApply) {
		this.diskApply = diskApply;
	}
	public Double getVgUsed() {
		return vgUsed;
	}
	public void setVgUsed(Double vgUsed) {
		this.vgUsed = vgUsed;
	}
	public Double getVgTotal() {
		return vgTotal;
	}
	public void setVgTotal(Double vgTotal) {
		this.vgTotal = vgTotal;
	}
	public Integer getTheoreticalValue() {
		return theoreticalValue;
	}
	public void setTheoreticalValue(Integer theoreticalValue) {
		this.theoreticalValue = theoreticalValue;
	}
	public String getTheoreticalInfo() {
		return theoreticalInfo;
	}
	public void setTheoreticalInfo(String theoreticalInfo) {
		this.theoreticalInfo = theoreticalInfo;
	}
}

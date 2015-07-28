/* 
* 文 件 名:  ZoneOverviewInfoVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-5-20 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.oss.monitoring.vo; 

/** 
 * <资源域概述数据集> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-20] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ZoneOverviewInfoVO {

	private long id;	
	private int hostTotal;//节点总数
	private int hostActiveNum;//活动的节点数
	private int vmActiveNum;//活动的虚拟机数
	private int vmTotal;//已经创建的虚拟机数
	private int theoreticalValue;//理论上还能创建的虚拟机数
	private int virtualCPUUsed;//虚拟CPU已使用数
	private int virtualCPUApply;//虚拟CPU申请数
	private int virtualCPUTotal;//虚拟CPU总数
	private int virtualMemoryUsed;//虚拟内存已使用数
	private int virtualMemoryApply;//虚拟内存申请数
	private int virtualMemoryTotal;//虚拟内存总数
	private int virtualDiskUsed;//虚拟磁盘已使用数
	private int virtualDiskApply;//虚拟磁盘申请数
	private int virtualDiskTotal;//虚拟磁盘总数
	private int ipUsed;//IP已经使用数（包括已分配、待释放、待分派）
	private int ipFree;//IP空闲数
	private int ipTotal;//IP总数
	private double expandDiskUsed;//扩展盘使用的容量
	private double expandDiskTotal;//扩展盘总共的容量
	private int vmPaidNum;//已经付费正式使用的虚拟机数量
	private int vmTrialNum;//试用的虚拟机数量
	private String theoreticalInfo;//理论上还能创建的虚拟机数信息
	private int vmOtherNum;//其他类型虚拟机数量
	private int vmWindowsNum;//windows系统的机器数量
	private int vmLinuxNum;//linux系统的机器数量
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getHostTotal() {
		return hostTotal;
	}
	public void setHostTotal(int hostTotal) {
		this.hostTotal = hostTotal;
	}
	public int getHostActiveNum() {
		return hostActiveNum;
	}
	public void setHostActiveNum(int hostActiveNum) {
		this.hostActiveNum = hostActiveNum;
	}
	public int getVmActiveNum() {
		return vmActiveNum;
	}
	public void setVmActiveNum(int vmActiveNum) {
		this.vmActiveNum = vmActiveNum;
	}
	public int getVmTotal() {
		return vmTotal;
	}
	public void setVmTotal(int vmTotal) {
		this.vmTotal = vmTotal;
	}
	public int getTheoreticalValue() {
		return theoreticalValue;
	}
	public void setTheoreticalValue(int theoreticalValue) {
		this.theoreticalValue = theoreticalValue;
	}
	public int getVirtualCPUUsed() {
		return virtualCPUUsed;
	}
	public void setVirtualCPUUsed(int virtualCPUUsed) {
		this.virtualCPUUsed = virtualCPUUsed;
	}
	public int getVirtualCPUApply() {
		return virtualCPUApply;
	}
	public void setVirtualCPUApply(int virtualCPUApply) {
		this.virtualCPUApply = virtualCPUApply;
	}
	public int getVirtualCPUTotal() {
		return virtualCPUTotal;
	}
	public void setVirtualCPUTotal(int virtualCPUTotal) {
		this.virtualCPUTotal = virtualCPUTotal;
	}
	public int getVirtualMemoryUsed() {
		return virtualMemoryUsed;
	}
	public void setVirtualMemoryUsed(int virtualMemoryUsed) {
		this.virtualMemoryUsed = virtualMemoryUsed;
	}
	public int getVirtualMemoryApply() {
		return virtualMemoryApply;
	}
	public void setVirtualMemoryApply(int virtualMemoryApply) {
		this.virtualMemoryApply = virtualMemoryApply;
	}
	public int getVirtualMemoryTotal() {
		return virtualMemoryTotal;
	}
	public void setVirtualMemoryTotal(int virtualMemoryTotal) {
		this.virtualMemoryTotal = virtualMemoryTotal;
	}
	public int getVirtualDiskUsed() {
		return virtualDiskUsed;
	}
	public void setVirtualDiskUsed(int virtualDiskUsed) {
		this.virtualDiskUsed = virtualDiskUsed;
	}
	public int getVirtualDiskApply() {
		return virtualDiskApply;
	}
	public void setVirtualDiskApply(int virtualDiskApply) {
		this.virtualDiskApply = virtualDiskApply;
	}
	public int getVirtualDiskTotal() {
		return virtualDiskTotal;
	}
	public void setVirtualDiskTotal(int virtualDiskTotal) {
		this.virtualDiskTotal = virtualDiskTotal;
	}
	public int getIpUsed() {
		return ipUsed;
	}
	public void setIpUsed(int ipUsed) {
		this.ipUsed = ipUsed;
	}
	public int getIpFree() {
		return ipFree;
	}
	public void setIpFree(int ipFree) {
		this.ipFree = ipFree;
	}
	public int getIpTotal() {
		return ipTotal;
	}
	public void setIpTotal(int ipTotal) {
		this.ipTotal = ipTotal;
	}
	public double getExpandDiskUsed() {
		return expandDiskUsed;
	}
	public void setExpandDiskUsed(double expandDiskUsed) {
		this.expandDiskUsed = expandDiskUsed;
	}
	public double getExpandDiskTotal() {
		return expandDiskTotal;
	}
	public void setExpandDiskTotal(double expandDiskTotal) {
		this.expandDiskTotal = expandDiskTotal;
	}
	public int getVmPaidNum() {
		return vmPaidNum;
	}
	public void setVmPaidNum(int vmPaidNum) {
		this.vmPaidNum = vmPaidNum;
	}
	public int getVmTrialNum() {
		return vmTrialNum;
	}
	public void setVmTrialNum(int vmTrialNum) {
		this.vmTrialNum = vmTrialNum;
	}
	public String getTheoreticalInfo() {
		return theoreticalInfo;
	}
	public void setTheoreticalInfo(String theoreticalInfo) {
		this.theoreticalInfo = theoreticalInfo;
	}
	public int getVmOtherNum() {
		return vmOtherNum;
	}
	public void setVmOtherNum(int vmOtherNum) {
		this.vmOtherNum = vmOtherNum;
	}
	public int getVmWindowsNum() {
		return vmWindowsNum;
	}
	public void setVmWindowsNum(int vmWindowsNum) {
		this.vmWindowsNum = vmWindowsNum;
	}
	public int getVmLinuxNum() {
		return vmLinuxNum;
	}
	public void setVmLinuxNum(int vmLinuxNum) {
		this.vmLinuxNum = vmLinuxNum;
	}
}

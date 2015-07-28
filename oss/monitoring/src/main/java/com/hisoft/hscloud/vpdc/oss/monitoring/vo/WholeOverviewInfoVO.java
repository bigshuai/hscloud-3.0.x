package com.hisoft.hscloud.vpdc.oss.monitoring.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
* @description 全局概述数据集
* @version 1.3
* @author ljg
* @update 2012-9-21 下午3:15:01
 */
 public class WholeOverviewInfoVO {
	 	private int id;
	 	private long enterpriseUserTotal;
	 	private long commonUserTotal;
		private long enterpriseUserOnNum;		
		private long commonUserOnNum;
		private int hostTotal;
		private int hostActiveNum;
		private int vmActiveNum;
		private int vmTotal;
		private int virtualCPUUsed;
		private int virtualCPUApply;
		private int virtualCPUTotal;
		private int virtualMemoryUsed;
		private int virtualMemoryApply;
		private int virtualMemoryTotal;
		private int virtualDiskUsed;
		private int virtualDiskApply;
		private int virtualDiskTotal;
		private int ipUsed;
		private int ipFree;//IP空闲数
		private int ipTotal;//IP总数
		private double expandDiskUsed;//扩展盘使用的容量
		private double expandDiskTotal;//扩展盘总共的容量
		private int vmPaidNum;//已经付费正式使用的虚拟机数量
		private int vmTrialNum;//试用的虚拟机数量
		private int vmOtherNum;//其他类型虚拟机数量
		private int vmWindowsNum;//windows系统的机器数量
		private int vmLinuxNum;//linux系统的机器数量
		private List<HostVO> hostError = new ArrayList<HostVO>();		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public long getEnterpriseUserTotal() {
			return enterpriseUserTotal;
		}
		public void setEnterpriseUserTotal(long enterpriseUserTotal) {
			this.enterpriseUserTotal = enterpriseUserTotal;
		}
		public long getCommonUserTotal() {
			return commonUserTotal;
		}
		public void setCommonUserTotal(long commonUserTotal) {
			this.commonUserTotal = commonUserTotal;
		}
		public long getEnterpriseUserOnNum() {
			return enterpriseUserOnNum;
		}
		public void setEnterpriseUserOnNum(long enterpriseUserOnNum) {
			this.enterpriseUserOnNum = enterpriseUserOnNum;
		}
		public long getCommonUserOnNum() {
			return commonUserOnNum;
		}
		public void setCommonUserOnNum(long commonUserOnNum) {
			this.commonUserOnNum = commonUserOnNum;
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
		public List<HostVO> getHostError() {
			return hostError;
		}
		public void setHostError(List<HostVO> hostError) {
			this.hostError = hostError;
		}
		
}

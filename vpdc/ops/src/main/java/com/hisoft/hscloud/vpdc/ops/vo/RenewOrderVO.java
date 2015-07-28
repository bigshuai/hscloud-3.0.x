/* 
* 文 件 名:  RenewOrderVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2012-11-20 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.vo; 

import java.math.BigDecimal;
import java.util.Date;

/** 
 * <续费VO> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2012-11-20] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class RenewOrderVO {
	private String vmId;
	private String vmName;
	private int cpu;//虚拟机的总的CPU
	private String cpuName;
	private int memory;//虚拟机的总的memory
	private String memName;
	private int disk;//虚拟机的总的disk
	private String diskName;
	private String os;//操作系统类别
	private String ip;
	private String network;//网络宽带值
	private String networkName;
	private String scname;// 套餐名称
	private Date renewPeriodStart;//续费周期起始时间
	private Date renewPeriodEnd;//续费周期终止时间
	private String pricePeriodType;//计费方式
	private BigDecimal totle;//付费总价 
	private BigDecimal balance;//账户余额 
	public String getVmId() {
		return vmId;
	}
	public void setVmId(String vmId) {
		this.vmId = vmId;
	}
	public String getVmName() {
		return vmName;
	}
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
	public int getCpu() {
		return cpu;
	}
	public void setCpu(int cpu) {
		this.cpu = cpu;
	}
	public String getCpuName() {
		return cpuName;
	}
	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}
	public int getMemory() {
		return memory;
	}
	public void setMemory(int memory) {
		this.memory = memory;
	}
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	public int getDisk() {
		return disk;
	}
	public void setDisk(int disk) {
		this.disk = disk;
	}
	public String getDiskName() {
		return diskName;
	}
	public void setDiskName(String diskName) {
		this.diskName = diskName;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getNetworkName() {
		return networkName;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	public String getScname() {
		return scname;
	}
	public void setScname(String scname) {
		this.scname = scname;
	}
	
	public Date getRenewPeriodStart() {
		return renewPeriodStart;
	}
	public void setRenewPeriodStart(Date renewPeriodStart) {
		this.renewPeriodStart = renewPeriodStart;
	}
	public Date getRenewPeriodEnd() {
		return renewPeriodEnd;
	}
	public void setRenewPeriodEnd(Date renewPeriodEnd) {
		this.renewPeriodEnd = renewPeriodEnd;
	}
	public String getPricePeriodType() {
		return pricePeriodType;
	}
	public void setPricePeriodType(String pricePeriodType) {
		this.pricePeriodType = pricePeriodType;
	}
	public BigDecimal getTotle() {
		return totle;
	}
	public void setTotle(BigDecimal totle) {
		this.totle = totle;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "RenewOrderVO [vmId=" + vmId + ", vmName=" + vmName + "," +
				"cpuName="+cpuName+",memName="+memName+", memory=" + memory + "," +
					"diskName="+diskName+", disk=" + disk + ", os=" + os+ "," +
						" ip=" + ip + ",scname="+scname+",renewPeriodStart="+renewPeriodStart+"," +
								"renewPeriodEnd="+renewPeriodEnd+",pricePeriodType="+pricePeriodType+"," +
										"totle=" +totle+",balance=" +balance+"]";
	}
}

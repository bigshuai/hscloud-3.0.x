package com.hisoft.hscloud.vpdc.oss.monitoring.vo;

import java.util.Date;

import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.CPUMonitorDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.DISKMonitorDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.IOPSMonitorDetailBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean.NETMonitorDetailBean;

/**
 * 
 * @description 虚拟机信息
 * @version 1.3
 * @author ljg
 * @update 2012-9-21 下午5:27:50
 */
public class VmInfoMonitorVO {

	private long referenceId;
	private String vmId;
	private String vmName;
	private String hostName;	
	private String ipInner;
	private String ipOuter;
	private int cpuCore;
	private double memory;
	private double disk;
	private double cpuUsage;
	private double memoryUsage;
	private double diskUsage;
	private String vmOS;
	private String remark;
	private String outComments;//外部属性
	private String vmStatus;
	private Date applyTime;//虚拟机申请时间
	private Date createTime;
	private int isEnable;//0:正常；1：手动禁用；2：到期禁用
	private String userName;
	private Date expireTime;//虚拟机到期时间
	private String vmType;//虚拟机类型（0：试用；1：正式）
	private String vmStatus_buss;//虚拟机业务状态（0:试用申请中；1：试用中；2：延期待审核；3：已延期；4：已转正；5:取消；6：试用到期）
	private String zoneCode;
	private String SysUser;//操作系统用户名
	private Integer scId;
	private Integer cpuLimit;
	private Integer diskRead;
	private Integer diskWrite;
	private Integer bandWidthIn;
	private Integer bandWidthOut;
	private Integer ipConnectionIn;
	private Integer ipConnectionOut;
	private Integer tcpConnectionIn;
	private Integer tcpConnectionOut;
	private Integer udpConnectionIn;
	private Integer udpConnectionOut;
	private CPUMonitorDetailBean cpuMonitorDetailBean;
	private IOPSMonitorDetailBean iopsMonitorDetailBean;
	private NETMonitorDetailBean netMonitorDetailBean;
	private DISKMonitorDetailBean diskMonitorDetailBean;
	private String hostAliases;//节点别名
	private String processState;//迁移进度
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
	public String getVmName() {
		return vmName;
	}
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getIpInner() {
		return ipInner;
	}
	public void setIpInner(String ipInner) {
		this.ipInner = ipInner;
	}
	public String getIpOuter() {
		return ipOuter;
	}
	public void setIpOuter(String ipOuter) {
		this.ipOuter = ipOuter;
	}
	public int getCpuCore() {
		return cpuCore;
	}
	public void setCpuCore(int cpuCore) {
		this.cpuCore = cpuCore;
	}
	public double getMemory() {
		return memory;
	}
	public void setMemory(double memory) {
		this.memory = memory;
	}
	public double getDisk() {
		return disk;
	}
	public void setDisk(double disk) {
		this.disk = disk;
	}
	public double getCpuUsage() {
		return cpuUsage;
	}
	public void setCpuUsage(double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	public double getMemoryUsage() {
		return memoryUsage;
	}
	public void setMemoryUsage(double memoryUsage) {
		this.memoryUsage = memoryUsage;
	}
	public double getDiskUsage() {
		return diskUsage;
	}
	public void setDiskUsage(double diskUsage) {
		this.diskUsage = diskUsage;
	}
	public String getVmOS() {
		return vmOS;
	}
	public void setVmOS(String vmOS) {
		this.vmOS = vmOS;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOutComments() {
		return outComments;
	}
	public void setOutComments(String outComments) {
		this.outComments = outComments;
	}
	public String getVmStatus() {
		return vmStatus;
	}
	public void setVmStatus(String vmStatus) {
		this.vmStatus = vmStatus;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	public String getVmType() {
		return vmType;
	}
	public void setVmType(String vmType) {
		this.vmType = vmType;
	}
	public String getVmStatus_buss() {
		return vmStatus_buss;
	}
	public void setVmStatus_buss(String vmStatus_buss) {
		this.vmStatus_buss = vmStatus_buss;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public String getSysUser() {
		return SysUser;
	}
	public void setSysUser(String sysUser) {
		SysUser = sysUser;
	}
	public Integer getScId() {
		return scId;
	}
	public void setScId(Integer scId) {
		this.scId = scId;
	}
	public Integer getCpuLimit() {
		return cpuLimit;
	}
	public void setCpuLimit(Integer cpuLimit) {
		this.cpuLimit = cpuLimit;
	}
	public Integer getDiskRead() {
		return diskRead;
	}
	public void setDiskRead(Integer diskRead) {
		this.diskRead = diskRead;
	}
	public Integer getDiskWrite() {
		return diskWrite;
	}
	public void setDiskWrite(Integer diskWrite) {
		this.diskWrite = diskWrite;
	}
	public Integer getBandWidthIn() {
		return bandWidthIn;
	}
	public void setBandWidthIn(Integer bandWidthIn) {
		this.bandWidthIn = bandWidthIn;
	}
	public Integer getBandWidthOut() {
		return bandWidthOut;
	}
	public void setBandWidthOut(Integer bandWidthOut) {
		this.bandWidthOut = bandWidthOut;
	}
	public Integer getIpConnectionIn() {
		return ipConnectionIn;
	}
	public void setIpConnectionIn(Integer ipConnectionIn) {
		this.ipConnectionIn = ipConnectionIn;
	}
	public Integer getIpConnectionOut() {
		return ipConnectionOut;
	}
	public void setIpConnectionOut(Integer ipConnectionOut) {
		this.ipConnectionOut = ipConnectionOut;
	}
	public Integer getTcpConnectionIn() {
		return tcpConnectionIn;
	}
	public void setTcpConnectionIn(Integer tcpConnectionIn) {
		this.tcpConnectionIn = tcpConnectionIn;
	}
	public Integer getTcpConnectionOut() {
		return tcpConnectionOut;
	}
	public void setTcpConnectionOut(Integer tcpConnectionOut) {
		this.tcpConnectionOut = tcpConnectionOut;
	}
	public Integer getUdpConnectionIn() {
		return udpConnectionIn;
	}
	public void setUdpConnectionIn(Integer udpConnectionIn) {
		this.udpConnectionIn = udpConnectionIn;
	}
	public Integer getUdpConnectionOut() {
		return udpConnectionOut;
	}
	public void setUdpConnectionOut(Integer udpConnectionOut) {
		this.udpConnectionOut = udpConnectionOut;
	}
	public CPUMonitorDetailBean getCpuMonitorDetailBean() {
		return cpuMonitorDetailBean;
	}
	public void setCpuMonitorDetailBean(CPUMonitorDetailBean cpuMonitorDetailBean) {
		this.cpuMonitorDetailBean = cpuMonitorDetailBean;
	}
	public IOPSMonitorDetailBean getIopsMonitorDetailBean() {
		return iopsMonitorDetailBean;
	}
	public void setIopsMonitorDetailBean(IOPSMonitorDetailBean iopsMonitorDetailBean) {
		this.iopsMonitorDetailBean = iopsMonitorDetailBean;
	}
	public NETMonitorDetailBean getNetMonitorDetailBean() {
		return netMonitorDetailBean;
	}
	public void setNetMonitorDetailBean(NETMonitorDetailBean netMonitorDetailBean) {
		this.netMonitorDetailBean = netMonitorDetailBean;
	}
	public DISKMonitorDetailBean getDiskMonitorDetailBean() {
		return diskMonitorDetailBean;
	}
	public void setDiskMonitorDetailBean(DISKMonitorDetailBean diskMonitorDetailBean) {
		this.diskMonitorDetailBean = diskMonitorDetailBean;
	}
	public String getHostAliases() {
		return hostAliases;
	}
	public void setHostAliases(String hostAliases) {
		this.hostAliases = hostAliases;
	}
	public String getProcessState() {
		return processState;
	}
	public void setProcessState(String processState) {
		this.processState = processState;
	}
}

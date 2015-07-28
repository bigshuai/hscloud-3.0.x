/**
 * @title InstanceDetailVo.java
 * @package com.hisoft.hscloud.vpdc.ops.vo
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-4-5 下午3:00:13
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.hisoft.hscloud.vpdc.ops.json.bean.VmExtDiskBean;
import com.hisoft.hscloud.vpdc.ops.json.bean.VmOsBean;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author hongqin.li
 * @update 2012-4-5 下午3:00:13
 */
public class InstanceDetailVo {
	private String vmId;
	private String vmStatus;//虚拟机的状态
	private String vmTask;//虚拟机的任务
	private int vmIsEnable;//虚拟机是否冻结
	private String vmName;
	private Date runTime;
	private Integer cpu;//虚拟机的总的CPU
	private String cpuName;
	private Integer memory;//虚拟机的总的memory
	private String memName;
	private Integer disk;//虚拟机的总的disk
	private String diskName;
	private List<VmExtDiskBean> extdisks;//虚拟机的扩展盘
	private String os;//操作系统类别
	private Integer osId;//操作系统id
	private List<VmOsBean> osList = new ArrayList<VmOsBean>();
	private String ip;
	private Integer scId;
	private Date createTime;
	private String network;//网络宽带值 add by dinghaibin
	private String networkName;
	private String scname;// 套餐名称 add by dinghaibin
	private Date effectiveDate;//生效日期 add by dinghaibin
	private Date expireDate;//到期日期 add by dinghaibin
	private Long spare;//剩余 add by dinghaibin
	private String orderNo;//订单编号 add by dinghaibin
	private Date orderDate;//订单日期 add by dinghaibin
	private String pricePeriodType;//计费方式 add by dinghaibin
	private BigDecimal price;//价格 add by dinghaibin
	private String zoneName;//区域名称
	private String osLoginUser;//操作系统登录用户名
	private String osLoginPwd;//操作系统登录密码
	private Integer vmType;//0:试用主机；1：正式主机；
	private Integer buyType;//默认为0：套餐购买；1：按需购买
	private Integer vmBusineStatus;
	private Integer ipNum;
	private Long referenceId;
	private String remark;
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return vmId : return the property vmId.
	 */
	public String getVmId() {
		return vmId;
	}
	/**
	 * @param vmId : set the property vmId.
	 */
	public void setVmId(String vmId) {
		this.vmId = vmId;
	}
	/**
	 * @return vmStatus : return the property vmStatus.
	 */
	public String getVmStatus() {
		return vmStatus;
	}
	/**
	 * @param vmStatus : set the property vmStatus.
	 */
	public void setVmStatus(String vmStatus) {
		this.vmStatus = vmStatus;
	}
	/**
	 * @return vmTask : return the property vmTask.
	 */
	public String getVmTask() {
		return vmTask;
	}
	/**
	 * @param vmTask : set the property vmTask.
	 */
	public void setVmTask(String vmTask) {
		this.vmTask = vmTask;
	}
	
	public int getVmIsEnable() {
		return vmIsEnable;
	}
	public void setVmIsEnable(int vmIsEnable) {
		this.vmIsEnable = vmIsEnable;
	}
	/**
	 * @return vmName : return the property vmName.
	 */
	public String getVmName() {
		return vmName;
	}
	/**
	 * @param vmName : set the property vmName.
	 */
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
	/**
	 * @return runTime : return the property runTime.
	 */
	public Date getRunTime() {
		return runTime;
	}
	/**
	 * @param runTime : set the property runTime.
	 */
	public void setRunTime(Date runTime) {
		this.runTime = runTime;
	}
	/**
	 * @return cpu : return the property cpu.
	 */
	public Integer getCpu() {
		return cpu;
	}
	/**
	 * @param cpu : set the property cpu.
	 */
	public void setCpu(Integer cpu) {
		this.cpu = cpu;
	}
	/**
	 * @return memory : return the property memory.
	 */
	public Integer getMemory() {
		return memory;
	}
	/**
	 * @param memory : set the property memory.
	 */
	public void setMemory(Integer memory) {
		this.memory = memory;
	}
	/**
	 * @return disk : return the property disk.
	 */
	public Integer getDisk() {
		return disk;
	}
	/**
	 * @param disk : set the property disk.
	 */
	public void setDisk(Integer disk) {
		this.disk = disk;
	}
	/**
	 * @return os : return the property os.
	 */
	public String getOs() {
		return os;
	}
	/**
	 * @param os : set the property os.
	 */
	public void setOs(String os) {
		this.os = os;
	}
	 
	public Integer getOsId() {
		return osId;
	}
	public void setOsId(Integer osId) {
		this.osId = osId;
	}
	
	public List<VmOsBean> getOsList() {
		return osList;
	}
	public void setOsList(List<VmOsBean> osList) {
		this.osList = osList;
	}
	/**
	 * @return ip : return the property ip.
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip : set the property ip.
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return createTime : return the property createTime.
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime : set the property createTime.
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return network : return the property network.
	 */
	public String getNetwork() {
		return network;
	}
	/**
	 * @param network : set the property network.
	 */
	public void setNetwork(String network) {
		this.network = network;
	}
	/**
	 * @return scname : return the property scname.
	 */
	public String getScname() {
		return scname;
	}
	/**
	 * @param scname : set the property scname.
	 */
	public void setScname(String scname) {
		this.scname = scname;
	}
	/**
	 * @return effectiveDate : return the property effectiveDate.
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate : set the property effectiveDate.
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public Long getSpare() {
		return spare;
	}
	public void setSpare(Long spare) {
		this.spare = spare;
	}
	/**
	 * @return orderNo : return the property orderNo.
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * @param orderNo : set the property orderNo.
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * @return orderDate : return the property orderDate.
	 */
	public Date getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate : set the property orderDate.
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * @return pricePeriodType : return the property pricePeriodType.
	 */
	public String getPricePeriodType() {
		return pricePeriodType;
	}
	/**
	 * @param pricePeriodType : set the property pricePeriodType.
	 */
	public void setPricePeriodType(String pricePeriodType) {
		this.pricePeriodType = pricePeriodType;
	}
	/**
	 * @return price : return the property price.
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price : set the property price.
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public String getCpuName() {
		return cpuName;
	}
	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	public String getDiskName() {
		return diskName;
	}
	public void setDiskName(String diskName) {
		this.diskName = diskName;
	}
	
	public List<VmExtDiskBean> getExtdisks() {
		return extdisks;
	}
	public void setExtdisks(List<VmExtDiskBean> extdisks) {
		this.extdisks = extdisks;
	}
	public String getNetworkName() {
		return networkName;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	
	public Integer getScId() {
		return scId;
	}
	public void setScId(Integer scId) {
		this.scId = scId;
	}
	
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public String getOsLoginUser() {
		return osLoginUser;
	}
	public void setOsLoginUser(String osLoginUser) {
		this.osLoginUser = osLoginUser;
	}
	public String getOsLoginPwd() {
		return osLoginPwd;
	}
	public void setOsLoginPwd(String osLoginPwd) {
		this.osLoginPwd = osLoginPwd;
	}
	public Integer getVmType() {
		return vmType;
	}
	public void setVmType(Integer vmType) {
		this.vmType = vmType;
	}
	public Integer getVmBusineStatus() {
		return vmBusineStatus;
	}
	public void setVmBusineStatus(Integer vmBusineStatus) {
		this.vmBusineStatus = vmBusineStatus;
	}
	
	public Integer getIpNum() {
		return ipNum;
	}
	public void setIpNum(Integer ipNum) {
		this.ipNum = ipNum;
	}
	
	public Long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}
	public Integer getBuyType() {
		return buyType;
	}
	public void setBuyType(Integer buyType) {
		this.buyType = buyType;
	}
	@Override
	public String toString() {
		return "InstanceDetailVo [vmId=" + vmId + ", vmStatus=" + vmStatus
				+ ", vmTask=" + vmTask + ", vmIsEnable=" + vmIsEnable
				+ ", vmName=" + vmName + ", runTime=" + runTime + ", cpu="
				+ cpu + ", cpuName=" + cpuName + ", memory=" + memory
				+ ", memName=" + memName + ", disk=" + disk + ", diskName="
				+ diskName + ", extdisks=" + extdisks + ", os=" + os
				+ ", osId=" + osId + ", osList=" + osList + ", ip=" + ip
				+ ", scId=" + scId + ", createTime=" + createTime
				+ ", network=" + network + ", networkName=" + networkName
				+ ", scname=" + scname + ", effectiveDate=" + effectiveDate
				+ ", expireDate=" + expireDate + ", spare=" + spare
				+ ", orderNo=" + orderNo + ", orderDate=" + orderDate
				+ ", pricePeriodType=" + pricePeriodType + ", price=" + price
				+ ", zoneName=" + zoneName + ", osLoginUser=" + osLoginUser
				+ ", osLoginPwd=" + osLoginPwd + ", vmType=" + vmType
				+ ", vmBusineStatus=" + vmBusineStatus + "]";
	}
	
}

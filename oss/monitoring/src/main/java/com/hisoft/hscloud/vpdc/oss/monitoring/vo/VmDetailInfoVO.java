package com.hisoft.hscloud.vpdc.oss.monitoring.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hisoft.hscloud.vpdc.ops.json.bean.VmExtDiskBean;

/**
 * 
* @description 虚拟机关联信息
* @version 1.3
* @author ljg
* @update 2012-9-21 下午5:38:24
 */
 public class VmDetailInfoVO {

	 private String vmId;//虚拟机ID
	 private String vmName;//虚拟机名称
	 private String cpuType;//CPU型号
	 private Integer cpuCore;//CPU数量
	 private Double memory;//内存容量
	 private Double disk;//硬盘容量	 
	 private String network;//网络	
	 private String ipInner;//内网IP
	 private String ipOuter;//外网IP
	 private String vmOS;//操作系统
	 private String catalogName;//套餐名称
	 private Date catalogDate;//套餐生效时间
	 private long catalogUsed;//套餐使用天数
	 private long catalogRemain;//套餐剩余时间
	 private String orderNumber;//订单编号
	 private Date orderDate;//订单时间
	 private String billingModel;//计费方式
	 private BigDecimal price;//单价
	 private String priceUnit;//价格单位
	 private String userName;//用户名
	 private List<VmExtDiskBean> extdisks;//虚拟机的扩展盘
	 private String zoneCode;//区域编码
	 private String zoneName;//区域名称
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
	public String getCpuType() {
		return cpuType;
	}
	public void setCpuType(String cpuType) {
		this.cpuType = cpuType;
	}
	public Integer getCpuCore() {
		return cpuCore;
	}
	public void setCpuCore(Integer cpuCore) {
		this.cpuCore = cpuCore;
	}
	public Double getMemory() {
		return memory;
	}
	public void setMemory(Double memory) {
		this.memory = memory;
	}
	public Double getDisk() {
		return disk;
	}
	public void setDisk(Double disk) {
		this.disk = disk;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
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
	public String getVmOS() {
		return vmOS;
	}
	public void setVmOS(String vmOS) {
		this.vmOS = vmOS;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	public Date getCatalogDate() {
		return catalogDate;
	}
	public void setCatalogDate(Date catalogDate) {
		this.catalogDate = catalogDate;
	}
	public long getCatalogUsed() {
		return catalogUsed;
	}
	public void setCatalogUsed(long catalogUsed) {
		this.catalogUsed = catalogUsed;
	}
	public long getCatalogRemain() {
		return catalogRemain;
	}
	public void setCatalogRemain(long catalogRemain) {
		this.catalogRemain = catalogRemain;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getBillingModel() {
		return billingModel;
	}
	public void setBillingModel(String billingModel) {
		this.billingModel = billingModel;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<VmExtDiskBean> getExtdisks() {
		return extdisks;
	}
	public void setExtdisks(List<VmExtDiskBean> extdisks) {
		this.extdisks = extdisks;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	@Override
	public String toString() {
		return "VmInfoVO [vmId=" + vmId
				+ ", vmName=" +vmName
				+ ", cpuType=" +cpuType
				+ ", cpuCore=" +cpuCore
				+ ", memory=" +memory
				+ ", disk=" +disk
				+ ", network=" +network
				+ ", ipInner=" +ipInner
				+ ", ipOuter=" +ipOuter
				+ ", vmOS=" +vmOS
				+ ", catalogName=" +catalogName
				+ ", catalogDate=" +catalogDate
				+ ", catalogUsed=" +catalogUsed					
				+ ", catalogRemain=" +catalogRemain	
				+ ", orderNumber=" +orderNumber
				+ ", orderDate=" +orderDate
				+ ", billingModel=" +billingModel
				+ ", price=" +price
				+ ", priceUnit=" +priceUnit
				+ ", userName=" +userName
				+ ", extdisks=" +extdisks
				+ ", zoneCode=" +zoneCode
				+ ", zoneName=" +zoneName
				+"]";
	}
}

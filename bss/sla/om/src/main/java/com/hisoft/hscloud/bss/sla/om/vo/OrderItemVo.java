/**
 * @title OrderItemVo.java
 * @package com.hisoft.hscloud.bss.sla.om.vo
 * @description 用一句话描述该文件做什么
 * @author MaDai
 * @update 2012-4-8 上午11:33:24
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.om.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author Madai
 * @update 2012-4-8 上午11:33:24
 */
public class OrderItemVo {
	private Long id;
	private String serviceCatalogName;// 套餐名
	private BigDecimal price;
	private BigDecimal amount;
	private int quantity=1;
	private String imageId;
	private String flavorId;
	private Date createDate;
	private String orderNo;
	private BigDecimal totalPrice;
	private BigDecimal totalAmount;
	private String os;
	private String osId;
	private String cpuModel;
	private String diskModel;
	private String networkType;
	private String memoryModel;
	private String cpu;
	private String addDisk;
	private String memory;
	private String disk;
	private Long feeTypeId;
	private String network;
	private String serviceCatalogId;
	private String software;
	private String priceType;
	private String pricePeriod;
	private String pricePeriodType;
	private String serviceDesc;
	private boolean tryOrNo;
	private int tryDuration;
	private String nodeName;
	private Date effectiveDate;
	private Date expirationDate;
	private String vCpus;
	private String machineNum;
	private String osIds;
	private String remark;
    private Integer rebateRate;
    private Integer giftsRebateRate;
    private BigDecimal totalPointAmount;
    private BigDecimal pointAmount;
    private BigDecimal totalGiftAmount;
    private BigDecimal giftAmount;
    private boolean useGiftOrNot;
    private boolean usePointOrNot;
    private BigDecimal originalPointAmount;
    private BigDecimal originalGiftAmount;
    private short orderType;
    private String ipNum;
	/**
	 * 类的构造方法
	 */
	public OrderItemVo() {
		super();
	}
	//套餐购买详情 
	public OrderItemVo(Long id, String serviceCatalogName,
			BigDecimal price, int quantity, Date createDate,String orderNo,
			BigDecimal totalPrice, String os, String cpu, String memory,
			String disk,String addDisk, String network, String software, String serviceDesc,
			String nodeName,Date expirationDate,Date effectiveDate,
			BigDecimal amount,String priceType,String pricePeriodType,
			String pricePeriod,String remark,Integer rebateRate,
			BigDecimal pointAmount,boolean usePointOrNot,BigDecimal totalPointAmount
			,BigDecimal totalAmount,short orderType,
			BigDecimal totalGiftAmount,BigDecimal giftAmount,
			Integer giftsRebateRate,boolean useGiftOrNot) {
		super();
		this.id = id;
		this.pointAmount=pointAmount;
		this.rebateRate=rebateRate;
		this.usePointOrNot=usePointOrNot;
		this.serviceCatalogName = serviceCatalogName;
		this.price = price;
		this.quantity = quantity;
		this.createDate = createDate;
		this.orderNo = orderNo;
		this.totalPrice = totalPrice;
		this.totalAmount=totalAmount;
		this.totalPointAmount=totalPointAmount;
		this.os = os;
		this.cpu = cpu;
		this.memory = memory;
		this.disk = disk;
		this.addDisk=addDisk;
		this.network = network;
		this.software = software;
		this.nodeName=nodeName;
		this.effectiveDate=effectiveDate;
		this.expirationDate=expirationDate;
		this.serviceDesc = serviceDesc;
		this.amount=amount;
		this.priceType=priceType;
		this.pricePeriodType=pricePeriodType;
		this.pricePeriod=pricePeriod;
		this.remark=remark;
		this.orderType=orderType;
		
		this.totalGiftAmount = totalGiftAmount;
		this.giftAmount = giftAmount;
		this.giftsRebateRate = giftsRebateRate;
		this.useGiftOrNot = useGiftOrNot;
	}
	//按需购买详情
	public OrderItemVo(Long id, Date createDate,
			BigDecimal price,String orderNo,
			BigDecimal totalPrice, String os, String cpu, String memory,
			String disk,String addDisk, String network, 
			Date expirationDate,Date effectiveDate,
			BigDecimal amount,
			String pricePeriod,Integer rebateRate,
			BigDecimal pointAmount,boolean usePointOrNot,
			BigDecimal totalPointAmount,
			BigDecimal totalAmount,String ipNum,short orderType){
		this.id = id;
		this.createDate=createDate;
		this.pointAmount=pointAmount;
		this.rebateRate=rebateRate;
		this.usePointOrNot=usePointOrNot;
		this.price = price;
		this.orderNo = orderNo;
		this.totalPrice = totalPrice;
		this.totalAmount=totalAmount;
		this.totalPointAmount=totalPointAmount;
		this.os = os;
		this.cpu = cpu;
		this.memory = memory;
		this.disk = disk;
		this.addDisk=addDisk;
		this.network = network;
		this.effectiveDate=effectiveDate;
		this.expirationDate=expirationDate;
		this.amount=amount;
		this.pricePeriod=pricePeriod;
		this.ipNum=ipNum;
		this.orderType=orderType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceCatalogName() {
		return serviceCatalogName;
	}

	public void setServiceCatalogName(String serviceCatalogName) {
		this.serviceCatalogName = serviceCatalogName;
	}



	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getFlavorId() {
		return flavorId;
	}

	public void setFlavorId(String flavorId) {
		this.flavorId = flavorId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOs() {
		return os;
	}

	public Long getFeeTypeId() {
		return feeTypeId;
	}

	public void setFeeTypeId(Long feeTypeId) {
		this.feeTypeId = feeTypeId;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getDisk() {
		return disk;
	}

	public void setDisk(String disk) {
		this.disk = disk;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getServiceCatalogId() {
		return serviceCatalogId;
	}

	public void setServiceCatalogId(String serviceCatalogId) {
		this.serviceCatalogId = serviceCatalogId;
	}

	public String getSoftware() {
		return software;
	}

	public void setSoftware(String software) {
		this.software = software;
	}

	public int getTryDuration() {
		return tryDuration;
	}

	public void setTryDuration(int tryDuration) {
		this.tryDuration = tryDuration;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getPricePeriod() {
		return pricePeriod;
	}

	public void setPricePeriod(String pricePeriod) {
		this.pricePeriod = pricePeriod;
	}

	public String getPricePeriodType() {
		return pricePeriodType;
	}

	public void setPricePeriodType(String pricePeriodType) {
		this.pricePeriodType = pricePeriodType;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public String getAddDisk() {
		return addDisk;
	}

	public void setAddDisk(String addDisk) {
		this.addDisk = addDisk;
	}

	public boolean getTryOrNo() {
		return tryOrNo;
	}

	public void setTryOrNo(boolean tryOrNo) {
		this.tryOrNo = tryOrNo;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getOsId() {
		return osId;
	}

	public void setOsId(String osId) {
		this.osId = osId;
	}

	public String getCpuModel() {
		return cpuModel;
	}

	public void setCpuModel(String cpuModel) {
		this.cpuModel = cpuModel;
	}

	public String getDiskModel() {
		return diskModel;
	}

	public void setDiskModel(String diskModel) {
		this.diskModel = diskModel;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getMemoryModel() {
		return memoryModel;
	}

	public void setMemoryModel(String memoryModel) {
		this.memoryModel = memoryModel;
	}

	public String getvCpus() {
		return vCpus;
	}

	public void setvCpus(String vCpus) {
		this.vCpus = vCpus;
	}

	public String getOsIds() {
		return osIds;
	}

	public void setOsIds(String osIds) {
		this.osIds = osIds;
	}

	public String getMachineNum() {
		return machineNum;
	}

	public void setMachineNum(String machineNum) {
		this.machineNum = machineNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getRebateRate() {
		return rebateRate;
	}

	public void setRebateRate(Integer rebateRate) {
		this.rebateRate = rebateRate;
	}

	public BigDecimal getPointAmount() {
		return pointAmount;
	}

	public void setPointAmount(BigDecimal pointAmount) {
		this.pointAmount = pointAmount;
	}

	public boolean getUsePointOrNot() {
		return usePointOrNot;
	}

	public void setUsePointOrNot(boolean usePointOrNot) {
		this.usePointOrNot = usePointOrNot;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalPointAmount() {
		return totalPointAmount;
	}

	public void setTotalPointAmount(BigDecimal totalPointAmount) {
		this.totalPointAmount = totalPointAmount;
	}

	public BigDecimal getOriginalPointAmount() {
		if(this.usePointOrNot){
			if(this.rebateRate!=null){
				return this.price.multiply(new BigDecimal(this.rebateRate)).divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
			}else{
				return BigDecimal.ZERO;
			}
		}else{
			return BigDecimal.ZERO;
		}
	}

	public void setOriginalPointAmount(BigDecimal originalPointAmount) {
		this.originalPointAmount = originalPointAmount;
	}
	
	

	public BigDecimal getOriginalGiftAmount() {
		if(this.useGiftOrNot){
			if(this.giftsRebateRate!=null){
				return this.price.multiply(new BigDecimal(this.giftsRebateRate)).divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
			}else{
				return BigDecimal.ZERO;
			}
			
		}else{
			return BigDecimal.ZERO;
		}
	}
	public void setOriginalGiftAmount(BigDecimal originalGiftAmount) {
		this.originalGiftAmount = originalGiftAmount;
	}
	public short getOrderType() {
		return orderType;
	}

	public void setOrderType(short orderType) {
		this.orderType = orderType;
	}
	public String getIpNum() {
		return ipNum;
	}
	public void setIpNum(String ipNum) {
		this.ipNum = ipNum;
	}
	public BigDecimal getTotalGiftAmount() {
		return totalGiftAmount;
	}
	public void setTotalGiftAmount(BigDecimal totalGiftAmount) {
		this.totalGiftAmount = totalGiftAmount;
	}
	public BigDecimal getGiftAmount() {
		return giftAmount;
	}
	public void setGiftAmount(BigDecimal giftAmount) {
		this.giftAmount = giftAmount;
	}
	public boolean getUseGiftOrNot() {
		return useGiftOrNot;
	}
	public void setUseGiftOrNot(boolean useGiftOrNot) {
		this.useGiftOrNot = useGiftOrNot;
	}
	public Integer getGiftRebateRate() {
		return giftsRebateRate;
	}
	public void setGiftRebateRate(Integer giftsRebateRate) {
		this.giftsRebateRate = giftsRebateRate;
	}
	
	
	
}

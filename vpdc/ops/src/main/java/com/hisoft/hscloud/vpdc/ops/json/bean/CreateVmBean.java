package com.hisoft.hscloud.vpdc.ops.json.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateVmBean {

	private String name;//虚拟机名称
	private Integer vmType;//虚拟机类型（0：试用；1：正式） 
	private Integer vmBussiness;//虚拟机业务状态（0:试用待审;1：试用中；2：延期待审；3：已延期；4：正式；5：取消；6：到期）
	private String imageId;//当前镜像ID
	private String osId;//当前系统ID
	private List<Long> osIds = new ArrayList<Long>();
	private String flavorId;//flavor ID
	private String order_item_id;//订单明细ID
	private String disk;//磁盘大小
	private String diskType;//磁盘型号
	private String addDisk;//扩展磁盘
	private String ipDeploy;//是否发布外网IP(0:不发布;1：发布)
	private String ipAssignValue;//是否制定IP
	private String vmZone;//域名称
	private String vmNode;//节点名称
	private String vcpus;//cpu个数
	private String vcpusType;//cpu型号
	private String ram;//内存大小
	private String ramType;//内存大小
	private String network;//宽带大小
	private String networkType;//宽带类型
	private String owner;//虚拟机所属人
	private Date start_time;//购买虚拟机的启用日期
	private Date end_time;//购买虚拟机的停用日期
	private int tryLong;//试用时长(按天计算)
	private String floating_ip;//外网IP
	private int scId;//套餐id
	private Integer cpuLimit;//CPU限制
	private Integer diskRead;//磁盘读取限制
	private Integer diskWrite;//磁盘写限制
	private int bwtIn;//宽带下行速度
	private int bwtOut;//宽带上行速度
	private int ipConnIn;//IP连接数上行
	private int ipConnOut;//IP连接数下行
	private int tcpConnIn;//TCP连接数上行
	private int tcpConnOut;//TCP连接数下行
	private int udpConnIn;//UDP连接数上行
	private int udpConnOut;//UDP连接数下行
	private Long vpdcId;//VPDC的ID
	private Integer buyType;
	private Integer ipNum;
	
	
	public String getVcpus() {
		return vcpus;
	}
	public void setVcpus(String vcpus) {
		this.vcpus = vcpus;
	}
	public String getVcpusType() {
		return vcpusType;
	}
	public void setVcpusType(String vcpusType) {
		this.vcpusType = vcpusType;
	}
	public String getRam() {
		return ram;
	}
	public void setRam(String ram) {
		this.ram = ram;
	}
	public String getRamType() {
		return ramType;
	}
	public void setRamType(String ramType) {
		this.ramType = ramType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getVmType() {
		return vmType;
	}
	public void setVmType(Integer vmType) {
		this.vmType = vmType;
	}
	public Integer getVmBussiness() {
		return vmBussiness;
	}
	public void setVmBussiness(Integer vmBussiness) {
		this.vmBussiness = vmBussiness;
	}
	public String getDisk() {
		return disk;
	}
	public void setDisk(String disk) {
		this.disk = disk;
	}
	public String getDiskType() {
		return diskType;
	}
	public void setDiskType(String diskType) {
		this.diskType = diskType;
	}
	
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getNetworkType() {
		return networkType;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getOsId() {
		return osId;
	}
	public void setOsId(String osId) {
		this.osId = osId;
	}
	public List<Long> getOsIds() {
		return osIds;
	}
	public void setOsIds(List<Long> osIds) {
		this.osIds = osIds;
	}
	public String getFlavorId() {
		return flavorId;
	}
	public void setFlavorId(String flavorId) {
		this.flavorId = flavorId;
	}
	/**
	 * @return order_item_id : return the property order_item_id.
	 */
	public String getOrder_item_id() {
		return order_item_id;
	}
	/**
	 * @param order_item_id : set the property order_item_id.
	 */
	public void setOrder_item_id(String order_item_id) {
		this.order_item_id = order_item_id;
	}
	/**
	 * @return addDisk : return the property addDisk.
	 */
	public String getAddDisk() {
		return addDisk;
	}
	/**
	 * @param addDisk : set the property addDisk.
	 */
	public void setAddDisk(String addDisk) {
		this.addDisk = addDisk;
	}
	/**
	 * @return ipDeploy : return the property ipDeploy.
	 */
	public String getIpDeploy() {
		return ipDeploy;
	}
	/**
	 * @param ipDeploy : set the property ipDeploy.
	 */
	public void setIpDeploy(String ipDeploy) {
		this.ipDeploy = ipDeploy;
	}
	/**
	
	 * @return ipAssignValue : return the property ipAssignValue.
	 */
	public String getIpAssignValue() {
		return ipAssignValue;
	}
	/**
	 * @param ipAssignValue : set the property ipAssignValue.
	 */
	public void setIpAssignValue(String ipAssignValue) {
		this.ipAssignValue = ipAssignValue;
	}
	public String getVmZone() {
		return vmZone;
	}
	public void setVmZone(String vmZone) {
		this.vmZone = vmZone;
	}
	/**
	
	 * @return vmNode : return the property vmNode.
	 */
	public String getVmNode() {
		return vmNode;
	}
	/**
	 * @param vmNode : set the property vmNode.
	 */
	public void setVmNode(String vmNode) {
		this.vmNode = vmNode;
	}
	
	public Date getStart_time() {
		return start_time;
	}
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	
	public int getTryLong() {
		return tryLong;
	}
	public void setTryLong(int tryLong) {
		this.tryLong = tryLong;
	}
	public int getScId() {
		return scId;
	}
	public void setScId(int scId) {
		this.scId = scId;
	}
	public String getFloating_ip() {
		return floating_ip;
	}
	public void setFloating_ip(String floating_ip) {
		this.floating_ip = floating_ip;
	}
	public Integer getCpuLimit() {
		return cpuLimit;
	}
	public void setCpuLimit(Integer cpuLimit) {
		this.cpuLimit = cpuLimit;
	}
	public Integer getIpNum() {
		return ipNum;
	}
	public void setIpNum(Integer ipNum) {
		this.ipNum = ipNum;
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
	public int getBwtIn() {
		return bwtIn;
	}
	public void setBwtIn(int bwtIn) {
		this.bwtIn = bwtIn;
	}
	public int getBwtOut() {
		return bwtOut;
	}
	public void setBwtOut(int bwtOut) {
		this.bwtOut = bwtOut;
	}
	public int getIpConnIn() {
		return ipConnIn;
	}
	public void setIpConnIn(int ipConnIn) {
		this.ipConnIn = ipConnIn;
	}
	public int getIpConnOut() {
		return ipConnOut;
	}
	public void setIpConnOut(int ipConnOut) {
		this.ipConnOut = ipConnOut;
	}
	public int getTcpConnIn() {
		return tcpConnIn;
	}
	public void setTcpConnIn(int tcpConnIn) {
		this.tcpConnIn = tcpConnIn;
	}
	public int getTcpConnOut() {
		return tcpConnOut;
	}
	public void setTcpConnOut(int tcpConnOut) {
		this.tcpConnOut = tcpConnOut;
	}
	public int getUdpConnIn() {
		return udpConnIn;
	}
	public void setUdpConnIn(int udpConnIn) {
		this.udpConnIn = udpConnIn;
	}
	public int getUdpConnOut() {
		return udpConnOut;
	}
	public void setUdpConnOut(int udpConnOut) {
		this.udpConnOut = udpConnOut;
	}
	
	public Long getVpdcId() {
		return vpdcId;
	}
	public void setVpdcId(Long vpdcId) {
		this.vpdcId = vpdcId;
	}
	
	public Integer getBuyType() {
		return buyType;
	}
	public void setBuyType(Integer buyType) {
		this.buyType = buyType;
	}
	@Override
	public String toString() {
		return "CreateVmBean [name=" + name + ", vmType=" + vmType
				+ ", vmBussiness=" + vmBussiness + ", imageId=" + imageId
				+ ", osId=" + osId + ", osIds=" + osIds + ", flavorId="
				+ flavorId + ", order_item_id=" + order_item_id + ", disk="
				+ disk + ", diskType=" + diskType + ", addDisk=" + addDisk
				+ ", ipDeploy=" + ipDeploy + ", ipAssignValue=" + ipAssignValue
				+ ", vmZone=" + vmZone + ", vmNode=" + vmNode + ", vcpus="
				+ vcpus + ", vcpusType=" + vcpusType + ", ram=" + ram
				+ ", ramType=" + ramType + ", network=" + network
				+ ", networkType=" + networkType + ", owner=" + owner
				+ ", start_time=" + start_time + ", end_time=" + end_time
				+ ", tryLong=" + tryLong + ", floating_ip=" + floating_ip
				+ ", scId=" + scId + ", cpuLimit=" + cpuLimit + ", diskRead="
				+ diskRead + ", diskWrite=" + diskWrite + ", bwtIn=" + bwtIn
				+ ", bwtOut=" + bwtOut + ", ipConnIn=" + ipConnIn
				+ ", ipConnOut=" + ipConnOut + ", tcpConnIn=" + tcpConnIn
				+ ", tcpConnOut=" + tcpConnOut + ", udpConnIn=" + udpConnIn
				+ ", udpConnOut=" + udpConnOut + ", vpdcId=" + vpdcId + "]";
	}
	 
}

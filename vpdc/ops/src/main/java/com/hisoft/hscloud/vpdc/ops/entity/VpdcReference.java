package com.hisoft.hscloud.vpdc.ops.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hisoft.hscloud.common.entity.AbstractEntity;

@Entity
@Table(name = "hc_vpdc_reference")
public class VpdcReference extends AbstractEntity {
	@OneToMany(mappedBy = "vpdcreference", targetEntity = VpdcInstance.class, cascade = {
			CascadeType.ALL }, fetch = FetchType.LAZY)
	@OrderBy("id asc")
	private Set<VpdcInstance> instance;
	
	// 扩展盘集合
	@OneToMany(mappedBy = "ed_reference", targetEntity = VpdcReference_extdisk.class, cascade = {
		CascadeType.ALL}, fetch = FetchType.LAZY)
	@OrderBy("id asc")
	private Set<VpdcReference_extdisk> extdisks;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "vpdc_id")
	private Vpdc vpdc;
	
	@Column(name = "event_time")
	private Date eventTime;

	@Column(name = "imageId")
	private String imageId;
	
	@Column(name = "osId")
	private Integer osId;

	@Column(name = "flavorId")
	private String flavorId;

	@Column(name = "cpu_core")
	private Integer cpu_core;
	
	@Column(name = "cpu_type")
	private String cpu_type;
	
	@Column(name = "disk_capacity")
	private Integer disk_capacity;
	
	@Column(name = "disk_type")
	private String disk_type;
	
	@Column(name = "mem_size")
	private Integer mem_size;
	
	@Column(name = "mem_type")
	private String mem_type;
	
	@Column(name = "network_bandwidth")
	private Integer network_bandwidth;
	
	@Column(name = "network_type")
	private String network_type;

	@Column(name = "is_enable", nullable = false)
	private int isEnable = 0;//0:正常；1：手动禁用；2：到期禁用
	
	@Column(name = "status")
	private int status;//0:正常；1：删除/停用
	
	@Column(name = "vm_owner")
	private Long owner;
	
	@Column(name = "creater_type")
	private String createrType;
	
	@Column(name = "vm_type")
	private Integer vm_type;//0:试用主机；1：正式主机；
	
	@Column(name = "vm_business_status")
	private Integer vm_business_status;//0:试用待审核；1：试用中；2：延期待审核；3：已延期；4:转正；5：试用审核未通过
	
	@Column(name = "try_long")
	private Integer tryLong;
	
	@Column(name = "sc_id")
	private Integer scId;
	
	@Column(name = "vm_zone")
	private String vmZone;
	
	@Column(name = "comments")
	private String comments;
	
	@Column(name = "outComments")
	private String outComments;

	@Column(name = "vm_status")
	private String vm_status;
	
	@Column(name = "vm_task_status")
	private String vm_task_status;
	
	@Column(name = "vm_innerIP")
	private String vm_innerIP;
	
	@Column(name = "vm_outerIP", length=2000)
	private String vm_outerIP;
	
	@Column(name = "radom_user")
	private String radom_user;
	
	@Column(name = "radom_pwd")
	private String radom_pwd;
	
	@Column(name = "bwtIn")
	private Integer bwtIn;
	
	@Column(name = "bwtOut")
	private Integer bwtOut;

	@Column(name = "ipConnIn")
	private Integer ipConnIn;
	
	@Column(name = "ipConnOut")
	private Integer ipConnOut;
	
	@Column(name = "tcpConnIn")
	private Integer tcpConnIn;
	
	@Column(name = "tcpConnOut")
	private Integer tcpConnOut;
	
	@Column(name = "udpConnIn")
	private Integer udpConnIn;
	
	@Column(name = "udpConnOut")
	private Integer udpConnOut;
	@Column
	private Integer cpuLimit;
	@Column
	private Integer diskRead;
	@Column
	private Integer diskWrite;
	
	@Column(name = "createflag")
	private Integer createflag;//创建VM是否完成标记：0,初始值；1,成功；其它值均为失败
	
	@Column(name="try_time",columnDefinition=" smallint DEFAULT 0")
	private Integer tryTime;//创建VM失败后重新创建VM次数
	
	@Column(name="ip_num")
	private Integer ipNum;
	
	@Column(name="buy_type")
	private Integer buyType;//默认为0：套餐购买；1：按需购买
	
	@Transient
	private boolean needToFindOptimZone=true;//是否还需要去重新获取zone
	@Column(name="process_state")
	private String processState;//进程状态(用于迁移进度)
	
	/**
	 * @return instances : return the property instances.
	 */
	public Set<VpdcInstance> getInstance() {
		return instance;
	}

	/**
	 * @param instances
	 *            : set the property instances.
	 */
	public void setInstances(Set<VpdcInstance> instance) {
		this.instance = instance;
	}

	/**
	 * @return vpdc : return the property vpdc.
	 */
	public Vpdc getVpdc() {
		return vpdc;
	}

	/**
	 * @param vpdc
	 *            : set the property vpdc.
	 */
	public void setVpdc(Vpdc vpdc) {
		this.vpdc = vpdc;
	}

	public Integer getOsId() {
		return osId;
	}

	public void setOsId(Integer osId) {
		this.osId = osId;
	}

	/**
	 * @return imageId : return the property imageId.
	 */
	public String getImageId() {
		return imageId;
	}

	/**
	 * @param imageId
	 *            : set the property imageId.
	 */
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	/**
	 * @return flavorId : return the property flavorId.
	 */
	public String getFlavorId() {
		return flavorId;
	}

	/**
	 * @param flavorId
	 *            : set the property flavorId.
	 */
	public void setFlavorId(String flavorId) {
		this.flavorId = flavorId;
	}

	public Integer getCpu_core() {
		return cpu_core;
	}

	public void setCpu_core(Integer cpu_core) {
		this.cpu_core = cpu_core;
	}

	public String getCpu_type() {
		return cpu_type;
	}

	public void setCpu_type(String cpu_type) {
		this.cpu_type = cpu_type;
	}

	public Integer getDisk_capacity() {
		return disk_capacity;
	}

	public void setDisk_capacity(Integer disk_capacity) {
		this.disk_capacity = disk_capacity;
	}

	public String getDisk_type() {
		return disk_type;
	}

	public void setDisk_type(String disk_type) {
		this.disk_type = disk_type;
	}

	public Integer getMem_size() {
		return mem_size;
	}

	public void setMem_size(Integer mem_size) {
		this.mem_size = mem_size;
	}

	public String getMem_type() {
		return mem_type;
	}

	public void setMem_type(String mem_type) {
		this.mem_type = mem_type;
	}

	public Integer getNetwork_bandwidth() {
		return network_bandwidth;
	}

	public void setNetwork_bandwidth(Integer network_bandwidth) {
		this.network_bandwidth = network_bandwidth;
	}

	public String getNetwork_type() {
		return network_type;
	}

	public void setNetwork_type(String network_type) {
		this.network_type = network_type;
	}

	public void setInstance(Set<VpdcInstance> instance) {
		this.instance = instance;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public Long getOwner() {
		return owner;
	}

	public void setOwner(Long owner) {
		this.owner = owner;
	}

	public String getCreaterType() {
		return createrType;
	}

	public void setCreaterType(String createrType) {
		this.createrType = createrType;
	}

	public Set<VpdcReference_extdisk> getExtdisks() {
		return extdisks;
	}

	public void setExtdisks(Set<VpdcReference_extdisk> extdisks) {
		this.extdisks = extdisks;
	}
	
	public Integer getVm_type() {
		return vm_type;
	}

	public void setVm_type(Integer vm_type) {
		this.vm_type = vm_type;
	}

	public Integer getVm_business_status() {
		return vm_business_status;
	}

	public void setVm_business_status(Integer vm_business_status) {
		this.vm_business_status = vm_business_status;
	}
	
	public Integer getTryLong() {
		return tryLong;
	}

	public void setTryLong(Integer tryLong) {
		this.tryLong = tryLong;
	}

	public Integer getScId() {
		return scId;
	}

	public void setScId(Integer scId) {
		this.scId = scId;
	}
	
	public String getVmZone() {
		return vmZone;
	}

	public void setVmZone(String vmZone) {
		this.vmZone = vmZone;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getOutComments() {
		return outComments;
	}

	public void setOutComments(String outComments) {
		this.outComments = outComments;
	}
	
	public String getVm_status() {
		return vm_status;
	}

	public void setVm_status(String vm_status) {
		this.vm_status = vm_status;
	}

	public String getVm_task_status() {
		return vm_task_status;
	}

	public void setVm_task_status(String vm_task_status) {
		this.vm_task_status = vm_task_status;
	}

	public String getVm_innerIP() {
		return vm_innerIP;
	}

	public void setVm_innerIP(String vm_innerIP) {
		this.vm_innerIP = vm_innerIP;
	}

	public String getVm_outerIP() {
		return vm_outerIP;
	}

	public void setVm_outerIP(String vm_outerIP) {
		this.vm_outerIP = vm_outerIP;
	}

	public String getRadom_user() {
		return radom_user;
	}

	public void setRadom_user(String radom_user) {
		this.radom_user = radom_user;
	}

	public String getRadom_pwd() {
		return radom_pwd;
	}

	public void setRadom_pwd(String radom_pwd) {
		this.radom_pwd = radom_pwd;
	}
	
	public Integer getBwtIn() {
		return bwtIn;
	}

	public void setBwtIn(Integer bwtIn) {
		this.bwtIn = bwtIn;
	}

	public Integer getBwtOut() {
		return bwtOut;
	}

	public void setBwtOut(Integer bwtOut) {
		this.bwtOut = bwtOut;
	}

	public Integer getIpConnIn() {
		return ipConnIn;
	}

	public void setIpConnIn(Integer ipConnIn) {
		this.ipConnIn = ipConnIn;
	}

	public Integer getIpConnOut() {
		return ipConnOut;
	}

	public void setIpConnOut(Integer ipConnOut) {
		this.ipConnOut = ipConnOut;
	}

	public Integer getTcpConnIn() {
		return tcpConnIn;
	}

	public void setTcpConnIn(Integer tcpConnIn) {
		this.tcpConnIn = tcpConnIn;
	}

	public Integer getTcpConnOut() {
		return tcpConnOut;
	}

	public void setTcpConnOut(Integer tcpConnOut) {
		this.tcpConnOut = tcpConnOut;
	}

	public Integer getUdpConnIn() {
		return udpConnIn;
	}

	public void setUdpConnIn(Integer udpConnIn) {
		this.udpConnIn = udpConnIn;
	}

	public Integer getUdpConnOut() {
		return udpConnOut;
	}

	public void setUdpConnOut(Integer udpConnOut) {
		this.udpConnOut = udpConnOut;
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

	public boolean isNeedToFindOptimZone() {
		return needToFindOptimZone;
	}

	public void setNeedToFindOptimZone(boolean needToFindOptimZone) {
		this.needToFindOptimZone = needToFindOptimZone;
	}
	
	public Integer getCreateflag() {
		return createflag;
	}

	public void setCreateflag(Integer createflag) {
		this.createflag = createflag;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
	
	public Integer getTryTime() {
		return tryTime;
	}

	public void setTryTime(Integer tryTime) {
		this.tryTime = tryTime;
	}
	
	public Integer getIpNum() {
		return ipNum;
	}

	public void setIpNum(Integer ipNum) {
		this.ipNum = ipNum;
	}
	
	public Integer getBuyType() {
		return buyType;
	}

	public void setBuyType(Integer buyType) {
		this.buyType = buyType;
	}

	public String getProcessState() {
		return processState;
	}

	public void setProcessState(String processState) {
		this.processState = processState;
	}

	@Override
	public String toString() {
		return "VpdcReference [instance=" + instance + ", extdisks=" + extdisks
				+ ", vpdc=" + vpdc + ", imageId=" + imageId + ", osId=" + osId
				+ ", flavorId=" + flavorId + ", cpu_core=" + cpu_core
				+ ", cpu_type=" + cpu_type + ", disk_capacity=" + disk_capacity
				+ ", disk_type=" + disk_type + ", mem_size=" + mem_size
				+ ", mem_type=" + mem_type + ", network_bandwidth="
				+ network_bandwidth + ", network_type=" + network_type
				+ ", isEnable=" + isEnable + ", status=" + status + ", owner="
				+ owner + ", createrType=" + createrType + ", vm_type="
				+ vm_type + ", vm_business_status=" + vm_business_status
				+ ", tryLong=" + tryLong + ", scId=" + scId + ", vmZone="
				+ vmZone + ", comments=" + comments + ", vm_status="
				+ vm_status + ", vm_task_status=" + vm_task_status
				+ ", vm_innerIP=" + vm_innerIP + ", vm_outerIP=" + vm_outerIP
				+ ", radom_user=" + radom_user + ", radom_pwd=" + radom_pwd
				+ ", bwtIn=" + bwtIn + ", bwtOut=" + bwtOut + ", ipConnIn="
				+ ipConnIn + ", ipConnOut=" + ipConnOut + ", tcpConnIn="
				+ tcpConnIn + ", tcpConnOut=" + tcpConnOut + ", udpConnIn="
				+ udpConnIn + ", udpConnOut=" + udpConnOut 
				+ ", cpuLimit=" + cpuLimit +", diskRead=" + diskRead +", diskWrite=" + diskWrite +"]";
	}
}

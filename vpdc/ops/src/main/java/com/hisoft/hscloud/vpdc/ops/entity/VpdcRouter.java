package com.hisoft.hscloud.vpdc.ops.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;
@Entity
@Table(name = "hc_vpdc_vrouter")
public class VpdcRouter extends AbstractEntity {
	@Column(name = "router_uuid")
	private String routerUUID;
	
	@Column(name = "cpu_core")
	private Integer cpuCore;

	@Column(name = "disk_capacity")
	private Integer diskCapacity;
	
	@Column(name = "mem_size")
	private Integer memSize;

	@Column(name = "band_width")
	private Integer bandWidth;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "vrouterTemplate")
	private VpdcVrouterTemplate vrouterTemplate;
	
	@Column(name = "flavorId")
	private Integer flavorId;
	
	@Column(name = "imageId")
	private String imageId;
	
	@Column(name = "osId")
	private Integer osId;
	
	@Column(name = "nodeName")
	private String nodeName;//节点名称（需要回写）
	
	@Column(name = "zone")
	private String zone;
	
	@Column(name = "buyLong")
	private Integer buyLong;//购买时长（单位：月）
	
	@Column(name = "is_enable")
	private int isEnable;//是否禁用（0：正常；1：手动禁用；2：到期禁用）
	
	@Column(name = "router_owner")
	private Long routerOwner;
	
	@Column(name = "fixIP")
	private String fixIP;//内网IP（需要回写）
	
	@Column(name = "floatingIP")
	private String floatingIP;
	
	@Column(name = "router_status")
	private String routerStatus;
	
	@Column(name = "router_task_status")
	private String routerTaskStatus;
	
	@Column(name = "comments")
	private String comments;
	
	@Column(name = "deleted")
	private int deleted;
	
	@Column(name = "process_state")
	private String processState;
	
	@Column(name = "event_time")
	private Date eventTime;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "vpdc_id")
	private Vpdc vpdc;
	
	public String getRouterUUID() {
		return routerUUID;
	}

	public void setRouterUUID(String routerUUID) {
		this.routerUUID = routerUUID;
	}

	public Integer getCpuCore() {
		return cpuCore;
	}

	public void setCpuCore(Integer cpuCore) {
		this.cpuCore = cpuCore;
	}

	public Integer getDiskCapacity() {
		return diskCapacity;
	}

	public void setDiskCapacity(Integer diskCapacity) {
		this.diskCapacity = diskCapacity;
	}

	public Integer getMemSize() {
		return memSize;
	}

	public void setMemSize(Integer memSize) {
		this.memSize = memSize;
	}

	public Integer getBandWidth() {
		return bandWidth;
	}

	public void setBandWidth(Integer bandWidth) {
		this.bandWidth = bandWidth;
	}

	public VpdcVrouterTemplate getVrouterTemplate() {
		return vrouterTemplate;
	}

	public void setVrouterTemplate(VpdcVrouterTemplate vrouterTemplate) {
		this.vrouterTemplate = vrouterTemplate;
	}

	

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public Long getRouterOwner() {
		return routerOwner;
	}

	public void setRouterOwner(Long routerOwner) {
		this.routerOwner = routerOwner;
	}

	public String getFixIP() {
		return fixIP;
	}

	public void setFixIP(String fixIP) {
		this.fixIP = fixIP;
	}

	public String getFloatingIP() {
		return floatingIP;
	}

	public void setFloatingIP(String floatingIP) {
		this.floatingIP = floatingIP;
	}

	public String getRouterStatus() {
		return routerStatus;
	}

	public void setRouterStatus(String routerStatus) {
		this.routerStatus = routerStatus;
	}

	public String getRouterTaskStatus() {
		return routerTaskStatus;
	}

	public void setRouterTaskStatus(String routerTaskStatus) {
		this.routerTaskStatus = routerTaskStatus;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Vpdc getVpdc() {
		return vpdc;
	}

	public void setVpdc(Vpdc vpdc) {
		this.vpdc = vpdc;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public Integer getFlavorId() {
		return flavorId;
	}

	public void setFlavorId(Integer flavorId) {
		this.flavorId = flavorId;
	}

	public Integer getOsId() {
		return osId;
	}

	public void setOsId(Integer osId) {
		this.osId = osId;
	}

	public Integer getBuyLong() {
		return buyLong;
	}

	public void setBuyLong(Integer buyLong) {
		this.buyLong = buyLong;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public String getProcessState() {
		return processState;
	}

	public void setProcessState(String processState) {
		this.processState = processState;
	}
	
}

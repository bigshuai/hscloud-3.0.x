/**
* @title ServerNode.java
* @package com.hisoft.hscloud.bss.sla.sc.entity
* @description 用一句话描述该文件做什么
* @author jiaquan.hu
* @update 2012-5-7 下午5:47:53
* @version V1.0
*/
package com.hisoft.hscloud.bss.sla.sc.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-5-7 下午5:47:53
 */
@Entity
@Table(name="hc_node")
public class ServerNode {
	private long id;
	private String name;//节点名称
	private String nodeAliases;//节点别名
	private String cpuInfo;//CPU核数
	private String cpuType;//CPU类型
	private String ramInfo;//MEM大小
	private String diskInfo;//disk大小
	private String ip;//节点外网IP
	private String innerIP;//节点内网IP
	private int cpuRate;//CPU配比
	private int ramRate;//MEM配比
	private int diskRate;//disk配比
	private Date createDate = new Date();//创建时间
	private Date updateDate = new Date();//更新时间
	private String description;//描述
	private String zone;//区域
	private int hostStatus = 0;
	//
	private ServerZone serverZone;//Zone信息
	private IPMIConfig ipmiConfig;//IPMI配置信息
	private NodeIsolationConfig nodeIsolationConfig;//资源隔离配置信息
	private int isEnable = 0;//启用/禁用节点（0：启用；1：禁用）
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(length=50,unique=true, nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(length=50, nullable = false)
	public String getNodeAliases() {
		return nodeAliases;
	}
	public void setNodeAliases(String nodeAliases) {
		this.nodeAliases = nodeAliases;
	}
	@Column(name="cpu_info",length=50, nullable = false,columnDefinition="INT default 0")
	public String getCpuInfo() {
		return cpuInfo;
	}
	public void setCpuInfo(String cpuInfo) {
		this.cpuInfo = cpuInfo;
	}
	public String getCpuType() {
		return cpuType;
	}
	public void setCpuType(String cpuType) {
		this.cpuType = cpuType;
	}
	@Column(name="ram_info",length=50, nullable = false,columnDefinition="INT default 0")
	public String getRamInfo() {
		return ramInfo;
	}
	public void setRamInfo(String ramInfo) {
		this.ramInfo = ramInfo;
	}
	@Column(name="disk_info",length=50, nullable = false,columnDefinition="INT default 0")
	public String getDiskInfo() {
		return diskInfo;
	}
	public void setDiskInfo(String diskInfo) {
		this.diskInfo = diskInfo;
	}
	@Column(length=50, nullable = false)
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Column(length=50)
	public String getInnerIP() {
		return innerIP;
	}
	public void setInnerIP(String innerIP) {
		this.innerIP = innerIP;
	}
	@Column(name="cpu_rate",length=3, nullable = false,columnDefinition="INT default 0")
	public int getCpuRate() {
		return cpuRate;
	}
	public void setCpuRate(int cpuRate) {
		this.cpuRate = cpuRate;
	}
	@Column(name="ram_rate",length=3, nullable = false,columnDefinition="INT default 0")
	public int getRamRate() {
		return ramRate;
	}
	public void setRamRate(int ramRate) {
		this.ramRate = ramRate;
	}
	@Column(name="disk_rate",length=3, nullable = false,columnDefinition="INT default 0")
	public int getDiskRate() {
		return diskRate;
	}
	public void setDiskRate(int diskRate) {
		this.diskRate = diskRate;
	}
	@Column(name="create_date", nullable = false)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name="update_date", nullable = false)
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Column(length=100)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	@Column(name="host_status",nullable=false,columnDefinition="INT default 0")
	public int getHostStatus() {
		return hostStatus;
	}
	public void setHostStatus(int hostStatus) {
		this.hostStatus = hostStatus;
	}
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="hc_node_zone",
	joinColumns=@JoinColumn(name="node_id",referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="zone_id",referencedColumnName="id"))
	public ServerZone getServerZone() {
		return serverZone;
	}
	public void setServerZone(ServerZone serverZone) {
		this.serverZone = serverZone;
	}
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="ipmiConfig_id",referencedColumnName="id",unique=true)
	public IPMIConfig getIpmiConfig() {
		return ipmiConfig;
	}
	public void setIpmiConfig(IPMIConfig ipmiConfig) {
		this.ipmiConfig = ipmiConfig;
	}
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="nodeIsolationConfig_id",referencedColumnName="id",unique=true)
	public NodeIsolationConfig getNodeIsolationConfig() {
		return nodeIsolationConfig;
	}
	public void setNodeIsolationConfig(NodeIsolationConfig nodeIsolationConfig) {
		this.nodeIsolationConfig = nodeIsolationConfig;
	}
	@Column(name="is_enable",nullable=false,columnDefinition="INT default 0")
	public int getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}	

}

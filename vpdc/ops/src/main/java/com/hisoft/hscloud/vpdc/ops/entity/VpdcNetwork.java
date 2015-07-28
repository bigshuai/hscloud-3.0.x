package com.hisoft.hscloud.vpdc.ops.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;

@Entity
@Table(name = "hc_vpdc_network")
public class VpdcNetwork  extends AbstractEntity {
	@Column(name = "label")
	private String label;//网络类型；必需。Lan(系统将创建一个内网网段)；wan(系统将创建一个外网网段，且为1对1关系) 

	@Column(name = "ipRange_id")
	private Long ipRangeId;
	
	@Column(name = "dns1")
	private String dns1;//域名服务器1
	
	@Column(name = "dns2")
	private String dns2;//域名服务器2
	
	@Column(name = "network_size")
	private int networkSize;//网络尺寸大小；如果label为lan，此项必需.
	
	@Column(name = "cidr")
	private String cidr;//子网网段；如果label为wan，此项必需，否则可选；系统依赖此参数，创建外网网段
	
	@Column(name = "gateway")
	private String gateway;//网关；如果label为wan，此项必需，否则可选；系统根据此参数，静态指定网段的网关，如果此参数为空，系统将从网段内动态分配一个地址作为此网段的网关
	
	@Column(name = "bridge")
	private String bridge;//网段所在网桥名称；此项可选；如果为空，系统将使用默认值，默认值依赖于网络的类型，lan类型的网段，默认值为br101，wan类型的网段，默认值br102；
	
	@Column(name = "bridge_interface")
	private String bridgeInterface;//网桥的物理端口
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name="securyty_lan") 
	private VpdcLan securytyVlan;//创建Lan返回值，创建路由绑定LanNetwork时用
	
	@Column(name = "vlan")
	private int vlanId;//创建LanNetwork返回值，业务暂不用
	
	@Column(name = "injected")
	private Boolean injected;//网络配置静态注入标志；此项可选
	
	@Column(name = "dhcp_disabled")
	private Boolean dhcpDisabled;//此网段是否禁用dhcp服务；此项可选；默认值true；
	
	@Column(name = "network_id")
	private String networkId;
	
	@Column(name = "netmask")
	private String netmask;//子网掩码
	
	@Column(name = "broadcast")
	private String broadcast;//广播地址
	
	@Column(name = "dhcp_start")
	private String dhcptart;//Dhcp分配起始地址
	
	@Column(name = "project_id")
	private String projectId;//所属project
	
	//0:有效,1:删除 
	@Column(name = "deleted")
	private int deleted;
	
	@Column(name = "zoneGroup_id")
	private Long zoneGroup;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Long getIpRangeId() {
		return ipRangeId;
	}

	public void setIpRangeId(Long ipRangeId) {
		this.ipRangeId = ipRangeId;
	}

	public String getDns1() {
		return dns1;
	}

	public void setDns1(String dns1) {
		this.dns1 = dns1;
	}

	public String getDns2() {
		return dns2;
	}

	public void setDns2(String dns2) {
		this.dns2 = dns2;
	}

	public int getNetworkSize() {
		return networkSize;
	}

	public void setNetworkSize(int networkSize) {
		this.networkSize = networkSize;
	}

	public String getCidr() {
		return cidr;
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getBridge() {
		return bridge;
	}

	public void setBridge(String bridge) {
		this.bridge = bridge;
	}

	public String getBridgeInterface() {
		return bridgeInterface;
	}

	public void setBridgeInterface(String bridgeInterface) {
		this.bridgeInterface = bridgeInterface;
	}
	
	public VpdcLan getSecurytyVlan() {
		return securytyVlan;
	}

	public void setSecurytyVlan(VpdcLan securytyVlan) {
		this.securytyVlan = securytyVlan;
	}

	public int getVlanId() {
		return vlanId;
	}

	public void setVlanId(int vlanId) {
		this.vlanId = vlanId;
	}

	public Boolean getInjected() {
		return injected;
	}

	public void setInjected(Boolean injected) {
		this.injected = injected;
	}

	public Boolean getDhcpDisabled() {
		return dhcpDisabled;
	}

	public void setDhcpDisabled(Boolean dhcpDisabled) {
		this.dhcpDisabled = dhcpDisabled;
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(String broadcast) {
		this.broadcast = broadcast;
	}

	public String getDhcptart() {
		return dhcptart;
	}

	public void setDhcptart(String dhcptart) {
		this.dhcptart = dhcptart;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public Long getZoneGroup() {
		return zoneGroup;
	}

	public void setZoneGroup(Long zoneGroup) {
		this.zoneGroup = zoneGroup;
	}

}

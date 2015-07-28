package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("network")
public class Network implements Serializable {
	@JsonProperty("bridge")
	private String bridge;
	@JsonProperty("bridge_interface")
	private String bridgeInterface;
	@JsonProperty("broadcast")
	private String broadcast;
	@JsonProperty("cidr")
	private String cidr;
	@JsonProperty("dhcp_start")
	private String dhcpStart;
	@JsonProperty("dns1")
	private String dns1;
	@JsonProperty("dns2")
	private String dns2;
	@JsonProperty("network_size")
	private Integer networkSize;
	@JsonProperty("gateway")
	private String gateway;
	@JsonProperty("id")
	private String id;
	@JsonProperty("injected")
	private Boolean injected;
	@JsonProperty("label")
	private String label;
	@JsonProperty("netmask")
	private String netmask;
	@JsonProperty("project_id")
	private String projectId;
	@JsonProperty("vlan")
	private Integer vlan;
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
	public String getBroadcast() {
		return broadcast;
	}
	public void setBroadcast(String broadcast) {
		this.broadcast = broadcast;
	}
	public String getCidr() {
		return cidr;
	}
	public void setCidr(String cidr) {
		this.cidr = cidr;
	}
	public String getDhcpStart() {
		return dhcpStart;
	}
	public void setDhcpStart(String dhcpStart) {
		this.dhcpStart = dhcpStart;
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
	
	public Integer getNetworkSize() {
		return networkSize;
	}
	public void setNetworkSize(Integer networkSize) {
		this.networkSize = networkSize;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Boolean getInjected() {
		return injected;
	}
	public void setInjected(Boolean injected) {
		this.injected = injected;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getNetmask() {
		return netmask;
	}
	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Integer getVlan() {
		return vlan;
	}
	public void setVlan(Integer vlan) {
		this.vlan = vlan;
	}
	@Override
	public String toString() {
		return "Network [bridge=" + bridge + ", bridgeInterface="
				+ bridgeInterface + ", broadcast=" + broadcast + ", cidr="
				+ cidr + ", dhcpStart=" + dhcpStart + ", dns1=" + dns1
				+ ", dns2=" + dns2 + ", networkSize=" + networkSize
				+ ", gateway=" + gateway + ", id=" + id + ", injected="
				+ injected + ", label=" + label + ", netmask=" + netmask
				+ ", projectId=" + projectId + ", vlan=" + vlan + "]";
	}
}

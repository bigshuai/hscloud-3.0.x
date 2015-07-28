package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("security_lan")
public class SecurityLan implements Serializable {
	@JsonProperty("id")
	private int id;
	@JsonProperty("type")
	private String type;
	@JsonProperty("router_uuid")
	private String routerUUID;
	@JsonProperty("network_uuid")
	private String networkUUID;
	@JsonProperty("router_vif_uuid")
	private String routerVifUUID;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRouterUUID() {
		return routerUUID;
	}
	public void setRouterUUID(String routerUUID) {
		this.routerUUID = routerUUID;
	}
	public String getNetworkUUID() {
		return networkUUID;
	}
	public void setNetworkUUID(String networkUUID) {
		this.networkUUID = networkUUID;
	}
	public String getRouterVifUUID() {
		return routerVifUUID;
	}
	public void setRouterVifUUID(String routerVifUUID) {
		this.routerVifUUID = routerVifUUID;
	}
	@Override
	public String toString() {
		return "SecurityLan [id=" + id + ", type=" + type + ", routerUUID="
				+ routerUUID + ", networkUUID=" + networkUUID
				+ ", routerVifUUID=" + routerVifUUID + "]";
	}
	
}

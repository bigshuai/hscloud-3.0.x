package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("floating_ip_detail")
public class FloatingIpDetail extends FloatingIpBase implements Serializable{

	@JsonProperty("tenant_id")
	private String tenantId;
	@JsonProperty("instance_id")
	private String instanceId;
	private String pool;
	@JsonProperty("interface")
	private String NicInterface;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getPool() {
		return pool;
	}

	public void setPool(String pool) {
		this.pool = pool;
	}

	public String getNicInterface() {
		return NicInterface;
	}

	public void setNicInterface(String nicInterface) {
		NicInterface = nicInterface;
	}

	@Override
	public String toString() {
		return "FloatingIpDetail [tenantId=" + tenantId + ", instanceId="
				+ instanceId + ", pool=" + pool + ", NicInterface="
				+ NicInterface + " " +super.getFloatingIp() +"]";
	}

}

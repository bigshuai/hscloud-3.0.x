package org.openstack.model.compute.nova.floatingip;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("floating_ip")
public class NovaFloatingIpForAssign implements Serializable {

	@JsonProperty("tenant_id")
	private String tenantId;

	@JsonProperty("assign_ip")
	private String floatingIp;

	@JsonProperty("pool")
	private String pool;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getFloatingIp() {
		return floatingIp;
	}

	public void setFloatingIp(String floatingIp) {
		this.floatingIp = floatingIp;
	}

	public String getPool() {
		return pool;
	}

	public void setPool(String pool) {
		this.pool = pool;
	}

	@Override
	public String toString() {
		return "NovaFloatingIpForCreate [tenantId=" + tenantId
				+ ", floatingIp=" + floatingIp + "]";
	}

}

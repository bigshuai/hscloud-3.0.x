package org.openstack.model.hscloud.impl;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("floating_ip")
public class FloatingIpBase {

	@JsonProperty("floating_ip")
	private String floatingIp;

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFloatingIp() {
		return floatingIp;
	}

	public void setFloatingIp(String floatingIp) {
		this.floatingIp = floatingIp;
	}

	@Override
	public String toString() {
		return "FloatingIpBase [floatingIp=" + floatingIp + ", id=" + id + "]";
	}

}

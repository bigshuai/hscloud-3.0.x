package org.openstack.model.compute.nova.server.actions;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.compute.ServerAction;
import org.openstack.model.compute.nova.NovaServer;

@JsonRootName("os-cold-migrate")
public class OsColdMigateAction implements Serializable, ServerAction {
	@JsonProperty("dest_host")
	private String destHost;
	@JsonProperty("dest_host_ip")
	private String destHostIp;
	public String getDestHost() {
		return destHost;
	}

	public void setDestHost(String destHost) {
		this.destHost = destHost;
	}

	public String getDestHostIp() {
		return destHostIp;
	}

	public void setDestHostIp(String destHostIp) {
		this.destHostIp = destHostIp;
	}

	@Override
	public Class<? extends Serializable> getReturnType() {
		return null;
	}

}

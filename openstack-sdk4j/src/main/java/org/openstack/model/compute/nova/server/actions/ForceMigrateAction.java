package org.openstack.model.compute.nova.server.actions;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.compute.ServerAction;
import org.openstack.model.compute.nova.NovaServer;

@JsonIgnoreProperties("returnType")
@JsonRootName("migrate2")
public class ForceMigrateAction implements Serializable, ServerAction {

	@JsonProperty("force_hosts")
	private String forceHost;

	public String getForceHost() {
		return forceHost;
	}

	public void setForceHost(String forceHost) {
		this.forceHost = forceHost;
	}

	@Override
	public Class<? extends Serializable> getReturnType() {
		return null;
	}

}

package org.openstack.model.compute.nova.server.actions;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.compute.ServerAction;
import org.openstack.model.compute.nova.NovaServer;

@JsonRootName("inject_admin_pass")
public class InjectAdminPasswd implements Serializable, ServerAction {

	@JsonProperty("admin_pass")
	private String adminPasswd;

	public String getAdminPasswd() {
		return adminPasswd;
	}

	public void setAdminPasswd(String adminPasswd) {
		this.adminPasswd = adminPasswd;
	}

	@Override
	@JsonIgnore
	public Class<? extends Serializable> getReturnType() {
		return NovaServer.class;
	}
}

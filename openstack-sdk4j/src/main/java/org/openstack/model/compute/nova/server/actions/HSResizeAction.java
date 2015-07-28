package org.openstack.model.compute.nova.server.actions;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.compute.ServerAction;

@JsonRootName("hs_resize")
public class HSResizeAction implements Serializable, ServerAction {

	@JsonProperty("flavor_id")
	private int flavorID;

	public int getFlavorID() {
		return flavorID;
	}

	public void setFlavorID(int flavorID) {
		this.flavorID = flavorID;
	}

	@Override
	public Class<? extends Serializable> getReturnType() {
		return null;
	}

}

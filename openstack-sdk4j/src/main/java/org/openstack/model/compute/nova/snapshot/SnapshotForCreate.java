package org.openstack.model.compute.nova.snapshot;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class SnapshotForCreate implements Serializable {

	@JsonProperty("name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

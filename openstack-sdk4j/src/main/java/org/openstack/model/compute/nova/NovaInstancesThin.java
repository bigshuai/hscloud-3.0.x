package org.openstack.model.compute.nova;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class NovaInstancesThin implements Serializable {

	@JsonProperty("result")
	private List<NovaInstanceThin> list = new ArrayList<NovaInstanceThin>();

	public List<NovaInstanceThin> getList() {
		return list;
	}

	public void setList(List<NovaInstanceThin> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "NovaInstancesThin [list=" + list + "]";
	}

}

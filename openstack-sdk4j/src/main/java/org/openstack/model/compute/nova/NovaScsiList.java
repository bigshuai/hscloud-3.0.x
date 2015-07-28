package org.openstack.model.compute.nova;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class NovaScsiList implements Serializable {

	@JsonProperty("res")
	private List<NovaScsi> list = new ArrayList<NovaScsi>();

	@JsonProperty("volume_name")
	private List<String> volumeList = new ArrayList<String>();
	
	public List<NovaScsi> getList() {
		return list;
	}

	public void setList(List<NovaScsi> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "NovaScsiList [list=" + list + "]";
	}

}

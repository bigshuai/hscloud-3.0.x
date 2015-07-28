package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class FloatingIpList implements Serializable {

	@JsonProperty("floating_ips")
	private List<FloatingIpBase> list = new ArrayList<FloatingIpBase>();

	public List<FloatingIpBase> getList() {
		return list;
	}

	public void setList(List<FloatingIpBase> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "FloatingIpList [list=" + list + "]";
	}

}

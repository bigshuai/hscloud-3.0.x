package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class FloatingIpDetailList implements Serializable {

	@JsonProperty("floating_ip_detail")
	private List<FloatingIpDetail> list = new ArrayList<FloatingIpDetail>();

	public List<FloatingIpDetail> getList() {
		return list;
	}

	public void setList(List<FloatingIpDetail> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "FloatingIpDetailList [list=" + list + "]";
	}

}

package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class HostList implements Serializable {

	@JsonProperty("hosts")
	private List<HostBasic> list = new ArrayList<HostBasic>();

	/**
	 * @return the list
	 */
	public List<HostBasic> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<HostBasic> list) {
		this.list = list;
	}

}

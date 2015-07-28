package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class HostGlobalAcquisition implements Serializable {

	@JsonProperty("hosts")
	List<HostAcquisition> list = new ArrayList<HostAcquisition>();

	/**
	 * @return the list
	 */
	public List<HostAcquisition> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<HostAcquisition> list) {
		this.list = list;
	}

}

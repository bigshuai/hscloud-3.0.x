package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class NETMonitorList implements Serializable {

	@JsonProperty("nets")
	private List<NETMonitor> list = new ArrayList<NETMonitor>();

	/**
	 * @return the list
	 */
	public List<NETMonitor> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<NETMonitor> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "NETMonitorList [list=" + list + "]";
	}

}

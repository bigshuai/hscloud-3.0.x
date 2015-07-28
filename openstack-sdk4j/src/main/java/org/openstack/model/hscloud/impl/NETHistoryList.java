package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class NETHistoryList implements Serializable {

	@JsonProperty("net_historys")
	private List<NETHistory> list = new ArrayList<NETHistory>();

	public NETHistoryList(){
		this.list = new ArrayList<NETHistory>();
	}
	
	/**
	 * @return the list
	 */
	public List<NETHistory> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<NETHistory> list) {
		this.list = list;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NETHistory [ " + list + " ]";
	}
	
	
}

package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class CPUHistoryList implements Serializable{

	@JsonProperty("cpu_historys")
	private List<CPUHistory> list = new ArrayList<CPUHistory>();

	public CPUHistoryList() {
		this.list = new ArrayList<CPUHistory>();
	}

	/**
	 * @return the list
	 */
	public List<CPUHistory> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<CPUHistory> list) {
		this.list = list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CPUHistoryList [ " + list + " ]";
	}

}

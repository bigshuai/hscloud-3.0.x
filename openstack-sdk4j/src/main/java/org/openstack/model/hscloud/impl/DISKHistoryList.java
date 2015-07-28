package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class DISKHistoryList implements Serializable {

	@JsonProperty("disk_historys")
	private List<DISKHistory> list = new ArrayList<DISKHistory>();

	public DISKHistoryList(){
		this.list = new ArrayList<DISKHistory>();
	}
	/**
	 * @return the list
	 */
	public List<DISKHistory> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<DISKHistory> list) {
		this.list = list;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DISKHistoryList [ " + list + " ]";
	}

	
}

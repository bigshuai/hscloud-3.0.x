package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class RAMHistoryList implements Serializable {

	@JsonProperty("ram_historys")
	private List<RAMHistory> list = new ArrayList<RAMHistory>();
	
	public RAMHistoryList(){
		this.list = new ArrayList<RAMHistory>(); 
	}

	/**
	 * @return the list
	 */
	public List<RAMHistory> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<RAMHistory> list) {
		this.list = list;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RAMHistoryList [ " + list + " ]";
	}
	
	
}

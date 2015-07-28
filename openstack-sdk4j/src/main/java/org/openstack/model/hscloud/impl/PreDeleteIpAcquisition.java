package org.openstack.model.hscloud.impl;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("pre_floating_ip")
public class PreDeleteIpAcquisition implements Serializable {

	private int total;
	@JsonProperty("will_delete")
	private int willDelete;
	private int deleted;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getWillDelete() {
		return willDelete;
	}

	public void setWillDelete(int willDelete) {
		this.willDelete = willDelete;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "PreDeleteIpAcquisition [total=" + total + ", willDelete="
				+ willDelete + ", deleted=" + deleted + "]";
	}

}

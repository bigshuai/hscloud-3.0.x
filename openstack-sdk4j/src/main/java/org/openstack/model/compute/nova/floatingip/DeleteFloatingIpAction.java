package org.openstack.model.compute.nova.floatingip;

import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("delete_floating_ips")
public class DeleteFloatingIpAction {
	private String from;
	private String to;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	@Override
	public String toString() {
		return "DeleteFloatingIpAction [from=" + from + ", to=" + to + "]";
	}

}

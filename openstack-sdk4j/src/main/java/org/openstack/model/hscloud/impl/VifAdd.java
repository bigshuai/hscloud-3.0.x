package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
@JsonRootName("add_vif")
public class VifAdd implements Serializable {
	@JsonProperty("requested_networks")
	private List<RequestedNetwork> requestedNetworks;
	public static final class RequestedNetwork implements Serializable {
		@JsonProperty("uuid")
		private String networkId;
		@JsonProperty("security_lan")
		private Integer lanId;
		public String getNetworkId() {
			return networkId;
		}
		public void setNetworkId(String networkId) {
			this.networkId = networkId;
		}
		public Integer getLanId() {
			return lanId;
		}
		public void setLanId(Integer lanId) {
			this.lanId = lanId;
		}
	}
	public List<RequestedNetwork> getRequestedNetworks() {
		return requestedNetworks;
	}

	public void setRequestedNetworks(List<RequestedNetwork> requestedNetworks) {
		this.requestedNetworks = requestedNetworks;
	}
}

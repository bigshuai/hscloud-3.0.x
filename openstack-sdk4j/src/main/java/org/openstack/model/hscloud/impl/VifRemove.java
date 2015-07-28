package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
@JsonRootName("del_vif")
public class VifRemove implements Serializable {
	@JsonProperty("requested_networks")
	private List<RequestedNetwork> requestedNetworks;
	public static final class RequestedNetwork implements Serializable {
		@JsonProperty("uuid")
		private String networkId;
		@JsonProperty("fixed_ip")
		private String fixedIp;
		public String getNetworkId() {
			return networkId;
		}
		public void setNetworkId(String networkId) {
			this.networkId = networkId;
		}
		public String getFixedIp() {
			return fixedIp;
		}
		public void setFixedIp(String fixedIp) {
			this.fixedIp = fixedIp;
		}
	}
	public List<RequestedNetwork> getRequestedNetworks() {
		return requestedNetworks;
	}

	public void setRequestedNetworks(List<RequestedNetwork> requestedNetworks) {
		this.requestedNetworks = requestedNetworks;
	}
}

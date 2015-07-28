package org.openstack.model.compute.nova.server.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.compute.ServerAction;

@JsonRootName("setSecurityIngressRules")
public class SecurityIngressRules implements Serializable, ServerAction {

	@Override
	public Class<? extends Serializable> getReturnType() {
		return null;
	}

	public static final class IngressRule implements Serializable {

		public class IpRange {
			private String cidr;

			public String getCidr() {
				return cidr;
			}

			public void setCidr(String cidr) {
				this.cidr = cidr;
			}

		}

		@JsonProperty("from_port")
		private int fromPort;
		@JsonProperty("to_port")
		private int toPort;
		@JsonProperty("ip_protocol")
		private String ipProtocol;
		@JsonProperty("ip_range")
		private IpRange ipRange = new IpRange();

		public int getFromPort() {
			return fromPort;
		}

		public void setFromPort(int fromPort) {
			this.fromPort = fromPort;
		}

		public int getToPort() {
			return toPort;
		}

		public void setToPort(int toPort) {
			this.toPort = toPort;
		}

		public String getIpProtocol() {
			return ipProtocol;
		}

		public void setIpProtocol(String ipProtocol) {
			this.ipProtocol = ipProtocol;
		}

		public IpRange getIpRange() {
			return ipRange;
		}

		public void setIpRange(IpRange ipRange) {
			this.ipRange = ipRange;
		}

	}

	@JsonProperty("ingress_rule")
	private List<IngressRule> ingressRules;

	public List<IngressRule> getIngressRules() {
		return ingressRules;
	}

	public void setIngressRules(List<IngressRule> ingressRules) {
		this.ingressRules = ingressRules;
	}

}

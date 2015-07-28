package org.openstack.model.compute.nova.server.actions;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.compute.nova.server.actions.SecurityRules.ConnlimitRule;

@JsonRootName("setSecurityConnlimitRules")
public class SetSecurityConnlimitRules {
	@JsonProperty("connlimit_rule")
	private ConnlimitRule connlimitRule = new ConnlimitRule();
	
	public ConnlimitRule getConnlimitRule() {
		return connlimitRule;
	}

	public void setConnlimitRule(ConnlimitRule connlimitRule) {
		this.connlimitRule = connlimitRule;
	}

	public static final class ConnlimitRule {
		@JsonProperty("ip_in")
		private int ipIn;
		@JsonProperty("ip_out")
		private int ipOut;
		@JsonProperty("tcp_in")
		private int tcpIn;
		@JsonProperty("tcp_out")
		private int tcpOut;
		@JsonProperty("udp_in")
		private int udpIn;
		@JsonProperty("udp_out")
		private int udpOut;

		public int getIpIn() {
			return ipIn;
		}

		public void setIpIn(int ipIn) {
			this.ipIn = ipIn;
		}

		public int getIpOut() {
			return ipOut;
		}

		public void setIpOut(int ipOut) {
			this.ipOut = ipOut;
		}

		public int getTcpIn() {
			return tcpIn;
		}

		public void setTcpIn(int tcpIn) {
			this.tcpIn = tcpIn;
		}

		public int getTcpOut() {
			return tcpOut;
		}

		public void setTcpOut(int tcpOut) {
			this.tcpOut = tcpOut;
		}

		public int getUdpIn() {
			return udpIn;
		}

		public void setUdpIn(int udpIn) {
			this.udpIn = udpIn;
		}

		public int getUdpOut() {
			return udpOut;
		}

		public void setUdpOut(int udpOut) {
			this.udpOut = udpOut;
		}

	}
}

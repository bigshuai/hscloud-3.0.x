package org.openstack.model.compute.nova.server.actions;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("security_rules")
public class SecurityRules {
	public static final class BandwidthRule implements Serializable {
		@JsonProperty("bwt_in")
		private int bwtIn;
		@JsonProperty("bwt_out")
		private int bwtOut;

		public int getBwtIn() {
			return bwtIn;
		}

		public void setBwtIn(int bwtIn) {
			this.bwtIn = bwtIn;
		}

		public int getBwtOut() {
			return bwtOut;
		}

		public void setBwtOut(int bwtOut) {
			this.bwtOut = bwtOut;
		}

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

	@JsonProperty("bandwidth_rule")
	private BandwidthRule bandwidthRule = new BandwidthRule();
	@JsonProperty("connlimit_rule")
	private ConnlimitRule connlimitRule = new ConnlimitRule();

	public BandwidthRule getBandwidthRule() {
		return bandwidthRule;
	}

	public void setBandwidthRule(BandwidthRule bandwidthRule) {
		this.bandwidthRule = bandwidthRule;
	}

	public ConnlimitRule getConnlimitRule() {
		return connlimitRule;
	}

	public void setConnlimitRule(ConnlimitRule connlimitRule) {
		this.connlimitRule = connlimitRule;
	}

}

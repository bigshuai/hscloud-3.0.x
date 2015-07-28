package org.openstack.model.compute.nova.server.actions;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.openstack.model.compute.ServerAction;
import org.openstack.model.compute.nova.server.actions.SecurityRules.BandwidthRule;

@JsonRootName("setSecurityBandwidthRules")
public class SetSecurityBandwidthRules{
	@JsonProperty("bandwidth_rule")
	private BandwidthRule bandwidthRule = new BandwidthRule();
	public BandwidthRule getBandwidthRule() {
		return bandwidthRule;
	}
	public void setBandwidthRule(BandwidthRule bandwidthRule) {
		this.bandwidthRule = bandwidthRule;
	}
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

}

package org.openstack.model.hscloud.impl;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;

@JsonRootName("historyMonitor")
public class HistoryMonitor implements Serializable {

	@JsonProperty("hN")
	private String hN;
	
	@JsonProperty("iT")
	private String iT;

	@JsonProperty("sT")
	private String sT;

	@JsonProperty("eT")
	private String eT;

	public String gethN() {
		return hN;
	}

	public void sethN(String hN) {
		this.hN = hN;
	}

	public String getiT() {
		return iT;
	}

	public void setiT(String iT) {
		this.iT = iT;
	}

	public String getsT() {
		return sT;
	}

	public void setsT(String sT) {
		this.sT = sT;
	}

	public String geteT() {
		return eT;
	}

	public void seteT(String eT) {
		this.eT = eT;
	}

	@Override
	public String toString() {
		return "HistoryMonitor [hN=" + hN + ", iT=" + iT + ", sT=" + sT
				+ ", eT=" + eT + "]";
	}

}

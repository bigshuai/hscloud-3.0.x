package com.hisoft.hscloud.vpdc.oss.monitoring.vo;
 public class NetworkDetailVO {

	 private String netName;
	 private double netRx;
	 private double netTx;
	 private double netTotal;
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public double getNetRx() {
		return netRx;
	}
	public void setNetRx(double netRx) {
		this.netRx = netRx;
	}
	public double getNetTx() {
		return netTx;
	}
	public void setNetTx(double netTx) {
		this.netTx = netTx;
	}
	public double getNetTotal() {
		return netTotal;
	}
	public void setNetTotal(double netTotal) {
		this.netTotal = netTotal;
	}
	@Override
	public String toString() {
		return "networkDetail [netName=" +netName
				+ ", netRx=" +netRx
				+ ", netTx=" +netTx
				+ ", netTotal=" +netTotal
				+"]";
	}
}

package com.hisoft.hscloud.vpdc.oss.monitoring.vo;
 public class DiskDetailVO {

	 private String diskName;
	 private double diskUsed;
	 private double diskTotal;
	 private double diskReadSpeed;
	 private double diskWriteSpeed;
	public String getDiskName() {
		return diskName;
	}
	public void setDiskName(String diskName) {
		this.diskName = diskName;
	}
	public double getDiskUsed() {
		return diskUsed;
	}
	public void setDiskUsed(double diskUsed) {
		this.diskUsed = diskUsed;
	}
	public double getDiskTotal() {
		return diskTotal;
	}
	public void setDiskTotal(double diskTotal) {
		this.diskTotal = diskTotal;
	}
	public double getDiskReadSpeed() {
		return diskReadSpeed;
	}
	public void setDiskReadSpeed(double diskReadSpeed) {
		this.diskReadSpeed = diskReadSpeed;
	}
	public double getDiskWriteSpeed() {
		return diskWriteSpeed;
	}
	public void setDiskWriteSpeed(double diskWriteSpeed) {
		this.diskWriteSpeed = diskWriteSpeed;
	}
	@Override
	public String toString() {
		return "diskDetail [diskName=" +diskName
				+ ", diskUsed=" +diskUsed
				+ ", diskTotal=" +diskTotal
				+ ", diskReadSpeed=" +diskReadSpeed
				+ ", diskWriteSpeed=" +diskWriteSpeed
				+"]";
	}
}

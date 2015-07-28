package com.hisoft.hscloud.common.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="hc_default_isolation_config")
public class DefaultIsolationConfig {
	private long id;//isolation记录Id
	private int cpuLimit;
	private int diskRead;
	private int diskWrite;
	private int bandWidthUp;
	private int bandWidthDown;
	private int ipConnectionUp;
	private int ipConnectionDown;
	private int tcpConnectionUp;
	private int tcpConnectionDown;
	private int udpConnectionUp;
	private int udpConnectionDown;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(name="cpu_limit",nullable=false,columnDefinition="INT default 0")
	public int getCpuLimit() {
		return cpuLimit;
	}
	public void setCpuLimit(int cpuLimit) {
		this.cpuLimit = cpuLimit;
	}
	@Column(name="disk_read",nullable=false,columnDefinition="INT default 0")
	public int getDiskRead() {
		return diskRead;
	}
	public void setDiskRead(int diskRead) {
		this.diskRead = diskRead;
	}
	@Column(name="disk_write",nullable=false,columnDefinition="INT default 0")
	public int getDiskWrite() {
		return diskWrite;
	}
	public void setDiskWrite(int diskWrite) {
		this.diskWrite = diskWrite;
	}
	@Column(name="bandwidth_up",nullable=false,columnDefinition="INT default 0")
	public int getBandWidthUp() {
		return bandWidthUp;
	}
	public void setBandWidthUp(int bandWidthUp) {
		this.bandWidthUp = bandWidthUp;
	}
	@Column(name="bandwidth_down",nullable=false,columnDefinition="INT default 0")
	public int getBandWidthDown() {
		return bandWidthDown;
	}
	public void setBandWidthDown(int bandWidthDown) {
		this.bandWidthDown = bandWidthDown;
	}
	@Column(name="ip_up",nullable=false,columnDefinition="INT default 0")
	public int getIpConnectionUp() {
		return ipConnectionUp;
	}
	public void setIpConnectionUp(int ipConnectionUp) {
		this.ipConnectionUp = ipConnectionUp;
	}
	@Column(name="ip_down",nullable=false,columnDefinition="INT default 0")
	public int getIpConnectionDown() {
		return ipConnectionDown;
	}
	public void setIpConnectionDown(int ipConnectionDown) {
		this.ipConnectionDown = ipConnectionDown;
	}
	@Column(name="tcp_up",nullable=false,columnDefinition="INT default 0")
	public int getTcpConnectionUp() {
		return tcpConnectionUp;
	}
	public void setTcpConnectionUp(int tcpConnectionUp) {
		this.tcpConnectionUp = tcpConnectionUp;
	}
	@Column(name="tcp_down",nullable=false,columnDefinition="INT default 0")
	public int getTcpConnectionDown() {
		return tcpConnectionDown;
	}
	public void setTcpConnectionDown(int tcpConnectionDown) {
		this.tcpConnectionDown = tcpConnectionDown;
	}
	@Column(name="udp_up",nullable=false,columnDefinition="INT default 0")
	public int getUdpConnectionUp() {
		return udpConnectionUp;
	}
	public void setUdpConnectionUp(int udpConnectionUp) {
		this.udpConnectionUp = udpConnectionUp;
	}
	@Column(name="udp_down",nullable=false,columnDefinition="INT default 0")
	public int getUdpConnectionDown() {
		return udpConnectionDown;
	}
	public void setUdpConnectionDown(int udpConnectionDown) {
		this.udpConnectionDown = udpConnectionDown;
	}
}


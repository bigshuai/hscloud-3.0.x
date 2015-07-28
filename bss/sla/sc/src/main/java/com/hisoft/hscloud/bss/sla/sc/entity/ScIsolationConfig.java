/* 
* 文 件 名:  ScIsolationConfig.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-8-5 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.entity; 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-8-5] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name="hc_catalog_isolation_config")
public class ScIsolationConfig {
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

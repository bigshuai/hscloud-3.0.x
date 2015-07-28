/* 
* 文 件 名:  HostIsolationConfig.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-7-29 
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
 * <节点资源隔离设置信息> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-7-29] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name="hc_node_isolation_config")
public class NodeIsolationConfig {

	private long id;//isolation记录Id
	private int hostNumber = 0;//主机数
	private int IOPSRead = 0;//输入
	private int IOPSWrite = 0;//输出
	private int CPUWorkload = 0;//CPU工作量
	private int networkRead = 0;//网络读
	private int networkWrite = 0;//网络写
	private int storageSpace = 300;//存储空间
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(name="host_num",nullable=false,columnDefinition="INT default 0")
	public int getHostNumber() {
		return hostNumber;
	}
	public void setHostNumber(int hostNumber) {
		this.hostNumber = hostNumber;
	}
	@Column(name="iops_read",nullable=false,columnDefinition="INT default 0")
	public int getIOPSRead() {
		return IOPSRead;
	}
	public void setIOPSRead(int iOPSRead) {
		IOPSRead = iOPSRead;
	}
	@Column(name="iops_write",nullable=false,columnDefinition="INT default 0")
	public int getIOPSWrite() {
		return IOPSWrite;
	}
	public void setIOPSWrite(int iOPSWrite) {
		IOPSWrite = iOPSWrite;
	}
	@Column(name="cpu_workload",nullable=false,columnDefinition="INT default 0")
	public int getCPUWorkload() {
		return CPUWorkload;
	}
	public void setCPUWorkload(int cPUWorkload) {
		CPUWorkload = cPUWorkload;
	}
	@Column(name="network_read",nullable=false,columnDefinition="INT default 0")
	public int getNetworkRead() {
		return networkRead;
	}
	public void setNetworkRead(int networkRead) {
		this.networkRead = networkRead;
	}
	@Column(name="network_write",nullable=false,columnDefinition="INT default 0")
	public int getNetworkWrite() {
		return networkWrite;
	}
	public void setNetworkWrite(int networkWrite) {
		this.networkWrite = networkWrite;
	}
	@Column(name="storageSpace",nullable=false,columnDefinition="INT default 300")
	public int getStorageSpace() {
		return storageSpace;
	}
	public void setStorageSpace(int storageSpace) {
		this.storageSpace = storageSpace;
	}	
}

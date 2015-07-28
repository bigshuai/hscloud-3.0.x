/* 
* 文 件 名:  IPMIConfig.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-3-11 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.entity; 

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * <IPMI信息配置> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-3-11] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name="hc_ipmi_config")
public class IPMIConfig {

	private long id;//IPMI记录Id
	private int powerConsumption;//功耗
	private int isConsumptionLimit;//功耗限制 开启标识（1：开启；0：关闭）
	private String ip;//IP
	private String userName;//用户名
	private String password;//密码
	private Date createDate = new Date();//创建时间
	private Date updateDate = new Date();//更新时间
	private String port;//端口
//	private ServerNode serverNode;//节点信息
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(name="powerConsumption")
	public int getPowerConsumption() {
		return powerConsumption;
	}
	public void setPowerConsumption(int powerConsumption) {
		this.powerConsumption = powerConsumption;
	}
	@Column(name="isConsumptionLimit",nullable=false,columnDefinition="INT default 0")
	public int getIsConsumptionLimit() {
		return isConsumptionLimit;
	}
	public void setIsConsumptionLimit(int isConsumptionLimit) {
		this.isConsumptionLimit = isConsumptionLimit;
	}
	@Column(name="IP")	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Column(name="userName")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name="password")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="createDate", nullable = false)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name="updateDate", nullable = false)
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
//	@OneToOne(cascade = CascadeType.ALL,mappedBy="ipmiConfig")	
//	public ServerNode getServerNode() {
//		return serverNode;
//	}
//	public void setServerNode(ServerNode serverNode) {
//		this.serverNode = serverNode;
//	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
}

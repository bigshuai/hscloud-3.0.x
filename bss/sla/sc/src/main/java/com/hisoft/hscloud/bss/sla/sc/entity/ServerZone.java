/* 
* 文 件 名:  ServerZone.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-3-8 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.entity; 

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;;

/** 
 * <Zone实体类> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-3-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name = "hc_zone")
public class ServerZone extends AbstractEntity{
	@Column(name = "description")
	private String description;//描述
	@Column(name = "code", nullable = false)
	private String code;//代码
	@Column(name = "is_default", nullable = false,columnDefinition="INT default 0")
	private int isDefault;//是否是默认标示（0：非默认；1：默认）
	@Column(name = "is_enable", nullable = false,columnDefinition="INT default 0")
	private int isEnable;//是否启用标示（0：禁用；1：启用）
	@ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="hc_zone_zonegroup",
	joinColumns=@JoinColumn(name="zone_id",referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="zonegroup_id",referencedColumnName="id"))
	private List<ZoneGroup> zoneGroupList;
	@Column(name = "is_custom", nullable = false,columnDefinition="INT default 0")
	private int isCustom;//是否支持按需购买（0：不支持；1：支持）
//	@OneToMany(mappedBy="serverZone")
//	@JoinTable(name="hc_zone_node",
//	joinColumns=@JoinColumn(name="zone_id",referencedColumnName="id"),
//	inverseJoinColumns=@JoinColumn(name="node_id",referencedColumnName="id"))
//	private List<ServerNode> serverNode;
//	@OneToMany(mappedBy="serverZone")
//	private List<IPRange> IPRange;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}
	public int getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}
	public List<ZoneGroup> getZoneGroupList() {
		return zoneGroupList;
	}
	public void setZoneGroupList(List<ZoneGroup> zoneGroupList) {
		this.zoneGroupList = zoneGroupList;
	}
	public int getIsCustom() {
		return isCustom;
	}
	public void setIsCustom(int isCustom) {
		this.isCustom = isCustom;
	}
	
	
//	public List<ServerNode> getServerNode() {
//		return serverNode;
//	}
//	public void setServerNode(List<ServerNode> serverNode) {
//		this.serverNode = serverNode;
//	}
//	public List<IPRange> getIPRange() {
//		return IPRange;
//	}
//	public void setIPRange(List<IPRange> iPRange) {
//		IPRange = iPRange;
//	}
	

}

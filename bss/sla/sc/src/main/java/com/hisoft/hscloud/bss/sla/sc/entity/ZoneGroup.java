/* 
* 文 件 名:  ZoneGroup.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-6-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.sc.entity; 

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;

/** 
 * <ZoneGroup实体类> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-6-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name = "hc_zonegroup")
@Inheritance(strategy=InheritanceType.JOINED)
public class ZoneGroup extends AbstractEntity{

	@Column(name = "description")
	private String description;//描述
	@Column(name = "code", nullable = false)
	private String code;//代码
	@Column(name = "is_enable", nullable = false,columnDefinition="INT default 0")
	private int isEnable;//是否启用标示（0：禁用；1：启用）
//	private List<ZoneGroup> zoneGroupList;
	@ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="hc_zonegroup_domain",
	joinColumns=@JoinColumn(name="zonegroup_id",referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="domain_id",referencedColumnName="id"))
	private List<Domain> domainList;

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

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public List<Domain> getDomainList() {
		return domainList;
	}

	public void setDomainList(List<Domain> domainList) {
		this.domainList = domainList;
	}
//	@ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//	@JoinTable(name="hc_os_zonegroup",
//	joinColumns=@JoinColumn(name="os_id",referencedColumnName="os_id"),
//	inverseJoinColumns=@JoinColumn(name="zoneGroup_id",referencedColumnName="id"))
//	public List<ZoneGroup> getZoneGroupList() {
//		return zoneGroupList;
//	}
//
//	public void setZoneGroupList(List<ZoneGroup> zoneGroupList) {
//		this.zoneGroupList = zoneGroupList;
//	}
}

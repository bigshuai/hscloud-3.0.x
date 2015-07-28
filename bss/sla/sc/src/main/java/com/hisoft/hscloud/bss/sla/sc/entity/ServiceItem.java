/**
 * @title ServiceItem.java
 * @package com.hisoft.hscloud.bss.sla.sc.entity
 * @description 用一句话描述该文件做什么
 * @author jiaquan.hu
 * @update 2012-5-4 上午10:01:50
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.sc.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-5-4 上午10:01:50
 */
@Entity
@Table(name = "hc_service_item")
@Inheritance(strategy=InheritanceType.JOINED)
public  class ServiceItem {
	//物理主键
	private int id;
	//资源类型
	private int serviceType;
	//资源创建日期
	private Date createDate;
	//资源更新日期
	private Date updateDate;
	//资源名称
	private String name;
	//资源状态
	private int status;
	//资源描述
	private String description;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="item_id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(length = 200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Column(name = "service_type",length=20)
	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "update_date")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	@Column(length=2)
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(length=200)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	


}

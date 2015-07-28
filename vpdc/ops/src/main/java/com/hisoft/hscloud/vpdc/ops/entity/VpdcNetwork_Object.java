package com.hisoft.hscloud.vpdc.ops.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hc_vpdcnetwork_object")
public class VpdcNetwork_Object{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "objectId")
	private Long objectId;//业务对象（vm/VRouter）的标识id
	
	@Column(name = "objectUUID")
	private String objectUUID;//业务对象（vm/VRouter）的uuid
	
	@Column(name = "objectType")
	private Integer objectType;//业务对象的类型（0：VM；1：VRouter）
	
	@Column(name = "networkId")
	private Long networkId;//业务network对应的标识id

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getObjectUUID() {
		return objectUUID;
	}

	public void setObjectUUID(String objectUUID) {
		this.objectUUID = objectUUID;
	}

	public Integer getObjectType() {
		return objectType;
	}

	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}

	public Long getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Long networkId) {
		this.networkId = networkId;
	}

	
}

package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.hisoft.hscloud.common.entity.AbstractEntity;

@Entity
@Table(name = "hc_resource", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"primKey", "resourceType" }) })
public class Resource extends AbstractEntity {

	// vpdcReference 或 menu 表中的 id
	@Column(nullable = false)
	String primKey;
	@Column(name = "resourceType", nullable = false)
	String resourceType;

	Long ownerId;

	public String getPrimKey() {
		return primKey;
	}

	public void setPrimKey(String primKey) {
		this.primKey = primKey;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {

		return "Resource[" + super.toString() + ",primKey=\"" + this.primKey
				+ ",ownerId=\"" + this.ownerId + "\",resourceType=\""
				+ this.resourceType + "\"]";

	}

}

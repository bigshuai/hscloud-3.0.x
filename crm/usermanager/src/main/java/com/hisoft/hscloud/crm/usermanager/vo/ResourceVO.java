package com.hisoft.hscloud.crm.usermanager.vo;

import com.hisoft.hscloud.common.entity.AbstractVO;

public class ResourceVO extends AbstractVO {

	long primKey;

	String resourceType;

	public long getPrimKey() {
		return primKey;
	}

	public void setPrimKey(long primKey) {
		this.primKey = primKey;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

}

package com.hisoft.hscloud.crm.usermanager.dao; 

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;

public interface ResourceTypeDao {

	public List<ResourceType> findAllResourceType(int status);

}

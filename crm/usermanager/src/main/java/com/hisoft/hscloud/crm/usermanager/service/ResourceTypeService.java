package com.hisoft.hscloud.crm.usermanager.service; 

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;

public interface ResourceTypeService {

	public Map<String, ResourceType> getResourceTypeMap(int status);

	public List<ResourceType> getResourceTypeList(int status);

}

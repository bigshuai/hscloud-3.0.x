package com.hisoft.hscloud.crm.usermanager.service.impl; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.crm.usermanager.dao.ResourceTypeDao;
import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;
import com.hisoft.hscloud.crm.usermanager.service.ResourceTypeService;

@Service
public class ResourceTypeServiceImpl implements ResourceTypeService{
	@Autowired
	private ResourceTypeDao resourceTypeDao;
	
	@Override
	public Map<String, ResourceType> getResourceTypeMap(int status) {
		List<ResourceType> list = resourceTypeDao.findAllResourceType(status);
		Map<String, ResourceType> resultMap = new HashMap<String, ResourceType>();
		for(ResourceType resourceType : list) {
			resultMap.put(resourceType.getResourceType(), resourceType);
		}
		return resultMap;
	}
	
	@Override
	public List<ResourceType> getResourceTypeList(int status) {
		return resourceTypeDao.findAllResourceType(status);
	}
}

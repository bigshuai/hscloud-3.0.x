package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.entity.Action;
import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;

public interface ActionService {
	
	public List<Action> getAction(String actionType);
	
	public List<Action> getAction(String actionType,long level, short front);
	
	public Action getActionByClassKey(String classKey);

	public Map<String, List<Action>> getAllAction(Map<String, ResourceType> resourceTypeMap);
}

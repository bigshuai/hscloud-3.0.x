package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.crm.usermanager.dao.ActionDao;
import com.hisoft.hscloud.crm.usermanager.entity.Action;
import com.hisoft.hscloud.crm.usermanager.entity.ResourceType;
import com.hisoft.hscloud.crm.usermanager.service.ActionService;

@Service
public class ActionServiceImp implements ActionService {
	
	private Logger logger = Logger.getLogger(ActionServiceImp.class);
	
	@Autowired
	private ActionDao actionDao;

	@Override
	public List<Action> getAction(String actionType) {
		
		if(logger.isDebugEnabled()){
			logger.debug("enter getAction method.");
			logger.debug("actionType:"+actionType);
		}
		String hql = "from Action where actionType=?";
		if(logger.isDebugEnabled()){
			logger.debug("hql:"+hql);
		}
		List<Action> actions = actionDao.find(hql, actionType);
		if(logger.isDebugEnabled()){
			logger.debug("actions:"+actions);
			logger.debug("exit getAction method.");
		}
		return actions;
		
	}

	@Override
	@Transactional
	public List<Action> getAction(String actionType, long level, short front) {
		
		if(logger.isDebugEnabled()){
			logger.debug("enter getAction method.");
			logger.debug("actionType:"+actionType);
			logger.debug("level:"+level);
		}
		String hql = "from Action where actionType=? and level=? and front = ? order by id";
		if(logger.isDebugEnabled()){
			logger.debug("hql:"+hql);
		}
		List<Action> actions = actionDao.find(hql, actionType, level, front);
		if(logger.isDebugEnabled()){
			logger.debug("actions:"+actions);
			logger.debug("exit getAction method.");
		}
		return actions;
		
	}

	@Override
	public Action getActionByClassKey(String classKey) {
		
		if(logger.isDebugEnabled()){
			logger.debug("enter getActionByClassKey method.");
			logger.debug("classKey:"+classKey);
		}
		Action action = actionDao.getActionByClassKey(classKey);
		if(logger.isDebugEnabled()){
			logger.debug("action:"+action);
			logger.debug("exit getActionByClassKey method.");
		}
		return action;
		
	}
	
	@Override
	public Map<String, List<Action>> getAllAction(Map<String, ResourceType> resourceTypeMap) {
	//	Map<String, String> map = PropertiesUtils.getResourceTypeMap();
		
		Map<String, List<Action>> result = new HashMap<String, List<Action>>();
		for(Entry<String, ResourceType> entry : resourceTypeMap.entrySet()) {
			result.put(entry.getKey(), getAction(entry.getKey()));
		}
		return result;
	}

}

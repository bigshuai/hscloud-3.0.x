package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import com.hisoft.hscloud.crm.usermanager.entity.Action;


public interface ActionDao {
	
	public List<Action> find(String hql, Object... values);
	
	public List<Action> findByIds(List<Long> ids);
	
	public Action getActionByClassKey(String classKey);

}

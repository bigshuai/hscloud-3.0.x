package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.entity.UserUserGroup;

public interface UserUserGroupDao {
	
	public List<UserUserGroup> findBy(String propertyName, Object value);
	
	public void save(UserUserGroup userUserGroup);
	
	public void delete(String hql ,Map<String,Object> map);

}

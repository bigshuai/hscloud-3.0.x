package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.entity.UserGroupPermission;

public interface UserGroupPermissionDao {
	
	public void save(UserGroupPermission userGroupPermission);
	
	public UserGroupPermission findUniqueBy(String propertyName, Object value);
	
	public List<UserGroupPermission> findBy(String propertyName, Object value);
	
	public void delete(String hql, Object... value);

	public void batchDelete(Map<String, Object> condition);

}

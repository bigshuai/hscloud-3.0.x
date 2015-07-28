package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.UserPermission;

public interface UserPermissionDao {
	
	public void save(UserPermission userPermission);
	
	public UserPermission findUniqueBy(String propertyName, Object value);
	
	public List<UserPermission> findBy(String propertyName, Object value);

	public void delete(String hql, Object... value);
}

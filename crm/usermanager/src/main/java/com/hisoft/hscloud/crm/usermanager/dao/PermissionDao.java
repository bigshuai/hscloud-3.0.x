package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.entity.Permission;

public interface PermissionDao {
	
	public void save(Permission permission);
	
	public Permission findUnique(String hql, Object... values);
	
	public List<Permission> find(String hql, Object... values);
	
	public List<Permission> findBySQL(String sql, Map<String, ?> map);
	
	public void deleteByIds(List<Long> ids);
	


}

package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.entity.UserGroup;

public interface UserGroupDao {
	
	public void save(UserGroup userGroup);
	
	public List<UserGroup> findByIds(List<Long> ids);
	
	public UserGroup findUniqueBy(String propertyName, Object value);
	
	public Page<UserGroup> findPage(Page<UserGroup> page, String hql, Object... object);
	
	public Page<UserGroup> findPage(final Page<UserGroup> page, final String hql, final Map<String, ?> values);
	
	public List<UserGroup> findBySQL(String sql,Map<String,?> map);
	
	public List<UserGroup> findByHQL(String hql,Map<String, ?> map);

}

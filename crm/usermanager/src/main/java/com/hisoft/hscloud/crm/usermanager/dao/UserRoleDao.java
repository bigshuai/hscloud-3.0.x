package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.UserRole;

public interface UserRoleDao {
	
	/**
	 * 保存UserRole
	 * @param userRole
	 */
	public void save(UserRole userRole);
	
	/**
	 * 通过属性查找UserRole
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public List<UserRole> findBy(String propertyName, Object value);
	
	/**
	 * 通过hql删除UserRole
	 * @param hql
	 * @param value
	 */
	public void delete(String hql, Object... value);

}

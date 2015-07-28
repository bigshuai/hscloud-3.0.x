package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.AdminRole;

public interface AdminRoleDao {
	
	/**
	 * 通过hql删除AdminRole
	 * @param hql
	 * @param value
	 */
	public void delete(String hql, Object... value);
	
	/**
	 * 通过hql查询
	 * @param hql
	 * @param value
	 */
	public List<AdminRole> find(String hql, Object... value);
	
	/**
	 * 添加AdminRole
	 * @param adminRole
	 */
	public void save(AdminRole adminRole);
	
	/**
	 * 通过hql和查询条件查询
	 * @param hql
	 * @param values
	 * @return
	 */
	public AdminRole findUnique(String hql, Object... values);
	/**
	 * <根据主键Id查找实体bean> 
	* <功能详细描述> 
	* @param adminRoleId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public AdminRole findAdminRoleById(long adminRoleId);
	
	public List<AdminRole> findBy(String propertyName, Object value);
	/**
	 * <根据物理Id删除hc_admin_role表相应的数据> 
	* <功能详细描述> 
	* @param adminRoleId 
	* @see [类、类#方法、类#成员]
	 */
	public void delete(Long adminRoleId);

}

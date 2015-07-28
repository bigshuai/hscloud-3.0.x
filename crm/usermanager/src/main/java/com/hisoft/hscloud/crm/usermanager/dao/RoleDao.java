package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.entity.Resource;
import com.hisoft.hscloud.crm.usermanager.entity.Role;

public interface RoleDao {
	
	/**
	 * 通过id删除UserRole
	 * @param hql
	 * @param value
	 */
	public void delete(long roleId);
	
	public void save(Role role);
	
	public List<Role> findByIds(List<Long> ids);
	
	/**
	 * 通过 hql分页查询Role
	 * @param page
	 * @param hql
	 * @param values
	 * @return
	 */
	public Page<Role> findPage(Page<Role> page, String hql, Object... values);
	
	public Page<Role> findPage(Page<Role> page, String hql);
	
	/**
	 * 查询所有角色（超级管理员调用）
	 * @return
	 */
	public List<Role> getAll();
	
	/**
	 * 通过参数查询角色
	 * @param hql
	 * @param values
	 * @return
	 */
	public List<Role> findRole(String hql, Map<String,?> values);
	
	/**
	 * 通过 角色id查询
	 * @param roleId
	 * @return
	 */
	public Role get(long roleId);

    /** <一句话功能简述> 
    * <功能详细描述> 
    * @param roleId
    * @return 
    * @see [类、类#方法、类#成员] 
    */
    public List<Resource> getResourceForRoleId(long roleId);
    
    public Role findUniqueBy(String propertyName, String value);
    
    public List<Role> findBySQL(String sql,Map<String, ?> map);

}

package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.entity.Resource;

public interface ResourceDao {
	
	/**
	 * 
	 * @param resource
	 */
	public void save(Resource resource);
	
	/**
	 * 通过属性名查询
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public List<Resource> findBy(String propertyName,Object value);
	
	/**
	 * 通过hql查询唯一值
	 * @param hql
	 * @param values
	 * @return
	 */
	public Resource findUnique(String hql, Object... values);

	/**
	 * 通过属性名查询唯一值
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public Resource findUniqueBy(String propertyName, Object value);
	
	/**
	 * 通过 hql查询
	 * @param hql
	 * @param values
	 * @return
	 */
	public List<Resource> find(String hql, Object... values);
	
	/**
	 * 通过 list中的id查询
	 * @param ids
	 * @return
	 */
	public List<Resource> findByIds(List<Long> ids);
	
	public List<Resource> findBySQL(String sql,Map<String,?> map);
	
	public List<Resource> findByHQL(String sql,Map<String,?> map);
	
	public List<Object> findPrimKeyByHQL(String hql,Map<String,?> map);
	
   /** <一句话功能简述> 
    * <功能详细描述> 
    * @param roleId
    * @return 
    * @see [类、类#方法、类#成员] 
    */
    public List<Resource> getResourceForRoleId(long roleId);
}

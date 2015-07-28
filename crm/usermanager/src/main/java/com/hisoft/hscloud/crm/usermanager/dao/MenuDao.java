package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.entity.Menu;

public interface MenuDao {
	
	/**
	 * 通过hql查询菜单
	 * @param hql
	 * @param values
	 * @return
	 */
	public List<Menu> find(String hql, Object... values);
	
	/**
	 * 通过ids中菜单的id,查询菜单
	 * @param ids
	 * @return
	 */
	public List<Menu> findByIds(List<Long> ids);
	
	/**
	 * 获得所有菜单
	 * @return
	 */
	public List<Menu> getAll();
	

    
    /** <一句话功能简述> 
    * <功能详细描述> 
    * @param sql
    * @param roleId
    * @return 
    * @see [类、类#方法、类#成员] 
    */
    List<Menu> getMenuTree(String sql, long roleId);

	public List<Menu> findByMap(String hql, Map<String, String> values);

}

package com.hisoft.hscloud.crm.usermanager.dao;

import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.entity.RolePermission;

public interface RolePermissionDao {
	
	public void save(RolePermission rolePermission);
	
	/**
	 * 通过hql删除RolePermission
	 * @param hql
	 * @param value
	 */
	public void delete(String hql, Object... value);

	public void batchDelete(Map<String, Object> condition);

	public void deleteRPForMenu(Long roleId, List<String> list);
//	public boolean addZoneOfRole(long roleId,List<Object> zoneIds);
//	public boolean deleteZoneOfRole(long roleId,List<Object> zoneIds);

}

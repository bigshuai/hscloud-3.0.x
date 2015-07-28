package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;

import com.hisoft.hscloud.common.util.HsCloudException;


public interface RolePermissionService {

	public void deleteRolePermissionByRoleId(long roleId);
	
	public void addRolePermissiion(long roleId, long permissionId);

	public void batchDelete(String resourceValue, long roleId);

	public void deleteRPForMenu(Long roleId, String noCheckStr);
	public boolean updateZoneOfRolePermission(long roleId, String oldZoneIds,
			String newZoneIds) throws HsCloudException;
	public boolean hasZoneOfRolePermission(long roleId,long zoneId);
	public List<Object> getZoneIdsByAdminId(long adminId);
	public boolean deleteZoneOfRole(long zoneId);
}

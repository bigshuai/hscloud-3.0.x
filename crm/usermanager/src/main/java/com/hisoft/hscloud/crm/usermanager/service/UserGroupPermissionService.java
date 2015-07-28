package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.UserGroupPermission;

public interface UserGroupPermissionService {
	
	/**
	 * 通过用户组id删除，删除用户下的所以权限
	 * @param userGroupId 用户组id
	 */
	public void deleteUserGroupPermission(long userGroupId);
	
	/**
	 * 先调用deleteUserGroupPermission方法，删除所有权限，然后再添加权限。
	 * @param userGroupId  要添加权限的组id
	 * @param permissionIds 要添加的权限id
	 */
	public void saveUserGroupPermission(long userGroupId,List<Long> permissionIds);
	
	public List<UserGroupPermission> getUserGroupPermissionByGroupId(long groupId);

	public void batchDelete(String resourceValue, long userGroupId);

}

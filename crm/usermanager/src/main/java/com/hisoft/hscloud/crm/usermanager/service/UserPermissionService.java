package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;

public interface UserPermissionService {
	
	/**
	 * 删除该userId下的所以权限
	 * @param userId
	 */
	public void deleteUserPermission(long userId);
	
	/**
	 * 先调用userId删除用户下所有权限，然后添加新的权限。
	 * @param userId
	 * @param permissionIds 要添加的权限
	 */
	public void saveUserPermission(long userId,List<Long> permissionIds);

}

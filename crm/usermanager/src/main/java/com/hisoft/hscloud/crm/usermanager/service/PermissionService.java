package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;

import com.hisoft.hscloud.crm.usermanager.entity.Permission;
import com.hisoft.hscloud.crm.usermanager.vo.PermissionVO;

public interface PermissionService {
	
	public List<Permission> getUPermission(long userId);
	public List<Permission> getApermission(long adminId);
	public List<Permission> getURolePermission(long userId);
	public List<Permission> getARolePermission(long adminId);
	public Permission getPermission(long actionId,long resourceId);
	
	/**
	 * 获得User下的Permisson
	 * @param userId
	 * @return
	 */
	public List<Permission> getUserPermission(long userId);
	/**
	 * 获得Group下的Permisson
	 * @param userId
	 * @return
	 */
	public List<Permission> getUserGroupPermission(long userId);
	
	/**
	 * 获得查询条件为actionId，resourceId所有权限（Group+User）
	 * @param userId
	 * @param actionId
	 * @param resourceId
	 * @return
	 */
	public List<Permission> getUPermission(long userId,long actionId,long resourceId);
	
	public List<Permission> getAPermission(long adminId,long actionId,long resourceId);
	
	public List<PermissionVO> getUPermissionVO(long userId,String resourceType);
	public List<PermissionVO> getAPermissionVO(long adminId,String resourceType);
	public List<Permission> getPermissionInGroup(long groupId);
	
	public boolean getAActionPermission(long adminId,String priKey,String resourceType,String classKey);
	
	public boolean getUActionPermission(long userId,String priKey,String resourceType,String classKey);
	
	public List<Long> getAPrimKey(long adminId,String resourceType,String classKey);
	public List<Long> getUPrimKey(long userId,String resourceType,String classKey);
	
	public List<Long> getAPrimKey(long adminId,String resourceType);
	public List<Long> getUPrimKey(long userId,String resourceType);
	/**
	 * 添加权限
	 * @param resourceId
	 * @param actionId
	 * @return
	 */
	public long addPermission (long resourceId,long actionId);
	
	//public List<Permission> getAllPermission();
	
	public void deletePermissions(List<Long> ids);
	
	public void savePermission(String resourceType,long ownerId,String primKey);


}

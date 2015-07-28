package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.crm.usermanager.dao.UserGroupPermissionDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroupPermission;
import com.hisoft.hscloud.crm.usermanager.service.UserGroupPermissionService;

@Service
public class UserGroupPermissionServiceImpl implements UserGroupPermissionService {
	
	@Autowired
	private UserGroupPermissionDao userGroupPermissionDao;


	@Override
	public void deleteUserGroupPermission(long userGroupId) {
		
		String hql = "delete UserGroupPermission ugp where ugp.userGroupId=?";
		userGroupPermissionDao.delete(hql, userGroupId);
		
	}

	
	@Override
	public void saveUserGroupPermission(long userGroupId,
			List<Long> permissionIds) {
	//	this.deleteUserGroupPermission(userGroupId);
		for (Long permissionId : permissionIds) {
			UserGroupPermission userGroupPermission = new UserGroupPermission();
			userGroupPermission.setUserGroupId(userGroupId);
			userGroupPermission.setPermissionId(permissionId);
			userGroupPermissionDao.save(userGroupPermission);
		}
		
	}


	@Override
	public List<UserGroupPermission> getUserGroupPermissionByGroupId(
			long groupId) {
		return userGroupPermissionDao.findBy("userGroupId", groupId);
	}
	
	@Override
	public void batchDelete(String resourceValue, long userGroupId) {
		String[] strArray = resourceValue.split(",");
		Long[] idArray = new Long[strArray.length];
		for(int i = 0; i < idArray.length; i++) {
			idArray[i] = Long.valueOf(strArray[i]);
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("resource", idArray);
		condition.put("userGroupId", userGroupId);
		userGroupPermissionDao.batchDelete(condition);
	}

}

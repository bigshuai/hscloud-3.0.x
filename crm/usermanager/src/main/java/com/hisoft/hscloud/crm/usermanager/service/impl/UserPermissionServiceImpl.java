package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.crm.usermanager.dao.UserPermissionDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserPermission;
import com.hisoft.hscloud.crm.usermanager.service.UserPermissionService;

public class UserPermissionServiceImpl implements UserPermissionService {
	
	@Autowired
	private UserPermissionDao userPermissionDao;

	@Override
	public void deleteUserPermission(long userId) {
		String hql = "delete UserPermission up where up.userId=?";
		userPermissionDao.delete(hql, userId);
	}

	@Override
	public void saveUserPermission(long userId, List<Long> permissionIds) {
		this.deleteUserPermission(userId);
		for (Long permissionId : permissionIds) {
			
			UserPermission userPermission = new UserPermission();
			userPermission.setUserId(userId);
			userPermission.setPermissionId(permissionId);
			userPermissionDao.save(userPermission);
			
		}
		
	}

}

package com.hisoft.hscloud.crm.usermanager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.crm.usermanager.dao.UserGroupCompanyDao;
import com.hisoft.hscloud.crm.usermanager.dao.UserUserGroupDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroupCompany;
import com.hisoft.hscloud.crm.usermanager.entity.UserUserGroup;
import com.hisoft.hscloud.crm.usermanager.service.UserUserGroupService;

@Service
public class UserUserGroupServiceImpl implements UserUserGroupService {
	
	@Autowired
	private UserUserGroupDao userUserGroupDao;
	
	@Autowired
	private UserGroupCompanyDao userGroupCompanyDao;

	@Override
	@Transactional
	public void saveUserUserGroup(long userId,long companyId,List<Long> groupIds) {
		
	    this.deleteUserUserGroupByUserId(userId, companyId);
		for (Long groupid : groupIds) {
			
			UserUserGroup userUserGroup = new UserUserGroup();
			userUserGroup.setUserId(userId);
			userUserGroup.setUserGroupId(groupid);
			
			userUserGroupDao.save(userUserGroup);
			
		}
		
	}

	@Override
	public void deleteUserUserGroupByUserId(long userId,long companyId) {
		List<UserGroupCompany> userGroupCompanys = userGroupCompanyDao.findBy("companyId", companyId);
		List<Long> userGroupIds = new ArrayList<Long>();
		for (UserGroupCompany userGroupCompany : userGroupCompanys) {
			userGroupIds.add(userGroupCompany.getUserGroupId());
		}
		String hql = "delete UserUserGroup uug where uug.userId=:userId and uug.userGroupId in(:ids)";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("ids", userGroupIds);
		userUserGroupDao.delete(hql, map);
	}


}

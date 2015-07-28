package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;

public interface UserUserGroupService {
	
	public void saveUserUserGroup(long userId,long companyId,List<Long> groupIds);
	
	public void deleteUserUserGroupByUserId(long userId,long companyId);

}

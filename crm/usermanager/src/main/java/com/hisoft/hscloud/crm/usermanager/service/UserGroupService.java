package com.hisoft.hscloud.crm.usermanager.service;

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Sort;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroup;
import com.hisoft.hscloud.crm.usermanager.vo.UserGroupVO;

public interface UserGroupService {
	
	/**
	 * 
	 * @param userGroup
	 */
	public long createUserGroup(UserGroup userGroup,long userId);
	
	/**
	 * 
	 * @param userGroup
	 */
	public void modifyUserGroup(UserGroup userGroup);

	public List<UserGroupVO> getPermissionUserGroup(long userId,List<Long> primKeys);

	public Page<UserGroupVO> searchPermissionGroup(List<Sort> sorts,String query,Page<UserGroup> page, long userId,List<Object> primKeys);

	/**
	 * 通过公司Id查询公司下的UserGroup
	 * @param companyId
	 * @return
	 */
	public List<UserGroupVO> getUserGroupByCompanyId(long companyId);
	
	/**
	 * 通过groupId删除group
	 * @param groupId
	 */
	public void deleteUserGroupById(long groupId);
	
	public UserGroup getUserGroupById(long groupId);

	public List<UserGroup> duplicateUserGroup(long companyId, String groupName);
	
}

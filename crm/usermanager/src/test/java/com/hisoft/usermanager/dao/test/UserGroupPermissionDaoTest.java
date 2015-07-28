package com.hisoft.usermanager.dao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.hisoft.hscloud.crm.usermanager.dao.UserGroupPermissionDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserGroupPermission;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})
public class UserGroupPermissionDaoTest {
	
	@Autowired
    private UserGroupPermissionDao userGroupPermissionDao;
	
	@Test
	public void testSave(){
		
		UserGroupPermission userGroupPermission = new UserGroupPermission();
		long permissionId = 12l;
		long userGroupId = 2l;
		userGroupPermission.setPermissionId(permissionId);
		userGroupPermission.setUserGroupId(userGroupId);
		userGroupPermissionDao.save(userGroupPermission);
		
	}
	

}

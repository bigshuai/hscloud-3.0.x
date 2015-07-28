package com.hisoft.usermanager.dao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.hisoft.hscloud.crm.usermanager.dao.UserPermissionDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserPermission;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})

public class UserPermissionDaoTest {
	
	@Autowired
	private UserPermissionDao userPermissionDao;
	
	@Test
	public void testSave(){
		
		UserPermission userPermission = new UserPermission();
		long permissionId = 12l;
		long userId =1l;
		userPermission.setPermissionId(permissionId);
		userPermission.setUserId(userId);
		
		userPermissionDao.save(userPermission);
		
	}

}

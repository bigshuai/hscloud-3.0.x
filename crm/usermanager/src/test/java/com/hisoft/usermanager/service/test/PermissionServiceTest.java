package com.hisoft.usermanager.service.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.crm.usermanager.constant.ResourceType;
import com.hisoft.hscloud.crm.usermanager.entity.Permission;
import com.hisoft.hscloud.crm.usermanager.service.PermissionService;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})
@TransactionConfiguration(transactionManager = "transactionManager",  defaultRollback= false)
@Transactional
public class PermissionServiceTest {

	@Autowired
	private PermissionService permissionService;
	
	
	@Test
	public void tetAddPermission(){
		//5
		long resourceId =12l;
		long actionId = 12l;
		long permissionId = permissionService.addPermission(resourceId, actionId);
		System.out.println(permissionId);
		
	}
	
	@Test
	public void testGetUserGroupPermission(){
		
		long userId = 1l;
		List<Permission> permissions = permissionService.getUserGroupPermission(userId);
		Assert.assertNotNull(permissions);
		System.out.println();
		
		
	}
	
	@Test
	public void testgetUserPermission(){
		long userId = 1l;
		List<Permission> permissions = permissionService.getUserPermission(userId);
		Assert.assertNotNull(permissions);
		System.out.println();

	}
	
	@Test
	public void testSavePermission(){
		permissionService.savePermission(ResourceType.VM.getEntityName(), 1, "1");
	}
	

	
}

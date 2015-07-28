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
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.Resource;
import com.hisoft.hscloud.crm.usermanager.entity.Role;
import com.hisoft.hscloud.crm.usermanager.service.RolePermissionService;
import com.hisoft.hscloud.crm.usermanager.service.RoleService;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-crm-usermanager.xml" })
public class RoleServiceTest {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RolePermissionService rolePermissionService;
	
	@Test
	public void testDeleteRole(){
		long roleId = 1l;
		roleService.deleteRole(roleId);
	}
	
	@Test
	public void testFindPage(){
		
		Page<Role> page = new Page<Role>();
		page.setPageNo(1);
		page.setPageSize(10);
		String roleName = "role1111";
		Page<Role> roles = roleService.findPage(page, roleName);
		Assert.assertNotNull(roles);
		
	}
	
	/*@Test
	public void testGetPermissionRole(){
		Admin admin = new Admin();
		admin.setId(5l);
		List<Role> roles = roleService.getPermissionRole(admin);
		Assert.assertNotNull(roles);
	}*/
	
	@Test
	public void testGetRole(){
		long adminId = 5l;
		Role role = roleService.getRole(adminId);
		Assert.assertNotNull(role);
	}
	
	@Test
	public void testBatchDelete() {
		long roleId = 8l;
		String resourceValue = "140,139,";
		rolePermissionService.batchDelete(resourceValue, roleId);
	}

}

package com.hisoft.usermanager.service.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.dao.AdminDao;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.service.AdminService;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})
public class AdminServiceTest {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private AdminDao adminDao;
	
	@Test
	public void testLoginAdminByEmail(){
		
		String email = "123@hisoft.com";
		String password = "123";
		Admin admin = adminService.loginAdminByEmail(email, password);
		Assert.assertNotNull(admin);
		
	}
	
	@Test
	public void testPageAdminInRole(){
		
		Page<Admin> page = new Page<Admin>();
		page.setPageNo(1);
		page.setPageSize(10);
		long roleId = 1l;
		Page<Admin> admins = adminService.pageAdminInRole(page, roleId);
		Assert.assertNotNull(admins);
		
	}
	
	@Test
	public void testAddAdmin(){
		
		Admin admin = new Admin();
		admin.setEmail("1234@hisoft.com");
		admin.setPassword("1234");
		admin.setName("1234");
		admin.setIsSuper(true);
		long roleId = 1l;
		adminService.addAdmin(admin, roleId);
		
	}
	
	@Test
	public void testDeleteAdmin(){
		
		long adminId = 1l;
		//adminService.deleteAdmin(adminId);
		System.out.println();
		
	}
	
	@Test
	public void testresetPassword(){
		
		long adminId = 1l;
		Admin admin = adminDao.get(adminId);
		//adminService.resetPassword(admin);
		
	}

	

}

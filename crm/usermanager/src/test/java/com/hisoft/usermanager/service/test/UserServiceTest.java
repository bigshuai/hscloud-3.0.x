package com.hisoft.usermanager.service.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.crm.usermanager.constant.UserType;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.entity.UserProfile;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.common.util.Sort;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})

public class UserServiceTest {

	@Autowired
	private UserService userService;

	/*@Test
	public void testPageSubUserInGroup(){
		Page<User> page = new Page<User>();
		page.setPageNo(1);
		page.setPageSize(10);
		long groupId = 1l; 
		Page<User> pages = userService.pageSubUserInGroup(page, groupId);
		Assert.assertNotNull(pages);
		System.out.println();
	}*/
	
	@Test
	public void testSearchUserLikeName(){
		Page<User> page = new Page<User>();
		page.setPageNo(1);
		page.setPageSize(10);
		long userId = 1l; 
		String username = "name";
		List<Sort> sorts = new ArrayList<Sort>();
		//Page<User> pages = userService.searchUserLikeName(username, sorts, page, userId);
		//Assert.assertNotNull(pages);
		System.out.println();
	}
	
	@Test
	public void testSearchUserLikeEmail(){
		String email = "email";
		List<Sort> sorts = new ArrayList<Sort>();
		Page<User> page = new Page<User>();
		page.setPageNo(1);
		page.setPageSize(10);
		long userId = 1l;
		//Page<User> pages = userService.searchUserLikeEmail(email, sorts, page, userId);
		//Assert.assertNotNull(pages);
		System.out.println();
	}
	
	@Test
	public void testSearchUser(){
		List<Sort> sorts = new ArrayList<Sort>();
		Page<User> page = new Page<User>();
		page.setPageNo(1);
		page.setPageSize(10);
		long userId = 1l;
		//Page<User> pages = userService.searchUser(sorts, page, userId);
		//Assert.assertNotNull(pages);
		System.out.println();
		
		
	}
	
	@Test
	public void testLoginUserByEmail(){
		String email = "big@hisoft.com";
		String password = "123";
		User user = userService.loginUserByEmail(email, password);
		UserProfile userProfile = user.getUserProfile();
		System.out.println(user.toString());
	}
	
	@Test
	public void testGetCurrentUserAdminID(){
//		long userId = 3l;
		long userId = 4l;
//		long adminId = userService.getCurrentUserAdminID(userId);
		System.out.println();
	}
	
	@Test
	public void testGetUser() {
	    long userId = 2l;
	    User user = userService.getUser(userId);
	    Assert.assertNotNull(user);
	}
}

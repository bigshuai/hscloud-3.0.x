package com.hisoft.usermanager.service.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.hisoft.hscloud.crm.usermanager.service.UserUserGroupService;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})

public class UserUserGroupServiceTest {
	
	@Autowired
	private UserUserGroupService userUserGroupService;
	
	@Test
	public void testSaveUserUserGroup(){
		long userId = 1l;
		List<Long> groupIds = new ArrayList<Long>();
		groupIds.add(3l);
		groupIds.add(4l);
		groupIds.add(5l);
		groupIds.add(6l);
		
		//userUserGroupService.saveUserUserGroup(userId, groupIds);
	}

}

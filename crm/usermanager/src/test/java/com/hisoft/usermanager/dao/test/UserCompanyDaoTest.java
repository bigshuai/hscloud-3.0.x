package com.hisoft.usermanager.dao.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.hisoft.hscloud.crm.usermanager.dao.UserCompanyDao;
import com.hisoft.hscloud.crm.usermanager.entity.UserCompany;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})

public class UserCompanyDaoTest {
	
	@Autowired
	private UserCompanyDao userCompanyDao;
	
	@Test
	public void testFindUniqueBy(){
		long userId = 1l;
		UserCompany userCompany = userCompanyDao.findUniqueBy("userId", userId);
		Assert.assertNotNull(userCompany);
		System.out.println(userCompany);
	}

}

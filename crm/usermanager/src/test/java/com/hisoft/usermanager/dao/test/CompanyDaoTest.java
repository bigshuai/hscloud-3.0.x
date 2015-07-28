package com.hisoft.usermanager.dao.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.hisoft.hscloud.crm.usermanager.dao.CompanyDao;
import com.hisoft.hscloud.crm.usermanager.entity.Company;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})

public class CompanyDaoTest {

	@Autowired
	private CompanyDao companyDao;
	
	@Test
	public void testFindUniqueBy(){
		long companyId = 1l;
		Company company = companyDao.findUniqueBy("id",companyId);
		Assert.assertNotNull(company);
		System.out.println(company);
		
	}
}

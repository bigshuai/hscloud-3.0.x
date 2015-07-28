package com.hisoft.usermanager.service.test;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.hisoft.hscloud.crm.usermanager.entity.Company;
import com.hisoft.hscloud.crm.usermanager.service.*;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})
public class CompanyServiceTest {
	
	private CompanyService CompanyService;
	
	public void tesTgetCompanyByUserId(){
		long userId = 1l;
		Company company = CompanyService.getCompanyByUserId(userId);
		Assert.assertNotNull(company);
	}

}

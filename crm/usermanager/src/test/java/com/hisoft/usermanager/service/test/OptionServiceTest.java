package com.hisoft.usermanager.service.test;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.hisoft.hscloud.crm.usermanager.entity.Country;
import com.hisoft.hscloud.crm.usermanager.entity.Industry;
import com.hisoft.hscloud.crm.usermanager.entity.Region;
import com.hisoft.hscloud.crm.usermanager.service.OptionService;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})
public class OptionServiceTest{
	
	@Autowired
	private OptionService optionService;
	
	@Test
	public void testLoadCountry(){
		List<Country> countrys = optionService.loadCountry();
		Assert.assertNotNull(countrys);
		System.out.println();
	}
	
	@Test
	public void testLoadRegion(){
		long countryId = 1l;
		List<Region> regions = optionService.loadRegion(countryId);
		Assert.assertNotNull(regions);
		System.out.println();
	}
	
	@Test
	public void testLoadIndustry(){
		List<Industry> industrys = optionService.loadIndustry();
		Assert.assertNotNull(industrys);
		System.out.println();
	}

}

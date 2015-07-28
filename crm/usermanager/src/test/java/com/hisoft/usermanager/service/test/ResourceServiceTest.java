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

import com.hisoft.hscloud.crm.usermanager.constant.ResourceType;
import com.hisoft.hscloud.crm.usermanager.entity.Resource;
import com.hisoft.hscloud.crm.usermanager.service.ResourceService;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})

public class ResourceServiceTest {
	
	@Autowired
	ResourceService resourceService;
	
	@Test
	public void testGetResource1(){
		String resourceType= ResourceType.VM.getType();
		List<Resource> resources = resourceService.getResource(resourceType);
		Assert.assertNotNull(resources);
		System.out.println();
	}
	
	@Test
	public void testGetResource2(){
		String resourceType= ResourceType.VM.getType();
		String primKey = "2";
		Resource resource = resourceService.getResource(resourceType, primKey);
		Assert.assertNotNull(resource);
		System.out.println();
	}
	
	@Test
	public void testAddResource(){
		String primKey = "2";
		String resourceType = ResourceType.VM.getType();
		resourceService.addResource(primKey, resourceType);
	}

}

package com.hisoft.usermanager.dao.test;

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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.crm.usermanager.dao.ResourceDao;
import com.hisoft.hscloud.crm.usermanager.entity.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})
@TransactionConfiguration(transactionManager = "transactionManager",  defaultRollback= false)
@Transactional
public class ResourceDaoTest {
	
	@Autowired
	ResourceDao resourceDao;
	
	@Test
	public void testSave(){

			Resource resource = new Resource();
			String primKey = "5";
			resource.setPrimKey(primKey);
			resource.setResourceType("vm");
			resourceDao.save(resource);
			
	

	}
	
	@Test
	public void testFindBy(){
		List<Resource> resources = resourceDao.findBy("resourceType", "com.hisoft.hscloud.crm.usermanager.entity.Menu");
		System.out.println(resources);
	}
	
	@Test
	public void testFindUnique(){
		String hql = "from Resource r where id=?";
		Resource resource = resourceDao.findUnique(hql, 2l);
		System.out.println(resource);
	}
	
	@Test
	public void testFindUniqueBy(){
		Resource resource = resourceDao.findUniqueBy("id", 2l);
		System.out.println(resource);
	}
	
	@Test
	public void testFind(){
		String hql = "from Resource r where id=?";
		resourceDao.find(hql, 2l);
	}
	
	@Test
	public void testFindByIds(){
		List<Long> ids = new ArrayList<Long>();
		ids.add(2l);
		ids.add(3l);
		ids.add(4l);
		List<Resource> resources = resourceDao.findByIds(ids);
		Assert.assertNotNull(resources);
		System.out.println(resources);
	}
	


}

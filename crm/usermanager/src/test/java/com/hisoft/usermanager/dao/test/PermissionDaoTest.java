package com.hisoft.usermanager.dao.test;

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
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.crm.usermanager.dao.PermissionDao;
import com.hisoft.hscloud.crm.usermanager.entity.Permission;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=true)
@Transactional
public class PermissionDaoTest {
	
	@Autowired
	private PermissionDao permissionDao;
	
	@Test
	public void testFind(){
		
		String hql = "from Permission where resoureId=?";
		List<Permission> permissions = permissionDao.find(hql, 5l);
		Assert.assertNotNull(permissions);
		System.out.println();
		
	}
	
	@Test
	public void testSave(){
		
		Permission permission = new Permission();
		permission.setActionId(100l);
		permission.setResourceId(100l);
		permissionDao.save(permission);
		System.out.println();
		
	}
	
	/*@Test
	public void testFindByHiBernateSQL(){
		String hql = "select * from hc_permission p where p.resoure_id=? and action_id=?";
		List<Permission> permissions =  permissionDao.findByHibernateSQL(hql, 5l,5l);
		Assert.assertNotNull(permissions);
		System.out.println();
		
		
	}*/

}

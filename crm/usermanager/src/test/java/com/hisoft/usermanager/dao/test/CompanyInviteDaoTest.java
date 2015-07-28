package com.hisoft.usermanager.dao.test;

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

import com.hisoft.hscloud.crm.usermanager.dao.CompanyInviteDao;
import com.hisoft.hscloud.crm.usermanager.entity.CompanyInvite;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})
@TransactionConfiguration(transactionManager = "transactionManager",  defaultRollback= false)
@Transactional
public class CompanyInviteDaoTest {
	
	@Autowired
	private CompanyInviteDao companyInviteDao;
	
	@Test
	public void testGet(){
		CompanyInvite companyInvite = companyInviteDao.findUniqueBy("id", 1l);
		System.out.println(companyInvite);
	}

}

package com.hisoft.usermanager.service.test;

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

import com.hisoft.hscloud.crm.usermanager.entity.UserBank;
import com.hisoft.hscloud.crm.usermanager.service.UserBankService;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-crm-usermanager.xml" })
public class UserBankServiceTest {
	@Autowired
	private UserBankService userBankService;
	
	@Test
	public void testSaveUserBank() {
		UserBank userBank = new UserBank();
		userBank.setBank("123");
		userBank.setAccount("2222");
		userBank.setPayee("332");
		userBank.setRemark("4444444");
		userBank.setUserId(8l);
		userBankService.saveUserBank(userBank);
	}
}

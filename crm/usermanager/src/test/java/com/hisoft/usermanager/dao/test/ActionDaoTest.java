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
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.crm.usermanager.dao.ActionDao;
import com.hisoft.hscloud.crm.usermanager.entity.Action;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-crm-usermanager.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=true)
@Transactional
public class ActionDaoTest {
	
	@Autowired
	ActionDao actionDao;
	
	@Test
	public void testFind(){
		
		String hql = "from Action a where a.id=?";
		List<Action> actions = actionDao.find(hql, 1l);
		Assert.assertNotNull(actions);
		System.out.println();
		
	}
	
	@Test
	public void testFindByIds(){
		
		List<Long> ids = new ArrayList<Long>();
		ids.add(1l);
		ids.add(2l);
		ids.add(3l);
		List<Action> actions = actionDao.findByIds(ids);
		Assert.assertNotNull(actions);
		System.out.println();
		
	}

}

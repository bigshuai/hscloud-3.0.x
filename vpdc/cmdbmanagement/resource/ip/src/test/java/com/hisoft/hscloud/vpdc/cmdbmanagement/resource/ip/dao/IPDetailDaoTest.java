package com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.ip.util.BeanReadUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-vpdc-cmdbmanagement-resource-ip-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional
public class IPDetailDaoTest {

	@Autowired
	private IPDetailDao iPDetailDao;
	IPDetailDao sIAct = (IPDetailDao)BeanReadUtil.getBean("iPDetailDao");
	@Test
	public void testGetIPStatisticsByRangeId(){
		//List list=sIAct.getIPStatisticsByRangeId(4);
		
		//System.out.println(list.get(1));
	}
	@Test
	public void testgetIPDetailByIP(){
		iPDetailDao.getIPDetailByIP("192.168.177.156");
	}
}

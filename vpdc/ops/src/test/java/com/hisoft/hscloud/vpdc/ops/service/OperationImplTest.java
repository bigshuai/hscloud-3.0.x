package com.hisoft.hscloud.vpdc.ops.service;

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
import org.springside.modules.orm.Page;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceVO;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-vpdc-ops-test.xml" })
public class OperationImplTest {
	// OperationImpl impl = (OperationImpl) BeanReadUtil.getBean("operation");

	@Autowired
	private Operation operation;

	@Test
	public void testUpdateVmName(){
		Assert.assertNotNull(operation);
	}
	@Test
	public void testGetVmsByUserId() throws HsCloudException{
		Page<InstanceVO> pages = new Page<InstanceVO>();
		//pages = operation.getVmsByUserId(userId, pages);
		System.out.println(pages.getTotalCount());
		Assert.assertEquals(pages.getTotalCount(), 10);
	}
	
	@Test
	public void closeVm() throws HsCloudException{
		String vmId = "1";
		operation.closeVm(vmId,null,null);
	}
	
	@Test
	public void openvm() throws HsCloudException{
		String vmId = "1";
		operation.openvm(vmId,null,null);
	}
	
	@Test
	public void rebootVm() throws HsCloudException{
		String vmId = "1";
		operation.rebootVm(vmId,null,null);
	}
	
	@Test
	public void backupsVm() throws HsCloudException{
		String vmId = "1";
		String backupName_stack = "test_db";
		String backupName_db="test_opsk";
		String comments="test";
		int sn_type = 0;
		//operation.backupsVm(vmId, backupName_stack, backupName_db, comments, sn_type);
	}
	
	@Test
	public void testExpireRemind() {
	    operation.expireRemind();
	}
}

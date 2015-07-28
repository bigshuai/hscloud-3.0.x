package com.hisoft.hscloud.bss.sla.om.service.test;
import java.util.List;

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

import com.hisoft.hscloud.bss.sla.om.service.OrderService;
import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVMVo;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-bss-sla-om.xml"})
public class OrderServiceTest {
	@Autowired
	private OrderService orderService;
	@Test
    public void getOrderItemVoList(){
    	List<OrderItemVMVo> result=orderService.getOrderItemVMByOrderId(1);
    }
}

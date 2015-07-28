/**
 * 
 */
package com.hisoft.hscloud.message.service;

import java.util.HashMap;

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

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.message.entity.Message;
import com.hisoft.hscloud.message.util.Constant;


/**
 * @author lihonglei
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-message-test.xml" })
public class MessageServiceTest {
	
//	private Logger logger = Logger.getLogger(MessageServiceTest.class);
	@Autowired
	private MessageService messageService;
	@Test
	public void testFindUnreadMessage( ) {
		Long userId = 1l;
		Page<Message> page = new Page<Message>(Constants.PAGE_NUM);
		page.setOrder(Page.DESC);
		page.setOrderBy("id");
	//	messageService.findUnreadMessage(pageRole, userId);
		page = messageService.findUnreadMessage(page, userId, new HashMap<String, Object>());
		System.out.println(page.getResult().size());
	}
	
	@Test
	public void testFindUnreadCount() {
		Long userId = 1l;
		long result = messageService.findUnreadCount(userId);
		System.out.println(result);
	}
	
	@Test
	public void testSaveMessage() {
		Message message = new Message();
		message.setMessage("是否续订11");
		message.setMessageType(Constant.MESSAGE_TYPE_RENEW_SUBSCRIPTION);
		message.setRemark("remark");
		message.setStatus(Constant.MESSAGE_STATUS_UNREAD);
		message.setUserId(1l);
		messageService.saveMessage(message);
	}
	
	@Test
	public void testModifyMessageStatus() {
		Long id = 3l;
		messageService.modifyMessageStatus(id);
	}
}

package com.hisoft.hscloud.mail.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.mail.entity.MailSender;
import com.hisoft.hscloud.mail.util.EmailAccountException;
import com.hisoft.hscloud.mail.util.MailFactory;
import com.hisoft.hscloud.mail.util.MailSenderUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-mail-test.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional
public class MailServiceTest {

//	@Autowired
//	private MailService mailService;
	
//	MailService ms=(MailService)BeanReadUtil.getBean("mailService");
	@Test
	public void testSendEmail(){
//		MailSender sender = new MailSender("203.158.21.29", "notice@xirang.com","www.xrnet.cn", "notice@xirang.com", "default");
		MailSenderUtil sender = null;		
		Map<String, String> map = new HashMap<String, String>();
		 map.put("body", "ceshi");
		try {
			MailSender ms = new MailSender();
			MailFactory factory = new MailFactory();
			sender = factory.getSender(ms);
			sender.send("jinggang.li@pactera.com", "ceshi", "test");
		} catch (EmailAccountException e1) {
			e1.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void testSaveEmail(){
//		boolean dataflg=ms.saveEmail("hisoft@hisoft.com", "hisoft@hisoft.com", "测试邮件主题2", "测试邮件内容2");
//		System.out.println(dataflg);
//	}
	
//	@Test
//	public void testSendEmail(){
//		ms.sendEmail();
//		System.out.println("ok");
//	}
//	 public static void main(String[] args) throws Exception {		
//		 MailSender sender = MailFactory.getSender();
//		 Map<String, String> map = new HashMap<String, String>();
//		 map.put("body", "ceshi");
//		 sender.send("jinggang.li@hisoft.com", "ceshi", map);
//	 //System.out.print(receiveUserArray.length);
//		
//		
//	 }
}

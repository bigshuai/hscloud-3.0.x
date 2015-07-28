package com.hisoft.hscloud.mail.service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.mail.constant.MailTemplateType;
import com.hisoft.hscloud.mail.dao.EmailQueueDao;
import com.hisoft.hscloud.mail.dao.MailSenderDao;
import com.hisoft.hscloud.mail.dao.MailTemplateDao;
import com.hisoft.hscloud.mail.entity.EmailQueue;
import com.hisoft.hscloud.mail.entity.MailSender;
import com.hisoft.hscloud.mail.entity.MailTemplate;
import com.hisoft.hscloud.mail.util.EmailAccountException;
import com.hisoft.hscloud.mail.util.MailFactory;
import com.hisoft.hscloud.mail.util.MailSenderUtil;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private EmailQueueDao emailQueueDao;
	@Autowired
	private MailTemplateDao mailTemplateDao;
	@Autowired
	private MailSenderDao mailSenderDao;
	
	private Map<Long, MailSender> mailSenderMap = new HashMap<Long, MailSender>();
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Transactional(readOnly = false)
	private long saveEmail(String receiveUsers, String sendUser,
			String subject, String body, long domainId) {
		EmailQueue emailQueue = null;
//		boolean resultFlg = false;
		long id=0;
		if("".equals(receiveUsers.trim())||"".equals(subject.trim())||"".equals(body.trim())){
//			resultFlg = false;
		}else{
			try {
				emailQueue=new EmailQueue();
				emailQueue.setReceiveUsers(receiveUsers);
				emailQueue.setSendUser("");
				emailQueue.setStatus(Constants.MAIL_UNSEND);
				emailQueue.setCreateTime(new Date());
				emailQueue.setSubject(subject);
				emailQueue.setBody(body);
				emailQueue.setDomainId(domainId);
				id=emailQueueDao.createEmailQueue(emailQueue);
				if(id!=0){
					logger.info("save success");
//					resultFlg = true;
				}
			}catch (Exception e) {
				logger.error("saveEmail Exception:",e);
			}
		}		
		//return resultFlg;
		return id;
	}
	
	
	public void sendEmail() {
		logger.debug("search email list");
	//	List<String> recipients =new ArrayList<String>();
	//	if(mailSenderMap.isEmpty()) {
            findMailSender();
   //     }
		
		List<EmailQueue> list=emailQueueDao.getEmailQueueByStatus(0);
		
		for(EmailQueue eq:list){
			sendEmail(eq);		
		}		
	}
	
	@Transactional(readOnly = false)
	private void sendEmail(EmailQueue eq){
	    MailSenderUtil sender = null;
		try {	
			String[] receiveUserArray = null;
			receiveUserArray=eq.getReceiveUsers().split(";");							
			if(receiveUserArray.length>1){				
				/*for(int i=0;i<receiveUserArray.length;i++){
					recipients.add(receiveUserArray[i]);
				}	*/
			    MailSender mailSender = mailSenderMap.get(eq.getDomainId());
			    if(mailSender == null) {
			        mailSender = mailSenderMap.get(0l);
			    }
			    MailFactory factory = new MailFactory();
				sender = factory.getSender(mailSender);
				sender.send(Arrays.asList(receiveUserArray), 
				        javax.mail.internet.MimeUtility.encodeText(eq.getSubject()),eq.getBody());
				eq.setSendUser(MailFactory.getSenderUsername());	
			}else{					
			    MailSender mailSender = mailSenderMap.get(eq.getDomainId());
                if(mailSender == null) {
                    mailSender = mailSenderMap.get(0l);
                }
                MailFactory factory = new MailFactory();
                sender = factory.getSender(mailSender);
				logger.info("mail ReceiveUsers:"+eq.getReceiveUsers());
				sender.send(eq.getReceiveUsers().trim(), eq.getSubject(), eq.getBody());
				logger.info("mail SenderUser:"+MailFactory.getSenderUsername());
				eq.setSendUser(MailFactory.getSenderUsername());					
			}
			eq.setStatus(Constants.MAIL_SENDED);
			eq.setPostTime(new Date());	
			emailQueueDao.updateEmailQueue(eq);
		} catch (EmailAccountException e) {
			logger.error("EmailAccount Exception:",e);
		} catch (AddressException e) {
			logger.error("Address Exception:",e);
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncoding Exception:",e);
		} catch (MessagingException e) {
			logger.error("Messaging Exception:",e);
		} catch(Exception e){
			logger.error("sendEmail Exception:",e);
		}
	}

	/**
	 * 获得mail项目下的模板
	 */
	@Override
	@Transactional(readOnly = false)
	public MailTemplate getMailTemplate(String keyword, long domainId) {
		
		if(logger.isDebugEnabled()){
			logger.debug("enter getMailTemplate");
			logger.debug("keyword:"+keyword);
		}
		
		MailTemplate template = mailTemplateDao.findMailTemplate(keyword, domainId);
		
		if(logger.isDebugEnabled()){
			logger.debug("template"+template);
			logger.debug("exit getMailTemplate");
		}
		return template;
	}
	
	/**
	 * <生成邮件> 
	* <功能详细描述> 
	* @param receiveUsers
	* @param sendUser
	* @param keyword
	* @param domain
	* @param map
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	@Override
	@Transactional(readOnly = false)
	public long saveEmail(String receiveUsers, String sendUser,String keyword, long domainId, Map<String,String> map){
	    MailTemplate template = getMailTemplate(keyword, domainId);
	    StringBuilder subject=new StringBuilder(template.getTitle());
	    if(keyword.equals(MailTemplateType.OPEN_VM_TEMPLATE.getType())){
	    	String ip=map.get("ip");
	    	if(StringUtils.isNotBlank(ip)){
	    		subject.append("(").append(ip).append(")");
	    	}
	    }
	    return saveEmail(receiveUsers, sendUser,subject.toString(),template.getReplacedTemplate(map), domainId);
	   
    }
	
	/**
	 *创建分平台模板
	* @param id
	 */
    @Override
    public void createMailTemplate(long domainId) {
        mailTemplateDao.createMailTemplate(domainId);
    }
    
    /**
     *创建分平台邮件发送者
    * @param id
     */
    @Override
    public void createMailSender(long domainId) {
        mailSenderDao.createMailSender(domainId);
        findMailSender();
    }
    
    public void findMailSender() {
        List<MailSender> list = mailSenderDao.findMailSender();
        for(MailSender mailSender : list) {
            mailSenderMap.put(mailSender.getDomainId(), mailSender);
        }
        
    }

}

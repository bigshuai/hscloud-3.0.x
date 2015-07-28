package com.hisoft.hscloud.mail.service;


import java.util.Map;

import com.hisoft.hscloud.mail.entity.MailTemplate;

public interface MailService {
	
	public void sendEmail();
    
	/**
	 * <获取邮件模板> 
	* <功能详细描述> 
	* @param keyword
	* @param domainId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public MailTemplate getMailTemplate(String keyword, long domainId);
	
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
    public long saveEmail(String receiveUsers, String sendUser, String keyword,
            long domainId, Map<String, String> map);
    
    /**
     * <创建分平台模板> 
    * <功能详细描述> 
    * @param id 
    * @see [类、类#方法、类#成员]
     */
    public void createMailTemplate(long domainId);
    
    /**
     * <创建分平台邮件发送者> 
    * <功能详细描述> 
    * @param domainId 
    * @see [类、类#方法、类#成员]
     */
    public void createMailSender(long domainId);
}

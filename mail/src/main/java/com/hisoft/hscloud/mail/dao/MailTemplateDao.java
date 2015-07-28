package com.hisoft.hscloud.mail.dao;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.mail.entity.MailTemplate;


public interface MailTemplateDao {
	
	public MailTemplate findUniqueBy(String propertyName, Object value);

	/**
	 * <获取邮件模板> 
	* <功能详细描述> 
	* @param keyword
	* @param domainId
	* @return 
	* @see [类、类#方法、类#成员]
	 */
    public MailTemplate findMailTemplate(String keyword, long domainId);

    /**
     * <创建分平台模板> 
    * <功能详细描述> 
    * @param domainId 
    * @see [类、类#方法、类#成员]
     */
    public void createMailTemplate(long domainId) throws HsCloudException;
}

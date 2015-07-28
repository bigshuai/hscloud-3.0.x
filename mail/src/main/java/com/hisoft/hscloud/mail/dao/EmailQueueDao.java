package com.hisoft.hscloud.mail.dao;

import java.util.List;

import com.hisoft.hscloud.mail.entity.EmailQueue;

public interface EmailQueueDao {
	
	public long createEmailQueue(EmailQueue emailQueue);
	public boolean updateEmailQueue(EmailQueue emailQueue);
	public List<EmailQueue> getEmailQueueByFieldCondition(String field,String fieldValue);
	public List<EmailQueue> getEmailQueueByStatus(int status);

}

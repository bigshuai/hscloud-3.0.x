package com.hisoft.hscloud.mail.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.mail.entity.EmailQueue;
@Repository
public class EmailQueueDaoImpl extends HibernateDao<EmailQueue, Long> implements EmailQueueDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Override
	//@Transactional
	public long createEmailQueue(EmailQueue emailQueue) {
		//hibernateTemplate.saveOrUpdate(emailQueue);
		this.save(emailQueue);
		return emailQueue.getId();
	}

	@Override
	public boolean updateEmailQueue(EmailQueue emailQueue) {
		boolean result=false;
		try{			
			hibernateTemplate.saveOrUpdate(emailQueue);
			result=true;
		}catch (Exception e) {			
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EmailQueue> getEmailQueueByFieldCondition(String field,
			String fieldValue) {
		String hql = "from EmailQueue eq where "+field+"='"+fieldValue+"'";			
		List<EmailQueue> list = hibernateTemplate.find(hql);		
		return list;
	}

	@Override
	@Transactional
	public List<EmailQueue> getEmailQueueByStatus(int status) {
		String hql = "from EmailQueue eq where status = ?";
		return this.find(hql,status);
	}

    


}

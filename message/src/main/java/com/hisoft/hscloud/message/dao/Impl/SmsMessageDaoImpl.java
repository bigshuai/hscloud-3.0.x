package com.hisoft.hscloud.message.dao.Impl; 

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.message.dao.SmsMessageDao;
import com.hisoft.hscloud.message.entity.SMSMessage;


@Repository
public class SmsMessageDaoImpl extends HibernateDao<SMSMessage,Long> implements SmsMessageDao {

	@Override
	public void saveSmsMessage(SMSMessage smsMessage) {
		this.save(smsMessage);
	}

	@Override
	public Page<SMSMessage> findSmsMessagePage(Page<SMSMessage> page,
			String query) {
		String hql = "from SMSMessage where status =0";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(query)) {
			hql += " and (content like :query or creater like:query)";
			paramMap.put("query", query);
		}
		hql += " order by createDate desc ";
		return this.findPage(page, hql, paramMap);
	}

	@Override
	public void delSmsMessage(SMSMessage smsMessage) {
		this.save(smsMessage);
	}

	@Override
	public SMSMessage findSmsMessageById(long id) {
		return this.findUniqueBy("id", id);
	}

}

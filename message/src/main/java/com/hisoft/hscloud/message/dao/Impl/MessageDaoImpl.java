/**
 * 
 */
package com.hisoft.hscloud.message.dao.Impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.hisoft.hscloud.message.dao.MessageDao;
import com.hisoft.hscloud.message.entity.Message;
import com.hisoft.hscloud.message.util.Constant;

/**
 * 消息数据库接口实现
 * @author lihonglei
 *
 */
@Repository
public class MessageDaoImpl extends HibernateDao<Message,Long> implements MessageDao {
	/**
	 * 查询消息列表
	* @param page
	* @param userId
	* @param status
	* @param condition
	* @return
	 */
    @Override
	public Page<Message> findMessage(Page<Message> page, Long userId, int status, Map<String, Object> condition) {
		String hql = "from Message where userId = :userId and status = :status";
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("status", status);
		
		if(condition.containsKey("dateFrom")) {
			hql += " and create_time >= :dateFrom";
			paramMap.put("dateFrom", condition.get("dateFrom"));
		}
		if(condition.containsKey("dateTo")) {
			Date date = (Date) condition.get("dateTo");
			Date addDays = DateUtils.addDays(date, 1);
			hql += " and create_time < :dateTo";
			paramMap.put("dateTo",addDays);
		}
		hql += " order by " + page.getOrderBy() + " " + page.getOrder();
		return this.findPage(page, hql, paramMap);
	}
	
	@Override
	public Long findUnreadCount(Long userId) {
		String hql = "select count(id) from Message where userId = :userId and status = :status";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("status", Constant.MESSAGE_STATUS_UNREAD);
		return this.findUnique(hql, paramMap);
	}

	@Override
	public long saveMessage(Message message) {
		this.save(message);
		return message.getId();
	}

	@Override
	public Message getMessage(Long id) {
		return this.findUniqueBy("id", id);
	}

	@Override
	public void deleteMessage(Long id) {
		this.delete(id);
	}

	@Override
	public void changeAllMessage(Long userId) {
		String hql= "update Message set status = 2 where userId = :userId and status = :status";
		Map<String, Object>paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("status", Constant.MESSAGE_STATUS_UNREAD);
		this.createQuery(hql, paramMap).executeUpdate();
	}

	@Override
	public void deleteAllMessage(Long userId) {
		String hql= "delete Message where userId = :userId and status = :status";
		Map<String, Object>paramMap = new HashMap<String,Object>();
		paramMap.put("userId", userId);
		paramMap.put("status", Constant.MESSAGE_STATUS_READED);
		this.createQuery(hql, paramMap).executeUpdate();
		
	}
}

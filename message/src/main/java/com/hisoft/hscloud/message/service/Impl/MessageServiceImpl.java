/**
 * 
 */
package com.hisoft.hscloud.message.service.Impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.message.dao.MessageDao;
import com.hisoft.hscloud.message.entity.Message;
import com.hisoft.hscloud.message.service.MessageService;
import com.hisoft.hscloud.message.util.Constant;

/**
 * @author lihonglei
 *
 */
@Service
public class MessageServiceImpl implements MessageService{
	@Autowired
	private MessageDao messageDao;
	
	@Override
	public Page<Message> findUnreadMessage(Page<Message> page, Long userId, Map<String, Object> condition) {
		return messageDao.findMessage(page, userId, Constant.MESSAGE_STATUS_UNREAD, condition);
	}
	
	@Override
	public Page<Message> findReadedMessage(Page<Message> page, Long userId, Map<String, Object> condition) {
		return messageDao.findMessage(page, userId, Constant.MESSAGE_STATUS_READED, condition);
	}
	
	@Override
	public long findUnreadCount(Long userId) {
		return messageDao.findUnreadCount(userId);
	}

	@Override
	@Transactional(readOnly = false)
	public long saveMessage(Message message) {
		return messageDao.saveMessage(message);
	}
	
	@Override
	public void modifyMessageStatus(Long id) {
		Message message = messageDao.getMessage(id);
		message.setStatus(Constant.MESSAGE_STATUS_READED);
		messageDao.saveMessage(message);
	}

	@Override
	public void deleteMessage(Long id) {
		messageDao.deleteMessage(id);
	}

	@Override
	public void modifyAllMessageStatus(Long id) {
		messageDao.changeAllMessage(id);
		
	}
	
	@Override
	public void deleteAllMessage(Long id){
		messageDao.deleteAllMessage(id);
	}
	
}

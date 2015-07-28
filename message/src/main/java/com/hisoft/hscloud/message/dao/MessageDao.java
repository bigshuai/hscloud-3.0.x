/**
 * 
 */
package com.hisoft.hscloud.message.dao;

import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.message.entity.Message;

/**
 * @author lihonglei
 *
 */
public interface MessageDao {
	public Page<Message> findMessage(Page<Message> page, Long userId, int status, Map<String, Object> condition);
	
	public Long findUnreadCount(Long userId);
	
	public long saveMessage(Message message);
	
	public Message getMessage(Long id);
	
	public void deleteMessage(Long id);
	
	public void changeAllMessage(Long userId);
	
	public void deleteAllMessage(Long userId);
}

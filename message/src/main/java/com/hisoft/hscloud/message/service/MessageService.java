/**
 * 
 */
package com.hisoft.hscloud.message.service;

import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.message.entity.Message;

/**
 * @author lihonglei
 *
 */
public interface MessageService {
	/**
	 * 查找未读信息
	 * @param page
	 * @param userId
	 * @param condition
	 * @return
	 */
	public Page<Message> findUnreadMessage(Page<Message> page, Long userId, Map<String, Object> condition);
	/**
	 * 查找未读信息数量
	 * @param userId
	 * @return
	 */
	public long findUnreadCount(Long userId);
	/**
	 * 保存信息内容
	 * @param message
	 * @return
	 */
	public long saveMessage(Message message);
	/**
	 * 查找已读信息
	 * @param page
	 * @param userId
	 * @param condition
	 * @return
	 */
	public Page<Message> findReadedMessage(Page<Message> page, Long userId, Map<String, Object> condition);
	/**
	 * 修改信息状态
	 * @param id
	 */
	public void modifyMessageStatus(Long id);
	/**
	 * 删除消息 
	* <功能详细描述> 
	* @param id 
	* @see [类、类#方法、类#成员]
	 */
	public void deleteMessage(Long id);
	/**
	 * x修改全部信息状态
	 * @param id
	 */
	public void modifyAllMessageStatus(Long id);
	
	/**
	 * 删除全部已读消息
	 */
	public void deleteAllMessage(Long id);
}

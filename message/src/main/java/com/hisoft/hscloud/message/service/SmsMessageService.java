package com.hisoft.hscloud.message.service; 

import java.util.List;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.message.entity.SMSMessage;


/**
 * @author yubenjie
 * 短信信息管理
 *
 */
public interface SmsMessageService {
	/**
	 * 保存短信信息并发送
	 */
	public void saveSmsMessage(SMSMessage smsMessage,List<String> mobileList);
	/**
	 * 显示发送信息
	 */
	public Page<SMSMessage> findSmsMessagePage(Page<SMSMessage> page,String query);
	/**
	 * 删除短信信息
	 */
	public void delSmsMessage(SMSMessage smsMessage);
	/**
	 * @param id 短信id
	 * @return
	 * 通过id获取短信信息
	 */
	public SMSMessage findSmsMessageById(long id);
}

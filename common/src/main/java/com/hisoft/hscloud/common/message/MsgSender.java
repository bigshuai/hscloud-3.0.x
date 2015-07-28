package com.hisoft.hscloud.common.message;

import java.io.IOException;
/**
 * 
* @description 这里用一句话描述这个类的作用
* @version 1.0
* @author AaronFeng
* @update 2012-5-9 下午4:03:30
 */
 public interface MsgSender {	 
	 /**
	  * 
	 * @title: sendRMQMessage
	 * @description 用一句话说明这个方法做什么
	 * @param message
	 * @throws IOException 设定文件
	 * @return void    返回类型
	 * @throws
	 * @version 1.0
	 * @author AaronFeng
	 * @update 2012-5-9 下午4:03:45
	  */
	 public void sendRMQMessage(String queueName, String message) throws IOException;
	 /**
	  * 
	 * @title: sendRMQTaskMessage
	 * @description 用一句话说明这个方法做什么
	 * @param strings
	 * @throws IOException 设定文件
	 * @return void    返回类型
	 * @throws
	 * @version 1.0
	 * @author AaronFeng
	 * @update 2012-5-9 下午4:03:53
	  */
	 public void sendRMQTaskMessage(String taskQueueName, String message) throws IOException;
	 /**
	  * 
	 * @title: sendRMQLogMessage
	 * @description 用一句话说明这个方法做什么
	 * @param strings
	 * @param exchangeType
	 * @throws IOException 设定文件
	 * @return void    返回类型
	 * @throws
	 * @version 1.0
	 * @author AaronFeng
	 * @update 2012-5-9 下午4:04:02
	  */
	 public void sendRMQLogMessage(String exchangeName,String message,String exchangeType,String routingKey) throws IOException;
	 public void sendMessage(String queueName, String message) throws IOException;
	 public void sendMessage(String exchangeName,String message,String exchangeType,String routingKey) throws IOException;

}

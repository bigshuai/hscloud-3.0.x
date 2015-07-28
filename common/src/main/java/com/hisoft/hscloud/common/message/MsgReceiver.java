package com.hisoft.hscloud.common.message;

import java.io.IOException;
import java.util.List;
/**
 * 
* @description 这里用一句话描述这个类的作用
* @version 1.0
* @author ljg
* @update 2012-5-10 上午10:11:57
 */
 public interface MsgReceiver {
	 /**
	  * 
	 * @title: receiveRMQMessage
	 * @description 用一句话说明这个方法做什么
	 * @throws Exception 设定文件
	 * @return void    返回类型
	 * @throws
	 * @version 1.0
	 * @author ljg
	 * @update 2012-5-9 下午4:21:58
	  */
	 public void receiveRMQMessage(String queueName) throws Exception;
	 
	 public List receiveRMQMessage(String exchangeName, String queueName,
				String routingKey) throws Exception;
	 /**
	  * 
	 * @title: receiveRMQWorkerMessage
	 * @description 用一句话说明这个方法做什么
	 * @throws IOException
	 * @throws InterruptedException 设定文件
	 * @return void    返回类型
	 * @throws
	 * @version 1.0
	 * @author ljg
	 * @update 2012-5-9 下午4:22:04
	  */
	 public void receiveRMQWorkerMessage(String taskQueueName) throws IOException, InterruptedException;
	 /**
	  * 
	 * @title: receiveRMQLogMessage
	 * @description 用一句话说明这个方法做什么
	 * @param exchangeType
	 * @throws Exception 设定文件
	 * @return void    返回类型
	 * @throws
	 * @version 1.0
	 * @author ljg
	 * @update 2012-5-9 下午4:22:14
	  */
	 public void receiveRMQLogMessage(String queueName,String exchangeName, String exchangeType,String routingKey) throws Exception;

}

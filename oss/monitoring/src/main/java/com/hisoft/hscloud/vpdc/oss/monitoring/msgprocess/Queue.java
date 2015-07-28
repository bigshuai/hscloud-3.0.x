/**
 * @title Queue.java
 * @package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess
 * @description 
 * @author YuezhouLi
 * @update 2012-5-24 上午9:32:06
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess;

import org.apache.log4j.Logger;

import com.hisoft.hscloud.common.message.RabbitMQConnection;
//import com.rabbitmq.client.ConnectionFactory;

/**
 * @description
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-24 上午9:32:06
 */
public class Queue extends RabbitMQConnection {
	private Logger logger = Logger.getLogger(this.getClass());
	private String queneName;
	//private ConnectionFactory qFactory;
	private MsgDispatchPolicy dispatchPolicy;

	public void subscribe() {
		//logger.info("===>>>The queueName is :" + queneName + " the qFactory:"
				//+ qFactory + " the dispatch plicy is :" + this.dispatchPolicy);
//		MsgReceiver receiver = new MsgReceiver(this.queneName, this.qFactory,
//				this.dispatchPolicy);
		//receiver.start();
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public String getQueneName() {
		return queneName;
	}

	public void setQueneName(String queneName) {
		this.queneName = queneName;
	}
/**
	public ConnectionFactory getqFactory() {
		return qFactory;
	}

	public void setqFactory(ConnectionFactory qFactory) {
		this.qFactory = qFactory;
	}
**/
	public MsgDispatchPolicy getDispatchPolicy() {
		return dispatchPolicy;
	}

	public void setDispatchPolicy(MsgDispatchPolicy dispatchPolicy) {
		this.dispatchPolicy = dispatchPolicy;
	}
	

}

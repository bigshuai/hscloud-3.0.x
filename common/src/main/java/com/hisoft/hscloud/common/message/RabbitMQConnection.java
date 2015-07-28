package com.hisoft.hscloud.common.message;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConnection {
	private Logger log = Logger.getLogger(this.getClass());
	//private ConnectionFactory qFactory;
	
	/**
	 * @return qFactory : return the property qFactory.
	 */
	/**
	public ConnectionFactory getQFactory() {
		return qFactory;
	}*/

	/**
	 * @param qFactory : set the property qFactory.
	 */
//	@Autowired
//	public void setQFactory(@Qualifier("qFactory")ConnectionFactory qFactory) {
//		this.qFactory = qFactory;
//	}
	/**
	private Connection conn = null;
	public Channel channel = null;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}*/

	public void init() throws Exception {
		/**
		if(conn != null && conn.isOpen()){
			if(conn == null || !channel.isOpen()){
				channel = conn.createChannel();
			}
		}else {
			conn = qFactory.newConnection();
			channel = conn.createChannel();
		}**/
	}

	public void close() {
		/**
		if (channel.isOpen()) {
			try {
				channel.close();
			} catch (IOException e) {
				log.error(e);
			}
		}
		if (conn.isOpen()) {
			try {
				conn.close();
			} catch (IOException e) {
				log.error(e);
			}
		}**/
	}
}

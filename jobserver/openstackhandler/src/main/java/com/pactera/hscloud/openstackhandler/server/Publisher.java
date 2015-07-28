package com.pactera.hscloud.openstackhandler.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import com.rabbitmq.client.ConnectionFactory;

import com.rabbitmq.client.MessageProperties;

/**
 * 向rabbitmq发送Openstack消息。
 * @author Minggang
 *
 */
public class Publisher {

	private static final String EXCHANGE_NAME = "hs_exc_4job";// 定义Exchange名称

	private static final boolean durable = true;// 消息队列持久化

	private static final String host = "192.168.4.10";

	public static void main(String[] args) throws java.io.IOException {

		ConnectionFactory factory = new ConnectionFactory();// 创建链接工厂

		factory.setHost(host);

		Connection connection = factory.newConnection();// 创建链接

		Channel channel = connection.createChannel();// 创建信息通道

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout", durable);// 创建交换机并生命持久化
		for (int i = 0; i < 100; i++) {
			String message = "Hello Wrold " + Math.random() + "-----" + i;
			System.out.println(message);
			channel.basicPublish(EXCHANGE_NAME, "",
					MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		}
		channel.close();
		connection.close();

	}

}

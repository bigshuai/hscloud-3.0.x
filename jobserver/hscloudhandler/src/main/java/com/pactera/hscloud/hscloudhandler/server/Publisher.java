package com.pactera.hscloud.hscloudhandler.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.hisoft.hscloud.common.entity.HcEventResource;
import com.hisoft.hscloud.common.entity.HcEventVmOps;
import com.hisoft.hscloud.common.util.Constants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * 向rabbitmq发送消息。
 * 
 * @author Minggang
 * 
 */
public class Publisher {

	private static final String EXCHANGE_NAME = "hs_exc_4job";// 定义Exchange名称

	private static final boolean durable = true;// 消息队列持久化

	private static final String host = "192.168.7.137";

	public static void main(String[] args) throws java.io.IOException {

		ConnectionFactory factory = new ConnectionFactory();// 创建链接工厂

		factory.setHost(host);

		Connection connection = factory.newConnection();// 创建链接

		Channel channel = connection.createChannel();// 创建信息通道

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout", durable);// 创建交换机并生命持久化
		// for (int i = 0; i < 100; i++) {
		Map<String, Object> map = new HashMap<String, Object>();
		HcEventResource her=new HcEventResource();
		HcEventVmOps hvo=new HcEventVmOps();
		hvo.setEvent_time(new Date());
		//her.setEvent_time(new Date());
		hvo.setOperator("houyh1");
		//her.setOperator("houyh1");
		map.put(Constants.RABBITMQ_METHOD_NAME, "openVM");
		//map.put(Constants.RABBITMQ_METHOD_NAME, "testInvoke");
		//map.put(Constants.RABBITMQ_PARAM, her);
		map.put(Constants.RABBITMQ_PARAM, "uuuuuuuuuiiiiiiiiddddddd");
		//Constants.RABBITMQ_OPSLOG
		//map.put(Constants.RABBITMQ_RESOURCELOG, her);
		map.put(Constants.RABBITMQ_OPSLOG, hvo);
		JSONObject jSONObject = JSONObject.fromObject(map);
		String message = jSONObject.toString();
		System.out.println(message);
		channel.basicPublish(EXCHANGE_NAME, "",
				MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		// }
		channel.close();
		connection.close();

	}

}

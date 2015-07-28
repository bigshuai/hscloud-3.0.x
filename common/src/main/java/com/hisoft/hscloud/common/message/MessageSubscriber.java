/**
 * @title MessageSubscriber.java
 * @package com.hisoft.hscloud.common.message
 * @description 用一句话描述该文件做什么
 * @author AaronFeng
 * @update 2012-4-27 上午10:58:02
 * @version V1.0
 */
package com.hisoft.hscloud.common.message;

import java.rmi.RemoteException;
import java.util.Hashtable;
/**
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;**/
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author AaronFeng
 * @update 2012-4-27 上午10:58:02
 */
public class MessageSubscriber {
	String topicName = "myTopic";
	/**
	TopicConnectionFactory topicConnectionFactory = null;
	TopicConnection topicConnection = null;
	Topic topic = null;
	TopicSession topicSession = null;
	TopicSubscriber topicSubscriber = null;
	TextMessage message = null;**/
	String id = "durable";
/**
	public static void main(String[] args) throws Exception {
		InitialContext ic = getInitialContext();
		MessageSubscriber subscriber = new MessageSubscriber();
		subscriber.init(ic);
		subscriber.subscribe();
		subscriber.close();
	}
**/
	public void init(InitialContext ctx) throws Exception {
		/**
		topicConnectionFactory = (TopicConnectionFactory) ctx
				.lookup("jms/TopicConnectionFactory");
		topicConnection = topicConnectionFactory.createTopicConnection();
		topicConnection.setClientID(id);
		topic = (Topic) ctx.lookup(topicName);**/
	}

	public void subscribe() throws NamingException,
			RemoteException {
		/**
		topicSession = topicConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		topicSubscriber = topicSession.createDurableSubscriber(topic, id);
		topicConnection.start();
		message = (TextMessage) topicSubscriber.receive();
		System.out.println("SUBSCRIBER THREAD: Reading message: "
				+ message.getText());**/
	}

	public void close() {
		/**
		if (topicConnection != null)
			topicConnection.close();**/
	}

	private static InitialContext getInitialContext() throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.apusic.naming.jndi.CNContextFactory");
		env.put(Context.PROVIDER_URL, "iiop://localhost:4888");
		return (new InitialContext(env));
	}

}

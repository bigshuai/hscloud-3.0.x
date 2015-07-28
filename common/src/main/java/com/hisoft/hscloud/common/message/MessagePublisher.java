/**
 * @title MessagePublisher.java
 * @package com.hisoft.hscloud.common.message
 * @description 用一句话描述该文件做什么
 * @author AaronFeng
 * @update 2012-4-27 上午10:55:57
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
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
**/
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author AaronFeng
 * @update 2012-4-27 上午10:55:57
 */
public class MessagePublisher {
	String topicName = "myTopic";
	/**
	TopicConnectionFactory topicConnectionFactory = null;
	Topic topic = null;
	TopicConnection topicConnection = null;
	TopicSession topicSession = null;
	TopicPublisher topicPublisher = null;
	String msgText = null;
	TextMessage message = null;

	public static void main(String[] args) throws Exception {

		InitialContext ic = getInitialContext();
		MessagePublisher publisher = new MessagePublisher();
		publisher.init(ic);
		publisher.publish();
		publisher.close();
	}
**/
	public void init(InitialContext ctx) throws Exception {
/**
		topicConnectionFactory = (TopicConnectionFactory) ctx
				.lookup("jms/TopicConnectionFactory");
		topicConnection = topicConnectionFactory.createTopicConnection();
		topic = (Topic) ctx.lookup(topicName);**/
	}

	public void publish() throws NamingException, RemoteException {
/**
		topicSession = topicConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);
		topicPublisher = topicSession.createPublisher(topic);
		message = topicSession.createTextMessage();
		msgText = "This is the published message";
		message.setText(msgText);
		topicPublisher.publish(message);**/
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

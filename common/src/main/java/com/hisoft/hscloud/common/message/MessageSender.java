/**
 * @title Queue.java
 * @package com.hisoft.hscloud.common.message
 * @description 用一句话描述该文件做什么
 * @author AaronFeng
 * @update 2012-4-27 上午10:23:32
 * @version V1.0
 */
package com.hisoft.hscloud.common.message;

import java.rmi.RemoteException;
/**
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;**/
import javax.naming.InitialContext;
//import org.apache.activemq.ActiveMQConnectionFactory;
import com.hisoft.hscloud.common.util.BeanReadUtil;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author AaronFeng
 * @update 2012-4-27 上午10:23:32
 */
public class MessageSender {
	//String url = "tcp://192.168.5.69:61616";
	//String queueName = "testQueue";// 消息的目的队列的队列名
	String routerName = "RouterB";// 消息的目的节点的路由名
	/**
	QueueConnectionFactory queueConnectionFactory = null;
	Queue queue = null;
	QueueConnection queueConnection = null;
	QueueSession queueSession = null;
	QueueSender queueSender = null;
	TextMessage message = null;**/
	String url;
	String queueName;
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getQueueName() {
		return queueName;
	}
	
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
/**
	public static void main(String[] args) throws Exception {

		InitialContext ic = null;//getInitialContext();
		MessageSender sender = new MessageSender();		
		sender.init(ic);
		sender.sendMessage("{\"username\": \"admin\", \"password\": \"hisoft\"}");
		sender.close();
	}
**/
	public void init(InitialContext ctx) throws Exception {
		//MessageReceiver receiver=(MessageReceiver)BeanReadUtil.getBean("messageReceiver");
		//ActiveMQConnectionFactory queueConnectionFactory = new ActiveMQConnectionFactory(receiver.getUrl());
		//connection = connectionFactory.createConnection();

		//queueConnectionFactory = (QueueConnectionFactory) ctx.lookup("jms/QueueConnectionFactory");
		//queueConnection = queueConnectionFactory.createQueueConnection();
	}

	public void sendMessage(String messageContent) throws RemoteException {
/**
		queueSession = queueConnection.createQueueSession(false,
				Session.AUTO_ACKNOWLEDGE);
		// 队列由："队列名" + "@" + "目的节点路由名" 在网络中唯一标识，通过该方式创建的队列
		// 将代表远程节点上的某个队列
		queue = queueSession.createQueue(queueName + "@" + routerName);
		queueSender = queueSession.createSender(queue);
		queueSender.setDeliveryMode(DeliveryMode.PERSISTENT);
		message = queueSession.createTextMessage();
		message.setText(messageContent);
		queueSender.send(message);**/
	}

	public void close() {
/**
		if (queueConnection != null)
			queueConnection.close();**/
	}

//	private static InitialContext getInitialContext() throws NamingException {
//		
//		Hashtable<String, String> env = new Hashtable<String, String>();
//		env.put(Context.INITIAL_CONTEXT_FACTORY,
//				"org.apache.activemq.ActiveMQConnectionFactory");
//		env.put(Context.PROVIDER_URL, "tcp://192.168.5.69:61616");
//		return (new InitialContext(env));
//
//	}

}

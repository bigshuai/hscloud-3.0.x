/**
 * @title MessageReceive.java
 * @package com.hisoft.hscloud.common.message
 * @description 用一句话描述该文件做什么
 * @author AaronFeng
 * @update 2012-4-27 上午10:36:40
 * @version V1.0
 */
package com.hisoft.hscloud.common.message;

import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
/**
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;**/
import javax.naming.InitialContext;
import javax.naming.NamingException;

//import org.apache.activemq.ActiveMQConnectionFactory;
import com.hisoft.hscloud.common.util.BeanReadUtil;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author AaronFeng
 * @update 2012-4-27 上午10:36:40
 */
public class MessageReceiver {
	//String url = "tcp://192.168.5.69:61616";
	//String queueName = "testQueue";
	String routerName = "RouterB";// 消息的目的节点的路由名
	/**
	QueueConnectionFactory queueConnectionFactory = null;
	Queue queue = null;
	QueueConnection queueConnection = null;
	QueueSession queueSession = null;
	QueueReceiver queueReceiver = null;
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
	public QueueConnection getQueueConnection() {
		return queueConnection;
	}
	
	public void setQueueConnection(QueueConnection queueConnection) {
		this.queueConnection = queueConnection;
	}
	
	public static void main(String[] args) throws Exception {
		
		InitialContext ic = null;//getInitialContext();
		MessageReceiver receiver = new MessageReceiver();				
		receiver.init(ic);
		receiver.TBreceiveMessage();// 你可以在此处调用YBreceiveMessage
		receiver.close();
	}
	**/

	public void init(InitialContext ctx) throws Exception {
		//MessageReceiver receiver=(MessageReceiver)BeanReadUtil.getBean("messageReceiver");
		//ActiveMQConnectionFactory queueConnectionFactory = new ActiveMQConnectionFactory(receiver.getUrl());
		//queueConnectionFactory = (QueueConnectionFactory) ctx.lookup("jms/QueueConnectionFactory");
		//queueConnection = queueConnectionFactory.createQueueConnection();
		// 采用以下的lookup方法找回来的队列是本地节点的队列
		
		//queue = (Queue) ctx.lookup(queueName);
		// 假如想收取远程节点的队列上的消息时，采用以下的createQueue方法创建一个远程的队列。
		// 队列由："队列名" + "@" + "目的节点路由名" 在网络中唯一标识。
		// 通过该方式创建的队列将代表远程节点上的某个队列，收取消息时将直接收取该节点的该队列上的消息。
		// queue = queueSession.createQueue(queueName + "@" + routerName);		
	}

	public void TBreceiveMessage() throws NamingException,
			RemoteException {
		/**
		queueSession = queueConnection.createQueueSession(false,
				Session.AUTO_ACKNOWLEDGE);
		queue = queueSession.createQueue(queueName + "@" + routerName);
		queueReceiver = queueSession.createReceiver(queue);
		queueConnection.start();
		for (;;) {
			message = (TextMessage) queueReceiver.receive();
			System.out.println("Reading message: " + message.getText());
			if (message.getText().equals("quit"))
				break;
		}**/
	}

	public void YBreceiveMessage() throws NamingException,
			RemoteException, IOException {
/**
		queueSession = queueConnection.createQueueSession(false,
				Session.AUTO_ACKNOWLEDGE);
		queueReceiver = queueSession.createReceiver(queue);
		// register my textListener which comes from MessageListener
		TextMessageListener textListener = new TextMessageListener();
		queueReceiver.setMessageListener(textListener);
		queueConnection.start();

		System.out.println("To end program, enter Q or q, then ");
		InputStreamReader reader = new InputStreamReader(System.in);
		char answer = '\0';

		while (!((answer == 'q') || (answer == 'Q')))
			answer = (char) reader.read();**/
	}

	public void close() {
/**
		if (queueReceiver != null)
			queueReceiver.close();
		if (queueSession != null)
			queueSession.close();
		if (queueConnection != null)
			queueConnection.close();**/
	}

//	private static InitialContext getInitialContext() throws NamingException {
//
//		Hashtable<String, String> env = new Hashtable<String, String>();
//		env.put(Context.INITIAL_CONTEXT_FACTORY,
//				"com.apusic.naming.jndi.CNContextFactory");
//		env.put(Context.PROVIDER_URL, "iiop://localhost:4888");
//		return (new InitialContext(env));
//
//	}

}

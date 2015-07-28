package com.pactera.hscloud.openstackhandler.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.SecurityHelper;
import com.pactera.hscloud.common4j.util.FileUtil;
import com.pactera.hscloud.openstackhandler.eventhandler.OpenstackEventHandler;
import com.pactera.hscloud.openstackhandler.exception.OpenstackHandlerException;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/*
 * 功能：
 * 1.接收Openstack VM同步信息，状态，更新VM信息，
 * 2.更新IP状态
 * 3.发送开通邮件
 * 4.记录同步消息日志.
 */
public class OpenstackServer {
	private static Log logger = LogFactory.getLog(OpenstackServer.class);
	private static Properties ps;
	private static String host = "192.168.4.10";
	private static String exchange = "hs_exc_4messager";
	private static String queue = "hs_Q_4M";
	private static String username="guest";
	private static String password="";
	private static String securityKey="";
	private static final boolean durable = true;// 消息队列持久化
	private static String job_name = "Messager-1";
	private static int max_thread = 5;
	private static int sleepTime=200;

	static {
		try {
			ps = new Properties();
			ps.load(new InputStreamReader(FileUtil
					.openPropertiesInputStream("messageserver.properties"),
					FileUtil.fileEncode));
			job_name = ps.getProperty("messager.name");
			max_thread = Integer
					.parseInt(ps.getProperty("messager.max_thread"));
			String sleepTimeStr=ps.getProperty("messager.sleepTime");
			if(StringUtils.isNotBlank(sleepTimeStr)){
				sleepTime=Integer.parseInt(sleepTimeStr);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	static {
		try {
			ps = new Properties();
			ps.load(new InputStreamReader(FileUtil
					.openPropertiesInputStream("rabbitmq.properties"),
					FileUtil.fileEncode));
			host = ps.getProperty("rabbitmq.host");
			exchange = ps.getProperty("messager.rabbitmq.exchange");
			username=ps.getProperty("username");
			password=ps.getProperty("password");
			securityKey=ps.getProperty("securityKey");
			if(StringUtils.isEmpty(securityKey)){
				securityKey=Constants.DEFAULT_SECURITY_KEY;
			}
			queue = ps.getProperty("messager.rabbitmq.queue");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException,
			FileNotFoundException, IOException, InterruptedException {
		Properties ps = new Properties();
		ps.load(new InputStreamReader(FileUtil
				.openPropertiesInputStream("log4j.properties"),
				FileUtil.fileEncode));
		PropertyConfigurator.configure(ps);

		//create thread pool
		OpenstackEventHandler job = new OpenstackEventHandler(job_name,
				max_thread);

		// get rabbitMQ message and handle message
		try {
			//init rabbitmq connection
			ConnectionFactory factory = new ConnectionFactory();
			logger.info("host:"+host);
			factory.setHost(host);
			logger.info("username:"+username);
	        factory.setUsername(username);
	        logger.info("password:"+password);
	        logger.info("securityKey:"+securityKey);
	        factory.setPassword(SecurityHelper.DecryptData(password, securityKey));
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(exchange, "fanout", durable);
			DeclareOk ok = channel.queueDeclare(queue, durable, false, false,
					null);

			String queueName = ok.getQueue();
			channel.queueBind(queueName, exchange, "");
			channel.basicQos(1);// 消息分发处理
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, false, consumer);
			logger.info("Begin to receive RabbitMQ event: host[" + host
					+ "] exchange[" + exchange + "] queue[" + queueName
					+ "] max-thread[" + max_thread + "]");

			while (true) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				if (FileUtil.stopProcess()) {
					logger.info("Messager receive stop process event and will be end......");
					break;
				}
				String message = new String(delivery.getBody());
				logger.debug(" [" + queue + "] Received '" + message + "'");
				
				/*
				 * handle rabbitmq message
				 */
				job.Handler(message);

				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				TimeUnit.MILLISECONDS.sleep(sleepTime);
			}
		} catch (OpenstackHandlerException oshe){
			logger.error(""+oshe.getMessage(), oshe);
			oshe.printStackTrace();
		}catch (Exception e) {
			logger.error(""+e.getMessage(), e);
			e.printStackTrace();
		}
		//关闭线程池
		job.Shutdown();
		logger.info("Messager Proecess finished!");
		System.exit(0);	
	}
}

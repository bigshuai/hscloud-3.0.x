/**
 * @title MsgProcess.java
 * @package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess
 * @description 
 * @author YuezhouLi
 * @update 2012-5-24 上午9:39:11
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.hisoft.hscloud.common.vo.ProcessThread;
/**
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;
**/

/**
 * @description
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-24 上午9:39:11
 */
public class MsgReceiver extends ProcessThread {
	private Logger log = Logger.getLogger(this.getClass());
	/*
	 * (非 Javadoc) <p>Title: run</p> <p>Description: </p>
	 * 
	 * @see java.lang.Runnable#run()
	 */
	private String threadName = "MsgReceiver";
	private String queueName ="hs_Q_app_monitor";
	//private ConnectionFactory qFactory;	

	private MsgDispatchPolicy dispatchPolicy;

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
//	public MsgReceiver(String queueName, ConnectionFactory qFactory,
//			MsgDispatchPolicy policy) {
//		this.queueName = queueName;
//		this.qFactory = qFactory;
//		this.dispatchPolicy = policy;
//	}	
	
	private String getHostName(){
		String hostName = "hostname";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostName;
	}

	public void run() {
//		log.info("start thread to subscribe queue");
//		boolean isSignalBroken = false ;
//		String hostName = getHostName();
//		super.setRunFlag(true);
//		super.setThreadName(threadName);
//		while(super.isRunFlag()){
//			try{
//				if (queueName == null || qFactory == null) {
//					log.error("the queue name is null or qFactory is null");
//				} else {
//					Connection conn = null;
//					Channel channel = null;
//					QueueingConsumer consumer = null;
//					try {
//						conn = qFactory.newConnection();
//						channel = conn.createChannel();
//						channel.queueDeclare(queueName, false, false, false, null);
//						// GetResponse response = channel.basicGet("hs_Q_app_SC", true);
//
//						/**
//						 * This tells RabbitMQ not to give more than one message to a
//						 * worker at a time. Or, in other words, don't dispatch a new
//						 * message to a worker until it has processed and acknowledged
//						 * the previous one. Instead, it will dispatch it to the next
//						 * worker that is not still busy
//						 */
//						channel.basicQos(1);
//
//						consumer = new QueueingConsumer(channel);
////						channel.basicConsume(this.queueName, false, consumer);
//						//Use hostname as consume tagname , So that We can monitor who consume this Queue
//						channel.basicConsume(this.queueName, false, hostName, consumer);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					Delivery delivery = null;
//					if (conn == null || channel == null || consumer == null) {
//						log.error("==>>>cannot connect to rabbitmq");
//					} else {
//						while (true) {
//							try {
//								//Get next message
//								delivery = consumer.nextDelivery();
//							} catch (ShutdownSignalException e) {
//								//If rabbitmq-server has closed , out of while
//								e.printStackTrace();
//								isSignalBroken = true ;
//								break;
//							} catch (ConsumerCancelledException e) {
//								e.printStackTrace();
//								log.warn("The consumer has cancelled , Try to re-consume");
//								//If the channel and conn have closed .
//								try{
//									//Sleep 1s and reconnect to rabbitmq-server
//									Thread.sleep(1000);
//									conn = qFactory.newConnection();
//									channel = conn.createChannel();
//									channel.queueDeclare(queueName, false, false, false, null);
//									
//									channel.basicQos(1);
//									
//									consumer = new QueueingConsumer(channel);
//									channel.basicConsume(this.queueName, false, consumer);
//									continue;
//								}catch (IOException e1) {
//									e1.printStackTrace();
//								} catch (InterruptedException e2) {
//									e2.printStackTrace();
//								}
//								
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}  
//							
//							
//							try{
//								
//								//process message
//								String message = null;
//								log.info("==>>>delivery:" + delivery);
//								if (delivery != null) {
//									message = new String(delivery.getBody());
//									log.info("===>>> msg: " + message + " in thread: "
//											+ Thread.currentThread().getName());
//								} else {
//									continue;
//								}
//								log.info("delever msg");
//								MsgDispatcher.dispatche(message, dispatchPolicy);
//							}catch (Exception e) {
//								//If throw exception when process message , close channel and conn , make sure this message not block . then re-work
//								e.printStackTrace();
//								try {
//									channel.basicCancel(hostName);
//									channel.close();
//									conn.close();
//								} catch (IOException e1) {
//									e1.printStackTrace();
//								}
//								continue;
//							}
//							
//							/**
//							 * If a consumer dies without sending an ack, RabbitMQ will
//							 * understand that a message wasn't processed fully and will
//							 * redeliver it to another consumer 
//							 * 
//							 * There aren't any message timeouts; 
//							 * RabbitMQ will redeliver the message only when
//							 * the worker connection dies. It's fine even if processing
//							 * a message takes a very, very long time
//							 */
//							try {
//								channel.basicAck(delivery.getEnvelope()
//										.getDeliveryTag(), false);
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//						//If because of the rabbitmq-server stop ,We will re-try connect to rabbtimq-server after 60s
//						if(isSignalBroken){
//							log.warn("The rabbitmq Server have broken , We Try to re-connect again After 60 seconds");
//							try {
//								Thread.sleep(1000*60);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//							this.run();
//						}
//					}
//				}
//				Thread.sleep(10000);
//			}
//			catch(InterruptedException e){
//				super.setRunFlag(false);
//				e.printStackTrace();
//			}
//		}
//		
	}
}

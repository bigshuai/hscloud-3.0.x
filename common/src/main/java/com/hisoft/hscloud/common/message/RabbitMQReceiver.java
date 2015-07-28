package com.hisoft.hscloud.common.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.common.util.BeanReadUtil;
import com.hisoft.hscloud.common.util.Constants;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.QueueingConsumer;

@Service
 public class RabbitMQReceiver extends RabbitMQConnection implements MsgReceiver{
	
	//@Autowired
	private MsgDBStore msgDBStore;
	//public Channel channel;	
	 
	private Logger log = Logger.getLogger(this.getClass());	

	public MsgDBStore getMsgDBStore() {
		return msgDBStore;
	}

	public void setMsgDBStore(MsgDBStore msgDBStore) {
		this.msgDBStore = msgDBStore;
	}

	/**
	public Channel getChannel() {
		return super.channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}**/

	public void receiveRMQMessage(String queueName) throws Exception{
		/**channel=super.channel;
		channel.queueDeclare(queueName, false, false, false, null);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, false, consumer);
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			log.info("receive message->"+message);
			msgDBStore.saveSingleMessage(message);	
			try {
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}**/
	}
	public List<String> receiveRMQMessage(String exchangeName, String queueName,
			String routingKey) throws Exception {
		List<String> list = new ArrayList<String>();
		/**channel = super.channel;
		channel.queueBind(queueName, exchangeName, routingKey);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);
		while (true) {
			QueueingConsumer.Delivery delivery = null;
			delivery = consumer.nextDelivery(10 * 1000);
			if (delivery == null) {
				break;
			}
			String message = new String(delivery.getBody());
			list.add(message);

		}**/
		return list;
	} 
	public void receiveRMQWorkerMessage(String taskQueueName) throws IOException, InterruptedException{
		/**channel=super.channel;
		channel.queueDeclare(taskQueueName, true, false, false, null);		
		channel.basicQos(1);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(taskQueueName, false, consumer);
	    while (true) {
	      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	      String message = new String(delivery.getBody());
	      log.info("receive message->"+message);
	      doWork(message);
	      
	      msgDBStore.saveSingleMessage(message);	      

	      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
	    }**/
	}
	 
	 public void receiveRMQLogMessage(String queueName,String exchangeName, String exchangeType,String routingKey) throws Exception{
		/**channel=super.channel;
		channel.exchangeDeclare(exchangeName, exchangeType);
		if(queueName==null||"".equals(queueName)){
			queueName = channel.queueDeclare().getQueue();
		}		
		channel.queueBind(queueName, exchangeName, routingKey);
		
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);
		while (true) {
		    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		    String message = new String(delivery.getBody());
		    log.info("receive message->"+message);
		    msgDBStore.saveSingleMessage(message);	               
		}**/
	 }
	 	 
	 private static void doWork(String task) throws InterruptedException {
		    for (char ch: task.toCharArray()) {
		        if (ch == '.') Thread.sleep(1000);
		    }
		}
	 
	 public static void main(String[] args) throws Exception {
//		 RabbitMQReceiver rabbitMQ=new RabbitMQReceiver();
//		 rabbitMQ.init();
//		 rabbitMQ.receiveRMQMessage(Constants.QNAME4CLOUDLOG);
//		 //rabbitMQ.receiveRMQWorkerMessage();
//		 //rabbitMQ.receiveRMQLogMessage("direct");
//		 rabbitMQ.close();
		 
	 }

}

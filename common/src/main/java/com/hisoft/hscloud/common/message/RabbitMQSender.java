package com.hisoft.hscloud.common.message;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.common.util.BeanReadUtil;
import com.hisoft.hscloud.common.util.Constants;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.MessageProperties;

@Service
 public class RabbitMQSender extends RabbitMQConnection implements MsgSender {	 	 
	 
	 
	 private Logger log = Logger.getLogger(this.getClass());
//	 @Autowired
//	 public void setQFactory(@Qualifier("qFactory")ConnectionFactory qFactory) {
//		 super.setQFactory(qFactory);
//	 }
	 	 
	 public void sendRMQMessage(String queueName, String message) throws IOException{
		//channel.queueDeclare(queueName, false, false, false, null);		 
		//channel.basicPublish("", queueName, null, message.getBytes());
		 System.out.println(" [x] Sent '" + message + "'");
	 }
	 
	 public void sendRMQTaskMessage(String taskQueueName,String message) throws IOException{
		 if(message!=null){
			 //channel.queueDeclare(taskQueueName, true, false, false, null);
			 //String message = getMessage(strings);
			 //channel.basicPublish( "", taskQueueName, 
			            //MessageProperties.PERSISTENT_TEXT_PLAIN,
			           // message.getBytes());
			 System.out.println(" [x] Sent '" + message + "'");
		 }		 
	 }
	 
	 public void sendRMQLogMessage(String exchangeName, String message,String exchangeType,String routingKey) throws IOException{
		 if(message!=null){
			 //channel.exchangeDeclare(exchangeName, exchangeType);
			 //String message = getMessage(strings);
			 //channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
			 System.out.println(" [x] Sent '" + message + "'");
		 }
	 }
	 	 
	 private static String getMessage(String[] strings){
	    if (strings.length < 1)
	        return null;
	    return joinStrings(strings, " ");
	}

	private static String joinStrings(String[] strings, String delimiter) {
	    int length = strings.length;
	    if (length == 0) return "";
	    StringBuilder words = new StringBuilder(strings[0]);
	    for (int i = 1; i < length; i++) {
	        words.append(delimiter).append(strings[i]);
	    }
	    return words.toString();
	}
	 
	 public static void main(String[] args) throws Throwable {
		 //String message = "{'body':{'roads':{'fanout':{'exchange': 'hs_exc_fanout'},'direct':{'routingKey':'hs_R_K_hisoft-server', 'exchange': 'hs_exc_direct'}},'ip': '192.168.27.56',       'hostname': 'hisoft-server','status': 'running'},'timestamp': 'Mon May 14 15:47:31 2012','type': 'one-time','format': 'discover_host'}";
		 //String message="{\"operator\": \"admin1\", \"actionName\": \"hisoft\",\"operationResult\":\"01\",\"parameter\":\"parameter\"}";
		 String message = "{'body':{'operator':'operator','actionName':'1ter','operationTime':'" + new Date() + "','operationResult':'01','parameter':'','remark':''}}";
		 String[]sArgs={"ss","dd"};
		 //RabbitMQSender rabbitMQ=new RabbitMQSender()
		 RabbitMQSender rabbitMQ=(RabbitMQSender)BeanReadUtil.getBean("rabbitMQSender");
		 rabbitMQ.init();
		 //rabbitMQ.sendRMQMessage(Constants.QNAME4CLOUDLOG,message);
		 rabbitMQ.sendRMQMessage("sicQueue",message);		 
		 //rabbitMQ.sendRMQTaskMessage(sArgs);
		 //rabbitMQ.sendRMQLogMessage(sArgs, "direct");
		 //rabbitMQ.close();
		 
	 }

	public void sendMessage(String queueName, String message) throws IOException {
		try {
			this.init();
			log.info("init message->"+message);
			this.sendRMQMessage(queueName,message);
			log.info("send message->"+message);
			this.close();
			log.info("sender close");
		} catch (Exception e) {
			log.error(e);
		}catch (Throwable e) {
			log.error(e);
		}
	}

	public void sendMessage(String exchangeName, String message,
			String exchangeType, String routingKey) throws IOException {
		try {
			this.init();
			log.info("init message->"+message);
			this.sendRMQLogMessage(exchangeName, message,exchangeType,routingKey);
			log.info("send message->"+message);
			this.close();
			log.info("sender close");
		} catch (Exception e) {
			log.error(e);
		}catch (Throwable e) {
			log.error(e);
		}
	}

}

package com.hisoft.hscloud.bss.sla.sc.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.message.RabbitMQConnection;
import com.hisoft.hscloud.vpdc.ops.service.Operation;
import com.hisoft.hscloud.vpdc.ops.vo.ImageVO;
//import com.rabbitmq.client.QueueingConsumer;

public class CloudCache extends RabbitMQConnection {
	private Logger logger = Logger.getLogger(CloudCache.class);
	
	private String exchangeSend;
	
	private String exchangeReceive;
	
	private String queueName;
	
	private String routingKey;

	private List<String> nodesInfo = new ArrayList<String>();
	@Autowired
	private Operation operation;

	
	public void initMessage(){
		try {
			// send
			String msg = "{\"action\":\"discover_host\",\"dect\":{\"routingKey\":\""+routingKey+"\",\"exchange\":\""+exchangeReceive+"\"}}";
			sendRMQLogMessage(exchangeSend, msg, "fanout", routingKey);
			logger.info(" [x] Sent '" + msg + "'");
			// receive
			nodesInfo = receiveRMQMessage(exchangeReceive, queueName, routingKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	 public void sendRMQLogMessage(String exchangeName, String message,String exchangeType,String routingKey) throws Exception{

		 if(message!=null){
			 init();
			 //channel.exchangeDeclare(exchangeName, exchangeType);
			 //String message = getMessage(strings);
			 //channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
			 close();
		 }

	 }
	 public List<String> receiveRMQMessage(String exchangeName, String queueName,
				String routingKey) throws Exception {
		 	init();
			List<String> list = new ArrayList<String>();
			/**
			channel.queueBind(queueName, exchangeName, routingKey);
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);
			while (true) {
				QueueingConsumer.Delivery delivery = null;
				delivery = consumer.nextDelivery(5 * 1000);
				if (delivery == null) {
					break;
				}
				String message = new String(delivery.getBody());
				logger.info("receive msg: "+message);
				list.add(message);

			}**/
			close();
			return list;
		}
	 
	 public List<ImageVO> getImages(){
		 Page<ImageVO> page = new Page<ImageVO>();
		 List<ImageVO> result = operation.fuzzyFindImages(null, null,page).getResult();
		 List<ImageVO> rs = new ArrayList<ImageVO>() ;
		for (int i = 0; i < result.size(); i++) {
			if (result.get(i).getMetadata().get("distro")!=null) {
			    String is_router = result.get(i).getMetadata().get("is_router");
	            if (is_router == null || is_router.equalsIgnoreCase("False")) {
	                rs.add(result.get(i));
	            }
			}
		}
		 return rs;
	 }
	 
    public List<ImageVO> getImagesForVRouter() {
        Page<ImageVO> page = new Page<ImageVO>();
        List<ImageVO> result = operation.fuzzyFindImages(null, null, page).getResult();
        List<ImageVO> rs = new ArrayList<ImageVO>();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getMetadata().get("distro") != null) {
                String is_router = result.get(i).getMetadata().get("is_router");
                if (is_router != null && is_router.equalsIgnoreCase("True")) {
                    rs.add(result.get(i));
                }
            }
        }
        return rs;
    }
	 
	 
	/**
	 * @return nodesInfo : return the property nodesInfo.
	 */
	public List<String> getNodesInfo() {
		return nodesInfo;
	}


	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public String getExchangeSend() {
		return exchangeSend;
	}

	public void setExchangeSend(String exchangeSend) {
		this.exchangeSend = exchangeSend;
	}

	public String getExchangeReceive() {
		return exchangeReceive;
	}

	public void setExchangeReceive(String exchangeReceive) {
		this.exchangeReceive = exchangeReceive;
	}
}

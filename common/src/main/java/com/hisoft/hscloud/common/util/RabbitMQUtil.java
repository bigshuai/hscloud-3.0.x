package com.hisoft.hscloud.common.util; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitMQUtil {
   // private static final String  EXCHANGE_NAME="exchange1";//定义Exchange名称
    private static final boolean durable = true;//消息队列持久化
    
    private Connection connection;  //rabbitMq连接
    
    public static Map<String, String> rabbitMqMap = new HashMap<String, String>();
    
    private String host;
    private String port;
    
    private String exchangeName; 
    private String queueName;
    private String exchangeType;
    private String routingKey;
    private String username;
    private String password;
    private String securityKey;
    
    public RabbitMQUtil() {
        rabbitMqMap = PropertiesUtils.getPropertiesMap("rabbitmq.properties", rabbitMqMap);
        host = rabbitMqMap.get("host");
        port = rabbitMqMap.get("port");
        
        exchangeName = rabbitMqMap.get("exchange");
        queueName = rabbitMqMap.get("messageQueue");
        exchangeType = rabbitMqMap.get("exchangeType");
        routingKey = rabbitMqMap.get("routingKey");
        username=rabbitMqMap.get("username");
        password=rabbitMqMap.get("password");
        securityKey=rabbitMqMap.get("securityKey");
        if(StringUtils.isEmpty(username)){
        	username="guest";
        }
        if(StringUtils.isEmpty(securityKey)){
        	securityKey=Constants.DEFAULT_SECURITY_KEY;
        }
        
        
        /*host = "192.168.139.134";
        port = "5672";
        
        exchangeName = "hs_exc_4job";
        queueName = "hs_Q_4J";
        exchangeType = "fanout";
        routingKey = "r_name";*/
    }
    
    //获取Connection
    private Connection getConnection() throws Exception{
        if(connection == null || connection.isOpen() == false) {
            ConnectionFactory factory = new ConnectionFactory();//创建链接工厂
            factory.setUsername(username);
            factory.setPassword(SecurityHelper.DecryptData(password, securityKey));
            factory.setHost(host);
            //factory.setPort(Integer.valueOf(port));
            connection = factory.newConnection(); //创建链接
        }
        
        return connection;
    }
    
    //获取Channel
    private Channel getChannel() throws Exception {
        connection = getConnection();
        Channel channel = connection.createChannel();//创建信息通道
        channel.basicQos(1);
        channel.exchangeDeclare(exchangeName, exchangeType, durable);//创建交换机并生命持久化
        
        //channel.queueDeclare(queueName, durable, false, false, null);
        //channel.queueBind(queueName, exchangeName, routingKey);  //使用routingKey将queue绑定到exchange上
        return channel;
    }
    
    //发送信息
    public void sendMessage(String message) throws Exception {
        Channel channel = null;
        channel = getChannel();
        channel.basicPublish(exchangeName, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        closeChannel(channel);
        closeConnection();
    }
    
    //发送信息
    public void sendMessage(String methodName, Object param, Object log,String logkey) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constants.RABBITMQ_METHOD_NAME, methodName);
        map.put(Constants.RABBITMQ_PARAM, param);
        map.put(logkey, log);
        JSONObject jSONObject = JSONObject.fromObject(map,OpenstackUtil.getJSONConfig());
        String json = jSONObject.toString();
        sendMessage(json);
    }
    
    //接收信息
    public Object receiveMessage() throws Exception {
        Channel channel = getChannel();
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            
            JSONObject jSONObject = JSONObject.fromObject(message);
            Map map = (Map)JSONObject.toBean(jSONObject, Map.class);
            System.out.println(map.get("routingKey"));
            Object obj = map.get("routingKey");
            return obj;
            
        }
    }
    
    
    //关闭Channel
    private void closeChannel(Channel channel) {
        if(channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    //关闭Connection
    private void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        RabbitMQUtil service = new RabbitMQUtil();
        
        
  //      service.sendMessage("file_upload_01", "------------------------");
        service.receiveMessage();
    }
    
    
}

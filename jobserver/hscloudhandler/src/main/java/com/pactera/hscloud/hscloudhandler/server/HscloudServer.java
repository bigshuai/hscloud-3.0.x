package com.pactera.hscloud.hscloudhandler.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudDateUtil;
import com.hisoft.hscloud.common.util.IPConvert;
import com.hisoft.hscloud.common.util.SecurityHelper;
import com.pactera.hscloud.common4j.util.DBUtil;
import com.pactera.hscloud.common4j.util.FileUtil;
import com.pactera.hscloud.hscloudhandler.bo.O_IP;
import com.pactera.hscloud.hscloudhandler.bo.UseSet;
import com.pactera.hscloud.hscloudhandler.eventhandler.HSCloudEventHandler;
import com.pactera.hscloud.hscloudhandler.util.IPMACUtil;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/*
 * 功能：
 * 1.接收HSCloud平台消息
 * 2.分析消息类型，调用Restful API接口
 * 3.记录操作日志
 * 4.更新Flavour表UUID
 * 
 * 
 */
public class HscloudServer {
	private static Log logger = LogFactory.getLog(HscloudServer.class);
	private static Properties ps;
	private static String host = "192.168.4.10";
	private static String exchange = "hs_exc_4job";
	private static String queue = "hs_Q_4J";
	private static String username="guest";
	private static String password="";
	private static String securityKey="";
	private static final boolean durable = true;// 消息队列持久化
	private static String job_name = "Server-1";
	private static int max_thread = 5;
	private static int sleepTime = 200;
	private static String jobServerType;
	private static Date expirationDate=null;
	static{
		try{
			List<UseSet> usl=DBUtil.getResult("hc_use_set");
			if(usl!=null&&usl.size()>0){
				UseSet us=usl.get(0);
				String key=us.getKeyStr();
				String authorizeKey=us.getAuthorizeKey();
				if(StringUtils.isNotBlank(key));{
					String decryptKey=SecurityHelper.DecryptData(key,Constants.DEFAULT_SECURITY_KEY);
					String[] splitKeys=decryptKey.split(";",2);
					String expiration=splitKeys[0];
					expirationDate=HsCloudDateUtil.transferStr2Date("yyyy-MM-dd",expiration);
					Date now=new Date();
					if(expirationDate.before(now)){
						logger.error("证书失效，系统终止服务。");
						System.exit(-1);
					}
					String mac=IPMACUtil.getMacAddr();
					String ip=IPMACUtil.getLocalIP();
					String compareStringLocal=mac+";"+ip+";"+authorizeKey;
					String compareStringRemote=splitKeys[1];
					if(!compareStringLocal.equals(compareStringRemote)){
						logger.error("证书有误，系统终止服务。");
						System.exit(-1);
					}
				}
			}
			
		}catch(Exception e){
			logger.debug(e.getMessage(),e);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	static {
		try {
			ps = new Properties();
			ps.load(new InputStreamReader(FileUtil
					.openPropertiesInputStream("jobserver.properties"),
					FileUtil.fileEncode));
			job_name = ps.getProperty("jobserver.name");
			max_thread = Integer.parseInt(ps
					.getProperty("jobserver.max-thread"));
			jobServerType = ps.getProperty("jobserver.jobServerType");
			String sleepTimeStr = ps.getProperty("jobserver.sleepTime");
			if (StringUtils.isNotBlank(sleepTimeStr)) {
				sleepTime = Integer.parseInt(sleepTimeStr);
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
			exchange = ps.getProperty("jobserver.rabbitmq.exchange");
			username=ps.getProperty("rabbitmq.username");
			password=ps.getProperty("rabbitmq.password");
			securityKey=ps.getProperty("rabbitmq.securityKey");
			if(StringUtils.isEmpty(securityKey)){
				securityKey=Constants.DEFAULT_SECURITY_KEY;
			}
			queue = ps.getProperty("jobserver.rabbitmq.queue");
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

		// 创建线程池
		HSCloudEventHandler job = new HSCloudEventHandler(job_name, max_thread);

		// 从RabbitMQ接收消息并处理
		try {
			// 初始化RabbitMQ连接
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(host);
			factory.setUsername(username);
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
				Date now=new Date();
				if(expirationDate.before(now)){
					logger.error("证书失效，系统终止服务。");
					System.exit(-1);
				}
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				if (FileUtil.stopProcess()) {
					logger.info("JobServer receive stop process event and will be end.....");
					break;
				}
				String message = new String(delivery.getBody());
				logger.debug(" [" + queue + "] Received '" + message + "'");
				// 处理创建虚拟机的消息，创建虚拟机需要分配ip,多以单独放在一个队列里面处理。
				if (jobServerType.equals("master")) {
					if (message.indexOf("createVM_") != -1) {// 根据消息内容判断消息类型
						try {
							// 对创建vm的消息进行处理，分配ip并填充到消息中
							message = handleMessage(message);
							// 创建线程去向openstack请求创建vm
							job.Handler(message);
						} catch (Exception e) {
							// 如果在分配ip过程中出现异常则捕获，把消息中的result:3,置换为result:4,error_info:错误信息
							logger.error(e.getMessage(), e);
							try {
								// 对消息填充和置换ip分配失败的信息
								message = saveGetIpExceptionLog(message,
										e.getMessage());
								// 即使ip分配失败仍然创建线程去向openstack请求创建vm
								job.Handler(message);
							} catch (Exception ex) {
								logger.error(ex.getMessage(), e);
							}
							channel.basicAck(delivery.getEnvelope()
									.getDeliveryTag(), false);
							continue;
						}

					}
					channel.basicAck(delivery.getEnvelope().getDeliveryTag(),
							false);
				} else if (jobServerType.equals("slaver")) {// 执行非创建vm的任务
					if (message.indexOf("createVM_") == -1) {
						job.Handler(message);
					}
					channel.basicAck(delivery.getEnvelope().getDeliveryTag(),
							false);

				} else if (jobServerType.equals("all")) {
					try {
						message = handleMessage(message);
						job.Handler(message);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						try {
							saveGetIpExceptionLog(message, e.getMessage());
							channel.basicAck(delivery.getEnvelope()
									.getDeliveryTag(), false);
							continue;
						} catch (Exception ex) {
							logger.error(ex.getMessage(), ex);
						}

					}
					channel.basicAck(delivery.getEnvelope()
							.getDeliveryTag(), false);

				}
				TimeUnit.MILLISECONDS.sleep(sleepTime);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 退出线程池
		job.Shutdown();

		while (!job.IsTerminate()) {
			logger.info("Waiting for JobServer finished!");
		}

		logger.info("JobServer finished!");
		System.exit(0);	

	}

	/**
	 * 对message的键值对进行获取，根据key获取值，其中key的类型为字符串类型
	 * 
	 * @param key
	 * @param message
	 * @return
	 */
	private static String pattern(String key, String message) {

		if (null == message || "".equals(message)) {
			return "";
		} else {
			// 形如
			// "zone":"beijing$Hscloud001",方法调用pattern(zone)=beijing$Hscloud001
			StringBuilder reg = new StringBuilder("\"");
			reg.append(key).append("\":\"[^\\[\\],{}\\\\]+\"");
			Pattern pattern = Pattern.compile(reg.toString());
			Matcher matcher = pattern.matcher(message);
			if (matcher.find()) {
				return matcher.group(0).split(":")[1].replaceAll("\"", "");
			}
			return "";
		}

	}

	/**
	 * 对message的键值对进行获取，根据key获取值，其中key的类型为整型
	 * 
	 * @param key
	 * @param message
	 * @return
	 */
	private static String patternInt(String key, String message) {
		if (null == message || "".equals(message)) {
			return "";
		} else {
			// 形如 "referenceId":333,方法调用patternInt(referenceId)=333
			StringBuilder reg = new StringBuilder("\"");
			reg.append(key).append("\":[^\\[\\],{}\\\\]+");
			Pattern pattern = Pattern.compile(reg.toString());
			Matcher matcher = pattern.matcher(message);
			if (matcher.find()) {
				return matcher.group(0).split(":")[1].replaceAll("\"", "");
			}
			return "";
		}

	}

	/**
	 * 该方法主要目的是把获取到的ip填充到message中去
	 * 
	 * @param ipstr
	 * @param message
	 * @return
	 */
	private static String replaceStr(String ipstr, String message) {
		if (null == message || "".equals(message)) {
			return "";
		} else {
			// securityGroups:[]目前为无用字段，所以把message中的"securityGroups":[],替换为"floatingIp":"192.168.7.137"
			String patternStr = "\"securityGroups\":\\[\\]";
			String replaceStr = "\"floatingIp\":\"" + ipstr + "\"";
			String patternIpExistStr="\"floatingIp\":\"(\\d+\\.\\d+\\.\\d+\\.\\d+)\"";
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(message);
			if (matcher.find()) {
				return matcher.replaceAll(replaceStr);
			}else {
				Pattern patternIpExist=Pattern.compile(patternIpExistStr);
				Matcher matcherIpExist=patternIpExist.matcher(message);
				if(matcherIpExist.find()){
					return matcherIpExist.replaceAll(replaceStr);
				}
			}
			return "";
		}
	}

	/**
	 * 对消息进行处理，主要是把获取到的ip填充到消息中去。
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	private static String handleMessage(String message) throws Exception {
		String resultMessage = message;
		try {
			// 获取消息中的neeIp键值对，needIp=0需要获取ip，needIp=1不需要获取ip
			String needIp = patternInt("needIP", message);
			boolean createVm=(message.indexOf("createVM_") != -1);
			boolean createRouter=(message.indexOf("createRouter_")!=-1);
			boolean createRouterOrVm=(createVm||createRouter);
			if (createRouterOrVm  && needIp != null
					&& needIp.equals("0")) {
				String ipStr = null;
				// 对获取ip操作重复操作，如果一次获取失败，再进行一次获取ip操作
				for (int i = 1; i <= 2; i++) {
					try {
						// 获取ip并update ip的一些信息
						if(message.indexOf("createRouter_Buy")==-1){
							ipStr = getIpAndUpdateIpStatus(message,1);
						}else{
							ipStr = getIpAndUpdateIpStatus(message,2);
						}
						
					} catch (Exception ex) {
						logger.error("\n===============getIpAndUpdateIpStatus exception times:"
								+ i);
						if (i == 2) {
							throw new Exception(ex.getMessage());// 在第二次获取ip失败后抛出异常。
						}
					}
					if (StringUtils.isNotBlank(ipStr)) {
						// 把获取到的ip置换到message中去
						resultMessage = replaceStr(ipStr, message);
						break;
					}
				}

			}
			return resultMessage;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 获取ip并更新hc_ip_detail 表中ip的object_id,status,modify_uid字段。
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String getIpAndUpdateIpStatus(String message,int type)
			throws Exception {
		String ipStr = null;
		try {
			// 获取zone信息，因为要根据zone去查询对应的ip
			String zone = getValue(message, "zone");
			if (StringUtils.isNotBlank(zone)) {
				StringBuilder sql = new StringBuilder();
				List<Object> ipDetailList=new ArrayList<Object>();
				if(type==1){
					sql.append(
							" where t.ip_range_id=t1.id and t1.id=t2.ip_id and t2.zone_id=t3.id ")
							.append(" and t.status = 0 and t3.code = '")
							.append(zone).append("' limit 10 ");
					// 一次性取出10个ip
					ipDetailList = DBUtil.getResult("hc_ip_detail",
							sql.toString());
					
				}else if(type==2){
					sql.append(
					" where t5.status = 0 and t4.type =1 AND t3.label = 'wan' AND t.code = '")
							.append(zone).append("' limit 10 ");
					// 一次性取出10个ip
					ipDetailList = DBUtil.getResult("hc_ip_detail_router",
							sql.toString());
				}
				
				if (null != ipDetailList && ipDetailList.size() >= 1) {
					Random rand = new Random(47);
					int ipNum = ipDetailList.size();
					O_IP ip = (O_IP) ipDetailList.get(rand.nextInt(ipNum));// //一次性取出10个ip,并从中随机选择一个
					Long ipLong = ip.getIp();
					Long id = ip.getId();
					logger.info("IPID:" + id);
					ipStr = IPConvert.getStringIP(ipLong);
					logger.info("IPSTR:" + ipStr);
					String object_idStr = patternInt("obj_instance_id", message);
					logger.info("OBJECT_IDSTR" + object_idStr);
					if (StringUtils.isNotBlank(object_idStr)) {
						ip.setObject_id(Long.parseLong(object_idStr));
					}
					String operator_idStr = patternInt("operator_id", message);
					if (StringUtils.isNotBlank(operator_idStr)) {
						ip.setModify_uid(Long.parseLong(operator_idStr));
					}
					logger.info("Operator_idStr" + operator_idStr);
					ip.setModify_time(new Date());
					ip.setStatus(1);
					try {
						// 更新ip的信息到数据库
						DBUtil.save(ip, "sync_hc_ip_detail.update");
						return ipStr;
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						throw new Exception(e.getMessage() + " 更新ip状态异常,ip="
								+ ipStr);
					}

				} else {
					throw new Exception("获取ip异常 zone=" + zone);
				}
			} else {
				throw new Exception("获取zone异常");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 从message中获取zone信息
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String getValue(String message, String param) throws Exception {
		try {
			String value = null;
			String zoneNode = pattern(param, message);
			if (zoneNode != null && zoneNode != "") {
				String[] zoneNodeArray = zoneNode.split("\\$");
				if (zoneNodeArray.length > 0) {
				    value = zoneNodeArray[0];
				}
			}
			return value;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
	}

	// 如果ip更新获取获取失败，把日志的result置为4（ip绑定失败），并附加错误信息
	public static String saveGetIpExceptionLog(String message, String error_info)
			throws Exception {
		String patternResultStr = "\"result\":3";
		String replaceResultStr = "\"result\":4,\"error_info\":\"" + error_info
				+ "\"";
		Pattern pattern = Pattern.compile(patternResultStr);
		Matcher matcher = pattern.matcher(message);
		if (matcher.find()) {
			return matcher.replaceAll(replaceResultStr);
		}
		return message;
	}

}

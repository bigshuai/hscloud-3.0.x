package com.hisoft.hscloud.vpdc.oss.monitoring.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.api.compute.ServerResource;
import org.openstack.api.compute.TenantResource;
import org.openstack.client.OpenStackClient;
import org.openstack.model.compute.Server;
import org.openstack.model.compute.nova.NovaServer;
import org.openstack.model.exceptions.OpenstackException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.common.message.MsgSender;
import com.hisoft.hscloud.common.message.RabbitMQSender;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.vpdc.ops.service.Operation;
import com.hisoft.hscloud.vpdc.ops.util.OpsException;
import com.hisoft.hscloud.vpdc.ops.vo.InstanceVO;
import com.hisoft.hscloud.vpdc.oss.monitoring.util.HostResourceConstants;

@Service
public class MonitorRabbitMQSender {
	@Autowired
	private Operation ops;

//	@Autowired
//private MsgSender mqSender;
	@Autowired
private RabbitMQSender mqSender;
	private Logger log = Logger.getLogger(this.getClass());
	private OpenStackClient client;
	private String configuration = "openstack.properties";
	private TenantResource compute;

	private Server server;

	public OpenStackClient getClient() {
		if (client == null)
			try {
				Properties properties = new Properties();
				properties.load(this.getClass().getClassLoader()
						.getResourceAsStream(configuration));// new
																// FileInputStream(configuration));

				// glanceEnabled =
				// Boolean.parseBoolean(properties.getProperty("test.glance"));
				// swiftEnabled =
				// Boolean.parseBoolean(properties.getProperty("test.swift"));

				// Command line properties should take precedence
				properties.putAll(System.getProperties());

				client = OpenStackClient.authenticate(properties);
			} catch (IOException e) {
				throw new OpenstackException(e.getMessage(), e);
			}
		return client;
	}

	public TenantResource getCompute() {
		if (compute == null) {
			compute = getClient().getComputeEndpoint();
		}
		return compute;
	}

	private String createMessage(String value, String type) {
		JSONObject jsonObject = new JSONObject();
		try {
			// jsonObject.append("action", "monitor");
			jsonObject.put("action", "monitor");
			List<String> targets = new ArrayList<String>();

			if (type.equals("host")) {
				targets.add("host");
			} else {
				targets.add("vms." + value);
			}
			Map<String, Object> mapParams = new HashMap<String, Object>();
			mapParams.put("period", HostResourceConstants.PERIOD);
			mapParams.put("targets", targets);
			jsonObject.put("params", mapParams);
			Map<String, String> mapDect = new HashMap<String, String>();
			mapDect.put("routingKey", "hs_R_K_app_monitor");
			mapDect.put("exchange", "hs_exc_direct");
			jsonObject.put("dect", mapDect);
			// System.out.println(jsonObject);
			log.info(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	private String getTheQueueName(String value, String type) {
		String queueName = null;
		if (type.endsWith("host")) {
			queueName = "hs_Q_" + value;
		} else {
			InstanceVO instance = ops.getInstance(value);
			if (instance == null) {
				return null;
			}
			// server = new NovaServer(value,null);
			// ServerResource serverResource =
			// getCompute().servers().server(value);
			// serverResource.get().get
			String hostName = instance.getHostName();
			queueName = "hs_Q_" + hostName;  

		}
		return queueName;
	}

	private String getRoutingKey(String value, String type) {
		String routingKey = null;
		if (type.equals("host")) {
			return "hs_R_K_" + value;
		}
		InstanceVO instance = ops.getInstance(value);
		if (instance == null) {
			return null;
		}
		// server = new NovaServer(value,null);
		// ServerResource serverResource =
		// getCompute().servers().server(value);
		// serverResource.get().get
		String hostName = instance.getHostName();
		routingKey = "hs_R_K_" + hostName;  

		return routingKey;
	}

	public void sendMessage(String value, String type) {
		String message = createMessage(value, type);
		// String queueName=getTheQueueName(value,type);
		String routingKey = getRoutingKey(value, type);
		if (routingKey != null && message != null) {
			try {
				log.info(mqSender);
				log.info(routingKey + "****");
				mqSender.sendMessage(Constants.EXCHANGE_NAME, message,
						Constants.EXCHANGE_TYPE, routingKey);
			} catch (Exception e) {
				log.error(e);
				log.error("it is wrong to send the message the queue");
			}
		}
	}

}

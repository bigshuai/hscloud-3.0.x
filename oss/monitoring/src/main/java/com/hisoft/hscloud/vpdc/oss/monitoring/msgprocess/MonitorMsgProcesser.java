/**
 * @title MonitorMsgProcesser.java
 * @package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess
 * @description 
 * @author YuezhouLi
 * @update 2012-5-24 上午11:11:52
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.hisoft.hscloud.vpdc.oss.monitoring.json.bean.HostMsgBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.json.bean.MonitorMsgBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.json.bean.VMMsgBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.util.CacheManager;
import com.hisoft.hscloud.vpdc.oss.monitoring.util.HostResourceConstants;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;


/**
 * @description 
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-24 上午11:11:52
 */
public class MonitorMsgProcesser implements MsgProcesser {

	private Logger logger = Logger.getLogger(this.getClass());
	private Gson gson = new Gson();
//	@Autowired
//	private ConnectionFactory qFactory;
	/* (非 Javadoc) 
	 * <p>Title: process</p> 
	 * <p>Description: </p> 
	 * @param msg 
	 * @see com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess.MsgProcesser#process(java.lang.String) 
	 */
	public void process(String msg) {
//		boolean isHostExpired = true;
//		boolean isVMExpired = true;
//		MonitorMsgBean monitorMsgBean = gson.fromJson(msg, MonitorMsgBean.class);
//		logger.info(monitorMsgBean);
//		HostMsgBean hostMsg = monitorMsgBean.getHost();
//		if(hostMsg != null){
//			String key = hostMsg.getHostname();
//			HostOnTimeMonitorVO hostVO = conventToHostOnTimeVO(hostMsg);
//			hostVO.setTimestamp(monitorMsgBean.getTimestamp());
//			logger.info("host vo:"+hostVO);
//			if(key == null){
//				logger.warn("The key is null , so we will not set into cache");
//			}else{
//				isHostExpired = CacheManager.putContent(key, hostVO);
//				logger.info("is host expired:"+isHostExpired);
//				if(isHostExpired){
//					CacheManager.remove(key);
//				}
//			}
//		}
//		VMMsgBean[] vmsMsg = monitorMsgBean.getVirt_machine();
//		logger.info("%%%%$$$"+vmsMsg.length);
//		if(vmsMsg != null){
//			logger.info("**&&& access into vm cache process  "+vmsMsg.length);
//			for(VMMsgBean vmMsg : vmsMsg){
//				String key = vmMsg.getVm_uuid();
//				InstanceOnTimeMonitorVO instanceVO = conventToInstanceOnTimeVO(vmMsg);
//				logger.info("instance vo:"+instanceVO+"vm uuid :"+key);
//				isVMExpired = CacheManager.putContent(key, instanceVO);
//				logger.info("is vm expired:"+isVMExpired);
//				if(isVMExpired){
//					CacheManager.remove(key);
//				}
//			}
//		}
//		
//		logger.info("isHostExpired:"+isHostExpired + " ,isVMExpired:"+isVMExpired);
//		if(isHostExpired && isVMExpired){
//			String hostname = monitorMsgBean.getLocation();
//			String body = "{\"action\":\"end_task\",\"params\":{\"task_id\":\""+monitorMsgBean.getTask_ident()+"\"}}";
//			logger.info("end task :"+hostname +"  "+body);
//			if(hostname != null){
//				Connection connection = null;
//				Channel channel = null;
//				try {
//					connection = qFactory.newConnection();
//					channel = connection.createChannel();
//					channel.basicPublish("hs_exc_direct", "hs_R_K_"+hostname, null, body.getBytes());
//					CacheManager.remove(hostname);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}finally{
//					 if(connection != null && channel != null){
//						 try {
//							channel.close();
//							connection.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					 }
//				}
//			}
//		}
	}
	
//	private HostOnTimeMonitorVO conventToHostOnTimeVO(HostMsgBean hostMsg){
//		if(hostMsg == null){
//			return null;
//		}
//		HostOnTimeMonitorVO hostVO = new HostOnTimeMonitorVO();
//		if(hostMsg.getCpu() != null){
//			hostVO.setCpuNumber(hostMsg.getCpu().cpu_total);
//			hostVO.setCpuRate(hostMsg.getCpu().cpu_rate);
//		}
//		if(hostMsg.getDisk() != null){
//			hostVO.setDiskRate(hostMsg.getDisk().disk_rate);
//			hostVO.setDiskTotal(hostMsg.getDisk().disk_total);
//		}
//		if(hostMsg.getMemory() != null){
//			hostVO.setMemoryRate(hostMsg.getMemory().mem_rate);
//			hostVO.setMemoryTotal(hostMsg.getMemory().mem_total);
//		}
//		if(hostMsg.getNetwork() != null){
//			hostVO.setNetworkIn(hostMsg.getNetwork().networkIn);
//			hostVO.setNetworkOut(hostMsg.getNetwork().networkOut);
//		}
//		if(hostMsg.getHost_status() != null){
//			hostVO.setStatus(hostMsg.getHost_status().global_status);
//			Map<String,Boolean> status = new HashMap<String,Boolean>();
//			for(HostMsgBean.HostStatus.ServiceStatus serviceStatus : hostMsg.getHost_status().services){
//				status.put(serviceStatus.service_name, serviceStatus.service_status);
//			}
//			hostVO.setServicesStatus(status);
//		}
//		if(hostMsg.getVms_status() != null){
//			hostVO.setVmsActiveNum(hostMsg.getVms_status().vms_active_num);
//			hostVO.setVmsErrorNum(hostMsg.getVms_status().vms_error_num);
//			hostVO.setVmsNum(hostMsg.getVms_status().vms_num);
//			hostVO.setVmsUUID(hostMsg.getVms_status().vm_uuids);
//		}
//		return hostVO;
//	}
	
//	private InstanceOnTimeMonitorVO conventToInstanceOnTimeVO(VMMsgBean vmMsg){
//		if(vmMsg == null){
//			return null;
//		}
//		InstanceOnTimeMonitorVO instanceVO = new InstanceOnTimeMonitorVO();
//		if(vmMsg.getCpu()!=null){
//			instanceVO.setCpuNumber(vmMsg.getCpu().num_cpu);
//			instanceVO.setCpuRate(vmMsg.getCpu().cpu_rate);
//		}
//		if(vmMsg.getMemory() != null){
//			instanceVO.setMemory(vmMsg.getMemory().mem);
//			instanceVO.setMemoryMax(vmMsg.getMemory().max_mem);
//		}
//		return instanceVO;
//	}
}

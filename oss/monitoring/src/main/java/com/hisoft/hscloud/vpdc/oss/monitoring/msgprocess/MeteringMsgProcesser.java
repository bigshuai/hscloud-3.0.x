/**
 * @title MeteringMsgProcesser.java
 * @package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess
 * @description 
 * @author YuezhouLi
 * @update 2012-5-25 上午11:23:25
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.hisoft.hscloud.vpdc.ops.dao.VpdcDao;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcInstance;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference;
import com.hisoft.hscloud.vpdc.ops.entity.VpdcReference_OrderItem;
import com.hisoft.hscloud.vpdc.oss.monitoring.dao.MonitorDao;
import com.hisoft.hscloud.vpdc.oss.monitoring.entity.HostMonitorHistory;
import com.hisoft.hscloud.vpdc.oss.monitoring.entity.VmMonitorHistory;
import com.hisoft.hscloud.vpdc.oss.monitoring.json.bean.HostMsgBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.json.bean.MonitorMsgBean;
import com.hisoft.hscloud.vpdc.oss.monitoring.json.bean.VMMsgBean;

/**
 * @description 
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-25 上午11:23:25
 */
@Component
public class MeteringMsgProcesser implements MsgProcesser{

	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private VpdcDao dao;
	
	@Autowired
	private MonitorDao monitorDao;
	
	public void createHistoryRecord(MonitorMsgBean msgBean){
		log.info("====>>>start store metering data");
		// JSONObject jsonobj = new JSONObject(message);
		List<VmMonitorHistory> vmMonitorHistories = new ArrayList<VmMonitorHistory>();
		List<HostMonitorHistory> hostMonitorHistories = new ArrayList<HostMonitorHistory>();
		Date time = msgBean.getTimestamp();
		log.info("timestamp" + time);

		HostMonitorHistory hostMonitorHistory = new HostMonitorHistory();

		HostMsgBean hostBean = msgBean.getHost();
		VMMsgBean[] vmsBean = msgBean.getVirt_machine();
		if(hostBean.getCpu()!=null){
			hostMonitorHistory.setCpu_rate(hostBean.getCpu().cpu_rate);
			hostMonitorHistory.setCpu_total(hostBean.getCpu().cpu_total);
		}
		if(hostBean.getDisk()!=null){
			hostMonitorHistory.setDisk_rate(hostBean.getDisk().disk_rate);
			hostMonitorHistory.setDisk_total(hostBean.getDisk().disk_total);
		}
		if(hostBean.getMemory()!=null){
			hostMonitorHistory.setMemory_rate(hostBean.getMemory().mem_rate);
			hostMonitorHistory.setMemory_total(hostBean.getMemory().mem_total);
		}
		if(hostBean.getNetwork()!=null){
			hostMonitorHistory.setNetworkIn(hostBean.getNetwork().networkIn);
			hostMonitorHistory.setNetworkOut(hostBean.getNetwork().networkOut);
		}
		hostMonitorHistory.setName(hostBean.getHostname());
		hostMonitorHistory.setMonitorTime(time);
		hostMonitorHistory.setStatus(hostBean.getHost_status().global_status
				+ "");
		if(hostBean.getHost_status().global_status){
			hostMonitorHistory.setVm_num(hostBean.getVms_status().vms_num);
			hostMonitorHistory
			.setVm_active_num(hostBean.getVms_status().vms_active_num);
			hostMonitorHistory
			.setVm_error_num(hostBean.getVms_status().vms_error_num);
		}
		hostMonitorHistories.add(hostMonitorHistory);
		System.out.println(hostMonitorHistory);

		boolean falg = monitorDao.saveHostMonitorHistory(hostMonitorHistories);
		log.info("the result of save the history of HostMonitoryHisory：" + falg);

		/** 收取虚拟机的历史记录 **/
		
		for (VMMsgBean vmBean : vmsBean) {
			VmMonitorHistory vmMonitorHistory = new VmMonitorHistory();
			VpdcInstance instance = dao.findVmByVmId(vmBean.getVm_uuid());
			VpdcReference reference = instance.getVpdcreference();
			if(reference==null){
				log.warn("===>>>>there is no vm record that reference to this vm uuid");
				continue;
			}
			
			 try{
				 VpdcReference_OrderItem vroi = dao.getOrderItemByReferenceId(reference.getId());
				 vmMonitorHistory.setOrder_item_id(Long.parseLong(vroi.getOrder_item_id()));
			 }catch(Exception e){
				 vmMonitorHistory.setOrder_item_id(0L);
			 }
			 
			vmMonitorHistory.setVmId(vmBean.getVm_uuid());
			vmMonitorHistory.setVmName(reference.getName());
			vmMonitorHistory.setCpu_rate(vmBean.getCpu().cpu_rate);
			vmMonitorHistory.setCpu_total(vmBean.getCpu().num_cpu);
			// TODO 虚拟机的监控数据memory ，disk ，network 为空，填充数据
			vmMonitorHistory.setMonitorTime(time);
			vmMonitorHistory.setStatus(vmBean.getStatus().vm_status + "");
			vmMonitorHistories.add(vmMonitorHistory);
		}

		falg = monitorDao.saveVmMonitorHistory(vmMonitorHistories);
		log.info("the result of save the history of VmMonitoryHisory：" + falg);
	}

	/*
	 * (非 Javadoc) <p>Title: process</p> <p>Description: </p>
	 * 
	 * @param msg
	 * 
	 * @see
	 * com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess.MsgProcesser#process
	 * (java.lang.String)
	 */
	public void process(String msg) {
		log.info("===>>>access metering msg processer");
		Gson gson = new Gson();
		MonitorMsgBean monitorBean = gson.fromJson(msg, MonitorMsgBean.class);
		createHistoryRecord(monitorBean);
	}
}

package com.pactera.hscloud.openstackhandler.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import com.hisoft.hscloud.common.util.IPConvert;
import com.pactera.hscloud.common4j.util.CommonThreadLocal;
import com.pactera.hscloud.openstackhandler.bo.O_Network;
import com.pactera.hscloud.openstackhandler.bo.O_Router;
import com.pactera.hscloud.openstackhandler.bo.O_VPDCInstance;
import com.pactera.hscloud.openstackhandler.bo.O_VPDC_Extdisk;
import com.pactera.hscloud.openstackhandler.bo.SyncEvent;
import com.pactera.hscloud.openstackhandler.vo.IPVO;
import com.pactera.hscloud.openstackhandler.vo.JobExt;
import com.pactera.hscloud.openstackhandler.vo.MessageVO;
import com.pactera.hscloud.openstackhandler.vo.SnapshotVO;
import com.pactera.hscloud.openstackhandler.vo.VMVO;
import com.pactera.hscloud.openstackhandler.vo.VPDCReferenceVO;

/**
 * 消息处理，返回执行方法
 * 
 * @author Minggang
 */
public class MessageDiscerner {

	private static Log logger = LogFactory.getLog(MessageDiscerner.class);

	/**
	 * 解析出错时返回error。执行error方法，并记录日志。
	 * 
	 * @param message
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	public static String discern(String message) {

		    SyncEvent sysEvent = new SyncEvent();
		try {
			CommonThreadLocal tl = new CommonThreadLocal();
			sysEvent.setMessage(message);
			tl.setSyncEvent(sysEvent);
			@SuppressWarnings({ "rawtypes"})
			Map<String, Class> classMap = new HashMap<String, Class>();
			classMap.put("disks", O_VPDC_Extdisk.class);
			String[] dateFormats = new String[] {"yyyy-MM-dd HH:mm:ss"};    
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFormats)); 
			JSONObject j = JSONObject.fromObject(message);
			MessageVO m = (MessageVO) JSONObject.toBean(j, MessageVO.class,classMap);
			tl.setMessage(m);
			sysEvent.setMethod(m.getSync_type());
			sysEvent.setEvent_time(m.getEvent_time());
			sysEvent.setBiz_type(m.getBiz_type());
			sysEvent.setFixed_ip(m.getFixed_Ip());
			sysEvent.setFloating_ip(m.getFloating_ip());
			sysEvent.setJob_id(m.getJob_id());
			if(null != m.getObj_type() && "" != m.getObj_type() && "1".equals(m.getObj_type())){// obj_type = 1 为路由  0 或其它 为 虚拟机    
				sysEvent.setProduct("ROUTER_"+m.getObj_type());
			}else{
				sysEvent.setProduct("VM_"+m.getObj_type());
			}
			if (null != m.getVm_id() && !"".equals(m.getVm_id())) {
				sysEvent.setReference_id(Long.valueOf(m.getVm_id()));
			}
			sysEvent.setResult(m.getResult());
			sysEvent.setType(m.getType());
			sysEvent.setUuid(m.getUuid());
			if (null != m.getVm_state()) {
				sysEvent.setVm_state(m.getVm_state().toUpperCase());
			}
			if (null != m.getTask_state()) {
				sysEvent.setVm_task(m.getTask_state().toUpperCase());
			}
			if ((null != m.getJob_ext() && !"".equals(m.getJob_ext()))) {
				sysEvent.setOperator_id(m.getJob_ext().getOperator_id());
				sysEvent.setOperator_type(m.getJob_ext().getOperator_type());
				sysEvent.setOperator(m.getJob_ext().getOperator());
			}
			if ("saveresult".equals(m.getSync_type())) {
				return MessageDiscerner.saveResult(m, tl);
			} else if ("unbindingip".equals(m.getSync_type())) {
				return MessageDiscerner.unbindingIP(m, tl);
			} else if ("syncvmstate".equals(m.getSync_type())) {
				return MessageDiscerner.syncVMState(m, tl);
			} else if ("createvm".equals(m.getSync_type())) {
				return MessageDiscerner.createVM(m, tl);
			} else if ("bindingip".equals(m.getSync_type())) {
				return MessageDiscerner.bindingIP(m, tl);
			} else if ("bindingdisk".equals(m.getSync_type())) {
				return MessageDiscerner.bindingDisk(m, tl);
			} else if ("backupVM".equals(m.getSync_type())) {
				return MessageDiscerner.backupVM(m, tl);
			} else if ("resetVmOs".equals(m.getSync_type())) {
				return MessageDiscerner.resetVmOs(m, tl);
			} else {
				sysEvent.setMethod("doNothing");
				return "doNothing";
			}

		} catch (Exception e) {
			logger.error("" + e.getMessage(), e);
			sysEvent.setMethod("error");
			return "error";
		}

	}

	@SuppressWarnings({ "static-access" })
	private static String saveResult(MessageVO m, CommonThreadLocal tl) {
		Object r = m.getResults();
		Object[] o = { r };
		tl.setParam(o);
		((SyncEvent) tl.getSyncEvent()).setExecuteMethod("saveResult");
		return "saveResult";
	}

	@SuppressWarnings({ "static-access" })
	private static String unbindingIP(MessageVO m, CommonThreadLocal tl) {
		IPVO ipvo = new IPVO();
		logger.info(m.getFloating_ip());
		logger.info(IPConvert.getIntegerIP(m.getFloating_ip()));
		ipvo.setIp(IPConvert.getIntegerIP(m.getFloating_ip()));
		ipvo.setUuid(m.getUuid());
		ipvo.setStatus(0);
		ipvo.setHost_id(0l);
		ipvo.setObject_id(0l);
		ipvo.setObject_type(null);
		Object[] o = { ipvo };
		tl.setParam(o);
		((SyncEvent) tl.getSyncEvent()).setExecuteMethod("unbindingIP");
		return "unbindingIP";
	}

	@SuppressWarnings({ "static-access" })
	private static String syncVMState(MessageVO m, CommonThreadLocal tl) {
		if(null != m.getObj_type() && "" != m.getObj_type() && "1".equals(m.getObj_type())){
			((SyncEvent) tl.getSyncEvent()).setExecuteMethod("syncRouterState");
			O_Router r = new O_Router();
			r.setId(null == m.getVm_id() || "".equals(m.getVm_id())?null:Long.valueOf(m.getVm_id()));
			r.setEvent_time(m.getEvent_time());
			r.setFixIP(m.getFixed_Ip());
			r.setFloatingIP(m.getFloating_ip());
			r.setProcess_state(m.getProcess_state());
			if (null != m.getVm_state()) {
				r.setRouter_status(m.getVm_state().toUpperCase());
			}
			if (null != m.getTask_state()) {
				r.setRouter_task_status(m.getTask_state().toUpperCase());
			}
			if(null != m.getHost() && !"".equals(m.getHost())){
				r.setNodeName(m.getHost());
			}
			if(null != m.getUuid() && !"".equals(m.getUuid())){
				r.setRouter_uuid(m.getUuid());
			}
			Object[] o = { r };
			tl.setParam(o);
			((SyncEvent) tl.getSyncEvent()).setExecuteMethod("syncRouterState");
			return "syncRouterState";
		}else{
			VPDCReferenceVO vpdcrvo = new VPDCReferenceVO();
			vpdcrvo.setVm_innerIP(m.getFixed_Ip());
			vpdcrvo.setEvent_time(m.getEvent_time());
			vpdcrvo.setVm_outerIP(m.getFloating_ip());
			vpdcrvo.setImageId(m.getImageId());
			vpdcrvo.setOsId(m.getOsId());
			vpdcrvo.setProcess_state(m.getProcess_state());
			vpdcrvo.setInstanceId(m.getB_instance_id());
			if (null != m.getVm_state()) {
				vpdcrvo.setVm_status(m.getVm_state().toUpperCase());
			}
			if (null != m.getTask_state()) {
				vpdcrvo.setVm_task_status(m.getTask_state().toUpperCase());
			}
			vpdcrvo.setRadom_user(m.getRadom_user());
			vpdcrvo.setRadom_pwd(m.getRadom_pwd());
			if (null != m.getVm_id() && !"".equals(m.getVm_id())) {
				vpdcrvo.setId(Long.valueOf(m.getVm_id()));
			}
			vpdcrvo.setUuid(m.getUuid());
			O_VPDCInstance vpdci = new O_VPDCInstance();
			vpdci.setVm_id(m.getUuid());
			vpdci.setId(m.getB_instance_id());
			if (null != m.getVm_id() && !"".equals(m.getVm_id())) {
				vpdci.setVpdcRefrenceId(Long.valueOf(m.getVm_id()));
			}
			vpdci.setNodeName(m.getHost());
			Object[] o = { vpdcrvo, vpdci };
			tl.setParam(o);
			((SyncEvent) tl.getSyncEvent()).setExecuteMethod("syncVMState");
			return "syncVMState";
		}
	}

	@SuppressWarnings({ "static-access" })
	private static String createVM(MessageVO m, CommonThreadLocal tl) {
		if(null != m.getObj_type() && "" != m.getObj_type()  && "1".equals(m.getObj_type())){
			O_Router r = new O_Router();
			if (null != m.getVm_id() && !"".equals(m.getVm_id())) {
				r.setId(Long.valueOf(m.getVm_id()));
			}
			r.setFixIP(m.getFixed_Ip());
			r.setFloatingIP(m.getFloating_ip());
			r.setNodeName(m.getHost());
			if (null != m.getVm_state()) {
				r.setRouter_status(m.getVm_state().toUpperCase());
			}
			if (null != m.getTask_state()) {
				r.setRouter_task_status(m.getTask_state().toUpperCase());
			}
			if(null != m.getUuid() && !"".equals(m.getUuid())){
				r.setRouter_uuid(m.getUuid());
			}
			r.setEvent_time(m.getEvent_time());
			Object[] o = {r};
			tl.setParam(o);
			((SyncEvent) tl.getSyncEvent()).setExecuteMethod("createRouter");
			return "createRouter";
		}else{
			VPDCReferenceVO vpdcrvo = new VPDCReferenceVO();
			vpdcrvo.setEvent_time(m.getEvent_time());
			vpdcrvo.setVm_innerIP(m.getFixed_Ip());
			vpdcrvo.setVm_outerIP(m.getFloating_ip());
			vpdcrvo.setImageId(m.getImageId());
			vpdcrvo.setOsId(m.getOsId());
			vpdcrvo.setInitVmId(m.getInitVmId());
			vpdcrvo.setInstanceId(m.getB_instance_id());
			if (null != m.getVm_state()) {
				vpdcrvo.setVm_status(m.getVm_state().toUpperCase());
			}
			if (null != m.getTask_state()) {
				vpdcrvo.setVm_task_status(m.getTask_state().toUpperCase());
			}
			vpdcrvo.setRadom_user(m.getRadom_user());
			vpdcrvo.setRadom_pwd(m.getRadom_pwd());
			if (null != m.getVm_id() && !"".equals(m.getVm_id())) {
				vpdcrvo.setId(Long.valueOf(m.getVm_id()));
			}
			vpdcrvo.setUuid(m.getUuid());
			O_VPDCInstance vpdci = new O_VPDCInstance();
			vpdci.setId(m.getB_instance_id());
			vpdci.setVm_id(m.getUuid());
			vpdci.setVpdcRefrenceId(Long.valueOf(m.getVm_id()));
			vpdci.setNodeName(m.getHost());
			Object[] o = { vpdcrvo, vpdci };
			tl.setParam(o);
			((SyncEvent) tl.getSyncEvent()).setExecuteMethod("createVM");
			return "createVM";
		}
	}

	@SuppressWarnings({ "static-access" })
	private static String bindingIP(MessageVO m, CommonThreadLocal tl) {
		JobExt jobExt = m.getJob_ext();
		if(null != m.getObj_type() && "" != m.getObj_type() && "1".equals(m.getObj_type())){
			O_Network network = new O_Network();
			IPVO ipvo = new IPVO();
			O_Router r = new O_Router();
			if (null != m.getVm_id() && !"".equals(m.getVm_id())) {
				network.setObjectId(Long.valueOf(m.getVm_id()));
			}
			if(null != m.getObj_type() && !"".equals(m.getObj_type())){
				network.setObjectType(Long.valueOf(m.getObj_type()));
				ipvo.setObject_type(Long.valueOf(m.getObj_type()));
			}
			if(null != m.getNetworkId() && !"".equals(m.getNetworkId())){
				network.setNetworkId(Long.valueOf(m.getNetworkId()));
			}
			if(null != m.getUuid() && !"".equals(m.getUuid())){
				network.setObjectUUID(m.getUuid());
			}
			logger.info(null != m.getFloating_ip()
					&& !"".equals(m.getFloating_ip()));
			logger.info(m.getFloating_ip());
			if (null != m.getFloating_ip() && !"".equals(m.getFloating_ip())) {
				logger.info(IPConvert.getIntegerIP(m.getFloating_ip()));
				ipvo.setIp(IPConvert.getIntegerIP(m.getFloating_ip()));
			}
			ipvo.setStatus(2);
			ipvo.setNodeName(m.getHost());
			ipvo.setUuid(m.getUuid());
			if (null != m.getVm_id() && !"".equals(m.getVm_id())) {
				r.setId(Long.valueOf(m.getVm_id()));
			}
			r.setFixIP(m.getFixed_Ip());
			r.setFloatingIP(m.getFloating_ip());
			r.setNodeName(m.getHost());
			if (null != m.getVm_state()) {
				r.setRouter_status(m.getVm_state().toUpperCase());
			}
			if (null != m.getTask_state()) {
				r.setRouter_task_status(m.getTask_state().toUpperCase());
			}
			if(null != m.getUuid() && !"".equals(m.getUuid())){
			   r.setRouter_uuid(m.getUuid());
			}
			r.setEvent_time(m.getEvent_time());
			Object[] o = {network, ipvo,r};
			tl.setParam(o);
			((SyncEvent) tl.getSyncEvent()).setExecuteMethod("bindingRouterIP");
			return "bindingRouterIP";
		}else{
			VPDCReferenceVO vpdcrvo = new VPDCReferenceVO();
			vpdcrvo.setEvent_time(m.getEvent_time());
			vpdcrvo.setVm_innerIP(m.getFixed_Ip());
			vpdcrvo.setVm_outerIP(m.getFloating_ip());
			vpdcrvo.setImageId(m.getImageId());
			vpdcrvo.setOsId(m.getOsId());
			vpdcrvo.setInstanceId(m.getB_instance_id());
			SyncEvent sysEvent = ((SyncEvent) tl.getSyncEvent());
			if (null != m.getVm_state()) {
				vpdcrvo.setVm_status(m.getVm_state().toUpperCase());
			}
			if (null != m.getTask_state()) {
				sysEvent.setVm_task(m.getTask_state().toUpperCase());
			}
			vpdcrvo.setRadom_user(m.getRadom_user());
			vpdcrvo.setRadom_pwd(m.getRadom_pwd());
			if (null != m.getVm_id() && !"".equals(m.getVm_id())) {
				vpdcrvo.setId(Long.valueOf(m.getVm_id()));
			}
			vpdcrvo.setUuid(m.getUuid());
			IPVO ipvo = new IPVO();

			logger.info(null != m.getFloating_ip()
					&& !"".equals(m.getFloating_ip()));
			logger.info(m.getFloating_ip());
			if (null != m.getFloating_ip() && !"".equals(m.getFloating_ip())) {
				logger.info(IPConvert.getIntegerIP(m.getFloating_ip()));
				ipvo.setIp(IPConvert.getIntegerIP(m.getFloating_ip()));
			}
			ipvo.setStatus(2);
			ipvo.setNodeName(m.getHost());
			ipvo.setUuid(m.getUuid());
			if(null != jobExt){
				ipvo.setObject_type(jobExt.getObj_type());
			}
			Object[] o = { vpdcrvo, ipvo };
			tl.setParam(o);
			sysEvent.setExecuteMethod("bindingIP");
			return "bindingIP";
		}
	}
	
	@SuppressWarnings({ "static-access" })
	private static String bindingDisk(MessageVO m, CommonThreadLocal tl) {
		List<O_VPDC_Extdisk> disks = m.getDisks();
		Object[] o = { disks };
		tl.setParam(o);
		((SyncEvent) tl.getSyncEvent()).setExecuteMethod("bindingDisk");
		return "bindingDisk";

	}

	@SuppressWarnings({ "static-access" })
	private static String backupVM(MessageVO m, CommonThreadLocal tl) {
		SnapshotVO snapshotvo = new SnapshotVO();
		snapshotvo.setUuid(m.getUuid());
		String sid = m.getBackupname().split("_")[0];
		if (!"".equals(sid) && null != sid) {
			snapshotvo.setId(Long.valueOf(sid));
		}
		snapshotvo.setSnapShot_id(m.getBackupname());
		snapshotvo.setStatus(1);
		Object[] o = { snapshotvo };
		tl.setParam(o);
		((SyncEvent) tl.getSyncEvent()).setExecuteMethod("backupVM");
		return "backupVM";
	}

	@SuppressWarnings({"static-access" })
	private static String resetVmOs(MessageVO m, CommonThreadLocal tl) {
		VMVO vm = new VMVO();
		vm.setUuid(m.getUuid());
		vm.setUser(m.getRadom_user());
		vm.setPassword(m.getRadom_pwd());
		vm.setImageId(m.getImageId());
		vm.setOsId(m.getOsId());
		Object[] o = { vm };
		vm.setInstanceId(m.getB_instance_id());
		tl.setParam(o);
		((SyncEvent) tl.getSyncEvent()).setExecuteMethod("resetVmOs");
		return "resetVmOs";
	}

	
}

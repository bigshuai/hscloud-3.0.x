package com.pactera.hscloud.openstackhandler.vo;

import java.util.Date;
import java.util.List;

import com.hisoft.hscloud.common.util.IPConvert;
import com.pactera.hscloud.openstackhandler.bo.OPSResult;
import com.pactera.hscloud.openstackhandler.bo.O_Network;
import com.pactera.hscloud.openstackhandler.bo.O_Router;
import com.pactera.hscloud.openstackhandler.bo.O_VPDCInstance;
import com.pactera.hscloud.openstackhandler.bo.O_VPDC_Extdisk;

public class MessageVO {

	private String message;
	private Date event_time;
	private Date deal_time;
	private Date finished_time;
	private Integer type;
	private Integer biz_type;
	private String messager;
	private Short result;
	private String error_info;
	private String remark;
	private Long job_id;
	private String uuid;
	private String vm_state;
	private String task_state;//虚拟机任务状态
	private String fixed_Ip;//内网ip
	private String floating_ip;//外网ip
	private String initVmId;
	private String obj_type;
	


	private Long snapshot_id;
	private String radom_user;//操作系统用户名
	private String radom_pwd;//密码
	private String vmname;//虚拟机名称
	private String vm_id;//虚拟机id
//	private String disks;//hc_vpdcreference_extdisk 中 name:volumeId
	private String job_type;//opsvm resource
	private JobExt job_ext;
	private Long networkId;
	private String sync_type;//同步方法名
	private String host;//节点名称
	private String zone_code;
	
	private String process_state;//创建进度
	
	private String imageId;
	private Integer osId;
	
	private Long b_instance_id;
	
	private String backupname;
	
	private List<O_VPDC_Extdisk> disks ;
	
	
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getEvent_time() {
		return event_time;
	}

	public void setEvent_time(Date event_time) {
		this.event_time = event_time;
	}

	public Date getDeal_time() {
		return deal_time;
	}

	public void setDeal_time(Date deal_time) {
		this.deal_time = deal_time;
	}

	public Date getFinished_time() {
		return finished_time;
	}

	public void setFinished_time(Date finished_time) {
		this.finished_time = finished_time;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getBiz_type() {
		return biz_type;
	}

	public void setBiz_type(Integer biz_type) {
		this.biz_type = biz_type;
	}

	public String getMessager() {
		return messager;
	}

	public void setMessager(String messager) {
		this.messager = messager;
	}

	public Short getResult() {
		return result;
	}

	public void setResult(Short result) {
		this.result = result;
	}

	public String getError_info() {
		return error_info;
	}

	public void setError_info(String error_info) {
		this.error_info = error_info;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getJob_id() {
		return job_id;
	}

	public void setJob_id(Long job_id) {
		this.job_id = job_id;
	}

//	public Long getReference_id() {
//		return reference_id;
//	}
//
//	public void setReference_id(Long reference_id) {
//		this.reference_id = reference_id;
//	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getVm_state() {
		return vm_state;
	}

	public void setVm_state(String vm_state) {
		this.vm_state = vm_state;
	}


	public String getTask_state() {
		return task_state;
	}

	public void setTask_state(String task_state) {
		this.task_state = task_state;
	}

	public String getFixed_Ip() {
		return fixed_Ip;
	}

	public void setFixed_Ip(String fixed_Ip) {
		this.fixed_Ip = fixed_Ip;
	}

	public String getFloating_ip() {
		return floating_ip;
	}

	public void setFloating_ip(String floating_ip) {
		this.floating_ip = floating_ip;
	}

	public String getVm_id() {
		return vm_id;
	}

	public void setVm_id(String vm_id) {
		this.vm_id = vm_id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getZone_code() {
		return zone_code;
	}

	public void setZone_code(String zone_code) {
		this.zone_code = zone_code;
	}

//	public String getDisks() {
//		return disks;
//	}
//
//	public void setDisks(String disks) {
//		this.disks = disks;
//	}

	public Long getSnapshot_id() {
		return snapshot_id;
	}

	public void setSnapshot_id(Long snapshot_id) {
		this.snapshot_id = snapshot_id;
	}

	public String getRadom_pwd() {
		return radom_pwd;
	}

	public void setRadom_pwd(String radom_pwd) {
		this.radom_pwd = radom_pwd;
	}

	public String getVmname() {
		return vmname;
	}

	public void setVmname(String vmname) {
		this.vmname = vmname;
	}

	public String getRadom_user() {
		return radom_user;
	}

	public void setRadom_user(String radom_user) {
		this.radom_user = radom_user;
	}

//	public String getOperator() {
//		return operator;
//	}
//
//	public void setOperator(String operator) {
//		this.operator = operator;
//	}

	public JobExt getJob_ext() {
		return job_ext;
	}

	public void setJob_ext(JobExt job_ext) {
		this.job_ext = job_ext;
	}

	public String getJob_type() {
		return job_type;
	}

	public void setJob_type(String job_type) {
		this.job_type = job_type;
	}

	public String getSync_type() {
		return sync_type;
	}

	public void setSync_type(String sync_type) {
		this.sync_type = sync_type;
	}

	public String getBackupname() {
		return backupname;
	}

	public void setBackupname(String backupname) {
		this.backupname = backupname;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public Integer getOsId() {
		return osId;
	}

	public void setOsId(Integer osId) {
		this.osId = osId;
	}

	public String getInitVmId() {
		return initVmId;
	}

	public void setInitVmId(String initVmId) {
		this.initVmId = initVmId;
	}

	public List<O_VPDC_Extdisk> getDisks() {
		if(null == this.disks){
			return disks;
		}
		for (O_VPDC_Extdisk disk : this.disks) {
			if (null != this.vm_id && !"".equals(this.vm_id)) {
				    disk.setReferenceId(Long.valueOf(this.vm_id));
			}
			disk.setVmId(this.uuid);
		}
		return disks;
	}

	public void setDisks(List<O_VPDC_Extdisk> disks) {
		this.disks = disks;
	}


	public Object getResults(){
		if ("OPS".equals(this.job_type)) {
			OPSResult opsr = new OPSResult();
			opsr.setId(this.job_id);
			opsr.setResult(this.result);
			opsr.setError_info(this.error_info);
			return opsr;
		} else {
			ResourceVO resourcervo = new ResourceVO();
			resourcervo.setId(this.job_id);
			resourcervo.setResult(this.result);
			resourcervo.setError_info(this.error_info);
			if (null != this.getJob_ext()) {
				resourcervo.setMethod(this.getJob_ext().getMethod());
			}
			return resourcervo;
		}
	}

	public VPDCReferenceVO getReference(){
		VPDCReferenceVO vpdcrvo = new VPDCReferenceVO();
		vpdcrvo.setEvent_time(this.event_time);
		vpdcrvo.setVm_innerIP(this.fixed_Ip);
		vpdcrvo.setVm_outerIP(this.floating_ip);
		vpdcrvo.setImageId(this.imageId);
		vpdcrvo.setOsId(this.osId);
		vpdcrvo.setInitVmId(this.initVmId);
		vpdcrvo.setProcess_state(this.process_state);
		vpdcrvo.setInstanceId(this.b_instance_id);
		if (null != this.vm_state) {
			vpdcrvo.setVm_status(this.vm_state.toUpperCase());
		}
		if (null != this.task_state) {
			vpdcrvo.setVm_task_status(this.task_state.toUpperCase());
		}
		vpdcrvo.setRadom_user(this.radom_user);
		vpdcrvo.setRadom_pwd(this.radom_pwd);
		if (null != this.vm_id && !"".equals(this.vm_id)) {
			vpdcrvo.setId(Long.valueOf(this.vm_id));
		}
		vpdcrvo.setUuid(this.uuid);
		return vpdcrvo;
	}
	
	public O_VPDCInstance getInstance(){
		O_VPDCInstance vpdci = new O_VPDCInstance();
		vpdci.setVm_id(this.uuid);
		vpdci.setId(this.b_instance_id);
		if (null != this.uuid && !"".equals(this.uuid) && null != this.vm_id) {
			vpdci.setVpdcRefrenceId(Long.valueOf(this.vm_id));
		}
		vpdci.setNodeName(this.host);
		return vpdci;
	}
	
	public IPVO getIP(){
		IPVO ipvo = new IPVO();
		if (null != this.floating_ip && !"".equals(this.floating_ip)) {
			ipvo.setIp(IPConvert.getIntegerIP(this.floating_ip));
		}
		ipvo.setStatus(2);
		ipvo.setNodeName(this.host);
		return ipvo;
	}

	public O_Router getRouter(){
		O_Router r = new O_Router();
		if (null != this.getVm_id() && !"".equals(this.getVm_id())) {
			r.setId(Long.valueOf(this.getVm_id()));
		}
		r.setFixIP(this.getFixed_Ip());
		r.setFloatingIP(this.getFloating_ip());
		r.setNodeName(this.getHost());
		if (null != this.getVm_state()) {
			r.setRouter_status(this.getVm_state().toUpperCase());
		}
		if (null != this.getTask_state()) {
			r.setRouter_task_status(this.getTask_state().toUpperCase());
		}
		if(null != this.getUuid() && !"".equals(this.getUuid())){
		   r.setRouter_uuid(this.getUuid());
		}
		r.setEvent_time(this.getEvent_time());
		return r;
	}
	
	public O_Network getNetwork(){
		O_Network n = new O_Network();
		if (null != this.getVm_id() && !"".equals(this.getVm_id())) {
			n.setObjectId(Long.valueOf(this.getVm_id()));
		}
		if(null != this.getObj_type() && !"".equals(this.getObj_type())){
			n.setObjectType(Long.valueOf(this.getObj_type()));
		}
		if(null != this.getNetworkId() && !"".equals(this.getNetworkId())){
			n.setNetworkId(Long.valueOf(this.getNetworkId()));
		}
		if(null != this.getUuid() && !"".equals(this.getUuid())){
			n.setObjectUUID(this.getUuid());
		}
		return n;
	}
	
	public String getProcess_state() {
		return process_state;
	}

	public void setProcess_state(String process_state) {
		this.process_state = process_state;
	}

	public String getObj_type() {
		return obj_type;
	}

	public void setObj_type(String obj_type) {
		this.obj_type = obj_type;
	}

	public Long getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Long networkId) {
		this.networkId = networkId;
	}

	public Long getB_instance_id() {
		return b_instance_id;
	}

	public void setB_instance_id(Long b_instance_id) {
		this.b_instance_id = b_instance_id;
	}
	
	
	
}

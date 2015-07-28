package com.pactera.hscloud.openstackhandler.bo;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SyncEvent implements Serializable {

	private Long id;
	private String message;
	private Date event_time;
	private Date update_time;
	private Date deal_time;
	private Date finish_time;
	private Integer type;
	private Integer biz_type;
	private Long operator_id;
	private String operator;
	private Integer operator_type;
	private String messager;
	private Short result;
	private String error_info;
	private String remark;
	private Long job_id;
	private Long reference_id;
	private String uuid;
	private String vm_state;
	private String vm_task;//虚拟机任务状态
	private String fixed_ip;//内网ip
	private String floating_ip;//外网ip
	private String method;//执行方法
	private String product;//产品类型   虚拟机 ，路由  为空程序不做操作，直接跳过
	private String executeMethod;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public String getVm_task() {
		return vm_task;
	}

	public void setVm_task(String vm_task) {
		this.vm_task = vm_task;
	}

	public String getFixed_ip() {
		return fixed_ip;
	}

	public void setFixed_ip(String fixed_ip) {
		this.fixed_ip = fixed_ip;
	}

	public String getFloating_ip() {
		return floating_ip;
	}

	public void setFloating_ip(String floating_ip) {
		this.floating_ip = floating_ip;
	}

	public Date getEvent_time() {
		return event_time;
	}

	public void setEvent_time(Date event_time) {
		this.event_time = event_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public Date getDeal_time() {
		return deal_time;
	}

	public void setDeal_time(Date deal_time) {
		this.deal_time = deal_time;
	}

	public Date getFinish_time() {
		return finish_time;
	}

	public void setFinish_time(Date finish_time) {
		this.finish_time = finish_time;
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

	public Long getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(Long operator_id) {
		this.operator_id = operator_id;
	}

	public Integer getOperator_type() {
		return operator_type;
	}

	public void setOperator_type(Integer operator_type) {
		this.operator_type = operator_type;
	}

	public Long getJob_id() {
		return job_id;
	}

	public void setJob_id(Long job_id) {
		this.job_id = job_id;
	}

	public Long getReference_id() {
		return reference_id;
	}

	public void setReference_id(Long reference_id) {
		this.reference_id = reference_id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getExecuteMethod() {
		return executeMethod;
	}

	public void setExecuteMethod(String executeMethod) {
		this.executeMethod = executeMethod;
	}

	
}

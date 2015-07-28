package com.hisoft.hscloud.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
//`id` bigint(20) NOT NULL,
//`message` varchar(1024) DEFAULT NULL COMMENT '消息体',
//`reference_id` bigint(20) DEFAULT NULL COMMENT 'hscloud侧reference物理ID',
//`event_time` date DEFAULT NULL COMMENT '发生时间，在业务层设置',
//`deal_time` date DEFAULT NULL COMMENT '开始处理时间，jobserver设置',
//`finish_time` date DEFAULT NULL COMMENT 'jobserver 处理完成时间',
//`update_time` date DEFAULT NULL COMMENT 'messager 回写处理结果时间',
//`ops` smallint(6) DEFAULT NULL COMMENT '1：启动2：重启3：关闭4：启用5：禁用6：备份7：还原',
//`operator` varchar(100) DEFAULT NULL COMMENT '操作人，记录操作人邮箱',
//`operator_id` bigint(20) DEFAULT NULL,
//`operator_type` smallint(6) DEFAULT NULL COMMENT '1前台 2 后台',
//`result` varchar(500) DEFAULT NULL COMMENT 'messager 回写的处理结果',
//`error_info` varchar(1024) DEFAULT NULL COMMENT 'messager 回写的错误信息',
//`remark` varchar(500) DEFAULT NULL,
//'obj_type'默认0：代表VM；1：代表Router
@Entity
@Table(name = "hc_event_vmops")
public class HcEventVmOps {
	@Id
	private Long id;
	private String message;
	private Long reference_id;
	private Date event_time;
	private Date deal_time;
	private Date finish_time;
	private Date update_time;
	private Short ops;
	private String uuid;
	private String operator;
	private Long operator_id;
	private Short operator_type;
	private Short result=3;
	private String error_info;
	private String remark;
	private String job_server;
	private Short obj_type;
	private String obj_name;
	private Long task_id;

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

	public Long getReference_id() {
		return reference_id;
	}

	public void setReference_id(Long reference_id) {
		this.reference_id = reference_id;
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

	public Date getFinish_time() {
		return finish_time;
	}

	public void setFinish_time(Date finish_time) {
		this.finish_time = finish_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public Short getOps() {
		return ops;
	}

	public void setOps(Short ops) {
		this.ops = ops;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Long getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(Long operator_id) {
		this.operator_id = operator_id;
	}

	public Short getOperator_type() {
		return operator_type;
	}

	public void setOperator_type(Short operator_type) {
		this.operator_type = operator_type;
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

	public String getJob_server() {
		return job_server;
	}

	public void setJob_server(String job_server) {
		this.job_server = job_server;
	}

	public String getObj_name() {
		return obj_name;
	}

	public void setObj_name(String obj_name) {
		this.obj_name = obj_name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getTask_id() {
		return task_id;
	}

	public void setTask_id(Long task_id) {
		this.task_id = task_id;
	}

	public Short getObj_type() {
		return obj_type;
	}

	public void setObj_type(Short obj_type) {
		this.obj_type = obj_type;
	}
	
	
}

package com.hisoft.hscloud.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//`id` bigint(20) NOT NULL,
//`message` varchar(1024) DEFAULT NULL COMMENT '消息体',
//`res_type` smallint(6) DEFAULT NULL COMMENT '资源类型\r\n\r\n1 zone \r\n2 flavor \r\n3 vm\r\n  4 ip \r\n5 extend_disk',
//`event_time` date DEFAULT NULL COMMENT '发生时间，在业务层设置',
//`deal_time` date DEFAULT NULL COMMENT '开始处理时间，jobserver设置',
//`finish_time` date DEFAULT NULL COMMENT 'jobserver 处理完成时间',
//`update_time` date DEFAULT NULL COMMENT 'messager 回写处理结果时间',
//`type` smallint(6) DEFAULT NULL COMMENT '1：新增2：修改3：删除4：重置5：迁移',
//`biz_type` smallint(6) DEFAULT NULL COMMENT '1：订单创建2：试用创建3：退款删除4：到期删除5：管理员创建6：管理员删除7：外网IP调整
//8：虚机Flavour调整
//9：手动迁移
//10：自动迁移
//11：新增扩展盘
//12：删除扩展盘
//13：调整扩展盘（暂不支持）
//',
//`operator` varchar(100) DEFAULT NULL COMMENT '操作人，记录操作人邮箱',
//`operator_id` bigint(20) DEFAULT NULL,
//`operator_type` smallint(6) DEFAULT NULL COMMENT '1前台 2 后台',
//`result` varchar(500) DEFAULT NULL COMMENT 'messager 回写的处理结果',
//`error_info` varchar(1024) DEFAULT NULL COMMENT 'messager 回写的错误信息',
//`obj_id` bigint(20) DEFAULT NULL COMMENT '资源在hscloud侧的物理id',
//`remark` varchar(500) DEFAULT NULL,
@Entity
@Table(name = "hc_event_resource")
public class HcEventResource {
	@Id
	private Long id;
	private String message;
	private Short res_type;
	private Date event_time;
	private Date deal_time;
	private Date finish_time;
	private Date update_time;
	private Short type;
	private Long domain_id;
	private Short biz_type;
	private String operator;
	private Long operator_id;
	private Short operator_type;
	private Short result=3;
	private String error_info;
	private Long obj_id;
	private String remark;
	private String job_server;
	private String obj_name;
	private Long obj_instance_id;
	private Short needIP=1;//0：需要通过RabbitMQ获取IP；1：不需获取
	private String ext_column_str;//扩展字段用于存放资源owner id
	private String owner_email;
	private Long task_id;

	public Short getNeedIP() {
		return needIP;
	}

	public void setNeedIP(Short needIP) {
		this.needIP = needIP;
	}

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

	public Short getRes_type() {
		return res_type;
	}

	public void setRes_type(Short res_type) {
		this.res_type = res_type;
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

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public Short getBiz_type() {
		return biz_type;
	}

	public void setBiz_type(Short biz_type) {
		this.biz_type = biz_type;
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

	public Long getObj_id() {
		return obj_id;
	}

	public void setObj_id(Long obj_id) {
		this.obj_id = obj_id;
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

	public Long getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(Long domain_id) {
		this.domain_id = domain_id;
	}

	public Long getObj_instance_id() {
		return obj_instance_id;
	}

	public void setObj_instance_id(Long obj_instance_id) {
		this.obj_instance_id = obj_instance_id;
	}

	public String getExt_column_str() {
		return ext_column_str;
	}

	public void setExt_column_str(String ext_column_str) {
		this.ext_column_str = ext_column_str;
	}

	public String getOwner_email() {
		return owner_email;
	}

	public void setOwner_email(String owner_email) {
		this.owner_email = owner_email;
	}

	public Long getTask_id() {
		return task_id;
	}

	public void setTask_id(Long task_id) {
		this.task_id = task_id;
	}

}

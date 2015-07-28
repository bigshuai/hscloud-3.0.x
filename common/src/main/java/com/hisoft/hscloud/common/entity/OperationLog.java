package com.hisoft.hscloud.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "hc_operation_log")
public class OperationLog {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@Column(name="domain_name",length=20)
	private String domainName;// 分平台简称
	@Column(name="operation",length=50)
	private String operator;// 操作人邮箱,vmId
	@Column(name="user_name",length=50)
	private String userName;
	@Column(name="action_name",length=30)
	private String actionName;// action中文名称，用于描述当前操作
	@Column(name="description",length=200)
	private String description;// 操作描述
	@Column(name="operate_object",length=200)
	private String operateObject;// 操作对象
	@Column(name="operation_date")
	private Date operationDate;// 操作时间
	@Column(name="operator_type")
	private short operatorType;//操作人類型
	@Column(name="operation_result")
	private short operationResult;// 操作结果 1，成功 2，失败
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}


	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public short getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(short operationResult) {
		this.operationResult = operationResult;
	}

	public short getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(short operatorType) {
		this.operatorType = operatorType;
	}

	public String getOperateObject() {
		return operateObject;
	}

	public void setOperateObject(String operateObject) {
		this.operateObject = operateObject;
	}
	
}
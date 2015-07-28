package com.hisoft.hscloud.common.vo;

import java.util.Date;

public class OperationLogQueryVo {
	private String domainAbb;// 分平台
	private String operator;// 操作人邮箱，用户名模糊搜索
	private String action;// 操作名称，描述模糊搜索
	private short operatorType;// 操作人类型
	private short operationResult;// 操作结果
	private Date startTime;
	private Date endTime;

	public String getDomainAbb() {
		return domainAbb;
	}

	public void setDomainAbb(String domainAbb) {
		this.domainAbb = domainAbb;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public short getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(short operatorType) {
		this.operatorType = operatorType;
	}

	public short getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(short operationResult) {
		this.operationResult = operationResult;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}



}

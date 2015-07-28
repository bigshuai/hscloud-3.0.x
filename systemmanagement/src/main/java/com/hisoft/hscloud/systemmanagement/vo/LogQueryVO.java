package com.hisoft.hscloud.systemmanagement.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogQueryVO {
	private String uuid;
	private Date startTime;
	private Date endTime;
	private short type;
	private short res_type;
	private short biz_type;
	private String operator;
	private short result;
	private String remark;
	private String message;
	private int operationType;
	private Short ops;
	private List<Object> zoneIdArray = new ArrayList<Object>();

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public short getResult() {
		return result;
	}

	public void setResult(short result) {
		this.result = result;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public short getBiz_type() {
		return biz_type;
	}

	public void setBiz_type(short biz_type) {
		this.biz_type = biz_type;
	}

	public short getRes_type() {
		return res_type;
	}

	public void setRes_type(short res_type) {
		this.res_type = res_type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}
	
	public Short getOps() {
		return ops;
	}

	public void setOps(Short ops) {
		this.ops = ops;
	}

	public List<Object> getZoneIdArray() {
		return zoneIdArray;
	}

	public void setZoneIdArray(List<Object> zoneIdArray) {
		this.zoneIdArray = zoneIdArray;
	}

	@Override
	public String toString() {
		return "LogQueryVO [uuid=" + uuid + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", type=" + type + ", res_type="
				+ res_type + ", biz_type=" + biz_type + ", operator="
				+ operator + ", result=" + result + ", remark=" + remark
				+ ", message=" + message + ", operationType=" + operationType
				+ "]";
	}

}
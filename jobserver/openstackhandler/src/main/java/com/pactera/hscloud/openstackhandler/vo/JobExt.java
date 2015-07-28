package com.pactera.hscloud.openstackhandler.vo;

public class JobExt {
	private Long obj_type; //创建 虚拟机和路由 时区分是  虚拟机 还是 路由
	private String resultType;
	private Long tempId;
	private Long operator_id;
	private String operator;
	private Integer operator_type;
	private String method;
	private Long networkId;
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public Long getTempId() {
		return tempId;
	}
	public void setTempId(Long tempId) {
		this.tempId = tempId;
	}
	public Long getOperator_id() {
		return operator_id;
	}
	public void setOperator_id(Long operator_id) {
		this.operator_id = operator_id;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Integer getOperator_type() {
		return operator_type;
	}
	public void setOperator_type(Integer operator_type) {
		this.operator_type = operator_type;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Long getObj_type() {
		return obj_type;
	}
	public void setObj_type(Long obj_type) {
		this.obj_type = obj_type;
	}
	public Long getNetworkId() {
		return networkId;
	}
	public void setNetworkId(Long networkId) {
		this.networkId = networkId;
	}
	

}

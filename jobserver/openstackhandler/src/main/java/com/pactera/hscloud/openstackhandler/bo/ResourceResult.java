package com.pactera.hscloud.openstackhandler.bo;

import java.util.Date;

public class ResourceResult extends Result {
	
	private Long id;

	private Date update_time;
	
	private String obj_name;

	private Short result;

	private String error_info;
	
	private String owner_email;
	
	private Long operator_id;
	
	private Short biz_type;
	
	private String message;
	
	private Long domain_id;
	
	private Long ext_column_str;
	
	private Integer obj_id;
	
	private Short type;
	
	private Short res_type;
	
	public Long getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(Long domain_id) {
		this.domain_id = domain_id;
	}

	public Short getBiz_type() {
		return biz_type;
	}

	public void setBiz_type(Short biz_type) {
		this.biz_type = biz_type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getObj_name() {
		return obj_name;
	}

	public void setObj_name(String obj_name) {
		this.obj_name = obj_name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
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

	public String getOwner_email() {
		return owner_email;
	}

	public void setOwner_email(String owner_email) {
		this.owner_email = owner_email;
	}

	public Long getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(Long operator_id) {
		this.operator_id = operator_id;
	}

	public Long getExt_column_str() {
		return ext_column_str;
	}

	public void setExt_column_str(Long ext_column_str) {
		this.ext_column_str = ext_column_str;
	}

	public Integer getObj_id() {
		return obj_id;
	}

	public void setObj_id(Integer obj_id) {
		this.obj_id = obj_id;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public Short getRes_type() {
		return res_type;
	}

	public void setRes_type(Short res_type) {
		this.res_type = res_type;
	}
	
	

}

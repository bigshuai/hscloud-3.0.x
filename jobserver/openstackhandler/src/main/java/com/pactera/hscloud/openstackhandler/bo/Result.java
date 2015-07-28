package com.pactera.hscloud.openstackhandler.bo;

import java.util.Date;

public  class Result {

	private Long id;

	private Date update_time;

	private Short result;

	private String error_info;

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

}

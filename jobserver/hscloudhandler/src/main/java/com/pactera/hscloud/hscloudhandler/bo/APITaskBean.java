package com.pactera.hscloud.hscloudhandler.bo;

public class APITaskBean {
	private Long id;
	private Long job_id;
	private String job_type;//RESOURCE OPS

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJob_id() {
		return job_id;
	}

	public void setJob_id(Long job_id) {
		this.job_id = job_id;
	}

	public String getJob_type() {
		return job_type;
	}

	public void setJob_type(String job_type) {
		this.job_type = job_type;
	}
    
}

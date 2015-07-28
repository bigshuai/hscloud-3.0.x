package com.pactera.hscloud.openstackhandler.bo;

import java.util.Date;

public class O_EmailQueue {
	
	private Integer status;
	
	private String receive_users;
	
	private String subject;
	
	private String body;
	
	private Date create_time;	
	
	private String remark;
	
	private Long domain_id;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReceive_users() {
		return receive_users;
	}

	public void setReceive_users(String receive_users) {
		this.receive_users = receive_users;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    public Long getDomain_id() {
        return domain_id;
    }

    public void setDomain_id(Long domain_id) {
        this.domain_id = domain_id;
    }

}

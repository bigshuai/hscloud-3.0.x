package com.pactera.hscloud.openstackhandler.bo;

import java.util.Date;

public class O_Message {
	
	private Date create_time;
	
	private String message;
	
	private Integer messge_type;
	
	private Integer status;
	
	private Long user_id;

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getMessge_type() {
		return messge_type;
	}

	public void setMessge_type(Integer messge_type) {
		this.messge_type = messge_type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	

}

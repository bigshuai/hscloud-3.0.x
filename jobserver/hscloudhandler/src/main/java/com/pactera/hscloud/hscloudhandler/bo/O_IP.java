package com.pactera.hscloud.hscloudhandler.bo;

import java.util.Date;

public class O_IP {

	private Long ip;

	private Long id;

	private Long object_id;

	private Long modify_uid;

	private Date modify_time;

	private Integer status;// 状态标示（0：空闲；1：待分配；2：已分配；3：禁用；4：待释放）

	public Long getIp() {
		return ip;
	}

	public void setIp(Long ip) {
		this.ip = ip;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getObject_id() {
		return object_id;
	}

	public void setObject_id(Long object_id) {
		this.object_id = object_id;
	}

	public Long getModify_uid() {
		return modify_uid;
	}

	public void setModify_uid(Long modify_uid) {
		this.modify_uid = modify_uid;
	}

}

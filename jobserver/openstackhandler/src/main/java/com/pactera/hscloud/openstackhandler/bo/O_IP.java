package com.pactera.hscloud.openstackhandler.bo;

import java.util.Date;

public class O_IP {

	private Long ip;
	
	private Long object_id;
	
	private Long object_type;
	
	private Long host_id;
	
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
	
	public Long getObject_id() {
		return object_id;
	}

	public void setObject_id(Long object_id) {
		this.object_id = object_id;
	}

	public Long getHost_id() {
		return host_id;
	}

	public void setHost_id(Long host_id) {
		this.host_id = host_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Long getObject_type() {
		return object_type;
	}

	public void setObject_type(Long object_type) {
		this.object_type = object_type;
	}

	public O_IP copy(O_IP replaceVO) {
		if(null == replaceVO){
			return this;
		}
		this.setStatus((null == replaceVO.getStatus() || "".equals(replaceVO
				.getStatus())) ? this.getStatus() : replaceVO.getStatus());
		this.setObject_type((null == replaceVO.getObject_type() || "".equals(replaceVO.getObject_type())) ? this
				.getObject_type() : replaceVO.getObject_type());
		this.setObject_id((null == replaceVO.getObject_id() || "".equals(replaceVO.getObject_id())) ? this
				.getObject_id() : replaceVO.getObject_id());
		this.setHost_id((null == replaceVO.getHost_id() || "".equals(replaceVO.getHost_id())) ? this
				.getHost_id() : replaceVO.getHost_id());
		return this;
	}

}

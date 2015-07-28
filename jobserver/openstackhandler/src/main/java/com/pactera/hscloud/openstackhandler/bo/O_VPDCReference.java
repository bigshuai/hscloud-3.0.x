package com.pactera.hscloud.openstackhandler.bo;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class O_VPDCReference implements Serializable {

	private Long id;
	
	private Date event_time;

	private String vm_innerIP;

	private String vm_outerIP;

	private String vm_status;


	private String vm_task_status;

	private String radom_user;// 操作系统用户名

	private String radom_pwd;// 密码
	
	private Long create_id;
	
	private String name;
	
	private Short createflag;
	
	private String imageId;
	
	private Integer osId;
	
	private Date update_date = new Date();
	
	private Integer try_time;
	
	private String process_state;

	public String getVm_status() {
		return vm_status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setVm_status(String vm_status) {
		this.vm_status = vm_status;
	}

	public String getVm_task_status() {
		return vm_task_status;
	}

	public void setVm_task_status(String vm_task_status) {
		this.vm_task_status = vm_task_status;
	}

	public String getVm_innerIP() {
		return vm_innerIP;
	}

	public void setVm_innerIP(String vm_innerIP) {
		this.vm_innerIP = vm_innerIP;
	}

	public String getVm_outerIP() {
		return vm_outerIP;
	}

	public void setVm_outerIP(String vm_outerIP) {
		this.vm_outerIP = vm_outerIP;
	}

	public String getRadom_user() {
		return radom_user;
	}

	public void setRadom_user(String radom_user) {
		this.radom_user = radom_user;
	}

	public String getRadom_pwd() {
		return radom_pwd;
	}

	public void setRadom_pwd(String radom_pwd) {
		this.radom_pwd = radom_pwd;
	}

	public Long getCreate_id() {
		return create_id;
	}

	public void setCreate_id(Long create_id) {
		this.create_id = create_id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Short getCreateflag() {
		return createflag;
	}

	public void setCreateflag(Short createflag) {
		this.createflag = createflag;
	}
	
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public Integer getOsId() {
		return osId;
	}

	public void setOsId(Integer osId) {
		this.osId = osId;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	
	public Date getEvent_time() {
		return event_time;
	}

	public void setEvent_time(Date event_time) {
		this.event_time = event_time;
	}

	public Integer getTry_time() {
		return try_time;
	}

	public void setTry_time(Integer try_time) {
		this.try_time = try_time;
	}
	
	public String getProcess_state() {
		return process_state;
	}

	public void setProcess_state(String process_state) {
		this.process_state = process_state;
	}

	public O_VPDCReference copy(O_VPDCReference replaceVO) {
		if (null == replaceVO) {
			return this;
		}
		this.setVm_status((null == replaceVO.getVm_status() || ""
				.equals(replaceVO.getVm_status())) ? this.getVm_status()
				: replaceVO.getVm_status());
		this.setVm_task_status(null == replaceVO.getVm_task_status()?this.getVm_task_status():replaceVO.getVm_task_status());
		this.setVm_innerIP((null == replaceVO.getVm_innerIP() || ""
				.equals(replaceVO.getVm_innerIP())) ? this.getVm_innerIP()
				: replaceVO.getVm_innerIP());
		this.setVm_outerIP((null == replaceVO.getVm_outerIP() || ""
				.equals(replaceVO.getVm_outerIP())) ? this.getVm_outerIP()
				: replaceVO.getVm_outerIP());
		this.setRadom_user((null == replaceVO.getRadom_user() || ""
				.equals(replaceVO.getRadom_user())) ? this.getRadom_user()
				: replaceVO.getRadom_user());
		this.setRadom_pwd((null == replaceVO.getRadom_pwd() || ""
				.equals(replaceVO.getRadom_pwd())) ? this.getRadom_pwd()
				: replaceVO.getRadom_pwd());
		this.setCreateflag((null == replaceVO.getCreateflag() || ""
				.equals(replaceVO.getCreateflag())) ? this.getCreateflag()
				: replaceVO.getCreateflag());
		this.setImageId((null == replaceVO.getImageId() || ""
				.equals(replaceVO.getImageId())) ? this.getImageId()
				: replaceVO.getImageId());
		this.setOsId((null == replaceVO.getOsId() || ""
				.equals(replaceVO.getOsId())) ? this.getOsId()
				: replaceVO.getOsId());
		this.setTry_time((null == replaceVO.getTry_time() || ""
				.equals(replaceVO.getTry_time())) ? this.getTry_time()
				: replaceVO.getTry_time());
		this.setProcess_state((null == replaceVO.getProcess_state() || ""
				.equals(replaceVO.getProcess_state())) ? this.getProcess_state()
				: replaceVO.getProcess_state());
				return this;
	}

}

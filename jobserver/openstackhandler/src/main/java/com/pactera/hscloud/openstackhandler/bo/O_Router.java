package com.pactera.hscloud.openstackhandler.bo;

import java.util.Date;

public class O_Router {
	
	public Long id;
	
	public String router_uuid;
	
	private Date event_time;

	private String fixIP;

	private String floatingIP;

	private String router_status;
	
	private String router_task_status;
	
	private String nodeName;
	
	private String process_state;
	
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRouter_uuid() {
		return router_uuid;
	}

	public void setRouter_uuid(String router_uuid) {
		this.router_uuid = router_uuid;
	}

	public Date getEvent_time() {
		return event_time;
	}

	public void setEvent_time(Date event_time) {
		this.event_time = event_time;
	}

	public String getFixIP() {
		return fixIP;
	}

	public void setFixIP(String fixIP) {
		this.fixIP = fixIP;
	}

	public String getFloatingIP() {
		return floatingIP;
	}

	public void setFloatingIP(String floatingIP) {
		this.floatingIP = floatingIP;
	}

	public String getRouter_status() {
		return router_status;
	}

	public void setRouter_status(String router_status) {
		this.router_status = router_status;
	}

	public String getRouter_task_status() {
		return router_task_status;
	}

	public void setRouter_task_status(String router_task_status) {
		this.router_task_status = router_task_status;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getProcess_state() {
		return process_state;
	}

	public void setProcess_state(String process_state) {
		this.process_state = process_state;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public O_Router copy(O_Router replaceVO) {
		if(null == replaceVO){
			return this;
		}
		this.setEvent_time(null == replaceVO.getEvent_time()?this.event_time:replaceVO.getEvent_time());
		this.setFixIP("" == replaceVO.getFixIP() || null == replaceVO.getFixIP() ? this.getFixIP():replaceVO.getFixIP());
		this.setFloatingIP("" == replaceVO.getFloatingIP() || null == replaceVO.getFloatingIP() ? this.getFloatingIP():replaceVO.getFloatingIP());
		this.setNodeName("" == replaceVO.getNodeName() || null == replaceVO.getNodeName() ? this.getNodeName():replaceVO.getNodeName());
		this.setProcess_state("" == replaceVO.getProcess_state() || null == replaceVO.getProcess_state() ? this.getProcess_state():replaceVO.getProcess_state());
		this.setRouter_status(null == replaceVO.getRouter_status() ? this.getRouter_status():replaceVO.getRouter_status());
		this.setRouter_task_status("" == replaceVO.getRouter_task_status() || null == replaceVO.getRouter_task_status() ? this.getRouter_task_status():replaceVO.getRouter_task_status());
		this.setRouter_uuid("" == replaceVO.getRouter_uuid() || null == replaceVO.getRouter_uuid() ? this.getRouter_uuid():replaceVO.getRouter_uuid());
		this.setName("" == replaceVO.getName() || null == replaceVO.getName() ? this.getName():replaceVO.getName());
		return this;
	}
	

}

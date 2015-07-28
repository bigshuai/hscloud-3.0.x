package com.hisoft.hscloud.vpdc.ops.vo;

public class VpdcReferenceQuotaInfo {
	private String vm_id;
	private int cpu_core;
	private int mem_size;
	private int disk_capacity;
	private int network_bandwidth;
	private int osId;
	private String name;
	private String vm_zone;
	private String vm_outerIP;
	private int status;
	private String vm_task_status;
	private String process_state;
	private String vm_innerIP;

	public String getVm_id() {
		return vm_id;
	}

	public void setVm_id(String vm_id) {
		this.vm_id = vm_id;
	}

	public int getCpu_core() {
		return cpu_core;
	}

	public void setCpu_core(int cpu_core) {
		this.cpu_core = cpu_core;
	}

	public int getMem_size() {
		return mem_size;
	}

	public void setMem_size(int mem_size) {
		this.mem_size = mem_size;
	}

	public int getDisk_capacity() {
		return disk_capacity;
	}

	public void setDisk_capacity(int disk_capacity) {
		this.disk_capacity = disk_capacity;
	}

	public int getNetwork_bandwidth() {
		return network_bandwidth;
	}

	public void setNetwork_bandwidth(int network_bandwidth) {
		this.network_bandwidth = network_bandwidth;
	}

	public int getOsId() {
		return osId;
	}

	public void setOsId(int osId) {
		this.osId = osId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVm_outerIP() {
		return vm_outerIP;
	}

	public void setVm_outerIP(String vm_outerIP) {
		this.vm_outerIP = vm_outerIP;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getVm_task_status() {
		return vm_task_status;
	}

	public void setVm_task_status(String vm_task_status) {
		this.vm_task_status = vm_task_status;
	}

	public String getProcess_state() {
		return process_state;
	}

	public void setProcess_state(String process_state) {
		this.process_state = process_state;
	}

	public String getVm_innerIP() {
		return vm_innerIP;
	}

	public void setVm_innerIP(String vm_innerIP) {
		this.vm_innerIP = vm_innerIP;
	}

	public String getVm_zone() {
		return vm_zone;
	}

	public void setVm_zone(String vm_zone) {
		this.vm_zone = vm_zone;
	}
	
}

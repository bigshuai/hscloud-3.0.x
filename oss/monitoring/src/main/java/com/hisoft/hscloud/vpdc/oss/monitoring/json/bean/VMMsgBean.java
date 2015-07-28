/**
 * @title VMMsgBean.java
 * @package com.hisoft.hscloud.vpdc.oss.monitoring.json.bean
 * @description 
 * @author YuezhouLi
 * @update 2012-5-24 下午2:38:24
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.oss.monitoring.json.bean;

/**
 * @description
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-24 下午2:38:24
 */
public class VMMsgBean {

	private String vm_uuid;
	private Status status;
	private Cpu cpu;
	private Memory memory;

	public class Status {
		public int vm_status;
		public String description;
	}

	public class Cpu {
		public int num_cpu;
		public float cpu_rate;
	}

	public class Memory {
		public int mem;
		public int max_mem;
	}

	public String getVm_uuid() {
		return vm_uuid;
	}

	public void setVm_uuid(String vm_uuid) {
		this.vm_uuid = vm_uuid;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Cpu getCpu() {
		return cpu;
	}

	public void setCpu(Cpu cpu) {
		this.cpu = cpu;
	}

	public Memory getMemory() {
		return memory;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	@Override
	public String toString() {
		return "VMMsgBean [vm_uuid=" + vm_uuid + ", status=" + status
				+ ", cpu=" + cpu + ", memory=" + memory + "]";
	}

}

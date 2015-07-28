/**
 * @title HostMsgBean.java
 * @package com.hisoft.hscloud.vpdc.oss.monitoring.json.bean
 * @description 
 * @author YuezhouLi
 * @update 2012-5-24 下午2:38:08
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.oss.monitoring.json.bean;

/**
 * @description
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-24 下午2:38:08
 */
public class HostMsgBean {

	private String hostname;
	private Network network;
	private VmsStatus vms_status;
	private HostStatus host_status;
	private Memory memory;
	private Disk disk;
	private Cpu cpu;

	public class Network {
		public float networkIn;
		public float networkOut;
	}

	public class VmsStatus {
		public int vms_num;
		public int vms_active_num;
		public String[] vm_uuids;
		public int vms_error_num;
		public String vms_belong_to;
	}

	public class HostStatus {
		public boolean global_status;
		public ServiceStatus[] services;

		public class ServiceStatus {
			public String service_name;
			public boolean service_status;
		}
	}

	public class Memory {
		public float mem_rate;
		public int mem_total;
	}

	public class Disk {
		public float disk_total;
		public float disk_rate;
	}

	public class Cpu {
		public int cpu_total;
		public float cpu_rate;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public VmsStatus getVms_status() {
		return vms_status;
	}

	public void setVms_status(VmsStatus vms_status) {
		this.vms_status = vms_status;
	}

	public HostStatus getHost_status() {
		return host_status;
	}

	public void setHost_status(HostStatus host_status) {
		this.host_status = host_status;
	}

	public Memory getMemory() {
		return memory;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public Disk getDisk() {
		return disk;
	}

	public void setDisk(Disk disk) {
		this.disk = disk;
	}

	public Cpu getCpu() {
		return cpu;
	}

	public void setCpu(Cpu cpu) {
		this.cpu = cpu;
	}

	@Override
	public String toString() {
		return "HostMsgBean [hostname=" + hostname + ", network=" + network
				+ ", vms_status=" + vms_status + ", host_status=" + host_status
				+ ", memory=" + memory + ", disk=" + disk + ", cpu=" + cpu
				+ "]";
	}

}

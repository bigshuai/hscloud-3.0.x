/**
 * @title OrderItemGoodsVM.java
 * @package com.hisoft.hscloud.bss.sla.om.entity
 * @description 订单项对应的VM商品实体
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.om.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;

import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVo;

/**
 * @description 订单项对应的VM商品实体
 * @version 1.0
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 */
@Entity
@Table(name = "hc_order_item_goods_vm")
public class OrderItemGoodsVM {
	private Long id;
	private String os;
	private String osId;
	private String cpuModel;
	private String diskModel;
	private String memoryModel;
	private String networkType;
	private String cpu;
	private String vCpus;
	private String memory;
	private String addDisk;
	private String disk;
	private String bandwidth;
	private String software;
	private String osIds;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getDisk() {
		return disk;
	}

	public void setDisk(String disk) {
		this.disk = disk;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getSoftware() {
		return software;
	}

	public void setSoftware(String software) {
		this.software = software;
	}
	
	public String getAddDisk() {
		return addDisk;
	}

	public void setAddDisk(String addDisk) {
		this.addDisk = addDisk;
	}
	
	@Column(name="os_id")
	public String getOsId() {
		return osId;
	}

	public void setOsId(String osId) {
		this.osId = osId;
	}
	@Column(name="cpu_model")
	public String getCpuModel() {
		return cpuModel;
	}

	public void setCpuModel(String cpuModel) {
		this.cpuModel = cpuModel;
	}
	@Column(name="disk_model")
	public String getDiskModel() {
		return diskModel;
	}

	public void setDiskModel(String diskModel) {
		this.diskModel = diskModel;
	}
	@Column(name="memory_model")
	public String getMemoryModel() {
		return memoryModel;
	}

	public void setMemoryModel(String memoryModel) {
		this.memoryModel = memoryModel;
	}
	@Column(name="network_type")
	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	@Column(name="v_cpus")
	public String getvCpus() {
		return vCpus;
	}

	public void setvCpus(String vCpus) {
		this.vCpus = vCpus;
	}
	

	public String getOsIds() {
		return osIds;
	}

	public void setOsIds(String osIds) {
		this.osIds = osIds;
	}

	@Transient
	public void submitVm(OrderItemVo vo) {
		os = vo.getOs();
		cpu = vo.getCpu();
		osId=vo.getOsId();
		osIds=vo.getOsIds();
		cpuModel=vo.getCpuModel();
		diskModel=vo.getDiskModel();
		memoryModel=vo.getMemoryModel();
		networkType=vo.getNetworkType();
		memory = vo.getMemory();
		addDisk=vo.getAddDisk();
		disk = vo.getDisk();
		vCpus=vo.getvCpus();
		bandwidth = vo.getNetwork();
		software = vo.getSoftware();
	}
	@Transient
	public void copyVm(OrderItemGoodsVM vm) throws Exception {
		BeanUtils.copyProperties(this, vm);
		this.id = null;
	}
}

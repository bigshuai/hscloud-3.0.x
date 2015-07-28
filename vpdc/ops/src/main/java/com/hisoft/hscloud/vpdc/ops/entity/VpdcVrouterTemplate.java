package com.hisoft.hscloud.vpdc.ops.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;

@Entity
@Table(name = "hc_vpdc_vrouterTemplate")
public class VpdcVrouterTemplate extends AbstractEntity{
	@Column(name = "cpu_core")
	private int cpuCore;

	@Column(name = "disk_capacity")
	private int diskCapacity;
	
	@Column(name = "mem_size")
	private int memSize;
	
	@Column(name = "flavorId")
	private int flavorId;
	
	@Column(name = "imageId")
	private String imageId;
	
	@Column(name = "osId")
	private int osId;
	
	//0:有效,1:删除 
	@Column(name = "deleted")
	private int deleted;

	public int getCpuCore() {
		return cpuCore;
	}

	public void setCpuCore(int cpuCore) {
		this.cpuCore = cpuCore;
	}

	public int getDiskCapacity() {
		return diskCapacity;
	}

	public void setDiskCapacity(int diskCapacity) {
		this.diskCapacity = diskCapacity;
	}

	public int getMemSize() {
		return memSize;
	}

	public void setMemSize(int memSize) {
		this.memSize = memSize;
	}

	public int getFlavorId() {
		return flavorId;
	}

	public void setFlavorId(int flavorId) {
		this.flavorId = flavorId;
	}
	
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public int getOsId() {
		return osId;
	}

	public void setOsId(int osId) {
		this.osId = osId;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
	
}

package com.hisoft.hscloud.bss.sla.sc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 
 * <扩展盘POJO> <功能详细描述>
 * 
 * @author houyh
 * @version [版本号, 2012-10-22]
 * @see [相关类/方法]
 * @since [HSCLOUD/1.4]
 */
@Entity
@Table(name = "hc_hardware_ext_disk")
@PrimaryKeyJoinColumn(name = "ext_disk_id", referencedColumnName = "item_id")
public class ExtDisk extends ServiceItem {
	private String model;
	private int capacity;
	private int rpm;

	@Column(length = 11)
	public int getRpm() {
		return rpm;
	}

	public void setRpm(int rpm) {
		this.rpm = rpm;
	}

	@Column(length = 50)
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Column(length = 5, unique = true)
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}

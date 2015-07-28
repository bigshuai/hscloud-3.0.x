/**
 * @author jiaquan.hu
 */

package com.hisoft.hscloud.bss.sla.sc.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @description ram的实体类
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-3-31 上午9:58:35
 */
@Entity
@Table(name = "hc_hardware_ram")
@PrimaryKeyJoinColumn(name = "ram_id", referencedColumnName = "item_id")
public class Ram extends ServiceItem {

	private int size;

	private String model;

	@Column(length=50)
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	@Column(length=8,unique=true)
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}



}

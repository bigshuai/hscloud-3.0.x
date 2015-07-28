/**
 * @author jiaquan.hu
 */

package com.hisoft.hscloud.bss.sla.sc.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @description cpu的实体类
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-3-31 上午9:58:09
 */
@Entity
@Table(name = "hc_hardware_cpu")
@PrimaryKeyJoinColumn(name = "cpu_id", referencedColumnName = "item_id")
public class Cpu extends ServiceItem {


	private String model;

	private BigDecimal frequency;

	private int coreNum;

	private String arch;

	private String vendor;

	@Column(length = 50)
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Column
	public BigDecimal getFrequency() {
		return frequency;
	}

	public void setFrequency(BigDecimal frequency) {
		this.frequency = frequency;
	}

	@Column(name = "core_num", length = 2)
	public int getCoreNum() {
		return coreNum;
	}

	public void setCoreNum(int coreNum) {
		this.coreNum = coreNum;
	}

	@Column(length = 5)
	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	@Column(length = 50)
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

}

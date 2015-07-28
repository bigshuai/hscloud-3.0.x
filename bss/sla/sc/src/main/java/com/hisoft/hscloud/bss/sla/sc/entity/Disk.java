/**
 * @author jiaquan.hu
 */

package com.hisoft.hscloud.bss.sla.sc.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.type.BigDecimalType;

/**
* @description disk的实体类
* @version 1.0
* @author jiaquan.hu
* @update 2012-3-31 上午9:58:26
*/
@Entity
@Table(name = "hc_hardware_disk")
@PrimaryKeyJoinColumn(name="disk_id",referencedColumnName="item_id")
public class Disk extends ServiceItem{


	private String model;

	private int capacity;
	
	private int rpm;
	
	

    @Column(length=11)
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
	@Column(length=5,unique=true)
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}




}

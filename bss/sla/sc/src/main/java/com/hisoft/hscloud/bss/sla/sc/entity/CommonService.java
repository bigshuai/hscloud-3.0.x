/**
 * @title CommonService.java
 * @package com.hisoft.hscloud.bss.sla.sc.entity
 * @description 用一句话描述该文件做什么
 * @author jiaquan.hu
 * @update 2012-5-7 上午10:37:18
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.sc.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-5-7 上午10:37:18
 */
@Entity
@Table(name = "hc_common_service")
@PrimaryKeyJoinColumn(name="service_id",referencedColumnName="item_id")
public class CommonService extends ServiceItem {
	
	private String name;

	private String vendor;

	private String type;
	
	private int times=1;
	
	private int measureType;

	@Column(length=50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length=50)
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	@Column(length=50)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Column(length=10)
	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
	@Column(name="measure_type",length=1)
	public int getMeasureType() {
		return measureType;
	}

	public void setMeasureType(int measureType) {
		this.measureType = measureType;
	}
}

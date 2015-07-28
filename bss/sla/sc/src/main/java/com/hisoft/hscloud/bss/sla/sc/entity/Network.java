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
 * @description network的实体类
 * @version 1.0
 * @author jiaquan.hu
 * @update 2012-3-31 上午9:58:44
 */
@Entity
@Table(name = "hc_network")
@PrimaryKeyJoinColumn(name = "network_id", referencedColumnName = "item_id")
public class Network extends ServiceItem {
	private int bandWidth;

	private String vendor;

	private String type;
	
	private String link;
	
	public String getLink() {
		return link;
	}
	@Column(name="link",length=50)
	public void setLink(String link) {
		this.link = link;
	}

	@Column(name = "band_width",length=5,unique=false)
	public int getBandWidth() {
		return bandWidth;
	}

	public void setBandWidth(int bandWidth) {
		this.bandWidth = bandWidth;
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

}

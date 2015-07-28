package com.hisoft.hscloud.bss.sla.sc.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
@Entity
@Table(name="hc_sc_feetype")
public class ScFeeType {
	private Long id;
	private String priceType="1";
	private String pricePeriodType="1";
	private String period;
	private BigDecimal price;
	private ServiceCatalog sc;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="price_type")
	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	@Column(name="price_period_type")
	public String getPricePeriodType() {
		return pricePeriodType;
	}

	public void setPricePeriodType(String pricePeriodType) {
		this.pricePeriodType = pricePeriodType;
	}
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	@ManyToOne
	@JsonIgnore
	public ServiceCatalog getSc() {
		return sc;
	}

	public void setSc(ServiceCatalog sc) {
//		if(sc==null){
//			throw new IllegalArgumentException("sc is null");
//		}
//		sc.getFeeTypes().add(this);
		this.sc = sc;
	}

}

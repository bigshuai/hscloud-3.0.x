package com.hisoft.hscloud.bss.sla.om.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="hc_order_product")
public class OrderProduct {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(name="product_name",length=50)
	private String productName;
	/**
	 * 1,cpu 2,ram 3,disk 4,os 5,network 6,software 7,commonService 8,extDisk 9,ip
	 */
	@Column(name="type")
	private int type;
	@Column(name="spec",length=50)
	private String spec;
	@Column(name="unit",length=20)
	private String unit;
	@Column(name="model")
	private String model;
	@Column(name="ext_column")
	private String extColumn;
	@Column(name="price")
	private BigDecimal price;
	@ManyToOne
	@JsonIgnore
	private OrderItem orderItem;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public OrderItem getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getExtColumn() {
		return extColumn;
	}
	public void setExtColumn(String extColumn) {
		this.extColumn = extColumn;
	}
	@Transient
	public void copyVm(OrderProduct orderProduct) throws Exception {
		if(orderProduct.getPrice()==null){
			BeanUtils.copyProperties(orderProduct,this,new String[]{"price"});
		}else{
			BeanUtils.copyProperties(orderProduct,this);
		}
		
		this.id = null;
	}
}

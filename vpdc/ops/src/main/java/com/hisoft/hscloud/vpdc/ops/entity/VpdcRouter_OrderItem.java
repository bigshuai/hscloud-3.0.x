package com.hisoft.hscloud.vpdc.ops.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hc_vpdcRouter_orderItem")
public class VpdcRouter_OrderItem{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;
	
	@Column(name = "routerId")
	private long vpdcRouterId;

	@Column(name = "order_item_id")
	private String order_item_id;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public long getVpdcRouterId() {
		return vpdcRouterId;
	}
	public void setVpdcRouterId(long vpdcRouterId) {
		this.vpdcRouterId = vpdcRouterId;
	}
	/**
	 * @return order_item_id : return the property order_item_id.
	 */
	public String getOrder_item_id() {
		return order_item_id;
	}
	/**
	 * @param order_item_id : set the property order_item_id.
	 */
	public void setOrder_item_id(String order_item_id) {
		this.order_item_id = order_item_id;
	}
	@Override
	public String toString() {
		return "VpdcRouter_OrderItem [id=" + id + ", vpdcRouterId="
				+ vpdcRouterId + ", order_item_id=" + order_item_id + "]";
	}
	
}

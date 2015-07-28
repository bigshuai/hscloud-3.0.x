package com.hisoft.hscloud.vpdc.ops.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hc_vpdcReference_orderItem")
public class VpdcReference_OrderItem{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;
	
	@Column(name = "renferenceId")
	private long vpdcRenferenceId;

	@Column(name = "order_item_id")
	private String order_item_id;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return instance : return the property instance.
	 */
	public long getVpdcRenferenceId() {
		return vpdcRenferenceId;
	}
	/**
	 * @param instance : set the property instance.
	 */
	public void setVpdcRenferenceId(long vpdcRenferenceId) {
		this.vpdcRenferenceId = vpdcRenferenceId;
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
		return "reference_orderitem [vpdcRenferenceId=" + vpdcRenferenceId + ",order_item_id=" + order_item_id
				+"]";
	}
}

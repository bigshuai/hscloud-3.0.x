package com.hisoft.hscloud.bss.sla.om.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.crm.usermanager.entity.User;

public class RefundResult {
	private BigDecimal refund;
	private BigDecimal refundPoint;
	private BigDecimal refundGift;
	private Map<Long, OrderItem> unRefundOrderItem;
	private List<OrderItem> refundItems;
	private String remark;
	private Long currentOrderId;
	private Long currentOrderItemId;
    private User user;

	public BigDecimal getRefund() {
		return refund;
	}

	public void setRefund(BigDecimal refund) {
		this.refund = refund;
	}

	public Map<Long, OrderItem> getUnRefundOrderItem() {
		return unRefundOrderItem;
	}

	public void setUnRefundOrderItem(Map<Long, OrderItem> unRefundOrderItem) {
		this.unRefundOrderItem = unRefundOrderItem;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderItem> getRefundItems() {
		return refundItems;
	}

	public void setRefundItems(List<OrderItem> refundItems) {
		this.refundItems = refundItems;
	}

	public Long getCurrentOrderId() {
		return currentOrderId;
	}

	public void setCurrentOrderId(Long currentOrderId) {
		this.currentOrderId = currentOrderId;
	}

	public Long getCurrentOrderItemId() {
		return currentOrderItemId;
	}

	public void setCurrentOrderItemId(Long currentOrderItemId) {
		this.currentOrderItemId = currentOrderItemId;
	}

	public BigDecimal getRefundPoint() {
		return refundPoint;
	}

	public void setRefundPoint(BigDecimal refundPoint) {
		this.refundPoint = refundPoint;
	}

	public BigDecimal getRefundGift() {
		return refundGift;
	}

	public void setRefundGift(BigDecimal refundGift) {
		this.refundGift = refundGift;
	}
	
}

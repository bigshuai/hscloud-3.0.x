package com.hisoft.hscloud.bss.sla.om.vo;

import java.math.BigDecimal;
import java.util.Date;

public class OrderVo {
	private String orderNo;
	private BigDecimal totalPrice;
	private String totalAmountDesc;
	private Date payDate;
	private String ownerEmail;
	private String remark;
	private Long orderId;
	private BigDecimal totalAmount;
	private BigDecimal totalPointAmount;
	private BigDecimal totalGiftAmount;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getTotalAmountDesc() {
		return totalAmountDesc;
	}

	public void setTotalAmountDesc(String totalAmountDesc) {
		this.totalAmountDesc = totalAmountDesc;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalPointAmount() {
		return totalPointAmount;
	}

	public void setTotalPointAmount(BigDecimal totalPointAmount) {
		this.totalPointAmount = totalPointAmount;
	}

	public BigDecimal getTotalGiftAmount() {
		return totalGiftAmount;
	}

	public void setTotalGiftAmount(BigDecimal totalGiftAmount) {
		this.totalGiftAmount = totalGiftAmount;
	}
	
}

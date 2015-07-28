package com.hisoft.hscloud.bss.sla.om.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="hc_user_refund")
public class UserRefund {
	private long id;
	private Date refundOn;
	private Date finishOn;
	private long refundBy;
	private String description;
	private long accountId;
	private BigDecimal refund;
	private long orderId;
	private boolean isAudit;
	private String remark;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getRefundOn() {
		return refundOn;
	}
	public void setRefundOn(Date refundOn) {
		this.refundOn = refundOn;
	}
	public Date getFinishOn() {
		return finishOn;
	}
	public void setFinishOn(Date finishOn) {
		this.finishOn = finishOn;
	}
	public long getRefundBy() {
		return refundBy;
	}
	public void setRefundBy(long refundBy) {
		this.refundBy = refundBy;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name="account_id")
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	@Column(name="order_id")
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	@Column(name="is_audit")
	public boolean getAudit() {
		return isAudit;
	}
	public void setAudit(boolean isAudit) {
		this.isAudit = isAudit;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BigDecimal getRefund() {
		return refund;
	}
	public void setRefund(BigDecimal refund) {
		this.refund = refund;
	}
	
	
}

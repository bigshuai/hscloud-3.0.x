package com.hisoft.hscloud.bss.sla.om.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "hc_vm_refund_log")
public class VmRefundLog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	// reference id
	@Column(unique = true)
	private Long referenceId;
	// 机器号
	@Column(unique = true)
	private String uuid;
	// 业务状态 1,申请中2,已审核3,拒绝 4,前台取消
	@Column(name = "status")
	private short status;
	// 订单编号
	private String orderNo;
	// 开通时间
	@Column(name = "open_date")
	private Date openDate;
	// 到期时间
	@Column(name = "expiration_date")
	private Date expirationDate;
	// 申请时间
	@Column(name = "apply_date")
	private Date applyDate;
	// 申请原因
	@Column(name = "apply_refund_reason")
	// 拒绝原因
	private String applyRefundReason;
	@Column(name = "reject_refund_reason")
	private String rejectRefundReason;
	// 所有者邮箱
	@Column(name = "owner_email")
	private String ownerEmail;
	@Column(name = "owner_id ")
	private Long ownerId;
	// 退款日期
	@Column(name = "refund_date")
	private Date refundDate;
	// 退款金额
	@Column(name = "refund_amount")
	private BigDecimal refundAmount;
	@Column(name="refund_point_amount")
	private BigDecimal refundPointAmount;
	//新增退款礼金，以前没有该字段
	@Column(name="refund_gift_amount")
	private BigDecimal refundGiftAmount;
	// 操作人邮箱
	private String operator;
	// 退款类型 1，部分退款 2，全额退款
	@Column(name = "refund_type")
	private short refundType;
	@Column(name = "update_date")
	private Date updateDate;
	@Transient
	private String vmName;
	@Transient
	private String outerIp;
	@Column(name="refund_reason_type")
	private Short refundReasonType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getApplyRefundReason() {
		return applyRefundReason;
	}

	public void setApplyRefundReason(String applyRefundReason) {
		this.applyRefundReason = applyRefundReason;
	}

	public String getRejectRefundReason() {
		return rejectRefundReason;
	}

	public void setRejectRefundReason(String rejectRefundReason) {
		this.rejectRefundReason = rejectRefundReason;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public Date getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public short getRefundType() {
		return refundType;
	}

	public void setRefundType(short refundType) {
		this.refundType = refundType;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getOuterIp() {
		return outerIp;
	}

	public void setOuterIp(String outerIp) {
		this.outerIp = outerIp;
	}
	
	public Short getRefundReasonType() {
		return refundReasonType;
	}

	public void setRefundReasonType(Short refundReasonType) {
		this.refundReasonType = refundReasonType;
	}
	

	public BigDecimal getRefundPointAmount() {
		return refundPointAmount;
	}

	public void setRefundPointAmount(BigDecimal refundPointAmount) {
		this.refundPointAmount = refundPointAmount;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public BigDecimal getRefundGiftAmount() {
		return refundGiftAmount;
	}

	public void setRefundGiftAmount(BigDecimal refundGiftAmount) {
		this.refundGiftAmount = refundGiftAmount;
	}

	@Override
	public String toString() {
		return "VmRefundLog [id=" + id + ", referenceId=" + referenceId
				+ ", uuid=" + uuid + ", status=" + status + ", orderNo="
				+ orderNo + ", openDate=" + openDate + ", expirationDate="
				+ expirationDate + ", applyDate=" + applyDate
				+ ", applyRefundReason=" + applyRefundReason
				+ ", rejectRefundReason=" + rejectRefundReason
				+ ", ownerEmail=" + ownerEmail + ", refundDate=" + refundDate
				+ ", refundAmount=" + refundAmount + ", operator=" + operator
				+ ", refundType=" + refundType + ", updateDate=" + updateDate
				+ ", vmName=" + vmName + ", outerIp=" + outerIp + "]";
	}

}
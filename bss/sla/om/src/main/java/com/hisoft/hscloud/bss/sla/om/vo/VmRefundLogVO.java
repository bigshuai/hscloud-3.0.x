package com.hisoft.hscloud.bss.sla.om.vo; 

import java.math.BigDecimal;
import java.util.Date;
public class VmRefundLogVO {
		private Long id;
		// reference id
		private Long referenceId;
		// 机器号
		private String uuid;
		// 业务状态 1,申请中2,已审核3,拒绝
		private short status;
		// 订单编号
		private String orderNo;
		// 开通时间
		private Date openDate;
		// 到期时间
		private Date expirationDate;
		// 申请时间
		private Date applyDate;
		// 申请原因
		// 拒绝原因
		private String applyRefundReason;
		private String rejectRefundReason;
		// 所有者邮箱
		private String ownerEmail;
		// 退款日期
		private Date refundDate;
		// 退款金额
		private BigDecimal refundAmount;
		// 操作人邮箱
		private String operator;
		// 退款类型 1，部分退款 2，全额退款
		private short refundType;
		private Date updateDate;
		private String vmName;
		private String outerIp;
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

}
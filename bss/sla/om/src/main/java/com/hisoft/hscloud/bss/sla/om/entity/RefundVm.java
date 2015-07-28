package com.hisoft.hscloud.bss.sla.om.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hc_refund_vm")
public class RefundVm {
	private long id;
	private long refundId;
	private long referenceId;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
    @Column(name="refund_id")
	public long getRefundId() {
		return refundId;
	}

	public void setRefundId(long refundId) {
		this.refundId = refundId;
	}
	@Column(name="reference_id")
	public long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}

}

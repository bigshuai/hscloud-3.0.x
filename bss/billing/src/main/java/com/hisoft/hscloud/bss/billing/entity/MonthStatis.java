package com.hisoft.hscloud.bss.billing.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;

@Entity
@Table(name = "hc_month_statis")
public class MonthStatis extends AbstractEntity{

	@Column(name = "order_cnt")
	private int orderCNT;//直接购买
	
	@Column(name = "positive_cnt")
	private int positiveCNT;//转正
	
	@Column(name = "renew_cnt")
	private int renewCNT;//续费VM
	
	@Column(name = "refund_cnt")
	private int refundCNT;//退款VM
	
	@Column(name = "expired_cnt")
	private int expiredCNT;//到期
	
	@Column(name = "order_incoming")
	private BigDecimal orderIncoming;//金额（购买+转正VM）
	
	@Column(name = "renew_incoming")
	private BigDecimal renewIncoming;//金额(续费VM)
	
	@Column(name = "refund_fee")
	private BigDecimal refundFee;//退款金额
	
	@Column(name = "statis_day",nullable=false)
	private Date statisDay;
	
	@Column(name = "domain_id",nullable=false)
	private Long domainId;//平台id

	public int getOrderCNT() {
		return orderCNT;
	}

	public void setOrderCNT(int orderCNT) {
		this.orderCNT = orderCNT;
	}

	public int getPositiveCNT() {
		return positiveCNT;
	}

	public void setPositiveCNT(int positiveCNT) {
		this.positiveCNT = positiveCNT;
	}

	public int getRenewCNT() {
		return renewCNT;
	}

	public void setRenewCNT(int renewCNT) {
		this.renewCNT = renewCNT;
	}

	public int getRefundCNT() {
		return refundCNT;
	}

	public void setRefundCNT(int refundCNT) {
		this.refundCNT = refundCNT;
	}

	public int getExpiredCNT() {
		return expiredCNT;
	}

	public void setExpiredCNT(int expiredCNT) {
		this.expiredCNT = expiredCNT;
	}

	public BigDecimal getOrderIncoming() {
		return orderIncoming;
	}

	public void setOrderIncoming(BigDecimal orderIncoming) {
		this.orderIncoming = orderIncoming;
	}

	public BigDecimal getRenewIncoming() {
		return renewIncoming;
	}

	public void setRenewIncoming(BigDecimal renewIncoming) {
		this.renewIncoming = renewIncoming;
	}

	public BigDecimal getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(BigDecimal refundFee) {
		this.refundFee = refundFee;
	}

	public Date getStatisDay() {
		return statisDay;
	}

	public void setStatisDay(Date statisDay) {
		this.statisDay = statisDay;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}
	


}

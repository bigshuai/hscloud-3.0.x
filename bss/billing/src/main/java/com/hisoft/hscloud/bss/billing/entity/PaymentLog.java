package com.hisoft.hscloud.bss.billing.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.hisoft.hscloud.bss.billing.constant.TradeStatus;
import com.hisoft.hscloud.common.entity.AbstractEntity;

@Entity
@Table(name = "hc_payment_log",uniqueConstraints = {@UniqueConstraint(columnNames = {
		"serialNumber", "tradeStatus" })})
public class PaymentLog extends AbstractEntity {

	// 交易流水号
	@Column(nullable = false)
	private String serialNumber;

	// 交易时间
	@Column(nullable = false)
	private Date tradeTime = new Date();

	// 交易金额
	@Column(nullable = false)
	private BigDecimal tradeAmount;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long accountId;
	@Column(nullable = false)
	private Long domainId;

	@Column(nullable = false)
	private Short tradeType;

	// 支付平台
	@Column(nullable = false)
	private String vendor;

	// 交易状态
	@Column(nullable = false)
	private short tradeStatus = TradeStatus.TRADE_UNFINISHED.getIndex();

	private String description;

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Short getTradeType() {
		return tradeType;
	}

	public void setTradeType(Short tradeType) {
		this.tradeType = tradeType;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public short getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(short tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}
	

}

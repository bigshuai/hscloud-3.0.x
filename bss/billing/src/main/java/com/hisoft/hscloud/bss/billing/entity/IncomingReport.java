package com.hisoft.hscloud.bss.billing.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.hisoft.hscloud.common.entity.AbstractEntity;

@Entity
@Table(name = "hc_incoming_report",uniqueConstraints={@UniqueConstraint(columnNames={"month","domainId"})})
public class IncomingReport extends AbstractEntity {
	
	private String month;//时间   yyyy-MM
	
	private Long domainId;//分平台id
	
    @Column(name = "deposit_balance", nullable = false)
	private BigDecimal depositBalance;//存款余额
	
	@Column(name = "consume_balance", nullable = false)
	private BigDecimal consumeBalance;//消费余额

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public BigDecimal getDepositBalance() {
		return depositBalance;
	}

	public void setDepositBalance(BigDecimal depositBalance) {
		this.depositBalance = depositBalance;
	}

	public BigDecimal getConsumeBalance() {
		return consumeBalance;
	}

	public void setConsumeBalance(BigDecimal consumeBalance) {
		this.consumeBalance = consumeBalance;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}
	
	

}

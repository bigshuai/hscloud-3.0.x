package com.hisoft.hscloud.bss.billing.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.hisoft.hscloud.common.entity.AbstractEntity;

public class OtherResponsibilityDetail extends AbstractEntity{
	
	@Column(name="domain_id")
	private Long domainId;
	
	private String email;
	
	@Column(name="transcation_id")
	private String transcationId;
	

	@Column(name="type")
	private Short type;//交易类型
	
	@Column(name="transcation_on")
	private Date transcationOn;//交易时间
	
	@Column(name="amount")
	private BigDecimal amount;//金额
	
	@Column(name="current_incoming")
	private BigDecimal currentIncoming;//当月权责收入
	
	@Column(name="nonevent_incoming")
	private BigDecimal noneventIncoming;//期末权责消费余额
	
	@Column(name="description")
	private String description;//描叙

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTranscationId() {
		return transcationId;
	}

	public void setTranscationId(String transcationId) {
		this.transcationId = transcationId;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public Date getTranscationOn() {
		return transcationOn;
	}

	public void setTranscationOn(Date transcationOn) {
		this.transcationOn = transcationOn;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getCurrentIncoming() {
		return currentIncoming;
	}

	public void setCurrentIncoming(BigDecimal currentIncoming) {
		this.currentIncoming = currentIncoming;
	}

	public BigDecimal getNoneventIncoming() {
		return noneventIncoming;
	}

	public void setNoneventIncoming(BigDecimal noneventIncoming) {
		this.noneventIncoming = noneventIncoming;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}

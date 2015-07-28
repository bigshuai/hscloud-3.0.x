package com.hisoft.hscloud.bss.billing.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.hisoft.hscloud.common.entity.AbstractEntity;

public class VMResponsibilityDetail extends AbstractEntity{
	
	@Column(name="domain_id")
	private Long domainId;
	
	private String email;
	
	@Column(name="transcation_id")
	private String transcationId;
	
	@Column(name="order_no")
	private String orderNo;//订单号
	
	@Column(name="vm_no")
	private String vmNo;//虚拟机号
	
	@Column(name="type")
	private Short type;//交易类型
	
	@Column(name="transcation_on")
	private Date transcationOn;//交易时间
	
	@Column(name="start_time")
	private Date startTime;//订单开始时间
	
	@Column(name="end_time")
	private Date endTime;//订单完成时间
	
	@Column(name="order_duration")
	private Long orderDuration;//订单时长
	
	@Column(name="used_duration")
	private Long usedDuration;//期末已用时长
	
	@Column(name="amount")
	private BigDecimal amount;//金额
	
	@Column(name="current_incoming")
	private BigDecimal currentIncoming;//当月权责收入
	
	@Column(name="finished_incoming")
	private BigDecimal finishedIncoming;//期末累计权责收入
	
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getVmNo() {
		return vmNo;
	}

	public void setVmNo(String vmNo) {
		this.vmNo = vmNo;
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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getOrderDuration() {
		return orderDuration;
	}

	public void setOrderDuration(Long orderDuration) {
		this.orderDuration = orderDuration;
	}

	public Long getUsedDuration() {
		return usedDuration;
	}

	public void setUsedDuration(Long usedDuration) {
		this.usedDuration = usedDuration;
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

	public BigDecimal getFinishedIncoming() {
		return finishedIncoming;
	}

	public void setFinishedIncoming(BigDecimal finishedIncoming) {
		this.finishedIncoming = finishedIncoming;
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

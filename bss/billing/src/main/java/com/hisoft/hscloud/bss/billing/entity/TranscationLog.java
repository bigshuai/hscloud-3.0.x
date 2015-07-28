package com.hisoft.hscloud.bss.billing.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hisoft.hscloud.bss.billing.constant.ServiceType;
import com.hisoft.hscloud.common.entity.AbstractEntity;

@Entity
@Table(name = "hc_transcation_log")
public class TranscationLog extends AbstractEntity {

	@Column(name = "transcation_on", nullable = false)
	private Date transcationOn = new Date();

	@Column(name = "description", length = 1024, nullable = false)
	private String description;

	@Column(nullable = false)
	private BigDecimal amount;//人民币金额
	
	@Column(nullable = false)
	private BigDecimal coupons;//点卷金额
	
	@Column(nullable = false)
	private BigDecimal rebateRate;//点卷毕率
	
	@Column(nullable = false)
	private BigDecimal giftsrebateRate;//礼金毕率

	@Column(nullable = false)
	private BigDecimal balance;// 交易后剩余金额
	
	@Column(nullable = false)
	private BigDecimal couponsBalance;//交易后剩余点劵
	
	@Column(nullable = false)
	private BigDecimal gifts;//交易礼金
	
	@Column(nullable = false)
	private BigDecimal giftsBalance;//交易后剩余礼金

	@Column(name = "payment_type", nullable = false)
	private Short paymentType;//线上网上平台，线下等。

	@Column(name = "deposit_source")
	private Short depositSource;//充值来源
	
	@Column(name = "transcation_type", nullable = false)
	private Short transcationType;//交易类型
	
	@Column(name = "operator_Type", nullable = false)
	private Short operatorType;//操作人类型（管理员：0，前台人员：1）
	
	@Column(name = "service_type", nullable = false)//服务类型
	private Short serviceType = ServiceType.SERVICE_NO.getIndex();

	@Column(name = "bank_account")
	private String bankAccount;

	@Column(name = "accountId", nullable = false)
	private Long accountId;

	private Long orderId;
	
	@Column(name = "rate_flag", nullable = false)
	private boolean rateFlag;
	
	@Column(name = "consume_type")//服务类型
    private Short consumeType;
	
	@Column(name = "domain_id")//所属平台
	private Long domainId;

	@Column(name = "remark", length = 255)
	private String remark;
	
	private Short flow;//资金流向 （ 1：转入   2：转出）

	public Date getTranscationOn() {
		return transcationOn;
	}

	public void setTranscationOn(Date transcationOn) {
		this.transcationOn = transcationOn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Short getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Short paymentType) {
		this.paymentType = paymentType;
	}

	public Short getTranscationType() {
		return transcationType;
	}

	public void setTranscationType(Short transcationType) {
		this.transcationType = transcationType;
	}

	
	public Short getServiceType() {
		return serviceType;
	}

	public void setServiceType(Short serviceType) {
		this.serviceType = serviceType;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	public Short getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(Short operatorType) {
		this.operatorType = operatorType;
	}

	public BigDecimal getCoupons() {
		return coupons;
	}

	public void setCoupons(BigDecimal coupons) {
		this.coupons = coupons;
	}

	public BigDecimal getRebateRate() {
		return rebateRate;
	}

	public void setRebateRate(BigDecimal rebateRate) {
		this.rebateRate = rebateRate;
	}

	public BigDecimal getCouponsBalance() {
		return couponsBalance;
	}

	public void setCouponsBalance(BigDecimal couponsBalance) {
		this.couponsBalance = couponsBalance;
	}
	
	public boolean isRateFlag() {
		return rateFlag;
	}

	public void setRateFlag(boolean rateFlag) {
		this.rateFlag = rateFlag;
	}

	public Short getConsumeType() {
		return consumeType;
	}

	public void setConsumeType(Short consumeType) {
		this.consumeType = consumeType;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	public Short getDepositSource() {
		return depositSource;
	}

	public void setDepositSource(Short depositSource) {
		this.depositSource = depositSource;
	}

	@Transient
	public String getBalanceShow() {
		return this.balance.toString();
	}

	@Transient
	public String getAmountShow() {
		return this.amount.toString();
	}

	public Short getFlow() {
		return flow;
	}

	public void setFlow(Short flow) {
		this.flow = flow;
	}

	public BigDecimal getGiftsrebateRate() {
		return giftsrebateRate;
	}

	public void setGiftsrebateRate(BigDecimal giftsrebateRate) {
		this.giftsrebateRate = giftsrebateRate;
	}

	public BigDecimal getGiftsBalance() {
		return giftsBalance;
	}

	public void setGiftsBalance(BigDecimal giftsBalance) {
		this.giftsBalance = giftsBalance;
	}

	public BigDecimal getGifts() {
		return gifts;
	}

	public void setGifts(BigDecimal gifts) {
		this.gifts = gifts;
	}

	
}

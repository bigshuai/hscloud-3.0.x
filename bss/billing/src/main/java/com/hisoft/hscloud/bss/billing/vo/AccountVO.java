package com.hisoft.hscloud.bss.billing.vo;

import java.math.BigDecimal;

public class AccountVO {
	
	private Long id;//账户id
	
	private Long userId;//账户关联用户id
	
	private Long operatorId;//操作用户id

	private Long orderId;//订单id
	
	private short front;//操作用户（后台用户：0，前台用户：1）
	
	private short paymentType;
	
	private short serviceType;
	
	private short consumeType;
	
	private BigDecimal amout;//RMB消费金额
	
	private BigDecimal balance;//RMB账户余额
	
	private BigDecimal coupons;//点劵消费金额
	
	private BigDecimal couponsBalance;//点劵账户余额
	
	private BigDecimal rebateRate;//点劵比率
	
	private BigDecimal gifts;//礼金消费金额
	
	private BigDecimal giftsBalance;//礼金账户余额
	
	private Integer giftsRebateRate;// 返点率
	
	private Integer couponsRebateRate;// 返点率
	
	private String description;//备注

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public short getFront() {
		return front;
	}

	public void setFront(short front) {
		this.front = front;
	}

	public short getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(short paymentType) {
		this.paymentType = paymentType;
	}

	public short getServiceType() {
		return serviceType;
	}

	public void setServiceType(short serviceType) {
		this.serviceType = serviceType;
	}

	public short getConsumeType() {
		return consumeType;
	}

	public void setConsumeType(short consumeType) {
		this.consumeType = consumeType;
	}

	public BigDecimal getAmout() {
		return amout;
	}

	public void setAmout(BigDecimal amout) {
		this.amout = amout;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getCoupons() {
		return coupons;
	}

	public void setCoupons(BigDecimal coupons) {
		this.coupons = coupons;
	}

	public BigDecimal getCouponsBalance() {
		return couponsBalance;
	}

	public void setCouponsBalance(BigDecimal couponsBalance) {
		this.couponsBalance = couponsBalance;
	}

	public BigDecimal getRebateRate() {
		return rebateRate;
	}

	public void setRebateRate(BigDecimal rebateRate) {
		this.rebateRate = rebateRate;
	}

	public BigDecimal getGifts() {
		return gifts;
	}

	public void setGifts(BigDecimal gifts) {
		this.gifts = gifts;
	}

	public BigDecimal getGiftsBalance() {
		return giftsBalance;
	}

	public void setGiftsBalance(BigDecimal giftsBalance) {
		this.giftsBalance = giftsBalance;
	}

	public Integer getGiftsRebateRate() {
		return giftsRebateRate;
	}

	public void setGiftsRebateRate(Integer giftsRebateRate) {
		this.giftsRebateRate = giftsRebateRate;
	}

	public Integer getCouponsRebateRate() {
		return couponsRebateRate;
	}

	public void setCouponsRebateRate(Integer couponsRebateRate) {
		this.couponsRebateRate = couponsRebateRate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

}

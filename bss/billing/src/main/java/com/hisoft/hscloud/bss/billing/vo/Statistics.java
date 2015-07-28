package com.hisoft.hscloud.bss.billing.vo;

import java.math.BigDecimal;

public class Statistics {
	
	private String yearMonth;//时间   yyyy-MM
	
	private BigDecimal deposit;//当期存款
	
	private BigDecimal absDraw;//提款
	
	private BigDecimal absConsume;//当期消费
	
	private BigDecimal refund;//退款
	
	private BigDecimal responsibility;//权责收入
	
	private BigDecimal absDepositBalance;
	
	private BigDecimal absConsumeBalance;
	
	private BigDecimal depositBalance;//存款余额
	
	private BigDecimal consumeBalance;//消费余额
	
    private BigDecimal draw;//提款
	
	private BigDecimal consume;//当期消费
	
	
	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	public BigDecimal getDraw() {
		return draw;
	}

	public void setDraw(BigDecimal draw) {
		this.draw = draw;
	}

	public BigDecimal getConsume() {
		return consume;
	}

	public void setConsume(BigDecimal consume) {
		this.consume = consume;
	}

	public BigDecimal getRefund() {
		return refund;
	}

	public void setRefund(BigDecimal refund) {
		this.refund = refund;
	}

	public BigDecimal getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(BigDecimal responsibility) {
		this.responsibility = responsibility;
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

	public BigDecimal getAbsDraw() {
		return this.draw.abs();
	}

	public void setAbsDraw(BigDecimal absDraw) {
		this.absDraw = absDraw;
	}

	public BigDecimal getAbsConsume() {
		return this.consume.abs();
	}

	public void setAbsConsume(BigDecimal absConsume) {
		this.absConsume = absConsume;
	}

	public BigDecimal getAbsDepositBalance() {
		return this.depositBalance.abs();
	}

	public void setAbsDepositBalance(BigDecimal absDepositBalance) {
		this.absDepositBalance = absDepositBalance;
	}

	public BigDecimal getAbsConsumeBalance() {
		return this.consumeBalance.abs();
	}

	public void setAbsConsumeBalance(BigDecimal absConsumeBalance) {
		this.absConsumeBalance = absConsumeBalance;
	}
	
	

}

package com.hisoft.hscloud.bss.billing.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.hisoft.hscloud.common.entity.AbstractEntity;


@Entity
@Table(name = "hc_report")
public class Report extends AbstractEntity {
	
	@Column(name = "yearmonth", nullable = false)
	private String yearMonth;
	
    @Column(name = "domain_id", nullable = false)
	private Long domainId;
    
    //云统计报表字段
    @Column(nullable = false)
    private BigDecimal deposit;//当期存款
    
    @Column(nullable = false)
    private BigDecimal draw;//云统计报表提款 权责收入报表提现金额
    
    @Column(nullable = false)
    private BigDecimal refund;//云统计报表退款    权责收入报表退款金额
    
    @Column(name = "deposit_balance", nullable = false)
    private BigDecimal depositBalance;//存款余额
    
    @Column(nullable = false)
    private BigDecimal consume;//云统计报表当期消费    权责收入报表消费金额
    
    @Column(name = "consume_balance", nullable = false)
    private BigDecimal consumeBalance;//消费余额
    
    @Column(nullable = false)
    private BigDecimal responsibility;//权责收入
    
    //存款分析
    @Column(nullable = false)
	private BigDecimal oldPlatform;//老平台
	
    @Column(nullable = false)
	private BigDecimal alipay;//支付宝
	
    @Column(nullable = false)
	private BigDecimal easyMoney;//块钱
	
    @Column(nullable = false)
	private BigDecimal eBank;//网银
	
    @Column(nullable = false)
	private BigDecimal cash;//现金
	
    @Column(nullable = false)
	private BigDecimal cheque;//支票
    
    //权责收入
    @Column(name = "prepay", nullable = false)
    private BigDecimal prepay;//预付金额
    
    @Column(name = "machine_responsibility", nullable = false)
	private BigDecimal machineResponsibility;//云主机权责
    
    @Column(name = "express", nullable = false)
    private BigDecimal expressResponsibility;//快递费
    
    @Column(name = "other_responsibility", nullable = false)
	private BigDecimal otherResponsibility;//其它权责
	
    @Column(name = "monthresponsibility_balance", nullable = false)
	private BigDecimal monthResponsibilityBalance;//期未权责消费余额
    
    @Column(name = "pre_consume_balance")
    private BigDecimal preConsumeBalance;//上期消费余额
    
    @Column(name = "pre_deposit_balance")
    private BigDecimal preDepositBalance;//上期存款余额

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
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

	public BigDecimal getRefund() {
		return refund;
	}

	public void setRefund(BigDecimal refund) {
		this.refund = refund;
	}

	public BigDecimal getDepositBalance() {
		return depositBalance;
	}

	public void setDepositBalance(BigDecimal depositBalance) {
		this.depositBalance = depositBalance;
	}

	public BigDecimal getConsume() {
		return consume;
	}

	public void setConsume(BigDecimal consume) {
		this.consume = consume;
	}

	public BigDecimal getConsumeBalance() {
		return consumeBalance;
	}

	public void setConsumeBalance(BigDecimal consumeBalance) {
		this.consumeBalance = consumeBalance;
	}

	public BigDecimal getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(BigDecimal responsibility) {
		this.responsibility = responsibility;
	}

	public BigDecimal getOldPlatform() {
		return oldPlatform;
	}

	public void setOldPlatform(BigDecimal oldPlatform) {
		this.oldPlatform = oldPlatform;
	}

	public BigDecimal getAlipay() {
		return alipay;
	}

	public void setAlipay(BigDecimal alipay) {
		this.alipay = alipay;
	}

	public BigDecimal getEasyMoney() {
		return easyMoney;
	}

	public void setEasyMoney(BigDecimal easyMoney) {
		this.easyMoney = easyMoney;
	}

	public BigDecimal geteBank() {
		return eBank;
	}

	public void seteBank(BigDecimal eBank) {
		this.eBank = eBank;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public BigDecimal getCheque() {
		return cheque;
	}

	public void setCheque(BigDecimal cheque) {
		this.cheque = cheque;
	}

	public BigDecimal getPrepay() {
		return prepay;
	}

	public void setPrepay(BigDecimal prepay) {
		this.prepay = prepay;
	}

	public BigDecimal getMachineResponsibility() {
		return machineResponsibility;
	}

	public void setMachineResponsibility(BigDecimal machineResponsibility) {
		this.machineResponsibility = machineResponsibility;
	}

	public BigDecimal getExpressResponsibility() {
		return expressResponsibility;
	}

	public void setExpressResponsibility(BigDecimal expressResponsibility) {
		this.expressResponsibility = expressResponsibility;
	}

	public BigDecimal getOtherResponsibility() {
		return otherResponsibility;
	}

	public void setOtherResponsibility(BigDecimal otherResponsibility) {
		this.otherResponsibility = otherResponsibility;
	}

	public BigDecimal getMonthResponsibilityBalance() {
		return monthResponsibilityBalance;
	}

	public void setMonthResponsibilityBalance(BigDecimal monthResponsibilityBalance) {
		this.monthResponsibilityBalance = monthResponsibilityBalance;
	}

	public BigDecimal getPreConsumeBalance() {
		return preConsumeBalance;
	}

	public void setPreConsumeBalance(BigDecimal preConsumeBalance) {
		this.preConsumeBalance = preConsumeBalance;
	}

	public BigDecimal getPreDepositBalance() {
		return preDepositBalance;
	}

	public void setPreDepositBalance(BigDecimal preDepositBalance) {
		this.preDepositBalance = preDepositBalance;
	}
	

}

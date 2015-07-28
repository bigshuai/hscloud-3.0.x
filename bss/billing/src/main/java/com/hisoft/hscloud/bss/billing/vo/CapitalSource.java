package com.hisoft.hscloud.bss.billing.vo;

import java.math.BigDecimal;

public class CapitalSource {
	
	private String  yearMonth;//yyyy-MM
	
	private BigDecimal oldPlatform;//老平台
	
	private BigDecimal alipay;//支付宝
	
	private BigDecimal easyMoney;//块钱
	
	private BigDecimal eBank;//网银
	
	private BigDecimal cash;//现金
	
	private BigDecimal cheque;//支票
	
	private BigDecimal total;//excel占位

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
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

	public BigDecimal getEBank() {
		return eBank;
	}

	public void setEBank(BigDecimal eBank) {
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

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}

package com.hisoft.hscloud.bss.billing.vo;

import java.math.BigDecimal;
import java.util.Date;


public class MonthStatisVO {

	private Date statisDay;
	
	private String abbName;
	
	private int orderCNT;//直接购买
	
	private int positiveCNT;//转正
	
	private int renewCNT;//续费VM
	
	private int refundCNT;//退款VM
	
	private int expiredCNT;//到期删除VM
	
	private BigDecimal orderIncoming;//金额（购买+转正VM）
	
	private BigDecimal renewIncoming;//金额(续费VM)
	
	private BigDecimal consumeAmout;//消费金额(excel占位)
	
	private BigDecimal refundFee;//退款金额
	
	private BigDecimal incoming;//收入
	
	private BigDecimal increaseVM;//新增vm
	
	private BigDecimal returnRate;//退单率
	
	private BigDecimal refundRate;//退款率
	
	private String domainName;

	private String consumeAmoutStr;
	
	public Date getStatisDay() {
		return statisDay;
	}

	public void setStatisDay(Date statisDay) {
		this.statisDay = statisDay;
	}

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

	public BigDecimal getConsumeAmout() {
		return this.orderIncoming.add(this.renewIncoming);
	}

	public void setConsumeAmout(BigDecimal consumeAmout) {
		this.consumeAmout = this.orderIncoming.add(this.renewIncoming);
	}

	public BigDecimal getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(BigDecimal refundFee) {
		this.refundFee = refundFee;
	}
	
	public String getRefundFeeStr(){
		return this.refundFee.toString();
	}
	
	public String getIncomingStr(){
		return this.orderIncoming.add(this.renewIncoming).add(this.refundFee.negate()).toString();
	}

	public BigDecimal getIncoming() {
		return incoming;
	}

	public void setIncoming(BigDecimal incoming) {
		this.incoming = incoming;
	}

	public BigDecimal getReturnRate() {
		return returnRate;
	}

	public void setReturnRate(BigDecimal returnRate) {
		this.returnRate = returnRate;
	}

	public BigDecimal getRefundRate() {
		return refundRate;
	}

	public void setRefundRate(BigDecimal refundRate) {
		this.refundRate = refundRate;
	}

	public BigDecimal getIncreaseVM() {
		return increaseVM;
	}

	public void setIncreaseVM(BigDecimal increaseVM) {
		this.increaseVM = increaseVM;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getAbbName() {
		return abbName;
	}

	public void setAbbName(String abbName) {
		this.abbName = abbName;
	}

	public String getConsumeAmoutStr() {
		return this.orderIncoming.add(this.renewIncoming).toString();
	}

	public void setConsumeAmoutStr(String consumeAmoutStr) {
		this.consumeAmout = this.orderIncoming.add(this.renewIncoming);
	}
	
	
}

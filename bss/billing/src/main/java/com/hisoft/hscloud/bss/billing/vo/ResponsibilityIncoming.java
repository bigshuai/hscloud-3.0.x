package com.hisoft.hscloud.bss.billing.vo;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ResponsibilityIncoming {
	
	private String  yearMonth;
	
	private BigDecimal absPrepay;//预付金额
	
	private BigDecimal absConsume;//消费金额
	
	private BigDecimal refund;//退款金额
	
	private BigDecimal absDrawCash;//提现金额
	
	private BigDecimal prepayBalance;//预付余额，excel占位。
	
	private BigDecimal responsibility;//权责收入，excel占位。
	
	private BigDecimal machine;//云主机权责
	
	private BigDecimal other;//其它权责
	
	private BigDecimal monthResponsibilityBalance;//期未权责消费余额
	
	private BigDecimal express;//快递费
	
	private BigDecimal consume;//消费金额
	
	private BigDecimal drawCash;//提现金额
	
	private BigDecimal prepay;//预付金额
	
	private String abbreviation;//平台简称  excel中不导出
	
	private Long domainId;
	


	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public BigDecimal getPrepay() {
		return prepay;
	}

	public void setPrepay(BigDecimal prepay) {
		this.prepay = prepay;
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

	public BigDecimal getDrawCash() {
		return drawCash;
	}

	public void setDrawCash(BigDecimal drawCash) {
		this.drawCash = drawCash;
	}

	public BigDecimal getPrepayBalance() {
		return prepayBalance;
	}

	public void setPrepayBalance(BigDecimal prepayBalance) {
		this.prepayBalance = prepayBalance;
	}

	public BigDecimal getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(BigDecimal responsibility) {
		this.responsibility = responsibility;
	}

	public BigDecimal getMachine() {
		return machine;
	}

	public void setMachine(BigDecimal machine) {
		this.machine = machine;
	}

	public BigDecimal getOther() {
		
		if(null == other){
			return  null == express? new BigDecimal("0"):this.express;
		}else if(null == express){
			return  null == other? new BigDecimal("0"):this.other;
		}else{
			return other.add(express);
		}
		
	}

	public void setOther(BigDecimal other) {
		this.other = other;
	}

	public BigDecimal getExpress() {
		return express;
	}

	public void setExpress(BigDecimal express) {
		this.express = express;
	}

	public BigDecimal getMonthResponsibilityBalance() {
		return monthResponsibilityBalance;
	}

	public void setMonthResponsibilityBalance(BigDecimal monthResponsibilityBalance) {
		this.monthResponsibilityBalance = monthResponsibilityBalance;
	}

	public BigDecimal getAbsPrepay() {
		return this.prepay.abs();
	}

	public void setAbsPrepay(BigDecimal absPrepay) {
		this.absPrepay = absPrepay;
	}

	public BigDecimal getAbsConsume() {
		return this.consume.abs();
	}

	public void setAbsConsume(BigDecimal absConsume) {
		this.absConsume = absConsume;
	}

	public BigDecimal getAbsDrawCash() {
		return this.drawCash.abs();
	}

	public void setAbsDrawCash(BigDecimal absDrawCash) {
		this.absDrawCash = absDrawCash;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	public String getConsumeStr(){
	    DecimalFormat df = new DecimalFormat("0.00"); 
		if(null == this.consume){
			return df.format(0);
		}
		return df.format(this.consume.abs());
	}
	
	public String getPrepayStr(){
		DecimalFormat df = new DecimalFormat("0.00"); 
		if(null == this.prepay){
			return df.format(0);
		}
		return df.format(this.prepay);
	}
	
	public String getRefundStr(){
		DecimalFormat df = new DecimalFormat("0.00"); 
		if(null == this.refund){
			return df.format(0);
		}
		return df.format(this.refund.abs());
	}
	
	public String getDrawCashStr(){
		DecimalFormat df = new DecimalFormat("0.00"); 
		if(null == this.drawCash){
			return df.format(0);
		}
		return df.format(this.drawCash.abs());
	}
	
	public String getMachineStr(){
		DecimalFormat df = new DecimalFormat("0.00"); 
		if(null == this.machine){
			return df.format(0);
		}
		return df.format(this.machine);
	}

	public String getExpressStr(){
		DecimalFormat df = new DecimalFormat("0.00"); 
		if(null == this.express){
			return df.format(0);
		}
		return df.format(this.express);
	}
	
	public String getOtherStr(){
		DecimalFormat df = new DecimalFormat("0.00"); 
		if(null == other){
			return  null == express? df.format(0):df.format(this.express);
		}else if(null == express){
			return  null == other? df.format(0):df.format(this.other);
		}else{
			return df.format(other.add(express));
		}
	}
	
	public String getMonthResponsibilityBalanceStr(){
		DecimalFormat df = new DecimalFormat("0.00"); 
		if(null == this.monthResponsibilityBalance){
			return df.format(0);
		}
		return df.format(this.monthResponsibilityBalance);
	}
	
	public String getPrepayBalanceStr(){
		DecimalFormat df = new DecimalFormat("0.00"); 
		return df.format(this.prepay.add(this.consume).add(this.refund).add(this.drawCash));
	}
	
	public String getResponsibilityStr(){
		DecimalFormat df = new DecimalFormat("0.00"); 
		BigDecimal m = (null==this.machine?new BigDecimal(0):this.machine);
		BigDecimal o = (null==this.other?new BigDecimal(0):this.other);
		BigDecimal e = (null==this.express?new BigDecimal(0):this.express);
		BigDecimal t = m.add(o).add(e);
		return df.format(t);
	}
	
	
	
}

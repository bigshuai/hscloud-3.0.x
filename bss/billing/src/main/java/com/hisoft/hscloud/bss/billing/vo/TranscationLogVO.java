package com.hisoft.hscloud.bss.billing.vo;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hisoft.hscloud.bss.billing.constant.TranscationType;
import com.hisoft.hscloud.common.entity.AbstractVO;

public class TranscationLogVO extends AbstractVO {

	
    private String transcationOnStr;
    
	private String businessType;

	private String username;

	private String email;
	
	private String abbreviation;
	
	private BigDecimal absAmount; // 交易金额
	
	private BigDecimal absCoupons;
	
	private BigDecimal absGifts;// 交易礼金
	
	private BigDecimal absBalance; // 交易后剩余金额
	
	private BigDecimal absCouponsBalance;
	
	private BigDecimal absGiftsBalance;
	
	private String flowStr;

	private String description;

	// 以上为excel导出字段，不能修改字段顺序
	// ///////////////////////////////////////////
	
	private BigDecimal amount; // 交易金额
	
	private BigDecimal coupons;//消费点数
	
	private BigDecimal couponsBalance;//剩余点数
	
	private BigDecimal gifts;//消费礼金数
	
	private BigDecimal giftsBalance;//剩余消费礼金数
	
	private Date transcationOn;

	private Short paymentType;

	private String bankAccount;

	private Long accountId;

	private Long orderId;

	private String remark;

	private Short transcationType;
	
	private BigDecimal balance; // 交易后剩余金额
	
	private String operator;
	
	private String orderNo;
	
	private Short flow;//资金流向 （ 1：转入   2：转出）
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Date getTranscationOn() {
		return transcationOn;
	}

	public void setTranscationOn(Date transcationOn) {
		this.transcationOn = transcationOn;
	}

	public Short getTranscationType() {
		return transcationType;
	}

	public void setTranscationType(Short transcationType) {
		this.transcationType = transcationType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Short getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Short paymentType) {
		this.paymentType = paymentType;
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

	public String getBusinessType() {

		if(TranscationType.TRANSCATION_CONSUME.getIndex()==this.transcationType){
			this.setBusinessType("消费");
		}else if(TranscationType.TRANSCATION_DEPOSIT.getIndex()==this.transcationType){
			this.setBusinessType("存款");
		}else if(TranscationType.TRANSCATION_DRAW.getIndex()==this.transcationType){
			this.setBusinessType("提现");
		}else if(TranscationType.TRANSCATION_REFUND.getIndex()==this.transcationType){
			this.setBusinessType("退款");
		}else if(TranscationType.TRANSCATION_CANCEL.getIndex()==this.transcationType){
			this.setBusinessType("撤消");
		}else if(TranscationType.TRANSCATION_COUPONS_DEPOSIT.getIndex()==this.transcationType){
			this.setBusinessType("充返点");
		}else if(TranscationType.TRANSCATION_COUPONS_DRAW.getIndex()==this.transcationType){
			this.setBusinessType("返点提现");
		}else if(TranscationType.TRANSCATION_GIFTS_DEPOSIT.getIndex()==this.transcationType){
			this.setBusinessType("充礼金");
		}else if(TranscationType.TRANSCATION_GIFTS_DRAW.getIndex()==this.transcationType){
			this.setBusinessType("礼金提现");
		}else if(TranscationType.TRANSCATION_ROLLIN.getIndex()==this.transcationType){
			this.setBusinessType("转入");
		}else if(TranscationType.TRANSCATION_ROLLOUT.getIndex()==this.transcationType){
			this.setBusinessType("转出");
		}else{
			this.setBusinessType("");
		}
		return this.businessType;
		
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTranscationOnStr() {
		return df.format(this.transcationOn);
	}

	public void setTranscationOnStr(String transcationOnStr) {
		this.transcationOnStr = transcationOnStr;
	}

	public static DateFormat getDf() {
		return df;
	}

	public static void setDf(DateFormat df) {
		TranscationLogVO.df = df;
	}

	public BigDecimal getAbsAmount(){
		return this.amount.abs();
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

	public BigDecimal getAbsGifts() {
		return this.gifts.abs();
	}

	public void setAbsGifts(BigDecimal absGifts) {
		this.absGifts = absGifts;
	}

	public BigDecimal getAbsGiftsBalance() {
		return this.giftsBalance.abs();
	}

	public void setAbsGiftsBalance(BigDecimal absGiftsBalance) {
		this.absGiftsBalance = absGiftsBalance;
	}
	
	public String getAbsTrGifts(){
		return this.getAbsGifts().toString();
	}
	
	public String getAbsTrGiftsBalance(){
		return this.getAbsGiftsBalance().toString();
	}

	public BigDecimal getAbsBalance(){
		return this.balance.abs();
	}
	public String getAbsTrAmount(){
		return this.getAbsAmount().toString();
	}
	public String getAbsTrBalance(){
		return this.getAbsBalance().toString();
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

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public BigDecimal getAbsCoupons(){
		return this.coupons.abs();
	}
	public String getAbsTrCoupons(){
		return this.getAbsCoupons().toString();
	}

	
	public BigDecimal getAbsCouponsBalance(){
		return this.couponsBalance.abs();
	}
	
	public String getAbsTrCouponsBalance(){
		return this.getAbsCouponsBalance().toString();
	}

	public void setAbsAmount(BigDecimal absAmount) {
		this.absAmount = absAmount;
	}

	public void setAbsCoupons(BigDecimal absCoupons) {
		this.absCoupons = absCoupons;
	}

	public void setAbsBalance(BigDecimal absBalance) {
		this.absBalance = absBalance;
	}

	public void setAbsCouponsBalance(BigDecimal absCouponsBalance) {
		this.absCouponsBalance = absCouponsBalance;
	}

	public String getFlowStr() {
		if(null == this.flow){
			return "";
		}else if(1==this.flow){
			return "转入 ";
		}else if(2==this.flow){
			return "转出";
		}else{
			return "";
		}
	}

	public void setFlowStr(String flowStr) {
		this.flowStr = flowStr;
	}

	public Short getFlow() {
		return flow;
	}

	public void setFlow(Short flow) {
		this.flow = flow;
	}
	
}

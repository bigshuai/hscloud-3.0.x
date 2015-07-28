package com.hisoft.hscloud.bss.billing.vo;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VMResponsibility {
	
	private String email;//帐号
	
	private String transcationId;//交易号
	
	private String orderNo;//订单号
	
	private Long refrenceId;//虚拟机号
	
	private String transcationType;//交易类型
	
	private String transcationOnStr;
	
	private String startTimeStr;
	
	private String endTimeStr;
	
	private Long orderDuration;//订单时长
	
	private Long usedDuration;//期末已用时长
	
	private BigDecimal itemAmout;
	
	private BigDecimal currentIncoming;//当月权责收入
	
	private BigDecimal finishedIncoming;//期末累计权责收入
	
	private BigDecimal noneventIncoming;//期末权责消费余额
	
	private String description;//描叙
	
	private Short type;
	
	private Date transcationOn;//交易时间
	
	private Date startTime;//订单完成（开始）时间
	
	private Date endTime;//订单完成（开始）时间
	
	private BigDecimal amount;//金额
	
	private Long objectId;
	
	private Long orderItemId;
	
	private Byte productType;
	
	private String vmNo;//虚拟机号
	
	private BigDecimal absAmount;//金额
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getTranscationType() {
		if(null != this.type){
			if(this.type==1){
				return "消费 ";
			}else if(this.type==2){
				return "存款";
			}else if(this.type==3){
				return "退款 ";
			}else if(this.type==4){
				return "提款";
			}else if(this.type==5){
				return "撤消";
			}else if(this.type==6){
				return "全额退款 ";
			}else{
				return "";
			}
		}else{
			return "";
		}
	}

	public void setTranscationType(String transcationType) {
		this.transcationType = transcationType;
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

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getTranscationId() {
		return transcationId;
	}

	public void setTranscationId(String transcationId) {
		this.transcationId = transcationId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTranscationOnStr() {
		return df.format(this.transcationOn);
	}

	public void setTranscationOnStr(String transcationOnStr) {
		this.transcationOnStr = transcationOnStr;
	}

	public String getStartTimeStr() {
		return df.format(this.startTime);
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return df.format(this.endTime);
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public static DateFormat getDf() {
		return df;
	}

	public static void setDf(DateFormat df) {
		VMResponsibility.df = df;
	}

	public BigDecimal getAbsAmount() {
		return this.amount.abs();
	}

	public void setAbsAmount(BigDecimal absAmount) {
		this.absAmount = absAmount;
	}
	
	public String getAbsAmountStr(){
		if(null == this.amount){
			return "";
		}
		return this.amount.abs().toString();
	}
	

	public String getCurrentIncomingStr(){
		if(null == this.currentIncoming){
			return "";
		}
		return this.currentIncoming.toString();
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Byte getProductType() {
		return productType;
	}

	public void setProductType(Byte productType) {
		this.productType = productType;
	}

	public Long getRefrenceId() {
		return refrenceId;
	}

	public void setRefrenceId(Long refrenceId) {
		this.refrenceId = refrenceId;
	}

	public BigDecimal getItemAmout() {
		return itemAmout;
	}

	public void setItemAmout(BigDecimal itemAmout) {
		this.itemAmout = itemAmout;
	}

	public String getItemAmountStr(){
		if(null == this.itemAmout){
			return "";
		}
		return this.itemAmout.toString();
	}
	
}

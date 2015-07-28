package com.hisoft.hscloud.bss.billing.vo;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OtherResponsibility {
	
	private String email;//帐号
	
	private String transcationId;//交易号
	
	private String transcationType;//交易类型
	
	private String transcationOnStr;//交易时间
	
	private BigDecimal absAmount;//金额
	
	private BigDecimal currentIncoming;//当前金额
	
	private BigDecimal noneventIncoming;//未消费金额
	
	private String description;//描叙
	
	private Short type;

	private BigDecimal amount;//金额
	
	private Date transcationOn;//交易时间
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public String getTranscationType() {
		//return TranscationType.getItem(this.type).getType();
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

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
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

	public BigDecimal getNoneventIncoming() {
		return noneventIncoming;
	}

	public void setNoneventIncoming(BigDecimal noneventIncoming) {
		this.noneventIncoming = noneventIncoming;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAbsAmount() {
		return this.amount.abs();
	}

	public void setAbsAmount(BigDecimal absAmount) {
		this.absAmount = absAmount;
	}

	public String getTranscationOnStr() {
		return df.format(this.transcationOn);
	}

	public void setTranscationOnStr(String transcationOnStr) {
		this.transcationOnStr = transcationOnStr;
	}

	public String getTranscationId() {
		return transcationId;
	}

	public void setTranscationId(String transcationId) {
		this.transcationId = transcationId;
	}

	public static DateFormat getDf() {
		return df;
	}

	public static void setDf(DateFormat df) {
		OtherResponsibility.df = df;
	}
	

}

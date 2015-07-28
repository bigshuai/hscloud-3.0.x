package com.hisoft.hscloud.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "hc_user_brand")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserBrand extends AbstractEntity {

	private String code;

	private String description;

	private short status = (short) 1;
	@Column(name = "rebate_rate")
	private Integer rebateRate;// 返点率
	@Column(name = "gifts_discount_rate")
	private Integer giftsDiscountRate;// 礼金率
	@Column(name = "approval_or_not")
	//true 免审，false 需要审核
	private boolean approvalOrNot=false;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public Integer getRebateRate() {
		return rebateRate;
	}

	public void setRebateRate(Integer rebateRate) {
		this.rebateRate = rebateRate;
	}

	
	public boolean isApprovalOrNot() {
		return approvalOrNot;
	}

	public void setApprovalOrNot(boolean approvalOrNot) {
		this.approvalOrNot = approvalOrNot;
	}

	public Integer getGiftsDiscountRate() {
		return giftsDiscountRate;
	}

	public void setGiftsDiscountRate(Integer giftsDiscountRate) {
		this.giftsDiscountRate = giftsDiscountRate;
	}

	@Override
	public String toString() {
		return "UserBrand [code=" + code + ", description=" + description + ", status=" + status + ", rebateRate="
				+ rebateRate + ", giftsDiscountRate=" + giftsDiscountRate + ", approvalOrNot=" + approvalOrNot + "]";
	}

}
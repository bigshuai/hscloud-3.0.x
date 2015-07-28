package com.hisoft.hscloud.bss.billing.vo;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hisoft.hscloud.bss.billing.constant.TranscationType;
import com.hisoft.hscloud.common.entity.AbstractVO;

public class ApplicationTranscationLogVO {
	private int id;
	private String app_order_id;
	private String username;
	private String useremail;
	private Date createDate;
	private Date dealDate;
	private BigDecimal transaction_account;
	private BigDecimal balance;
	private String app_name;
	private boolean supplier;
	//0:先付款  1：后付费
	private int type;
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApp_order_id() {
		return app_order_id;
	}

	public void setApp_order_id(String app_order_id) {
		this.app_order_id = app_order_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getDealDate() {
		return dealDate;
	}

	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}

	public BigDecimal getTransaction_account() {
		return transaction_account;
	}

	public void setTransaction_account(BigDecimal transaction_account) {
		this.transaction_account = transaction_account;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public boolean isSupplier() {
		return supplier;
	}

	public void setSupplier(boolean supplier) {
		this.supplier = supplier;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}

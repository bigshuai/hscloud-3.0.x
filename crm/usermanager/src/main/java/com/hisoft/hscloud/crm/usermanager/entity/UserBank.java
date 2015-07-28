/**
 * 
 */
package com.hisoft.hscloud.crm.usermanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lihonglei
 *
 */
@Entity
@Table(name = "hc_user_bank")
public class UserBank {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "user_id", length = 20, nullable = false)
	private long userId;
	
	@Column(name = "bank", length = 255, nullable = false)
	private String bank;
	
	@Column(name = "account", length = 255, nullable = false)
	private String account;
	
	@Column(name = "payee", length = 255, nullable = false)
	private String payee;
	
	@Column(name = "remark", length = 64, nullable = true)
	private String remark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}
}

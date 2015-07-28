/* 
 * 文 件 名:  AlipayConfig.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  houyh 
 * 修改时间:  2012-12-19 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.payment.alipay.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.hisoft.hscloud.crm.usermanager.entity.Domain;

/**
 * <实体类用户存放支付宝支付所需参数> <功能详细描述>
 * 
 * @author houyh
 * @version [版本号, 2012-12-19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Entity
@Table(name = "hc_payment_config")
public class PaymentInterface {
	private long id;
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	private String partner;
	// 交易安全检验码，由数字和字母组成的32位字符串
	private String key;
	// 签约支付宝账号或卖家收款支付宝帐户
	private String sellerEmail;
	//创建日期
	private Date createDate=new Date();
	//更新日期
	private Date updateDate=new Date();
	//数据状态 0  删除  1正常
	private short dataStatus=(short)1;
	private Domain domain;
	@Id@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="partner",nullable=false)
	public String getPartner() {
		return partner;
	}
	
	public void setPartner(String partner) {
		this.partner = partner;
	}
	@Column(name="verify_key",nullable=false)
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	@Column(name="seller_email",nullable=false)
	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name="update_date")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Column(name="data_status")
	public short getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(short dataStatus) {
		this.dataStatus = dataStatus;
	}
	@OneToOne
	@JoinColumn(name="domain_id",unique=true)
	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}
	
}

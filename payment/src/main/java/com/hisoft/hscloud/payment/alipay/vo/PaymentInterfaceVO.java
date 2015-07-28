package com.hisoft.hscloud.payment.alipay.vo;

import com.hisoft.hscloud.crm.usermanager.entity.Domain;

public class PaymentInterfaceVO {
	private long id;
	private String partner;
	// 交易安全检验码，由数字和字母组成的32位字符串
	private String key;
	// 签约支付宝账号或卖家收款支付宝帐户
	private String sellerEmail;
	// 支付接口对应的分平台
	private Domain domain;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSellerEmail() {
		return sellerEmail;
	}

	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

}

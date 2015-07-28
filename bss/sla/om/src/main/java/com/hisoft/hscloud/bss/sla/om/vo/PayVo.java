/**
 * @title PayVo.java
 * @package com.hisoft.hscloud.bss.sla.om.vo
 * @description 用一句话描述该文件做什么
 * @author MaDai
 * @update 2012-4-8 下午1:58:18
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.om.vo;

import java.math.BigDecimal;

/**
 * @description 值对象，用于支付功能
 * @version 1.0
 * @author Madai
 * @update 2012-4-8 下午1:58:18
 */
public class PayVo {
	private Long id;
	private String orderNo;
	private BigDecimal price;
	private BigDecimal accountAmount;

	public PayVo() {
		super();
	}

	public PayVo(Long id, String orderNo, BigDecimal price,
			BigDecimal accountAmount) {
		super();
		this.id = id;
		this.orderNo = orderNo;
		this.price = price;
		this.accountAmount = accountAmount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(BigDecimal accountAmount) {
		this.accountAmount = accountAmount;
	}
}

/**
 * @title OrderVo.java
 * @package com.hisoft.hscloud.bss.billing.vo
 * @description 用一句话描述该文件做什么
 * @author MaDai
 * @update 2012-5-17 上午10:43:22
 * @version V1.1
 */
package com.hisoft.hscloud.bss.billing.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description 传递订单内容
 * @version 1.1
 * @author Madai
 * @update 2012-5-17 上午10:43:22
 */
public class OrderVo {
	private Long orderItemId;
	private BigDecimal price;
	private Long ruleId;
	private Long accountId;
	private Date startDate;
	private Date endDate;
	private BigDecimal adjustAmount;
	private boolean adjustAccount;
	private List<OrderServiceVo> services = new ArrayList<OrderServiceVo>();

	public void addOrderServiceVo(BigDecimal price, Long serviceId, int times) {
		OrderServiceVo service = new OrderServiceVo();
		service.setPrice(price);
		service.setServiceId(serviceId);
		service.setTimes(times);
		services.add(service);
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isAdjustAccount() {
		return adjustAccount;
	}

	public void setAdjustAccount(boolean adjustAccount) {
		this.adjustAccount = adjustAccount;
	}

	public BigDecimal getAdjustAmount() {
		return adjustAmount;
	}

	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}

	public List<OrderServiceVo> getServices() {
		return services;
	}

	public void setServices(List<OrderServiceVo> services) {
		this.services = services;
	}
}


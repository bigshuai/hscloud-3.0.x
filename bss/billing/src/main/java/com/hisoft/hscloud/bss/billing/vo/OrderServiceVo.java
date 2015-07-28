/**
 * @title OrderServiceVo.java
 * @package com.hisoft.hscloud.bss.billing.vo
 * @description 用一句话描述该文件做什么
 * @author MaDai
 * @update 2012-5-17 上午11:17:18
 * @version V1.1
 */
package com.hisoft.hscloud.bss.billing.vo;

import java.math.BigDecimal;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.1
 * @author Madai
 * @update 2012-5-17 上午11:17:18
 */
public class OrderServiceVo {
	private BigDecimal price;
	private Long serviceId;
	private int times;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
}

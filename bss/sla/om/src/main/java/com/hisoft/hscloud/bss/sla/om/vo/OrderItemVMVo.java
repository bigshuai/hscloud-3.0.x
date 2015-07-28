package com.hisoft.hscloud.bss.sla.om.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.hisoft.hscloud.bss.sla.om.util.OrderUtils;

public class OrderItemVMVo {
	private Long orderId;
	private Long orderItemId;
	private Long referenceId;
	private String vmName;
	private BigDecimal totalPrice;
	private BigDecimal orderItemPrice;
	private String serviceCatalogName;
	private Date effectiveDate;
	private String usedTime;
	public String getUsedTime() {
		Date now=new Date();
		if(effectiveDate==null||now.before(effectiveDate)){
			return "0";
		}else{
			long days=OrderUtils.getDaysBetweenDateByHour(now,effectiveDate);
			return String.valueOf(days);
		}
	}

	public void setUsedTime(String usedTime) {
		this.usedTime=usedTime;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getOrderItemPrice() {
		return orderItemPrice;
	}

	public void setOrderItemPrice(BigDecimal orderItemPrice) {
		this.orderItemPrice = orderItemPrice;
	}


	public String getServiceCatalogName() {
		return serviceCatalogName;
	}

	public void setServiceCatalogName(String serviceCatalogName) {
		this.serviceCatalogName = serviceCatalogName;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

}

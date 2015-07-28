/**
 * @title InstanceDetailVo.java
 * @package com.hisoft.hscloud.vpdc.ops.vo
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-4-5 下午3:00:13
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author haibin.ding
 * @update 2012-8-27 下午3:00:13
 */
public class InstanceDetail_ServiceCatalogVo {
	private String orderItemId;
	private String serviceCatalogName;
	private Date effectiveDate;
	private Date expirationDate;
	private Date orderDate;
	private String orderNo;
	private BigDecimal price;
	private int period;
	private String priceType;
	private String periodType;
	private String priceTypeName;
	
	/**
	 * @return orderItemId : return the property orderItemId.
	 */
	public String getOrderItemId() {
		return orderItemId;
	}
	/**
	 * @param orderItemId : set the property orderItemId.
	 */
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	/**
	 * @return serviceCatalogName : return the property serviceCatalogName.
	 */
	public String getServiceCatalogName() {
		return serviceCatalogName;
	}
	/**
	 * @param serviceCatalogName : set the property serviceCatalogName.
	 */
	public void setServiceCatalogName(String serviceCatalogName) {
		this.serviceCatalogName = serviceCatalogName;
	}
	/**
	 * @return effectiveDate : return the property effectiveDate.
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate : set the property effectiveDate.
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	/**
	 * @return orderNo : return the property orderNo.
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * @param orderNo : set the property orderNo.
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * @return orderDate : return the property orderDate.
	 */
	public Date getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate : set the property orderDate.
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public String getPriceType() {
		return priceType;
	}
	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	
	public String getPeriodType() {
		return periodType;
	}
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
	public String getPriceTypeName() {
		return priceTypeName;
	}
	public void setPriceTypeName(String priceTypeName) {
		this.priceTypeName = priceTypeName;
	}
	/**
	 * @return price : return the property price.
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price : set the property price.
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}

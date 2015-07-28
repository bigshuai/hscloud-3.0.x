package com.hisoft.hscloud.bss.sla.om.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.hisoft.hscloud.bss.sla.om.entity.Order.Status;

public class TryOrderVo {
	private Long id;
	private String orderNo;
	private Status status;
	private Date createDate;
	private Date payDate;
	private Date updateDate;
	private String serviceCatalogName;
	private BigDecimal itemPrice;
	private String userName;
	private Date itemEffectiveDate;
	private Date itemExpirationDate;
	private Long orderItemId;
	private int tryDuration;
	private String priceType;
	private String pricePeriodType;
	private String pricePeriod;

	public TryOrderVo(Long id, String orderNo, Status status, Date createDate,
			Date payDate, Date updateDate, Date itemEffectiveDate,
			Date itemExpirationDate, Long orderItemId, int tryDuration,
			String userName, String serviceCatalogName, BigDecimal itemPrice,String priceType,String pricePeriodType,String pricePeriod) {
		this.id = id;
		this.orderNo = orderNo;
		this.status = status;
		this.createDate = createDate;
		this.payDate = payDate;
		this.updateDate = updateDate;
		this.itemEffectiveDate = itemEffectiveDate;
		this.itemExpirationDate = itemExpirationDate;
		this.orderItemId = orderItemId;
		this.tryDuration = tryDuration;
		this.userName = userName;
		this.serviceCatalogName=serviceCatalogName;
		this.itemPrice=itemPrice;
		this.pricePeriod=pricePeriod;
		this.pricePeriodType=pricePeriodType;
		this.priceType=priceType;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getItemEffectiveDate() {
		return itemEffectiveDate;
	}

	public void setItemEffectiveDate(Date itemEffectiveDate) {
		this.itemEffectiveDate = itemEffectiveDate;
	}

	public Date getItemExpirationDate() {
		return itemExpirationDate;
	}

	public void setItemExpirationDate(Date itemExpirationDate) {
		this.itemExpirationDate = itemExpirationDate;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getTryDuration() {
		return tryDuration;
	}

	public void setTryDuration(int tryDuration) {
		this.tryDuration = tryDuration;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getServiceCatalogName() {
		return serviceCatalogName;
	}

	public void setServiceCatalogName(String serviceCatalogName) {
		this.serviceCatalogName = serviceCatalogName;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getPricePeriodType() {
		return pricePeriodType;
	}

	public void setPricePeriodType(String pricePeriodType) {
		this.pricePeriodType = pricePeriodType;
	}

	public String getPricePeriod() {
		return pricePeriod;
	}

	public void setPricePeriod(String pricePeriod) {
		this.pricePeriod = pricePeriod;
	}
	
}

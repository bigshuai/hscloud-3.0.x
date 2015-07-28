package com.hisoft.hscloud.web.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.hisoft.hscloud.bss.sla.om.entity.OrderItemGoodsVM;

public class OrderItemTemp {
	private Long id;
	private String serviceCatalogId;// 套餐名
	private String serviceCatalogName;// 套餐名
	private BigDecimal amount;
	private BigDecimal price;
	private int quantity;
	private String imageId;
	private String flavorId;
	private String nodeName;
	private String priceType;
	private String pricePeriod;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getServiceCatalogId() {
		return serviceCatalogId;
	}
	public void setServiceCatalogId(String serviceCatalogId) {
		this.serviceCatalogId = serviceCatalogId;
	}
	public String getServiceCatalogName() {
		return serviceCatalogName;
	}
	public void setServiceCatalogName(String serviceCatalogName) {
		this.serviceCatalogName = serviceCatalogName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getFlavorId() {
		return flavorId;
	}
	public void setFlavorId(String flavorId) {
		this.flavorId = flavorId;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getPriceType() {
		return priceType;
	}
	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	public String getPricePeriod() {
		return pricePeriod;
	}
	public void setPricePeriod(String pricePeriod) {
		this.pricePeriod = pricePeriod;
	}
	public String getPricePeriodType() {
		return pricePeriodType;
	}
	public void setPricePeriodType(String pricePeriodType) {
		this.pricePeriodType = pricePeriodType;
	}
	public String getServiceDesc() {
		return serviceDesc;
	}
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public boolean isTryOrNo() {
		return tryOrNo;
	}
	public void setTryOrNo(boolean tryOrNo) {
		this.tryOrNo = tryOrNo;
	}
	public int getTryDuration() {
		return tryDuration;
	}
	public void setTryDuration(int tryDuration) {
		this.tryDuration = tryDuration;
	}
	public OrderItemGoodsVM getVmGoods() {
		return vmGoods;
	}
	public void setVmGoods(OrderItemGoodsVM vmGoods) {
		this.vmGoods = vmGoods;
	}
	private String pricePeriodType;
	private String serviceDesc;
	private Date effectiveDate;
	private Date expirationDate;
	private boolean tryOrNo;
	private int tryDuration;
	private OrderItemGoodsVM vmGoods;
}

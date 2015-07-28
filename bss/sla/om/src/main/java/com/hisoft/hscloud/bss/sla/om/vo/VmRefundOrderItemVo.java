package com.hisoft.hscloud.bss.sla.om.vo; 

import java.math.BigDecimal;
import java.util.Date;

public class VmRefundOrderItemVo {
    private String orderNo ;//订单编号
    private String totalPriceDetail;//订单总额详情
    private BigDecimal totalPrice;//订单总额
    private BigDecimal amount;//订单项金额
    private BigDecimal usedAmount;//已经发生金额
    private BigDecimal unUsedAmount;//未发生金额
    private String refundMarking;//退款备注
    private String period;//购买时长
    private Date effectiveDate;//生效时间
    private Date expirationDate;//到期时间
    private BigDecimal totalPointAmount;
    private BigDecimal totalAmount;
    private BigDecimal pointAmount;
    private BigDecimal usedPointAmount;
    private BigDecimal unUsedPointAmount;
    private BigDecimal totalGiftAmount;
    private BigDecimal giftAmount;
    private BigDecimal usedGiftAmount;
    private BigDecimal unUsedGiftAmount;
    
    
	public BigDecimal getTotalPointAmount() {
		return totalPointAmount;
	}
	public void setTotalPointAmount(BigDecimal totalPointAmount) {
		this.totalPointAmount = totalPointAmount;
	}
	public BigDecimal getUsedPointAmount() {
		return usedPointAmount;
	}
	public void setUsedPointAmount(BigDecimal usedPointAmount) {
		this.usedPointAmount = usedPointAmount;
	}
	public BigDecimal getUnUsedPointAmount() {
		return unUsedPointAmount;
	}
	public void setUnUsedPointAmount(BigDecimal unUsedPointAmount) {
		this.unUsedPointAmount = unUsedPointAmount;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getTotalPriceDetail() {
		return totalPriceDetail;
	}
	public void setTotalPriceDetail(String totalPriceDetail) {
		this.totalPriceDetail = totalPriceDetail;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getUsedAmount() {
		return usedAmount;
	}
	public void setUsedAmount(BigDecimal usedAmount) {
		this.usedAmount = usedAmount;
	}
	public BigDecimal getUnUsedAmount() {
		return unUsedAmount;
	}
	public void setUnUsedAmount(BigDecimal unUsedAmount) {
		this.unUsedAmount = unUsedAmount;
	}
	public String getRefundMarking() {
		return refundMarking;
	}
	public void setRefundMarking(String refundMarking) {
		this.refundMarking = refundMarking;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
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
	public BigDecimal getPointAmount() {
		return pointAmount;
	}
	public void setPointAmount(BigDecimal pointAmount) {
		this.pointAmount = pointAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getTotalGiftAmount() {
		return totalGiftAmount;
	}
	public void setTotalGiftAmount(BigDecimal totalGiftAmount) {
		this.totalGiftAmount = totalGiftAmount;
	}
	public BigDecimal getGiftAmount() {
		return giftAmount;
	}
	public void setGiftAmount(BigDecimal giftAmount) {
		this.giftAmount = giftAmount;
	}
	public BigDecimal getUsedGiftAmount() {
		return usedGiftAmount;
	}
	public void setUsedGiftAmount(BigDecimal usedGiftAmount) {
		this.usedGiftAmount = usedGiftAmount;
	}
	public BigDecimal getUnUsedGiftAmount() {
		return unUsedGiftAmount;
	}
	public void setUnUsedGiftAmount(BigDecimal unUsedGiftAmount) {
		this.unUsedGiftAmount = unUsedGiftAmount;
	}
    
    
}

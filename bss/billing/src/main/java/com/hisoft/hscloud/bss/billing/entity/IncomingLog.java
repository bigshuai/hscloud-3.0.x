package com.hisoft.hscloud.bss.billing.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hc_incoming_log")
public class IncomingLog {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(name = "transaction_id")
	private Long transactionId;// 交易日志id
	@Column(name = "transaction_type")
	private Short transcationType;// 交易日志类型
	@Column(name = "product_type")
	private Short productType;// 1，云主机/2，快递费/3，其他
	@Column(name = "object_id")
	private Long objectId;// 与productType对应的物理id
	private BigDecimal amount;// 当前消费金额
	@Column(name = "day_price")
	private BigDecimal dayPrice;// 平均到每一天的金额
	@Column(name = "incoming_type")
	private Short incomingType;// 1,权责消费2，一次消费
	@Column(name = "effective_date")
	private Date effectiveDate;// 生效日期--productType不为1的情况下为当前日期且与expirationDate值相同
	@Column(name = "expiration_date")
	private Date expirationDate;// 失效日志--productType不为1的情况下为当前日期且与effectiveDate值相同
	@Column(name = "order_id")
	private Long orderId;
	private String orderNo;
	@Column(name = "order_item_id")
	private Long orderItemId;
	private String email;
	@Column(name = "account_id")
	private Long accountId;
	@Column(name="sc_name")
	private String scName;
	@Column(name="sc_id")
	private Integer scId;
	@Column(name="domain_id")
	private Long domainId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Short getTranscationType() {
		return transcationType;
	}

	public void setTranscationType(Short transcationType) {
		this.transcationType = transcationType;
	}

	public Short getProductType() {
		return productType;
	}

	public void setProductType(Short productType) {
		this.productType = productType;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getDayPrice() {
		return dayPrice;
	}

	public void setDayPrice(BigDecimal dayPrice) {
		this.dayPrice = dayPrice;
	}

	public Short getIncomingType() {
		return incomingType;
	}

	public void setIncomingType(Short incomingType) {
		this.incomingType = incomingType;
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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getScName() {
		return scName;
	}

	public void setScName(String scName) {
		this.scName = scName;
	}

	public Integer getScId() {
		return scId;
	}

	public void setScId(Integer scId) {
		this.scId = scId;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}
	
}

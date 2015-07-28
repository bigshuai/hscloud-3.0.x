package com.hisoft.hscloud.bss.billing.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 月报表 amount=finishedIncoming+currentIncoming+noneventIncoming
 * 
 * @author Minggang
 * 
 */
@Entity
@Table(name = "hc_incoming_statis")
public class MonthIncoming  {
    
    // 主健
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(name = "incoming_log_id", nullable = false)
    private long incomingLogId;
    
	@Column(nullable = false)
	private String month; // 月份
	
	@Column(nullable = false)
	private String year;
	
	//1:退款   0:消费
	@Column(nullable = false)
	private int type;
	
	//1:referenceId, 2:快递,3:其他
	@Column(name = "product_type", nullable = false)
	private Byte productType; // 产品类型

	@Column(name = "object_id")
	private Long objectId;

	@Column(nullable = false)
	private BigDecimal amount; // 总金额

	@Column(name = "finished_incoming", nullable = false)
	private BigDecimal finishedIncoming; // 已消费金额

	@Column(name = "current_incoming", nullable = false)
	private BigDecimal currentIncoming; // 当前消费金额

	@Column(name = "nonevent_incoming", nullable = false)
	private BigDecimal noneventIncoming; // 未消费金额

	@Column(name = "account_id", nullable = false)
	private Long accountId; // 消费账户id

	@Column(nullable = false)
	private String email; // 邮箱
	
	@Column(name = "order_item_id")
    private Long orderItemId;
	
	@Column(name = "remark", nullable = true)
    private String remark;
	
	// 创建时间
    @Column(name = "create_date", nullable = false)
    private Date createDate = new Date();
    
    @Column(name="domain_id")
    private Long domainId;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Byte getProductType() {
		return productType;
	}

	public void setProductType(Byte productType) {
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

	public BigDecimal getFinishedIncoming() {
		return finishedIncoming;
	}

	public void setFinishedIncoming(BigDecimal finishedIncoming) {
		this.finishedIncoming = finishedIncoming;
	}

	public BigDecimal getCurrentIncoming() {
		return currentIncoming;
	}

	public void setCurrentIncoming(BigDecimal currentIncoming) {
		this.currentIncoming = currentIncoming;
	}

	public BigDecimal getNoneventIncoming() {
		return noneventIncoming;
	}

	public void setNoneventIncoming(BigDecimal noneventIncoming) {
		this.noneventIncoming = noneventIncoming;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public long getIncomingLogId() {
        return incomingLogId;
    }

    public void setIncomingLogId(long incomingLogId) {
        this.incomingLogId = incomingLogId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }
}

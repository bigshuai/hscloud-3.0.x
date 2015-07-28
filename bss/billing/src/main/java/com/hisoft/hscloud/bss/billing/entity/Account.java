package com.hisoft.hscloud.bss.billing.entity;

/**
 * @title Account.java
 * @package com.hisoft.hscloud.crm.usermanager.entity
 * @description 用一句话描述该文件做什么
 * @author guole.liang
 * @update 2012-3-30 下午2:42:59
 * @version V1.0
 */
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import com.hisoft.hscloud.common.entity.AbstractEntity;
import com.hisoft.hscloud.crm.usermanager.entity.User;

/**
 * @description Account 实体 
 * @version 1.0
 * @author guole.liang
 * @update 2012-3-30 下午2:42:59
 */
@Entity
@Table(name="hc_account",uniqueConstraints={@UniqueConstraint(columnNames={"user_id"})})
public class Account extends AbstractEntity{
	
	private BigDecimal balance;
	
	private BigDecimal coupons;
	
	private BigDecimal giftsBalance;//礼金账户
	
	private Integer giftsRebateRate;// 返点率
	
	private Integer couponsRebateRate;// 返点率
	
//	@Column(name="user_id")
//	private long userId;

	@OneToOne()
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
//	public long getUserId() {
//		return userId;
//	}
//	public void setUserId(long userId) {
//		this.userId = userId;
//	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
    public BigDecimal getCoupons() {
		return coupons;
	}
    
	public void setCoupons(BigDecimal coupons) {
		this.coupons = coupons;
	}
	
	@Transient
	public String getAccountBalance(){
		return this.balance.toString();
	}
	
	public String getCouponsBalance(){
		return this.coupons.toString();
	}
	
	@Transient  
	public String getCouponsRebateRateStr(){
		return this.couponsRebateRate.toString();
	}
	
	
	public BigDecimal getGiftsBalance() {
		return giftsBalance;
	}
	
	public void setGiftsBalance(BigDecimal giftsBalance) {
		this.giftsBalance = giftsBalance;
	}
	
	@Transient  
	public String getGiftsBalanceStr(){
		return this.giftsBalance.toString();
	}
	
	public Integer getGiftsRebateRate() {
		return giftsRebateRate;
	}
	
	@Transient  
	public String getGiftsRebateRateStr(){
		return this.giftsRebateRate.toString();
	}
	
	public void setGiftsRebateRate(Integer giftsRebateRate) {
		this.giftsRebateRate = giftsRebateRate;
	}
	
	public Integer getCouponsRebateRate() {
		return couponsRebateRate;
	}
	
	public void setCouponsRebateRate(Integer couponsRebateRate) {
		this.couponsRebateRate = couponsRebateRate;
	}
	
}

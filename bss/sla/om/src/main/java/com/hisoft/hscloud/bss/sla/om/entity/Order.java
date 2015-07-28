/**
 * @title Order.java
 * @package com.hisoft.hscloud.bss.sla.om.entity
 * @description 订单实体
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.om.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.hisoft.hscloud.bss.sla.om.util.Constants;
import com.hisoft.hscloud.bss.sla.om.util.OrderUtils;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.bss.sla.om.service.*;

/**
 * @description 订单实体
 * @version 1.0
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 */
@Entity
@Table(name = "hc_order")
public class Order {
	private Long id;
	private String orderNo;
	private int quantity;
	private BigDecimal totalPrice;
	private Status status;
	private Date createDate;
	private Date payDate;
	private Date updateDate;
	private Date expireDate;
	private String remark;//试用订单转正，延期等操作备注
	private List<OrderItem> items;
	private User user;
	private BigDecimal totalAmount;
    private BigDecimal totalPointAmount;
    private BigDecimal totalGiftAmount;
	private Integer rebateRate;//返点率
	private Integer giftsRebateRate;
	private short orderType;//订单类型 1.虚拟机按套餐购买   2.虚拟机按需购买   3.路由按需购买
	public enum Status {
		Unpaid, Paid, Cancelled, TryWaitApprove,Try,DelayWaitApprove,WaitNormalApprove,TryOver,TryDelay,All;
		public static Status getOrderStatusByStr(String str){
	        return valueOf(str);
	    }
		public static Status getItem(short index){
			for (Status status:Status.values()) {
				if(status.ordinal()==index){
					return status;
				}
			}
			return Status.All;
			
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false, unique = true)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Enumerated
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

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
    @Column(name="rebate_rate")
	public Integer getRebateRate() {
		return rebateRate;
	}

	public void setRebateRate(Integer rebateRate) {
		this.rebateRate = rebateRate;
	}

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	@ManyToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Column(length=500)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name="pay_date")
	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	@Column(name="total_point_amount")
	public BigDecimal getTotalPointAmount() {
		return totalPointAmount;
	}

	public void setTotalPointAmount(BigDecimal totalPointAmount) {
		this.totalPointAmount = totalPointAmount;
	}
    @Column(name="total_amount")
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
    @Column(name="total_gift_amount")
	public BigDecimal getTotalGiftAmount() {
		return totalGiftAmount;
	}

	public void setTotalGiftAmount(BigDecimal totalGiftAmount) {
		this.totalGiftAmount = totalGiftAmount;
	}
	@Column(name="gifts_rebate_rate")
	public Integer getGiftsRebateRate() {
		return giftsRebateRate;
	}

	public void setGiftsRebateRate(Integer giftsRebateRate) {
		this.giftsRebateRate = giftsRebateRate;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	@Column(name="order_type")
	public short getOrderType() {
		return orderType;
	}

	public void setOrderType(short orderType) {
		this.orderType = orderType;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderNo=" + orderNo + ", totalPrice="
				+ totalPrice + ", status=" + status + ", createDate="
				+ createDate + ", updateDate=" + updateDate + ", items="
				+ items + ", user=" + user + "]";
	}
	
	/**
	 * <一句话功能简述> 
	* <功能详细描述> 
	* @param orderItems
	* @param user 
	* @see [类、类#方法、类#成员]
	 */
	@Transient
	public void submit(List<OrderItem> orderItems,
			User user,String orderStatus,Integer rebateRate,Integer giftRebateRate)throws Exception {
		Date now =new Date();
		this.items = orderItems;
		if(user!=null){
			this.user = user;
		}else{
			throw new Exception("when submit order can't find user information");
		}
		//Session session =this.getSessionFactory().openSession();
		this.rebateRate=rebateRate;
		this.giftsRebateRate=giftRebateRate;
		//this.orderNo = generateOrderNo(user);
		OrderUtils orderUtils  = new OrderUtils();
		this.orderNo = orderUtils.generateOrderNo(user);
		this.status = Status.getOrderStatusByStr(orderStatus);
		this.createDate = now;
		this.updateDate = now;
		if("Paid".equals(orderStatus)){
			this.payDate=now;
		}
		this.expireDate = DateUtils.addDays(now, Constants.DELAY_DAYS);

		BigDecimal totalAmount = new BigDecimal(0);
		BigDecimal totalPointPrice=new BigDecimal(0);
		BigDecimal totalPrice=new BigDecimal(0);
		int quantity = 0;
		for (OrderItem item : items) {
			totalAmount = totalAmount.add(item.getAmount());
			totalPrice = totalPrice.add(item.getPrice());
			if(item.getPointAmount()!=null){
				totalPointPrice=totalPointPrice.add(item.getPointAmount());
			}
			quantity += item.getQuantity();
			item.setOrder(this);
		}
		this.quantity = quantity;
		this.totalPointAmount=totalPointPrice;
		this.totalAmount = totalAmount;
		this.totalPrice=totalPrice;
	}
	
	/**
	 * <一句话功能简述> 
	* <功能详细描述> 
	* @param orderItems
	* @param user 
	* @see [类、类#方法、类#成员]
	 */
	@Transient
	public void submitWithoutOrderNo(List<OrderItem> orderItems,
			User user,String orderStatus,Integer rebateRate,Integer giftRebateRate)throws Exception {
		Date now =new Date();
		this.items = orderItems;
		if(user!=null){
			this.user = user;
		}else{
			throw new Exception("when submit order can't find user information");
		}
		//Session session =this.getSessionFactory().openSession();
		this.rebateRate=rebateRate;
		this.giftsRebateRate=giftRebateRate;
		//this.orderNo = generateOrderNo(user);
		this.status = Status.getOrderStatusByStr(orderStatus);
		this.createDate = now;
		this.updateDate = now;
		if("Paid".equals(orderStatus)){
			this.payDate=now;
		}
		this.expireDate = DateUtils.addDays(now, Constants.DELAY_DAYS);

		BigDecimal totalAmount = new BigDecimal(0);
		BigDecimal totalPointPrice=new BigDecimal(0);
		BigDecimal totalPrice=new BigDecimal(0);
		int quantity = 0;
		for (OrderItem item : items) {
			totalAmount = totalAmount.add(item.getAmount());
			totalPrice = totalPrice.add(item.getPrice());
			if(item.getPointAmount()!=null){
				totalPointPrice=totalPointPrice.add(item.getPointAmount());
			}
			quantity += item.getQuantity();
			item.setOrder(this);
		}
		this.quantity = quantity;
		this.totalPointAmount=totalPointPrice;
		this.totalAmount = totalAmount;
		this.totalPrice=totalPrice;
	}
	/**
	 * <一句话功能简述> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	@Transient
	private String generateOrderNo(User user) {
		
		String orderHead = "P";
		long domainId = user.getDomain().getId();
		long date=System.currentTimeMillis();
		return orderHead + domainId + date 
//		return RandomStringUtils.randomAlphanumeric(6)
				+ System.currentTimeMillis();
	}


}

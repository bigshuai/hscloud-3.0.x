/**
 * @title OrderItem.java
 * @package com.hisoft.hscloud.bss.sla.om.entity
 * @description 订单实体
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.om.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.hisoft.hscloud.bss.sla.om.vo.OrderItemVo;

/**
 * @description 订单项实体
 * @version 1.0
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 */
@Entity
@Table(name = "hc_order_item")
public class OrderItem {
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
	private String pricePeriodType;
	private String serviceDesc;
	private String vpdcName;
	private Date effectiveDate;
	private BigDecimal pointAmount;
    private boolean usePointOrNot;//是否使用点劵 true 使用 ;false 不使用
    private boolean useGiftOrNot;
    private BigDecimal giftAmount;
	private Date expirationDate;
	private Order order;
	private boolean tryOrNo;
	private int tryDuration;
	private OrderItemGoodsVM vmGoods;
	private List<OrderProduct> orderProducts;
	private String planId;
	private Long vpdcId;//当前云主机所属的vpdc_id
	private String extColumn;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	@ManyToOne
	@JsonIgnore
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	@OneToOne(cascade = CascadeType.ALL)
	public OrderItemGoodsVM getVmGoods() {
		return vmGoods;
	}

	public void setVmGoods(OrderItemGoodsVM vmGoods) {
		this.vmGoods = vmGoods;
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
	@Column(name="effective_date")
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	@Column(name="expiration_date")
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	@Column(name="try_or_no")
	public boolean getTryOrNo() {
		return tryOrNo;
	}

	public void setTryOrNo(boolean tryOrNo) {
		this.tryOrNo = tryOrNo;
	}
	@Column(name="try_duration")
	public int getTryDuration() {
		return tryDuration;
	}

	public void setTryDuration(int tryDuration) {
		this.tryDuration = tryDuration;
	}
	
	@Column(name="node_name")
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	@Column(name="point_amount")
	public BigDecimal getPointAmount() {
		return pointAmount;
	}

	public void setPointAmount(BigDecimal pointAmount) {
		this.pointAmount = pointAmount;
	}
	
    @Column(name="use_point_or_not")
	public boolean getUsePointOrNot() {
		return usePointOrNot;
	}

	public void setUsePointOrNot(boolean usePointOrNot) {
		this.usePointOrNot = usePointOrNot;
	}
	@OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL)
	public List<OrderProduct> getOrderProducts() {
		return orderProducts;
	}
	
	public void setOrderProducts(List<OrderProduct> orderProducts) {
		for(OrderProduct op:orderProducts){
			op.setOrderItem(this);
		}
		this.orderProducts = orderProducts;
	}

	@Column(name="plan_id")
	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}
	
	
	@Column(name="vpdc_name")
	public String getVpdcName() {
		return vpdcName;
	}
	
	public void setVpdcName(String vpdcName) {
		this.vpdcName = vpdcName;
	}
	@Column(name="ext_column")
	public String getExtColumn() {
		return extColumn;
	}

	public void setExtColumn(String extColumn) {
		this.extColumn = extColumn;
	}
	
	@Column(name="use_gift_or_not")
	public boolean getUseGiftOrNot() {
		return useGiftOrNot;
	}

	public void setUseGiftOrNot(boolean useGiftOrNot) {
		this.useGiftOrNot = useGiftOrNot;
	}
	@Column(name="gift_amount")
	public BigDecimal getGiftAmount() {
		return giftAmount;
	}

	public void setGiftAmount(BigDecimal giftAmount) {
		this.giftAmount = giftAmount;
	}
	@Column(name="vpdc_id")
	public Long getVpdcId() {
		return vpdcId;
	}

	public void setVpdcId(Long vpdcId) {
		this.vpdcId = vpdcId;
	}

	@Transient
	public void submitItem(OrderItemVo vo) {
		OrderItemGoodsVM vmGoods = new OrderItemGoodsVM();
		vmGoods.submitVm(vo);
		this.vmGoods = vmGoods;
		this.serviceCatalogName = vo.getServiceCatalogName();
		this.serviceCatalogId = vo.getServiceCatalogId();
		this.amount = vo.getPrice();
		this.price=vo.getPrice();
		this.usePointOrNot=vo.getUsePointOrNot();
		this.useGiftOrNot=vo.getUseGiftOrNot();
		this.priceType = vo.getPriceType();
		this.pricePeriod = vo.getPricePeriod();
		this.pricePeriodType = vo.getPricePeriodType();
		this.serviceDesc = vo.getServiceDesc();
		this.quantity = 1;
		this.nodeName=vo.getNodeName();
		this.tryOrNo=vo.getTryOrNo();
		this.tryDuration=vo.getTryDuration();
		this.flavorId =vo.getFlavorId();
		this.imageId = vo.getImageId();
	}
	@Transient
	public void copyItem(OrderItem item,int orderType) throws Exception {
		if(item.getPointAmount()==null){
			item.setPointAmount(BigDecimal.ZERO);
		}
		String[] ignoreProperties=null;
		List<String> ignorePropertiesList=new ArrayList<String>();

		if(item.getEffectiveDate()==null){
			ignorePropertiesList.add("effectiveDate");
		}
		if(item.getExpirationDate()==null){
			ignorePropertiesList.add("expirationDate");
		}
		if(orderType==1){
			ignorePropertiesList.add("orderProducts");
		}
		ignoreProperties=ignorePropertiesList.toArray(new String[ignorePropertiesList.size()]);
		BeanUtils.copyProperties(item,this,ignoreProperties);		
		if(orderType==1){
			OrderItemGoodsVM vmGoods = new OrderItemGoodsVM();
			vmGoods.copyVm(item.getVmGoods());
			this.vmGoods = vmGoods;
		}else if(orderType==2){
			List<OrderProduct> oldProducts=item.getOrderProducts();
			List<OrderProduct> newProducts=new ArrayList<OrderProduct>();
			for(OrderProduct product:oldProducts){
				OrderProduct op=new OrderProduct();
				op.copyVm(product);
				op.setOrderItem(this);
				newProducts.add(op);
			}
			this.orderProducts=newProducts;
		}
		
		this.id = null;
	}
}

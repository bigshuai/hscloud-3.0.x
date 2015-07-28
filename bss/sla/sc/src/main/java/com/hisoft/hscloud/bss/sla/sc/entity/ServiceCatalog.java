/**
 * @author jiaquan.hu
 */

package com.hisoft.hscloud.bss.sla.sc.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.hisoft.hscloud.common.entity.UserBrand;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;

/**
* @description service catalog的实体类
* @version 1.0
* @author jiaquan.hu
* @update 2012-3-31 上午9:59:01
*/
@Entity
@Table(name = "hc_service_catalog")
public class ServiceCatalog {
	//物理主键
	private int id;
	//套餐名称
	private String name;
	//套餐描述
	private String description;
	//套餐对应的openstack配置flavor
	private int flavorId;
	//套餐生效日期
	private Date effectiveDate;
	//套餐失效日期
	private Date expirationDate;
	//创建日期
	private Date createDate;
	//更新日期
	private Date updateDate;
	//套餐状态 0 未审批 1 已审批 2 停用 3仅试用
	private int status;
	//0 ：试用时长不计入订单 1：试用时长计入订单
	private boolean tryTimeAddOrNo=false;
	//是否是试用套餐
	private boolean tryOrNo=false;
	//是否免审
	private boolean tryApproveOrNo=false;
	//试用时长
	private int tryDuration=0;
	//创建用户
	private String createUser;
	//备注信息
	private String comment;
	//发布节点名称
	private String nodeName;
	//套餐对应的资源列表
	private List<ServiceItem> items;
	//套餐对应的计费规则
	private List<ScFeeType> feeTypes;
	
	//private String userLevel;
	//套餐对应的品牌列表
	//@JsonIgnore
	private List<UserBrand> userBrand;
	//套餐对应的品牌分平台
	//@JsonIgnore
	private List<Domain> domainList;
	
	private List<ServerZone> zoneList;
	
	private boolean usePointOrNot;
	
	private boolean useGiftOrNot;
	
	private ScIsolationConfig scIsolationConfig;//资源隔离配置
	
	private String catalogCode;//套餐code
	
	//wgsg 是否是推荐商品
	private boolean recommendedProduct=false;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length = 10)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(length = 50, nullable = false,unique=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 200)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name="flavor_id",length=10)
	public int getFlavorId() {
		return flavorId;
	}

	public void setFlavorId(int flavorId) {
		this.flavorId = flavorId;
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
	@Column(name="create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name="update_date")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Column(length=2)
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	@Column(name="create_user",length=20)
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(length=100)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="hc_service_catalog_item",
	joinColumns=@JoinColumn(name="service_catalog_id",referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="item_id",referencedColumnName="item_id"))
	public List<ServiceItem> getItems() {
		return items;
	}
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="hc_catalog_zone",joinColumns=@JoinColumn(name="service_catalog_id",referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="zone_id",referencedColumnName="id"))
	public List<ServerZone> getZoneList() {
		return zoneList;
	}

	public void setZoneList(List<ServerZone> zoneList) {
		this.zoneList = zoneList;
	}

	public void setItems(List<ServiceItem> items) {
		this.items = items;
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
	/*@Column(name="user_level")
	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}*/
	
	@OneToMany(mappedBy = "sc", cascade = CascadeType.ALL)
	public List<ScFeeType> getFeeTypes() {
		return feeTypes;
	}

	public void setFeeTypes(List<ScFeeType> feeTypes) {
		for(ScFeeType feeType:feeTypes){
			feeType.setSc(this);
		}
		this.feeTypes = feeTypes;
	}

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="hc_catalog_brand",
	joinColumns=@JoinColumn(name="service_catalog_id",referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="brand_id",referencedColumnName="id"))
	public List<UserBrand> getUserBrand() {
		return userBrand;
	}

	public void setUserBrand(List<UserBrand> userBrand) {
		this.userBrand = userBrand;
	}
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="hc_catalog_domain",
	joinColumns=@JoinColumn(name="service_catalog_id",referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="domain_id",referencedColumnName="id"))
	public List<Domain> getDomainList() {
		return domainList;
	}

	public void setDomainList(List<Domain> domainList) {
		this.domainList = domainList;
	}
	@Column(name="try_approve_or_no")
	public boolean getTryApproveOrNo() {
		return tryApproveOrNo;
	}

	public void setTryApproveOrNo(boolean tryApproveOrNo) {
		this.tryApproveOrNo = tryApproveOrNo;
	}
	@Column(name="use_point_or_not")
	public boolean getUsePointOrNot() {
		return usePointOrNot;
	}

	public void setUsePointOrNot(boolean usePointOrNot) {
		this.usePointOrNot = usePointOrNot;
	}
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="scIsolationConfig_id",referencedColumnName="id",unique=true)
	public ScIsolationConfig getScIsolationConfig() {
		return scIsolationConfig;
	}

	public void setScIsolationConfig(ScIsolationConfig scIsolationConfig) {
		this.scIsolationConfig = scIsolationConfig;
	}
	@Column(name="use_gift_or_not")
	public boolean getUseGiftOrNot() {
		return useGiftOrNot;
	}

	public void setUseGiftOrNot(boolean useGiftOrNot) {
		this.useGiftOrNot = useGiftOrNot;
	}
	
	@Column(name="c_code",length=50)
	public String getCatalogCode() {
		return catalogCode;
	}

	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}

	public boolean isTryTimeAddOrNo() {
		return tryTimeAddOrNo;
	}

	public void setTryTimeAddOrNo(boolean tryTimeAddOrNo) {
		this.tryTimeAddOrNo = tryTimeAddOrNo;
	}

	public boolean isRecommendedProduct() {
		return recommendedProduct;
	}

	public void setRecommendedProduct(boolean recommendedProduct) {
		this.recommendedProduct = recommendedProduct;
	}
}

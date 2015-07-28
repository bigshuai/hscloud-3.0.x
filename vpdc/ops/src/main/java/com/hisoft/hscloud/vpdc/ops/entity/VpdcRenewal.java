/* 
* 文 件 名:  VpdcRenewal.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-5-11 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.entity; 

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;

/** 
 * <续订业务记录表> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-11] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name = "hc_vpdc_renewal")
public class VpdcRenewal extends AbstractEntity{

	@Column(name = "referenceId",nullable=false,unique=true)
	private long referenceId;//虚拟机配置ID
	@Column(name = "vmUUID",nullable=false)
	private String vmUUID;//虚拟机UUID
	@Column(name = "orderNo")
	private String orderNo;//订单编号
	@Column(name = "business_type",nullable=false)
	private int businessType;//0:试用主机；1：正式主机；
	@Column(name = "business_status")
	private Integer businessStatus;//0:试用待审核；1：试用中；2：延期待审核；3：已延期；4:转正；5：试用审核未通过
	@Column(name = "start_time",nullable=false)
	private Date startTime;//开通时间
	@Column(name = "end_time",nullable=false)
	private Date endTime;//到期时间
	@Column(name = "userId",nullable=false)
	private long userId;//用户Id
	@Column(name = "sc_id")
	private Long serviceCatalogId;//套餐Id
	@Column(name = "is_enable", nullable = false,columnDefinition="INT default 0")
	private int isEnable = 0;//0:正常；1：手动禁用；2：到期禁用
	@Column(name="buy_type")
	private Integer buyType;
	public long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}
	public String getVmUUID() {
		return vmUUID;
	}
	public void setVmUUID(String vmUUID) {
		this.vmUUID = vmUUID;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public int getBusinessType() {
		return businessType;
	}
	public void setBusinessType(int businessType) {
		this.businessType = businessType;
	}
	public Integer getBusinessStatus() {
		return businessStatus;
	}
	public void setBusinessStatus(Integer businessStatus) {
		this.businessStatus = businessStatus;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Long getServiceCatalogId() {
		return serviceCatalogId;
	}
	public void setServiceCatalogId(Long serviceCatalogId) {
		this.serviceCatalogId = serviceCatalogId;
	}
	public int getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}
	public Integer getBuyType() {
		return buyType;
	}
	public void setBuyType(Integer buyType) {
		this.buyType = buyType;
	}
	
}

/* 
* 文 件 名:  InvoiceRecord.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.entity; 

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * <发票记录> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name = "hc_invoice_record")
public class InvoiceRecord {

    // 主健
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    //用户名
    @Column(name = "user_id", nullable = true)
    private long userId;
    
    //内部标注
    @Column(name = "inner_description", length=500, nullable = true)
    private String innerDescription;
    
    //发票号
    @Column(name = "invoice_no", nullable = true)
    private String invoiceNo;
    
    //发票抬头
    @Column(name = "invoice_title", length=500, nullable = true)
    private String invoiceTitle;
    
    //发票金额 = 申请中金额
    @Column(name = "invoice_amount", nullable = true)
    private BigDecimal invoiceAmount;
    
    //状态  0:未开   1：已开   2：未通过
    @Column(name = "status", nullable = true)
    private String status;
    
    //申请时间
    @Column(name = "application_time", nullable = true)
    private Date applicationTime = new Date();
    
    //开票时间
    @Column(name = "billing_time", nullable = true)
    private Date billingTime;
    
    //发送时间
    @Column(name = "send_time", nullable = true)
    private Date sendTime;
    
    //附注
    @Column(name = "description", length=500, nullable = true)
    private String description;
    
    //取票方式 0:快递10元   1:EMS20元  2:前台自取（免费）
    @Column(name = "take_invoice_type", nullable = true)
    private String takeInvoiceType;
    
    //快递送货时间   0:上班时间  1:双休节假日  2:时间不限
    @Column(name = "courier_delivery_time", nullable = true)
    private String courierDeliveryTime;
    
    //收件人
    @Column(name = "recipient", nullable = true)
    private String recipient;
    
    //收件公司
    @Column(name = "recipient_company", length=500, nullable = true)
    private String recipientCompany;
    
    //省份
    @Column(name = "province", nullable = true)
    private String province;
    
    //城市
    @Column(name = "city", nullable = true)
    private String city;
    
    //地址
    @Column(name = "address", length=500, nullable = true)
    private String address;
    
    //邮编
    @Column(name = "postcode", nullable = true)
    private String postcode;
    
    //手机
    @Column(name = "mobile", nullable = true)
    private String mobile;
    
    //电话
    @Column(name = "telephone", nullable = true)
    private String telephone;
    
    //传真
    @Column(name = "fax", nullable = true)
    private String fax;
    
    //可开票总额
    @Column(name = "account", nullable = false)
    private BigDecimal account;
    
    //备注
    @Column(name = "remark", nullable = true)
    private String remark;
    
    //发票类型 1：增值税普通发票 2：增值税专业发票
    @Column(name="invoiceType",nullable = true)
    private String invoiceType;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getInnerDescription() {
        return innerDescription;
    }

    public void setInnerDescription(String innerDescription) {
        this.innerDescription = innerDescription;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

    public Date getBillingTime() {
        return billingTime;
    }

    public void setBillingTime(Date billingTime) {
        this.billingTime = billingTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTakeInvoiceType() {
        return takeInvoiceType;
    }

    public void setTakeInvoiceType(String takeInvoiceType) {
        this.takeInvoiceType = takeInvoiceType;
    }

    public String getCourierDeliveryTime() {
        return courierDeliveryTime;
    }

    public void setCourierDeliveryTime(String courierDeliveryTime) {
        this.courierDeliveryTime = courierDeliveryTime;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipientCompany() {
        return recipientCompany;
    }

    public void setRecipientCompany(String recipientCompany) {
        this.recipientCompany = recipientCompany;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public BigDecimal getAccount() {
        return account;
    }

    public void setAccount(BigDecimal account) {
        this.account = account;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
    
}

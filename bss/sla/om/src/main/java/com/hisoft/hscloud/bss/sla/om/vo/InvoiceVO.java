/* 
* 文 件 名:  InvoiceVo.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-17 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.vo; 

import java.math.BigDecimal;
import java.util.Date;



/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-17] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class InvoiceVO {
    // 主健
    private long id;
    //可开票总额
    private BigDecimal account = new BigDecimal(0);
    //存款净增
    private BigDecimal deposit = new BigDecimal(0);
    //申请中金额
    private BigDecimal applicationValue = new BigDecimal(0);
    //开票金额
    private BigDecimal billingValue = new BigDecimal(0);
    //用户id
    private long userId;
    //状态     0:未申请  1:申请中 
    private String status;

    
    //内部标注
    private String innerDescription;
    //发票号
    private String invoiceNo;
    //发票抬头
    private String invoiceTitle;
    //发票金额 = 申请中金额
    private BigDecimal invoiceAmount = new BigDecimal(0);
    //申请时间
    private Date applicationTime = new Date();
    //开票时间
    private Date billingTime = new Date();
    //发送时间
    private Date sendTime = new Date();
    //附注
    private String description;
    //取票方式
    private String takeInvoiceType;
    //快递送货时间
    private String courierDeliveryTime;
    //收件人
    private String recipient;
    //收件公司
    private String recipientCompany;
    //省份
    private String province;
    //城市
    private String city;
    //地址
    private String address;
    //邮编
    private String postcode;
    //手机
    private String mobile;
    //电话
    private String telephone;
    //传真
    private String fax;
    //发票类型  1：增值税普通发票 2：增值税专业发票
    private String invoiceType;
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public BigDecimal getAccount() {
        return account;
    }
    public void setAccount(BigDecimal account) {
        this.account = account;
    }
    public BigDecimal getDeposit() {
        return deposit;
    }
    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }
    public BigDecimal getApplicationValue() {
        return applicationValue;
    }
    public void setApplicationValue(BigDecimal applicationValue) {
        this.applicationValue = applicationValue;
    }
    public BigDecimal getBillingValue() {
        return billingValue;
    }
    public void setBillingValue(BigDecimal billingValue) {
        this.billingValue = billingValue;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
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
    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }
    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
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
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
    
    /*public static void main(String[] args) {
        InvoiceVO invoiceVO = new InvoiceVO();
        invoiceVO.setAccount(new BigDecimal(100));
        invoiceVO.setAddress("address");
        invoiceVO.setCity("city");
        invoiceVO.setCourierDeliveryTime("22");
        invoiceVO.setDescription("description");
        invoiceVO.setFax("fax");
   //     invoiceVO.setInvoiceAmount(new BigDecimal(30));
        invoiceVO.setInvoiceTitle("invoiceTitle");
        invoiceVO.setMobile("12345");
        invoiceVO.setPostcode("postcode");
        invoiceVO.setProvince("province");
        invoiceVO.setRecipient("recipient");
        invoiceVO.setRecipientCompany("recipientCompany");
        invoiceVO.setStatus("0");
        invoiceVO.setTakeInvoiceType("2");
        invoiceVO.setTelephone("123456");
        invoiceVO.setUserId(1l);
        InvoiceRecord invoiceRecord = new InvoiceRecord();
        try {
            BeanUtils.copyProperties(invoiceRecord, invoiceVO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }*/
}

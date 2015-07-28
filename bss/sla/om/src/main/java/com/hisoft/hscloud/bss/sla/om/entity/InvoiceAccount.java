/* 
* 文 件 名:  InvoiceAccount.java 
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hisoft.hscloud.bss.sla.om.util.Constants;

/** 
 * <发票账户> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name = "hc_invoice_account")
public class InvoiceAccount {
    // 主健
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    //可开票总额
    @Column(name = "account", nullable = false)
    private BigDecimal account = new BigDecimal(0);
    
    //存款净增
    @Column(name = "deposit", nullable = false)
    private BigDecimal deposit = new BigDecimal(0);
    
    //申请中金额
    @Column(name = "application_value", nullable = false)
    private BigDecimal applicationValue = new BigDecimal(0);
    
    //开票金额
    @Column(name = "billing_value", nullable = false)
    private BigDecimal billingValue = new BigDecimal(0);
    
    //用户id
    @Column(name = "user_id", nullable = false)
    private long userId;
    
    //状态     0:未申请  1:申请中 
    @Column(name = "status", length=10, nullable = false)
    private String status = Constants.INVOICE_ACCOUNT_STATUS_IDLENESS;
    
    //备注
    @Column(name = "remark", nullable = true)
    private String remark;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

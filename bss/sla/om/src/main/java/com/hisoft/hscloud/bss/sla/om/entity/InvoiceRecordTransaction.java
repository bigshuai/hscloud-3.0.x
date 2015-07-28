/* 
* 文 件 名:  InvoiceRecordTransaction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-27 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.entity; 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * <发票交易日志关联表> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-27] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name = "hc_invoice_record_transaction")
public class InvoiceRecordTransaction {
    
    // 主健
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    //发票id
    @Column(name = "invoice_record_id", nullable = false)
    private long invoiceRecordId;
    
    //交易日志id
    @Column(name = "transaction_log_id", nullable = false)
    private long transactionLogId;
    
    //备注
    @Column(name = "remark", nullable = true)
    private String remark;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInvoiceRecordId() {
        return invoiceRecordId;
    }

    public void setInvoiceRecordId(long invoiceRecordId) {
        this.invoiceRecordId = invoiceRecordId;
    }

    public long getTransactionLogId() {
        return transactionLogId;
    }

    public void setTransactionLogId(long transactionLogId) {
        this.transactionLogId = transactionLogId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    
}

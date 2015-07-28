/* 
* 文 件 名:  InvoiceAccountService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.service; 

import java.math.BigDecimal;

import com.hisoft.hscloud.bss.sla.om.entity.InvoiceAccount;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface InvoiceAccountService {
    /**
     * 创建账户
    * <功能详细描述> 
    * @param invoiceAccount
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public long createInvoiceAccount(InvoiceAccount invoiceAccount);
    /**
     * 申请开发票
    * <功能详细描述> 
    * @param userId
    * @param applicationValue
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public String applyInvoice(long userId, BigDecimal applicationValue);
    /**
     * 开票
    * <功能详细描述> 
    * @param userId
    * @param applicationValue 
    * @see [类、类#方法、类#成员]
     */
    public void invoice(long userId, BigDecimal applicationValue);
    /**
     * 申请失败
    * <功能详细描述> 
    * @param userId
    * @param applicationValue 
    * @see [类、类#方法、类#成员]
     */
    public void applyInvoiceFail(long userId, BigDecimal applicationValue);
    /**
     * 存款
    * <功能详细描述> 
    * @param userId
    * @param money 
    * @see [类、类#方法、类#成员]
     */
    public void deposit(long userId, BigDecimal money);
    /**
     * 退款
    * <功能详细描述> 
    * @param userId
    * @param money 
     * @return 
    * @see [类、类#方法、类#成员]
     */
    public BigDecimal refund(long userId, BigDecimal money);
    /**
     * 通过userId查询发票账户 
    * <功能详细描述> 
    * @param userId 
     * @return 
    * @see [类、类#方法、类#成员]
     */
    public InvoiceAccount findInvoiceAccount(long userId);
    /**
     * 退款前检查账户
    * 账户金额小于提款金额返回true，否则返回false 
    * @param userId
    * @param money
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public BigDecimal checkAccountForRefund(long userId, BigDecimal money);
    
}

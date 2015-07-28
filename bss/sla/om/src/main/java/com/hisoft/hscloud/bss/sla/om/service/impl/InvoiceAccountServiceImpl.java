/* 
* 文 件 名:  InvoiceAccountServiceImpl.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.service.impl; 

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.bss.sla.om.dao.InvoiceAccountDao;
import com.hisoft.hscloud.bss.sla.om.entity.InvoiceAccount;
import com.hisoft.hscloud.bss.sla.om.service.InvoiceAccountService;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Service
public class InvoiceAccountServiceImpl implements InvoiceAccountService {
    @Autowired
    private InvoiceAccountDao invoiceAccountDao;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    /**
     * 创建发票账户 
    * <功能详细描述> 
    * @param invoiceAccount
    * @return 
    * @see [类、类#方法、类#成员]
     */
    @Override
    public long createInvoiceAccount(InvoiceAccount invoiceAccount){
        return invoiceAccountDao.saveInvoiceAccount(invoiceAccount);
    }
    //申请
    @Override
    public String applyInvoice(long userId, BigDecimal applicationValue) {
        InvoiceAccount invoiceAccount = invoiceAccountDao.getInvoiceAccount(userId);
        if(invoiceAccount.getAccount().compareTo(applicationValue) < 0) {
            return Constants.INVOICE_BALANCE_EXCEPTION;
        }
      /*  if(com.hisoft.hscloud.bss.sla.om.util.Constants.INVOICE_ACCOUNT_STATUS_APPLICATION.equals(
                invoiceAccount.getStatus())) {
            return Constants.INVOICE_APPLICATION_STATUS_EXCEPTION;
        }*/
        invoiceAccount.setApplicationValue(invoiceAccount.getApplicationValue().add(applicationValue));
        invoiceAccount.setStatus(com.hisoft.hscloud.bss.sla.om.util.Constants.INVOICE_ACCOUNT_STATUS_APPLICATION);
        invoiceAccount.setAccount(invoiceAccount.getAccount().subtract(applicationValue));
        invoiceAccountDao.saveInvoiceAccount(invoiceAccount);
        return Constants.SUCCESS;
    }
    //开票
    @Override
    public void invoice(long userId, BigDecimal applicationValue) {
        InvoiceAccount invoiceAccount = invoiceAccountDao.getInvoiceAccount(userId);
        invoiceAccount.setApplicationValue(invoiceAccount.getApplicationValue().subtract(applicationValue));
        
        invoiceAccount.setBillingValue(invoiceAccount.getBillingValue().add(applicationValue));
        invoiceAccount.setStatus(com.hisoft.hscloud.bss.sla.om.util.Constants.INVOICE_ACCOUNT_STATUS_IDLENESS);
        invoiceAccountDao.saveInvoiceAccount(invoiceAccount);
    }
    
    //审批不通过
    @Override
    public void applyInvoiceFail(long userId, BigDecimal applicationValue) {
        InvoiceAccount invoiceAccount = invoiceAccountDao.getInvoiceAccount(userId);
        invoiceAccount.setStatus(com.hisoft.hscloud.bss.sla.om.util.Constants.INVOICE_ACCOUNT_STATUS_IDLENESS);
        invoiceAccount.setAccount(invoiceAccount.getAccount().add(applicationValue)); //可开票总额  增加
        invoiceAccount.setApplicationValue(invoiceAccount.getApplicationValue().subtract(applicationValue));
        invoiceAccountDao.saveInvoiceAccount(invoiceAccount);
    }
    
    //存款
    @Override
    public void deposit(long userId, BigDecimal money) {
        InvoiceAccount invoiceAccount = invoiceAccountDao.getInvoiceAccount(userId);
      //  invoiceAccount.setApplicationValue(new BigDecimal(0));
        invoiceAccount.setDeposit(invoiceAccount.getDeposit().add(money)); //存款净增 增加
        invoiceAccount.setAccount(invoiceAccount.getAccount().add(money)); //可开票总额  增加
     //   invoiceAccount.setBillingValue(invoiceAccount.getBillingValue().add(applicationValue));
     //   invoiceAccount.setStatus("0");
        invoiceAccountDao.saveInvoiceAccount(invoiceAccount);
    }
    
    //退款
    @Override
    public BigDecimal refund(long userId, BigDecimal money) {
        InvoiceAccount invoiceAccount = invoiceAccountDao.getInvoiceAccount(userId);
      //  invoiceAccount.setApplicationValue(new BigDecimal(0));
        invoiceAccount.setDeposit(invoiceAccount.getDeposit().subtract(money)); //存款净增 减少
        invoiceAccount.setAccount(invoiceAccount.getAccount().subtract(money)); //可开票总额 减少
     //   invoiceAccount.setBillingValue(invoiceAccount.getBillingValue().add(applicationValue));
     //   invoiceAccount.setStatus("0");
        invoiceAccountDao.saveInvoiceAccount(invoiceAccount);
        return invoiceAccount.getAccount();
    }
    /**
     * 退款前检查账户
     * 账户金额小于提款金额返回true，否则返回false
    * @param userId
    * @param money
    * @return
     */
    @Override
    public BigDecimal checkAccountForRefund(long userId, BigDecimal money) {
        InvoiceAccount invoiceAccount = invoiceAccountDao.getInvoiceAccount(userId);
        return invoiceAccount.getAccount().subtract(money);
    }
    
    //查询发票账户
    @Override
    public InvoiceAccount findInvoiceAccount(long userId) {
        InvoiceAccount invoiceAccount = invoiceAccountDao.getInvoiceAccount(userId);
        if(invoiceAccount == null) {
            /*invoiceAccount = new InvoiceAccount();
            invoiceAccount.setUserId(userId);
            invoiceAccountDao.saveInvoiceAccount(invoiceAccount);*/
            throw new HsCloudException(Constants.INVOICE_ACCOUNT_UNEXIST_EXCEPTION, 
                    "userId:" + userId + "发票账户不存在", logger);
        }
        return invoiceAccount;
    }
}

/* 
* 文 件 名:  InvoiceAccountServiceTest.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.service.test; 

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.hisoft.hscloud.bss.sla.om.entity.InvoiceAccount;
import com.hisoft.hscloud.bss.sla.om.service.InvoiceAccountService;
import com.hisoft.hscloud.common.util.Constants;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = {"classpath:applicationContext-hscloud-bss-sla-om.xml"})
public class InvoiceAccountServiceTest {
    @Autowired
    private InvoiceAccountService invoiceAccountService;
    
    @Test
    public void testCreateInvoiceAccount() {
        InvoiceAccount invoiceAccount = new InvoiceAccount();
        invoiceAccount.setUserId(1l);
        invoiceAccountService.createInvoiceAccount(invoiceAccount);
    }
    
    @Test
    public void testApplyInvoice() {
        long userId = 1l;
        BigDecimal applicationValue = new BigDecimal(20);
        String result = invoiceAccountService.applyInvoice(userId, applicationValue);
        Assert.assertEquals(result, Constants.SUCCESS);
    }
    
    @Test
    public void testInvoice() {
        long userId = 1l;
        BigDecimal applicationValue = new BigDecimal(20);
        invoiceAccountService.invoice(userId, applicationValue);
    }
    
    @Test
    public void testApplyInvoiceFail() {
        long userId = 1l;
        BigDecimal applicationValue = new BigDecimal(20);
        invoiceAccountService.applyInvoiceFail(userId, applicationValue);
    }
    
    @Test
    public void testDeposit() {
        long userId = 2l;
        BigDecimal money = new BigDecimal(40);
        invoiceAccountService.deposit(userId, money);
    }
    
    @Test
    public void testRefund() {
        long userId = 2l;
        BigDecimal money = new BigDecimal(20);
        BigDecimal result = invoiceAccountService.refund(userId, money);
        System.out.println(result);
    }
    
    @Test
    public void testFindInvoiceAccount() {
        long userId = 2l;
        InvoiceAccount invoiceAccount = invoiceAccountService.findInvoiceAccount(userId);
        if(invoiceAccount == null) {
            System.out.println("tsete");
        }
    }
    
    @Test
    public void testCheckAccountForRefund() {
        long userId = 2l;
        BigDecimal money = new BigDecimal(10);
        BigDecimal result = invoiceAccountService.checkAccountForRefund(userId, money);
        System.out.println(result);
    }
}

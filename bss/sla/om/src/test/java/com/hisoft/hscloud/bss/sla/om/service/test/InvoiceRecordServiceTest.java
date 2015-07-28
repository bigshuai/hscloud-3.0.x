/* 
* 文 件 名:  InvoiceRecordServiceTest.java 
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
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
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.entity.InvoiceRecord;
import com.hisoft.hscloud.bss.sla.om.service.InvoiceRecordService;
import com.hisoft.hscloud.bss.sla.om.vo.TakeInvoiceType;

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
public class InvoiceRecordServiceTest {
    @Autowired
    private InvoiceRecordService invoiceRecordService;
    
    @Test
    public void testApplyInvoiceRecord(){
        InvoiceRecord invoiceRecord = new InvoiceRecord();
        invoiceRecord.setAccount(new BigDecimal(100));
        invoiceRecord.setAddress("address");
        invoiceRecord.setCity("city");
        invoiceRecord.setCourierDeliveryTime("22");
        invoiceRecord.setDescription("description");
        invoiceRecord.setFax("fax");
        invoiceRecord.setInvoiceAmount(new BigDecimal(30));
        invoiceRecord.setInvoiceTitle("invoiceTitle");
        invoiceRecord.setMobile("12345");
        invoiceRecord.setPostcode("postcode");
        invoiceRecord.setProvince("province");
        invoiceRecord.setRecipient("recipient");
        invoiceRecord.setRecipientCompany("recipientCompany");
        invoiceRecord.setStatus("0");
        invoiceRecord.setTakeInvoiceType("2");
        invoiceRecord.setTelephone("123456");
        invoiceRecord.setUserId(1l);
        invoiceRecordService.applyInvoiceRecord(invoiceRecord);
    }
    
    @Test
    public void testApprovalSuccess() {
      /*  InvoiceRecord invoiceRecord = new InvoiceRecord();
        invoiceRecordService.approvalSuccess(invoiceRecord);*/
    }
    
    @Test
    public void testFindInvoiceRecordByInvoiceNo(){
        List<InvoiceRecord> list = invoiceRecordService.findInvoiceRecordByInvoiceNo("678");
        Assert.assertEquals(list.isEmpty(), true);
        TakeInvoiceType type = TakeInvoiceType.getTakeInvoiceType(0);
        System.out.println(type.getIndex());
        System.out.println(type.getPrice());
        System.out.println(type.getDescription());
    }
    
    @Test
    public void testFindInvoiceRecordList() {
        Map<String, Object> condition = new HashMap<String, Object>();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        page.setPageNo(1);
        page.setPageSize(16);
        condition.put("email", "li@hisoft.com");
        condition.put("invoiceNo", "2");
        page = invoiceRecordService.findInvoiceRecordList(page, condition);
        System.out.println(page.getResult().size());
        Map<String, Object> map = (Map<String, Object>) page.getResult().get(1);
        System.out.println(map.get("id"));
    }
    
    @Test
    public void testFindInvoiceRecordList2() {
        Map<String, Object> condition = new HashMap<String, Object>();
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        page.setPageNo(1);
        page.setPageSize(16);
        condition.put("email", "li@hisoft.com");
        page = invoiceRecordService.findInvoiceRecordList(page, condition);
        System.out.println(page.getResult().size());
        Map<String, Object> map = (Map<String, Object>) page.getResult().get(1);
        System.out.println(map.get("id"));
    }
}

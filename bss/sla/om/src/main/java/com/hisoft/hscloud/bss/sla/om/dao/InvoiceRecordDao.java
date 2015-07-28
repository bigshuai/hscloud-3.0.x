/* 
* 文 件 名:  InvoiceRecordDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.dao; 

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.entity.InvoiceRecord;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface InvoiceRecordDao {
    /**
     * 录入发票记录 
    * <功能详细描述> 
    * @param invoiceRecord
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public long writeInvoiceRecord(InvoiceRecord invoiceRecord);
    /**
     * 查询发票列表 
    * <功能详细描述> 
    * @param page
    * @param condition
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Page<Map<String, Object>> findInvoiceRecordList(
            Page<Map<String, Object>> page, Map<String, Object> condition);
    
    /**
     * 根据id查询发票 
    * <功能详细描述> 
    * @param id
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public InvoiceRecord getInvoiceRecord(long id);
    /**
     * 根据订单号查询订单 
    * <功能详细描述> 
    * @param invoiceNo
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public List<InvoiceRecord> findInvoiceRecordByInvoiceNo(String invoiceNo);
    /**
     * <导出发票> 
    * <功能详细描述> 
    * @param condition
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public List<Map<String, Object>> invoiceExcelExport(
            Map<String, Object> condition);
}

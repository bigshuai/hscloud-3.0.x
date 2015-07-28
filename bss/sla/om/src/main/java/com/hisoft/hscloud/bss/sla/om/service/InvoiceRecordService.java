/* 
* 文 件 名:  InvoiceRecordService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.sla.om.service; 

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.entity.InvoiceRecord;
import com.hisoft.hscloud.bss.sla.om.vo.InvoiceVO;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface InvoiceRecordService {
    /**
     * 发票申请记录 
    * <功能详细描述> 
    * @param invoiceRecord
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public long applyInvoiceRecord(InvoiceRecord invoiceRecord);
    
    public Page<Map<String, Object>> findInvoiceRecordList(
            Page<Map<String, Object>> page, Map<String, Object> condition);

    public InvoiceRecord approvalSuccess(long id, String innerDescription, String invoiceNo,
            Date sendTime);

    public InvoiceRecord approvalFail(long id, String innerDescription);

    public List<InvoiceRecord> findInvoiceRecordByInvoiceNo(String invoiceNo);

    public List<Map<String, Object>> invoiceExcelExport(Map<String, Object> condition);

    public String modifyInvoice(InvoiceVO invoiceVO);
}

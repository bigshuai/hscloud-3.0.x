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
package com.hisoft.hscloud.bss.sla.om.service.impl; 

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.dao.InvoiceRecordDao;
import com.hisoft.hscloud.bss.sla.om.entity.InvoiceRecord;
import com.hisoft.hscloud.bss.sla.om.service.InvoiceRecordService;
import com.hisoft.hscloud.bss.sla.om.util.Constants;
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
@Service
public class InvoiceRecordServiceImpl implements InvoiceRecordService {
    @Autowired
    private InvoiceRecordDao invoiceRecordDao;

    @Override
    public long applyInvoiceRecord(InvoiceRecord invoiceRecord) {
        invoiceRecord.setStatus(Constants.INVOICE_APPLY_STATUS_WAIT);
        return invoiceRecordDao.writeInvoiceRecord(invoiceRecord);
    }
    /**
     * 审批通过
    * @param invoiceRecord
    * @return
     */
    @Override
    public InvoiceRecord approvalSuccess(long id, String innerDescription, String invoiceNo, Date sendTime) {
        InvoiceRecord invoiceRecord = invoiceRecordDao.getInvoiceRecord(id);
        invoiceRecord.setBillingTime(new Date());
        invoiceRecord.setSendTime(sendTime);
        invoiceRecord.setInvoiceNo(invoiceNo);
        invoiceRecord.setInnerDescription(innerDescription);
        invoiceRecord.setStatus(Constants.INVOICE_APPLY_STATUS_PASS);
        invoiceRecordDao.writeInvoiceRecord(invoiceRecord);
        return invoiceRecord;
    }
    
    /**
     * 根据发票号查询发票记录
    * @param invoiceNo
    * @return
     */
    @Override
    public  List<InvoiceRecord> findInvoiceRecordByInvoiceNo(String invoiceNo) {
        return invoiceRecordDao.findInvoiceRecordByInvoiceNo(invoiceNo);
    }
    
    /**
     * 审批失败
    * @param invoiceRecord
    * @return
     */
    @Override
    public InvoiceRecord approvalFail(long id, String innerDescription) {
        InvoiceRecord invoiceRecord = invoiceRecordDao.getInvoiceRecord(id);
        invoiceRecord.setBillingTime(new Date());
        invoiceRecord.setInnerDescription(innerDescription);
        invoiceRecord.setStatus(Constants.INVOICE_APPLY_STATUS_NOT_PASS);
        invoiceRecordDao.writeInvoiceRecord(invoiceRecord);
        return invoiceRecord;
    }
    
    @Override
    public List<Map<String, Object>> invoiceExcelExport(Map<String, Object> condition) {
        return invoiceRecordDao.invoiceExcelExport(condition);
    }

    @Override
    public Page<Map<String, Object>> findInvoiceRecordList(
            Page<Map<String, Object>> page, Map<String, Object> condition) {
        return invoiceRecordDao.findInvoiceRecordList(page, condition);
    }
    
    @Override
    public String modifyInvoice(InvoiceVO invoiceVO) {
        List<InvoiceRecord> list = invoiceRecordDao.findInvoiceRecordByInvoiceNo(invoiceVO.getInvoiceNo());
        InvoiceRecord invoiceRecord = invoiceRecordDao.getInvoiceRecord(invoiceVO.getId());
        if(list != null && list.size() > 0) {
            InvoiceRecord temp = list.get(0);
            if(temp.getId() != invoiceVO.getId()) {
                return com.hisoft.hscloud.common.util.Constants.INVOICE_NUMBER_EXIST_EXCEPTION;
            }
        }
        invoiceRecord.setInvoiceNo(invoiceVO.getInvoiceNo());
        invoiceRecord.setInnerDescription(invoiceVO.getInnerDescription());
        invoiceRecordDao.writeInvoiceRecord(invoiceRecord);
        return com.hisoft.hscloud.common.util.Constants.SUCCESS;
        
    }
}

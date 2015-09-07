/* 
* 文 件 名:  InvoiceAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-17 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.sla.om.util.ExcelExport;
import com.hisoft.hscloud.bss.sla.om.vo.InvoiceVO;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2015-6-29 14:11:41] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class InvoiceAction extends HSCloudAction {

    /** 
    * 注释内容 
    */
    private static final long serialVersionUID = -3871381496574214977L;
    
    private Page<Map<String, Object>> invoicePage = new Page<Map<String, Object>>();
    private Logger logger = Logger.getLogger(this.getClass());
    private int page;
    private int limit;
    
    private long id;
    private String innerDescription;
    private String invoiceNo;
    private Date sendTime;
    
    private String query;
    
    private Date fromDateApply;
    private Date toDateApply;
    private Date fromDateBill;
    private Date toDateBill;
    private Date fromDateSend;
    private Date toDateSend;
    
    private String status;
    private String domainId;
    private User user;
    
    private Account account;
    
    private InvoiceVO invoiceVO = new InvoiceVO();

    @Autowired
    private Facade facade;
    
    /**
     * 发票列表展示 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void findInvoiceList() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findInvoiceList method.");			
		}
        invoicePage.setPageNo(page);
        invoicePage.setPageSize(limit);
        Map<String, Object> condition = new HashMap<String, Object>();
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
                if(StringUtils.isNotBlank(query)) {
                    query = query.trim();
                    query = new String(query.getBytes("iso8859_1"), "UTF-8");
                    condition.put("username", "%" + query + "%");
                    condition.put("invoiceNo", "%" + query + "%");
                    condition.put("invoiceTitle", "%" + query + "%");
                }
                assembleCondition(condition);
                invoicePage = facade.findInvoiceRecordList(invoicePage, condition, admin);
                super.fillActionResult(invoicePage);
            } catch(Exception e) {
                this.dealThrow(Constants.INVOICE_EXCEPTION, e, logger);
            }            
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findInvoiceList method.takeTime:" + takeTime + "ms");
		}
    }

    private void assembleCondition(Map<String, Object> condition) {
        if(fromDateApply != null) {
            condition.put("fromDateApply", fromDateApply);
        }
        if(toDateApply != null) {
            toDateApply.setTime(toDateApply.getTime() + 86400 * 1000);
            condition.put("toDateApply", toDateApply);
        }
        
        if(fromDateBill != null) {
            condition.put("fromDateBill", fromDateBill);
        }
        if(toDateBill != null) {
            toDateBill.setTime(toDateBill.getTime() + 86400 * 1000);
            condition.put("toDateBill", toDateBill);
        }
        
        if(fromDateSend != null) {
            condition.put("fromDateSend", fromDateSend);
        }
        if(toDateSend != null) {
            toDateSend.setTime(toDateSend.getTime() + 86400 * 1000);
            condition.put("toDateSend", toDateSend);
        }
        if(StringUtils.isNotBlank(status)) {
            condition.put("status", status);
        }
        if(StringUtils.isNotBlank(domainId)) {
            condition.put("domainId", Long.valueOf(domainId));
        }
    }
    
    public void exportInvoice() {
        long beginRunTime = 0;
        if(logger.isDebugEnabled()){
            beginRunTime = System.currentTimeMillis();
            logger.debug("enter excelExport method.");          
        }
       
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try {
                HttpServletResponse response = ServletActionContext
                        .getResponse();
                response.reset();
                String exportFileName = "Invoice.xls";

                String mimeType = "application/msexcel;charset=UTF-8";
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition",
                        "attachment;filename=\"" + exportFileName + "\"");
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setHeader("Cache-Control",
                        "must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "public");
                response.setDateHeader("Expires",
                        (System.currentTimeMillis() + 1000));
                
                Map<String, Object> condition = new HashMap<String, Object>();
                if (StringUtils.isNotBlank(query)) {
                    query = query.trim();
               //     query = new String(query.getBytes("iso8859_1"), "UTF-8");
                    condition.put("username", "%" + query + "%");
                    condition.put("invoiceNo", "%" + query + "%");
                    condition.put("invoiceTitle", "%" + query + "%");
                }
                assembleCondition(condition);
                ExcelExport excel = facade.invoiceExcelExport(condition, admin);
                ServletOutputStream os = response.getOutputStream();
                excel.getWb().write(os);
                os.flush();
                os.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(logger.isDebugEnabled()){
            long takeTime = System.currentTimeMillis() - beginRunTime;
            logger.debug("exit excelExport method.takeTime:" + takeTime + "ms");
        }
        this.setActionResult(null);
    }
    /**
     * 审批不通过 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void approvalInvoiceFail() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter approvalInvoiceFail method.");			
		}
		Admin admin=null;
        try{
            admin = (Admin) super.getCurrentLoginUser();
            facade.approvalInvoiceFail(id, innerDescription);
            facade.insertOperationLog(admin,"发票审批不通过","发票审批不通过",Constants.RESULT_SUCESS);
        } catch(Exception ex) {
            facade.insertOperationLog(admin,"发票审批不通过错误:"+ex.getMessage(),"发票审批不通过",Constants.RESULT_FAILURE);
            this.dealThrow(Constants.INVOICE_EXCEPTION, ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit approvalInvoiceFail method.takeTime:" + takeTime + "ms");
		}
    }
    /**
     * 审批通过 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void approvalInvoiceSuccess() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter approvalInvoiceSuccess method.");			
		}
		Admin admin=null;
        try{
            admin = (Admin) super.getCurrentLoginUser();
            String result = facade.approvalInvoiceSuccess(id, innerDescription, invoiceNo, sendTime);
            if (!Constants.SUCCESS.equals(result)) { //返回错误代码，输入错误代码
                facade.insertOperationLog(admin,"发票审批通过错误:"+result,"发票审批通过",Constants.RESULT_FAILURE);
                fillActionResult(result);
            } else {
                facade.insertOperationLog(admin,"发票审批通过","发票审批通过",Constants.RESULT_SUCESS);
            }
        } catch(Exception ex) {
            facade.insertOperationLog(admin,"发票审批通过错误:"+ex.getMessage(),"发票审批通过",Constants.RESULT_FAILURE);
            this.dealThrow(Constants.INVOICE_EXCEPTION, ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit approvalInvoiceSuccess method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * <发票修改> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void modifyInvoice() {
        long beginRunTime = 0;
        if(logger.isDebugEnabled()){
            beginRunTime = System.currentTimeMillis();
            logger.debug("enter findInvoiceList method.");          
        }
        Admin admin=null;
        try{
            String result = facade.modifyInvoice(invoiceVO);
            if (!Constants.SUCCESS.equals(result)) { //返回错误代码，输入错误代码
                fillActionResult(result);
                facade.insertOperationLog(admin,"发票修改错误:"+result,"发票修改",Constants.RESULT_FAILURE);
            } else {
                facade.insertOperationLog(admin,"发票修改","发票修改",Constants.RESULT_SUCESS);
            }
        } catch(Exception ex) {
            facade.insertOperationLog(admin,"发票修改错误:"+ex.getMessage(),"发票修改",Constants.RESULT_FAILURE);
            this.dealThrow(Constants.INVOICE_EXCEPTION, ex, logger);
        }
        if(logger.isDebugEnabled()){
            long takeTime = System.currentTimeMillis() - beginRunTime;
            logger.debug("exit findInvoiceList method.takeTime:" + takeTime + "ms");
        }
    }
    
    /**
     * <检查账户金额> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void checkAccountForRefund(){
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter checkAccountForRefund method.");			
		}
    	BigDecimal invoiceAmount = facade.checkAccountForRefund(user.getId(), account.getBalance());
    	fillActionResult(null,invoiceAmount.toString());
    	if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit checkAccountForRefund method.takeTime:" + takeTime + "ms");
		}
    }

    public Page<Map<String, Object>> getInvoicePage() {
        return invoicePage;
    }

    public void setInvoicePage(Page<Map<String, Object>> invoicePage) {
        this.invoicePage = invoicePage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
    public String getDomainId() {
        return domainId;
    }
    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public Date getFromDateApply() {
        return fromDateApply;
    }

    public void setFromDateApply(Date fromDateApply) {
        this.fromDateApply = fromDateApply;
    }

    public Date getToDateApply() {
        return toDateApply;
    }

    public void setToDateApply(Date toDateApply) {
        this.toDateApply = toDateApply;
    }

    public Date getFromDateBill() {
        return fromDateBill;
    }

    public void setFromDateBill(Date fromDateBill) {
        this.fromDateBill = fromDateBill;
    }

    public Date getToDateBill() {
        return toDateBill;
    }

    public void setToDateBill(Date toDateBill) {
        this.toDateBill = toDateBill;
    }

    public Date getFromDateSend() {
        return fromDateSend;
    }

    public void setFromDateSend(Date fromDateSend) {
        this.fromDateSend = fromDateSend;
    }

    public Date getToDateSend() {
        return toDateSend;
    }

    public void setToDateSend(Date toDateSend) {
        this.toDateSend = toDateSend;
    }

    public InvoiceVO getInvoiceVO() {
        return invoiceVO;
    }

    public void setInvoiceVO(InvoiceVO invoiceVO) {
        this.invoiceVO = invoiceVO;
    }
}

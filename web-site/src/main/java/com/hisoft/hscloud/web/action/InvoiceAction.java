/* 
* 文 件 名:  InvoiceAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-2-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 


import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.bss.sla.om.vo.InvoiceVO;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-2-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class InvoiceAction extends HSCloudAction {
    
    /** 
    * 注释内容 
    */
    private static final long serialVersionUID = -3322792186811400720L;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    /**
     * 每页显示条数
     */
    private final int PAGE_NUM = 8;
    
    private InvoiceVO invoiceVO = new InvoiceVO();
    @Autowired
    private Facade facade;
    
    private Page<Map<String, Object>> invoicePage = new Page<Map<String, Object>>(PAGE_NUM);
    
    private int page;
    
    private int limit;
    
    private String operateObject;
    
    /**
     * 开发票页面初始化 
    * <功能详细描述> 
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public String initInvoice() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter initInvoice method.");			
		}
        User user = (User)this.getCurrentLoginUser();
        if(user != null) {
            try{
                Map<String, Object> result = facade.initInvoice(user.getId());
                this.fillActionResult(result);
                return null;
            } catch(Exception e) {
                this.dealThrow(Constants.INVOICE_EXCEPTION, "发票初始化异常", e, logger);
            }
            
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit initInvoice method.takeTime:" + takeTime + "ms");
		}
        return "login";
    }
    /**
     * 申请开发票 
    * <功能详细描述> 
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public String applyInvoice() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter applyInvoice method.");			
		}
        User user = (User)this.getCurrentLoginUser();
        if(user != null) {
            try {
                invoiceVO = Utils.strutsJson2Object(InvoiceVO.class);
                if(invoiceVO.getAccount().equals(0)){
                	return null;
                			
                }
                operateObject = "InvoiceVO[recipient:" + invoiceVO.getRecipient() + "]";
                String result = facade.applyInvoice(user, invoiceVO);
                if(!Constants.SUCCESS.equals(result)) {
                    this.fillActionResult(result);
                    facade.insertOperationLog(user,"申请开发票错误:"+result,"申请开发票",Constants.RESULT_FAILURE,operateObject);
                } else {
                    facade.insertOperationLog(user,"申请开发票","申请开发票",Constants.RESULT_SUCESS,operateObject);
                }
            } catch (Exception e) {
                facade.insertOperationLog(user,"申请开发票错误:"+e.getMessage(),"申请开发票",Constants.RESULT_FAILURE,operateObject);
                this.dealThrow(Constants.INVOICE_EXCEPTION, "发票申请异常", e, logger);
            }
            
            return null;
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit applyInvoice method.takeTime:" + takeTime + "ms");
		}
        return "login";
    }
    
    /**
     * <查询发票历史> 
    * <功能详细描述> 
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public String findInvoiceList() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findInvoiceList method.");			
		}
        invoicePage.setPageNo(page);
        invoicePage.setPageSize(limit);
        Map<String, Object> condition = new HashMap<String, Object>();
        User user = (User)this.getCurrentLoginUser();
        if(user != null) {
            try{
                condition.put("email", user.getEmail());
                invoicePage = facade.findInvoiceRecordList(invoicePage, condition);
                super.fillActionResult(invoicePage);
            } catch(Exception e) {
                this.dealThrow(Constants.INVOICE_EXCEPTION, "发票查询异常", e, logger);
            }
            return null;
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findInvoiceList method.takeTime:" + takeTime + "ms");
		}
        return "login";
    }

    public InvoiceVO getInvoiceVO() {
        return invoiceVO;
    }

    public void setInvoiceVO(InvoiceVO invoiceVO) {
        this.invoiceVO = invoiceVO;
    }
    public Page<Map<String, Object>> getInvoicePage() {
        return invoicePage;
    }
    public void setInvoicePage(Page<Map<String, Object>> invoicePage) {
        this.invoicePage = invoicePage;
    }
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
    
}

/* 
* 文 件 名:  ApplicationInvoiceAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  zhangfeifei 
* 修改时间:  2015年6月29日15:35:43 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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
import com.hisoft.hscloud.common.entity.Excel;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.facade.Facade;
import com.wgdawn.persist.model.ApplicationInvoiceAccount;
import com.wgdawn.persist.more.model.appDetail.MoreAppDetail;
import com.wgdawn.persist.more.model.center.ApplicationInvoiceRecord;
import com.wgdawn.persist.more.model.center.ApplicationInvoiceRecordVO;
import com.wgdawn.service.ApplicationInvoiceRecordService;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  zhangfeifei 
 * @version  [版本号, 2015-6-29 14:11:41] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ApplicationInvoiceAction extends HSCloudAction {

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
    private InvoiceVO invoiceVO;
    private ApplicationInvoiceRecordVO applicationInvoiceRecordVO;
    private Account account;
    private Page<ApplicationInvoiceAccount> applicationInvoiceAccountPage = new Page<ApplicationInvoiceAccount>();
    private ApplicationInvoiceRecord applicationInvoiceRecord;
    
    @Autowired
    private Facade facade;
    
    @Autowired
    private ApplicationInvoiceRecordService applicationInvoiceRecordService;
    /**
     * 发票列表展示 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */

    public void appFindInvoiceList(){
    	long beginRunTime = 0;
    	List<ApplicationInvoiceAccount> list=new ArrayList<ApplicationInvoiceAccount>();
    	Admin admin = (Admin) this.getCurrentLoginUser();
    	
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter appFindInvoiceList method.");			
		}
		applicationInvoiceAccountPage.setPageNo(page);
		applicationInvoiceAccountPage.setPageSize(limit);        
		applicationInvoiceAccountPage.setResult(new ArrayList<ApplicationInvoiceAccount>());
        Map<String,Object> countMap = new HashMap<String,Object>();
		Map<String,Object> queryMap = new HashMap<String,Object>();
		countMap.put("username",admin);
		countMap.put("invoiceNo",invoiceNo);
		countMap.put("status", status);
		countMap.put("query", query);
		countMap.put("fromDateApply", fromDateApply);
		countMap.put("toDateApply",toDateApply);
		countMap.put("fromDateBill",fromDateBill);
		countMap.put("toDateBill",toDateBill);
		countMap.put("fromDateSend",fromDateSend);
		countMap.put("toDateSend",toDateSend);
		applicationInvoiceAccountPage.setTotalCount(applicationInvoiceRecordService.selectAppDetailCount(countMap));
		queryMap.putAll(countMap);
		queryMap.put("start",(page - 1) * limit);
		queryMap.put("size", limit);
        list=applicationInvoiceRecordService.selectAppDetailInfo(queryMap);
        applicationInvoiceAccountPage.setResult(list);
        fillActionResult(applicationInvoiceAccountPage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAppDetailListPage method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    /**
     * 应用发票的审核
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void appAuditApplicationInvoice() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter appAuditApplicationInvoice method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	   if(null!=applicationInvoiceRecord){
            		   Date today = Calendar.getInstance().getTime();
            		   applicationInvoiceRecord.setBillingTime(today);
            		   System.out.println("applicationInvoiceRecord.getBillingTime()===="+applicationInvoiceRecord.getBillingTime());
            		   applicationInvoiceRecord.setUserId(Constants.ADMINISTRATOR_ID);//管理员id为0
            		   applicationInvoiceRecord.setStatus("1");//上架
            		   applicationInvoiceRecordService.updateApplicationInvoiceInfo(applicationInvoiceRecord);
            	   }
            } catch(Exception e) {
                facade.insertOperationLog(admin,"审核应用发票错误:"+e.getMessage(),"审核应用发票错误",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit appAuditApplicationInvoice method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 应用发票的审核
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void appmodifyApplicationInvoice() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter modifyApplicationInvoiceInfo method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	   if(null!=applicationInvoiceRecord){
            		  applicationInvoiceRecord.setUserId(Constants.ADMINISTRATOR_ID);//管理员id为0
            		  applicationInvoiceRecordService.modifyApplicationInvoiceInfo(applicationInvoiceRecord);
            	   }
            } catch(Exception e) {
                facade.insertOperationLog(admin,"审核应用发票错误:"+e.getMessage(),"审核应用发票错误",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit appAuditApplicationInvoice method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 应用发票的拒绝审核
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void appApprovalInvoiceFail() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter appApprovalInvoiceFail method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	   if(null!=applicationInvoiceRecord){
            		   	SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            			Date date = new Date();  
           	        	String today = f.format(date);  
           	        	applicationInvoiceRecord.setBillingTime(date);
           	        	System.out.println("applicationInvoiceRecord.getBillingTime()===="+applicationInvoiceRecord.getBillingTime());
            		  
            		   
            		   applicationInvoiceRecord.setUserId(Constants.ADMINISTRATOR_ID);//管理员id为0
            		   applicationInvoiceRecord.setStatus("2");//上架
            		   applicationInvoiceRecordService.updateApplicationInvoiceInfo(applicationInvoiceRecord);
            	   }
            } catch(Exception e) {
                facade.insertOperationLog(admin,"拒绝审核应用发票错误:"+e.getMessage(),"拒绝审核应用发票错误",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit appApprovalInvoiceFail method.takeTime:" + takeTime + "ms");
		}
    }
    


	/**
	 * 导出应用发票列表
	 */
	public void excelExportApplicationInvoice() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter excelExportApplicationInvoice method.");			
		}
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest  request = ServletActionContext.getRequest();
			//查询登录管理员id,作为查询导出条件
			Admin admin = (Admin)request.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
			response.reset();
			String exportFileName = "ApplicationInvoiceList.xls";

			String mimeType = "application/msexcel;charset=UTF-8";
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ exportFileName + "\"");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setDateHeader("Expires",(System.currentTimeMillis() + 1000));
			Map<String,Object> queryMap = new HashMap<String,Object>();
			queryMap.put("domainId",domainId);
			queryMap.put("status",status);
			Excel excel = facade.excelExportApplicationInvoice(queryMap);
			ServletOutputStream os = response.getOutputStream();
			excel.getWb().write(os);
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit excelExportApplicationInvoice method.takeTime:" + takeTime + "ms");
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


	/**
	 * @return the applicationInvoiceAccountPage
	 */
	public Page<ApplicationInvoiceAccount> getApplicationInvoiceAccountPage() {
		return applicationInvoiceAccountPage;
	}


	/**
	 * @param applicationInvoiceAccountPage the applicationInvoiceAccountPage to set
	 */
	public void setApplicationInvoiceAccountPage(
			Page<ApplicationInvoiceAccount> applicationInvoiceAccountPage) {
		this.applicationInvoiceAccountPage = applicationInvoiceAccountPage;
	}


	/**
	 * @return the applicationInvoiceRecord
	 */
	public ApplicationInvoiceRecord getApplicationInvoiceRecord() {
		return applicationInvoiceRecord;
	}


	/**
	 * @param applicationInvoiceRecord the applicationInvoiceRecord to set
	 */
	public void setApplicationInvoiceRecord(
			ApplicationInvoiceRecord applicationInvoiceRecord) {
		this.applicationInvoiceRecord = applicationInvoiceRecord;
	}


	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	/**
	 * @return the applicationInvoiceRecordVO
	 */
	public ApplicationInvoiceRecordVO getApplicationInvoiceRecordVO() {
		return applicationInvoiceRecordVO;
	}


	/**
	 * @param applicationInvoiceRecordVO the applicationInvoiceRecordVO to set
	 */
	public void setApplicationInvoiceRecordVO(
			ApplicationInvoiceRecordVO applicationInvoiceRecordVO) {
		this.applicationInvoiceRecordVO = applicationInvoiceRecordVO;
	}
    
    
    
}

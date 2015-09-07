/* 
* 文 件 名:  AnnouncementAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-25 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.web.facade.Facade;
import com.wgdawn.persist.model.AppWorkOrder;
import com.wgdawn.persist.model.AppWorkOrderType;
import com.wgdawn.persist.more.model.center.AppWorkOrderVo;
import com.wgdawn.service.WorkOrderService;
/** 
 * 工单管理
 * <功能详细描述> 
 * 
 * @author liutao
 * @version  [版本号, 2013-1-25] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class WorkOrderAction extends HSCloudAction {

    /** 
    * 注释内容 
    */
    private static final long serialVersionUID = 7444751807265744225L;
    
    @Autowired
    private Facade facade;
    @Autowired
    private WorkOrderService workOrderService;
    private Page<AppWorkOrderVo> appWorkOrderPage = new Page<AppWorkOrderVo>();
    private Logger logger = Logger.getLogger(this.getClass());
    private int page;
    private int limit;
    private String query;
    private AppWorkOrder  appWorkOrderVO;
    private String workOrderType;
    private String workOrderStatus;
    private String replyFlag;
    /**
     * 创建工单
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void saveWorkOrder(){
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter saveWorkOrder method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	if(appWorkOrderVO!=null){
            		if(appWorkOrderVO.getId()!=null&&!appWorkOrderVO.getId().equals("")){
            			if(null!=replyFlag){
                    		if(replyFlag.equals("1")){
                    			//管理员 id存0
                    			appWorkOrderVO.setStatus(Constants.WORK_ORDER_STATUS_TWO);
                    			appWorkOrderVO.setReplyStaffId(Constants.ADMINISTRATOR_ID);
                    			appWorkOrderVO.setReplyDate(new Date());
                    		}
                    	}
            			appWorkOrderVO.setUpdateDate(new Date());
            			//管理员id为0
            			appWorkOrderVO.setUpdateby(String.valueOf(Constants.ADMINISTRATOR_ID));
            			workOrderService.updateWorkOrderByID(appWorkOrderVO);
            			//facade.insertOperationLog(admin,"修改工单","修改工单",Constants.RESULT_SUCESS);
            		}else{
            			appWorkOrderVO.setCreateDate(new Date());
                    	appWorkOrderVO.setStatus(Constants.WORK_ORDER_STATUS_ONE);
                    	appWorkOrderVO.setDeleteFlag("N");
                    	appWorkOrderVO.setUserId(Constants.ADMINISTRATOR_ID);
                    	appWorkOrderVO.setAtuoManual(Constants.WORK_ORDER_CREATE_MANUAL);//1 代表手动
                    	appWorkOrderVO.setAppOrNot(Constants.WORK_ORDER_ZERO);//0 代表云商城
                        workOrderService.insertWorkOrder(appWorkOrderVO);
                       // facade.insertOperationLog(admin,"创建工单","创建工单",Constants.RESULT_SUCESS);
            		}
            	}
            	
            	
            } catch(Exception e) {
                //facade.insertOperationLog(admin,"创建工单:"+e.getMessage(),"创建工单",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit saveWorkOrder method.takeTime:" + takeTime + "ms");
		}
    }
    /**
     * 创建应用工单
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void saveAppWorkOrder(){
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter saveAppWorkOrder method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	if(appWorkOrderVO!=null){
            		if(appWorkOrderVO.getId()!=null&&!appWorkOrderVO.getId().equals("")){
            			if(null!=replyFlag){
                    		if(replyFlag.equals("1")){
                    			//管理员 id存0
                    			appWorkOrderVO.setStatus(Constants.WORK_ORDER_STATUS_TWO);
                    			appWorkOrderVO.setReplyStaffId(Constants.ADMINISTRATOR_ID);
                    			appWorkOrderVO.setReplyDate(new Date());
                    		}
                    	}
            			appWorkOrderVO.setUpdateDate(new Date());
            			//管理员id为0
            			appWorkOrderVO.setUpdateby(String.valueOf(Constants.ADMINISTRATOR_ID));
            			workOrderService.updateWorkOrderByID(appWorkOrderVO);
            			//facade.insertOperationLog(admin,"修改云应用工单","修改云应用工单",Constants.RESULT_SUCESS);
            		}else{
            			appWorkOrderVO.setCreateDate(new Date());
                    	appWorkOrderVO.setStatus(Constants.WORK_ORDER_STATUS_ONE);
                    	appWorkOrderVO.setDeleteFlag("N");
                    	appWorkOrderVO.setUserId(Constants.ADMINISTRATOR_ID);
                    	appWorkOrderVO.setAtuoManual(Constants.WORK_ORDER_CREATE_MANUAL);//1 代表手动
                    	appWorkOrderVO.setAppOrNot(Constants.WORK_ORDER_ONE);//0 代表云商城
                        workOrderService.insertWorkOrder(appWorkOrderVO);
                        //facade.insertOperationLog(admin,"创建云应用工单","创建云应用工单",Constants.RESULT_SUCESS);
            		}
            	}
            	
            } catch(Exception e) {
                //facade.insertOperationLog(admin,"创建云应用工单:"+e.getMessage(),"创建云应用工单",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit saveAppWorkOrder method.takeTime:" + takeTime + "ms");
		}
    }
    /**
     * 查询云商城工单信息列表
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void findWorkOrderPage(){
    	long beginRunTime = 0;
    	List<AppWorkOrderVo> list=new ArrayList<AppWorkOrderVo>();
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findWorkOrderPage method.");			
		}
		appWorkOrderPage.setPageNo(page);
		appWorkOrderPage.setPageSize(limit);        
        /*if (StringUtils.isNotBlank(query)) {
            try {
                query = new String(query.getBytes("iso8859_1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }*/
        appWorkOrderPage.setResult(new ArrayList<AppWorkOrderVo>());
        Map<String,Object> countMap = new HashMap<String,Object>();
		Map<String,Object> queryMap = new HashMap<String,Object>();
		countMap.put("workOrderType", workOrderType);
		countMap.put("status", workOrderStatus);
		countMap.put("appOrNot", Constants.WORK_ORDER_ZERO);//云商城
		appWorkOrderPage.setPageNo(page);
		appWorkOrderPage.setPageSize(limit); 
		appWorkOrderPage.setTotalCount(workOrderService.countWorkOrderByParameters(countMap));
		queryMap.putAll(countMap);
		queryMap.put("start",(page - 1) * limit);
		queryMap.put("size", limit);
        list=workOrderService.queryWorkOrderByParameters(queryMap);
        appWorkOrderPage.setResult(list);
        fillActionResult(appWorkOrderPage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findWorkOrderPage method.takeTime:" + takeTime + "ms");
		}
    }
    /**
     * 查询应用商城工单信息列表
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void findAppWorkOrderPage(){
    	long beginRunTime = 0;
    	List<AppWorkOrderVo> list=new ArrayList<AppWorkOrderVo>();
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAppWorkOrderPage method.");			
		}
		appWorkOrderPage.setPageNo(page);
		appWorkOrderPage.setPageSize(limit);        
       /* if (StringUtils.isNotBlank(query)) {
            try {
                query = new String(query.getBytes("iso8859_1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }*/
        appWorkOrderPage.setResult(new ArrayList<AppWorkOrderVo>());
        Map<String,Object> countMap = new HashMap<String,Object>();
		Map<String,Object> queryMap = new HashMap<String,Object>();
		countMap.put("workOrderType", workOrderType);
		countMap.put("status", workOrderStatus);
		countMap.put("appOrNot", Constants.WORK_ORDER_ONE);//应用商城
		appWorkOrderPage.setPageNo(page);
		appWorkOrderPage.setPageSize(limit); 
		appWorkOrderPage.setTotalCount(workOrderService.countWorkOrderByParameters(countMap));
		queryMap.putAll(countMap);
		queryMap.put("start",(page - 1) * limit);
		queryMap.put("size", limit);
        list=workOrderService.queryWorkOrderByParameters(queryMap);
        appWorkOrderPage.setResult(list);
        fillActionResult(appWorkOrderPage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAppWorkOrderPage method.takeTime:" + takeTime + "ms");
		}
    }
    /**
     * 工单删除 
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void delWorkOrder() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delWorkOrder method.");			
		}
		Admin admin=null;
        try{
            admin = (Admin) super.getCurrentLoginUser();
            if(appWorkOrderVO!=null){
        		if(appWorkOrderVO.getId()!=null&&!appWorkOrderVO.getId().equals("")){
        			appWorkOrderVO.setUpdateDate(new Date());
        			//管理员id为0
        			appWorkOrderVO.setUpdateby(String.valueOf(Constants.ADMINISTRATOR_ID));
        			appWorkOrderVO.setDeleteFlag("Y");
        			workOrderService.updateWorkOrderByID(appWorkOrderVO);
        			// facade.insertOperationLog(admin,"删除工单","删除工单",Constants.RESULT_SUCESS);
        		}
        	}
        } catch(Exception e) {
           // facade.insertOperationLog(admin,"删除工单:"+e.getMessage(),"删除工单",Constants.RESULT_FAILURE);
            this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION,e, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delWorkOrder method.takeTime:" + takeTime + "ms");
		}
    }

    /**
     * 获取Vm工单类型
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void getVmWorkOrderTypes() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllDomain method.");			
		}
        try{
        	Map<String,String> paramMap = new HashMap<String,String>();
    		paramMap.put("website", "houtai");
    		paramMap.put("vm_or_app", "vm");
        	List<AppWorkOrderType> list = facade.getWorkOrderTpye(paramMap);
            this.fillActionResult(list);
        } catch(Exception ex) {
            this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION,ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllDomain method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 获取app工单类型
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void getAppWorkOrderTypes() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAllDomain method.");			
		}
        try{
        	Map<String,String> paramMap = new HashMap<String,String>();
    		paramMap.put("website", "houtai");
    		paramMap.put("vm_or_app", "app");
        	List<AppWorkOrderType> list = facade.getWorkOrderTpye(paramMap);
            this.fillActionResult(list);
        } catch(Exception ex) {
            this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION,ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAllDomain method.takeTime:" + takeTime + "ms");
		}
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
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
	public Page<AppWorkOrderVo> getAppWorkOrderPage() {
		return appWorkOrderPage;
	}
	public void setAppWorkOrderPage(Page<AppWorkOrderVo> appWorkOrderPage) {
		this.appWorkOrderPage = appWorkOrderPage;
	}
	public AppWorkOrder getAppWorkOrderVO() {
		return appWorkOrderVO;
	}
	public void setAppWorkOrderVO(AppWorkOrder appWorkOrderVO) {
		this.appWorkOrderVO = appWorkOrderVO;
	}
	public String getWorkOrderType() {
		return workOrderType;
	}
	public void setWorkOrderType(String workOrderType) {
		this.workOrderType = workOrderType;
	}
	public String getWorkOrderStatus() {
		return workOrderStatus;
	}
	public void setWorkOrderStatus(String workOrderStatus) {
		this.workOrderStatus = workOrderStatus;
	}
	public String getReplyFlag() {
		return replyFlag;
	}
	public void setReplyFlag(String replyFlag) {
		this.replyFlag = replyFlag;
	}
}

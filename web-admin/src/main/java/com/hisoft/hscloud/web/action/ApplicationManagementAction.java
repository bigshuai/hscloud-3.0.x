package com.hisoft.hscloud.web.action; 
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;
import org.springside.modules.web.struts2.Struts2Utils;

import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.service.IServiceCatalogService;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.PropertiesUtils;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.hisoft.hscloud.web.facade.Facade;
import com.ibm.icu.text.SimpleDateFormat;
import com.wgdawn.common.SystemParameters;
import com.wgdawn.persist.model.AppApplicationDivide;
import com.wgdawn.persist.model.AppCouponPolicy;
import com.wgdawn.persist.model.AppPriceSystem;
import com.wgdawn.persist.model.AppUploadMirror;
import com.wgdawn.persist.model.Application;
import com.wgdawn.persist.model.ApplicationCategory;
import com.wgdawn.persist.model.ApplicationDetail;
import com.wgdawn.persist.model.Material;
import com.wgdawn.persist.model.PriceCloudThreshold;
import com.wgdawn.persist.more.model.MoreSupplierUser;
import com.wgdawn.persist.more.model.appDetail.MoreAppDetail;
import com.wgdawn.persist.more.model.priceSystem.MoreAppPriceSystem;
import com.wgdawn.service.ApplicationCategoryService;
import com.wgdawn.service.IssueApplicationService;
/** 
 * 应用管理
 * <功能详细描述> 
 * 
 * @author liutao
 * @version  [版本号, 2015-6-1] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ApplicationManagementAction extends HSCloudAction {
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(this.getClass());	
	@Autowired
	private ApplicationCategoryService applicationCategoryService;
	
    @Autowired
    private Facade facade;
    
    @Autowired
    private IssueApplicationService issueApplicationService;
    
    
    @Autowired
	private IServiceCatalogService serviceCatalogService;
    
	private Integer nodeId;
	
	private ApplicationCategory applicationCategory;
	
    private Page<MoreAppDetail> appDetailPage = new Page<MoreAppDetail>();
    
    private Page<AppCouponPolicy> appCouponPolicyPage = new Page<AppCouponPolicy>();
    
    private Page<PriceCloudThreshold> appThresholdPage = new Page<PriceCloudThreshold>();
    
    private Page<MoreAppPriceSystem> appPriceSystemPage = new Page<MoreAppPriceSystem>();
    
    private Page<AppUploadMirror> appUploadMirrorPage = new Page<AppUploadMirror>();
    
    private Page<Material> appUploadPhotosPage = new Page<Material>();
   
    private int page;
    
    private int limit;
    
    private String appCategoryId;
    
    private String  appStatus;
    
    private String  appName;
    
    private String appIsCommend;
	
    private AppPriceSystem  appPriceSystemVo;
    
    private AppCouponPolicy  appCouponPolicyVo;
    
    private PriceCloudThreshold priceCloudThresholdVo;
	
    private File file;
    
	private String fileFileName;
	
	private File appLogo;
	    
    private String appLogoFileName;
    
    private File weChat;
    
    private String weChatFileName;
	
	private AppUploadMirror appUploadMirror;
	
	private Application applicationVo;
	
	private ApplicationDetail applicationDetail;
	
	private Integer appId;
	
	private String duration;
	
	private Material material;
	
	private AppApplicationDivide applicationDivideVo;
	
	@Autowired
	private UserService userService;
	
    /**
     * 创建/修改菜单
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void saveMenuInfo() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter saveMenuInfo method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	if(applicationCategory!=null){
            		if(applicationCategory.getId()!=null&&!applicationCategory.getId().equals("")){
            			applicationCategory.setUpdateTime(new Date());
            			//管理员id为0
            			applicationCategory.setUpdateId(Constants.ADMINISTRATOR_ID);
            			applicationCategoryService.updateApplicationMenuInfo(applicationCategory);
            			facade.insertOperationLog(admin,"修改菜单","修改菜单",Constants.RESULT_SUCESS);
            			fillActionResult(applicationCategory);
            		}else{
            			applicationCategory.setCreateTime(new Date());
            			applicationCategory.setDeleteFlag(Constants.DELETE_STATUS_ZERO);
            			applicationCategory.setCreateId(Constants.ADMINISTRATOR_ID);
            			applicationCategoryService.insertApplicationMenuInfo(applicationCategory);
                        facade.insertOperationLog(admin,"添加应用菜单","添加应用菜单",Constants.RESULT_SUCESS);
                        fillActionResult(applicationCategory);
            		}
            	}
            	
            	
            } catch(Exception e) {
                facade.insertOperationLog(admin,"创建应用菜单:"+e.getMessage(),"创建应用菜单",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit saveMenuInfo method.takeTime:" + takeTime + "ms");
		}
    }
    
    

    /**
     * 查询该应用是添加了价格体系信息
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void ifPriceSystemInfo() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter ifPriceSystemInfo method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	
        		Map<String,Object> countMap = new HashMap<String,Object>();
        		countMap.put("appId",appId);
        		int i=issueApplicationService.selectPriceSystemByAppidCount(countMap);
        	    if(i>0){
        	    	fillActionResult(true);
        	    }else{
        	    	fillActionResult(false);
        	    }
            	
            } catch(Exception e) {
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ifPriceSystemInfo method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 查询该应用最低的价格
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void findMinPriceByAppId() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findMinPriceByAppId method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
        		Integer i=issueApplicationService.findMinPriceByAppId(appId);
        	    if(i==null){
        	    	fillActionResult(null);
        	    }else{
        	    	fillActionResult(i);
        	    }
            } catch(Exception e) {
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findMinPriceByAppId method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 查询该应用是否添加了时长管理
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void ifDurationSystemInfo() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter ifDurationSystemInfo method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	
        		Map<String,Object> countMap = new HashMap<String,Object>();
        		countMap.put("appId",appId);
        		countMap.put("duration",duration);
        		int i=issueApplicationService.ifDrationExistById(countMap);
        	    if(i>0){
        	    	fillActionResult(false);
        	    }else{
        	    	fillActionResult(true);
        	    }
            	
            } catch(Exception e) {
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ifDurationSystemInfo method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 查询该应用是否被购买过
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void ifApplicationBuyInfo() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter ifApplicationBuyInfo method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	
        		Map<String,Object> countMap = new HashMap<String,Object>();
        		countMap.put("appId",appId);
        		int i=issueApplicationService.selectApplicationBuyCount(countMap);
        	    if(i>0){
        	    	fillActionResult(true);
        	    }else{
        	    	fillActionResult(false);
        	    }
            	
            } catch(Exception e) {
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit ifApplicationBuyInfo method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    
    /**
     * 创建应用
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void saveApplicationInfo() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter saveApplicationInfo method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	    String path=SystemParameters.getUploadResourcePath();
            	    String dbpath=SystemParameters.getUploadResource();
            		if(applicationDetail!=null){
            			  if(null!=applicationDetail.getId()){
            				  applicationDetail.setUpdateTime(new Date());
            				  applicationDetail.setUpdateId(Constants.ADMINISTRATOR_ID);
            				  issueApplicationService.updateApplicationDetailInfo(applicationDetail);
            				 // facade.insertOperationLog(admin,"修改应用信息","修改应用信息",Constants.RESULT_SUCESS);
            			  }else{
            				  applicationDetail.setCreateTime(new Date());
        	              	  applicationDetail.setCreateId(Constants.ADMINISTRATOR_ID);
        	              	  applicationDetail.setDeleteFlag(Constants.DELETE_STATUS_ZERO);
        	              	  issueApplicationService.insertApplicationDetailInfo(applicationDetail);
        	              	  //facade.insertOperationLog(admin,"添加应用信息","添加应用信息",Constants.RESULT_SUCESS);
            			  }
			              	if(applicationVo!=null){
			              	  if(null!=applicationVo.getId()){
			              		applicationVo.setUpdateId(Constants.ADMINISTRATOR_ID);
			              		applicationVo.setUpdateTime(new Date());
			              		issueApplicationService.updateApplicationInfo(applicationVo);
			              	  //  facade.insertOperationLog(admin,"修改应用信息","修改应用信息",Constants.RESULT_SUCESS);
			              	  }else{
			              		  applicationVo.setCreateTime(new Date());
				              	  applicationVo.setCreateId(Constants.ADMINISTRATOR_ID);
				              	  applicationVo.setDeleteFlag(Constants.DELETE_STATUS_ZERO);
				              	  applicationVo.setApplicationDetailId(applicationDetail.getId());
				              	  //applicationVo.setStatus(Constants.APPLIACTION_NO_APPLY);//已申请
				              	  issueApplicationService.insertApplicationInfo(applicationVo);
				              	  AppApplicationDivide  applicationDivide=new AppApplicationDivide();
				              	  applicationDivide.setAppId(applicationVo.getId());
				              	  applicationDivide.setCreateId(Constants.ADMINISTRATOR_ID);
				              	  applicationDivide.setProportion(Constants.APPLICATION_DIVIDE_PROPORTION);
				              	  applicationDivide.setDeleteFlag(Constants.DELETE_STATUS_ZERO);
				              	  applicationDivide.setCreateTime(new Date());
				              	  issueApplicationService.insertApplicationDivide(applicationDivide);
				              	 // facade.insertOperationLog(admin,"添加应用信息","添加应用信息",Constants.RESULT_SUCESS);
			              	  }
			              	}
    	              	}
                    if(appLogo!=null&&appLogoFileName!=null){
                    	 String folderName=getCurrentDay();
                 		 String fileNameStr=getCurrentTime();
                    	if(uploadImage(appLogo,appLogoFileName,path,folderName,fileNameStr)){
	                    	  Map<String, Object> param=new HashMap<String, Object>();
	                    	  param.put("tableId",applicationVo.getId());
	                    	  param.put("tableType",Constants.MATERIAL_STATUS_ONE);
                    		  issueApplicationService.deleteMaterialByTableIdType(param);
                    		  Material material=new Material();
                     		  material.setCreateId(Constants.ADMINISTRATOR_ID);
                     		  material.setCreateTime(new Date());
                     		  material.setFileName(appLogoFileName);
                     		  material.setPath(dbpath+"/"+folderName+"/"+fileNameStr+appLogoFileName.substring(appLogoFileName.lastIndexOf(".")));
                     		  material.setTableType(Constants.MATERIAL_STATUS_ONE); //1-代表应用logo
                     		  material.setFileSize(Integer.parseInt(String.valueOf(appLogo.length()))/1024);
                     		  material.setTableId(applicationVo.getId());
                     		  issueApplicationService.insertMaterialinfo(material);
                    	}
                    }
                    if(weChat!=null&&weChatFileName!=null){
                    	 String folderName=getCurrentDay();
                		 String fileNameStr=getCurrentTime();
                       if(uploadImage(weChat,weChatFileName,path,folderName,fileNameStr)){
                    	  Map<String, Object> param=new HashMap<String, Object>();
	                      param.put("tableId",applicationVo.getId());
	                      param.put("tableType",Constants.MATERIAL_STATUS_TWO);
                 		  issueApplicationService.deleteMaterialByTableIdType(param);
                    	   Material material=new Material();
                  		   material.setCreateId(Constants.ADMINISTRATOR_ID);
                  		   material.setCreateTime(new Date());
                  		   material.setFileName(weChatFileName);
                  		   material.setPath(dbpath+"/"+folderName+"/"+fileNameStr+weChatFileName.substring(weChatFileName.lastIndexOf(".")));
                  		   material.setFileSize(Integer.parseInt(String.valueOf(weChat.length()))/1024);
                  		   material.setTableType(Constants.MATERIAL_STATUS_TWO); //2-应用微信图片
                  		   material.setTableId(applicationVo.getId());
                  		   issueApplicationService.insertMaterialinfo(material);
                    	}
                    }
            	   HttpServletResponse response = ServletActionContext.getResponse();
           	       response.setContentType("text/html");
                   response.getWriter().write("{success:true,applicationId:"+applicationVo.getId()+",applicationDetailId:"+applicationDetail.getId()+",payMethod:"+applicationDetail.getPayMethod()+",needVm:"+applicationVo.getNeedVm()+"}");
           
            	
            } catch(Exception e) {
               // facade.insertOperationLog(admin,"添加应用:"+e.getMessage(),"添加应用",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit saveApplicationInfo method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    /**
     * 保存分账比例
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void saveApplicationDivideInfo() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter saveApplicationDivideInfo method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null){
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        }else{
            try{
        		if(applicationDivideVo!=null){
        			  if(null!=applicationDivideVo.getId()){
        				  applicationDivideVo.setUpdateTime(new Date());
        				  applicationDivideVo.setUpdateId(Constants.ADMINISTRATOR_ID);
        				  issueApplicationService.updateApplicationDivide(applicationDivideVo);
        			  }else{
        				  applicationDivideVo.setCreateTime(new Date());
        				  applicationDivideVo.setCreateId(Constants.ADMINISTRATOR_ID);
        				  applicationDivideVo.setDeleteFlag(Constants.DELETE_STATUS_ZERO);
        				  issueApplicationService.insertApplicationDivide(applicationDivideVo);
        			  }
	              	}
            }catch(Exception e) {
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit saveApplicationDivideInfo method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 发布应用
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void issueApplicationInfo() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter issueApplicationInfo method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	   
	              	if(applicationVo!=null){
	              	  applicationVo.setUpdateTime(new Date());
	              	  applicationVo.setUpdateId(Constants.ADMINISTRATOR_ID);
	              	  applicationVo.setStatus(Constants.APPLIACTION_SUBMIT_APPLY);//已申请
	              	  issueApplicationService.updateApplicationInfo(applicationVo);
	              	  facade.insertOperationLog(admin,"发布应用信息","发布应用信息",Constants.RESULT_SUCESS);
	              	}
            	  /* HttpServletResponse response = ServletActionContext.getResponse();
           	       response.setContentType("text/html");
                   response.getWriter().write("{success:true,applicationId:"+applicationVo.getId()+"}");*/
            } catch(Exception e) {
                facade.insertOperationLog(admin,"发布应用:"+e.getMessage(),"发布应用",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit issueApplicationInfo method.takeTime:" + takeTime + "ms");
		}
    }
	/**
	 * 删除节点
	 */
    public void delApplicationMenu() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delApplicationMenu method.");			
		}
		Admin admin=null;
        try{
            admin = (Admin) super.getCurrentLoginUser();
            if(applicationCategory!=null){
        		if(applicationCategory.getId()!=null&&!applicationCategory.getId().equals("")){
        			applicationCategory.setUpdateTime(new Date());
        			//管理员id为0
        			applicationCategory.setUpdateId(Constants.ADMINISTRATOR_ID);
        			applicationCategory.setDeleteFlag(Constants.DELETE_STATUS_ONE);
        			applicationCategoryService.updateApplicationMenuInfo(applicationCategory);
        			facade.insertOperationLog(admin,"删除菜单","删除菜单",Constants.RESULT_SUCESS);
        		}
        	}
        } catch(Exception e) {
            facade.insertOperationLog(admin,"删除菜单:"+e.getMessage(),"删除菜单",Constants.RESULT_FAILURE);
            this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION,e, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delApplicationMenu method.takeTime:" + takeTime + "ms");
		}
    }

	
	/**
	 * 根据父ID获取菜单信息
	 * @return
	 */
	public String getApplicationMenuByPid() {
		long beginRunTime = 0;
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		setActionResult(null);
		if(nodeId==null||nodeId.equals("")){
			nodeId=0;
		}
		List<ApplicationCategory> list=applicationCategoryService.getApplicationCategoryByPid(nodeId);
		StringBuffer returnStr = new StringBuffer();
		for(ApplicationCategory ac:list){
			returnStr.append("{text:");
			returnStr.append("\"" + ac.getName() + "\"");
		    //returnStr.append(",icon:'images/zoneTree.png'");
			returnStr.append(",expanded:false");
			returnStr.append(",depth:1");
			returnStr.append(",id:");
			returnStr.append("\"" + ac.getId() + "\"");
			returnStr.append(",leaf:false},");
		}
		
		try{
			Struts2Utils.renderText(("[" + returnStr + "]").replaceAll(",]", "]"));
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_HOST_VM_TREE_EXCEPTION,
					"getZoneTree Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getZoneTree method.takeTime:" + takeTime + "ms");
		}
		
		return null;
    }
	
	/**
	 * 根据父ID获取下面的叶子节点
	 * @return
	 */
	public String getNodeTree(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getNodeTree method.");			
		}
		Admin admin = (Admin) super.getCurrentLoginUser();
		if(admin==null){
			super.fillActionResult(Constants.OPTIONS_TIMEOUT);
			return null;
		}
		setActionResult(null);		
		List<ApplicationCategory> list=applicationCategoryService.getApplicationCategoryByPid(nodeId);
		StringBuffer returnStr = new StringBuffer();
		for(ApplicationCategory ac:list){
			returnStr.append("{text:");
			returnStr.append("\"" + ac.getName() + "\"");
			//returnStr.append(",icon:'images/nodeTree.png'");
			returnStr.append(",expanded:false");
			returnStr.append(",depth:2");
			returnStr.append(",id:");
			returnStr.append("\"" + ac.getId() + "\"");
			returnStr.append(",leaf:true},");
		}
		
		try{
			Struts2Utils.renderText(("[" + returnStr + "]").replaceAll(",]", "]"));
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.MONITOR_HOST_VM_TREE_EXCEPTION,
					"getNodeTree Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getNodeTree method.takeTime:" + takeTime + "ms");
		}
		return null;
	}
	
	
	
	 /**
     * 查询公告页 
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void findAppDetailListPage(){
    	long beginRunTime = 0;
    	List<MoreAppDetail> list=new ArrayList<MoreAppDetail>();
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAppDetailListPage method.");			
		}
		appDetailPage.setPageNo(page);
		appDetailPage.setPageSize(limit);        
		appDetailPage.setResult(new ArrayList<MoreAppDetail>());
        Map<String,Object> countMap = new HashMap<String,Object>();
		Map<String,Object> queryMap = new HashMap<String,Object>();
		countMap.put("appCategoryId",appCategoryId);
		countMap.put("appStatus",appStatus);
		countMap.put("appName",appName);
		countMap.put("appIsCommend",appIsCommend);
		appDetailPage.setTotalCount(issueApplicationService.selectAppDetailCount(countMap));
		queryMap.putAll(countMap);
		queryMap.put("start",(page - 1) * limit);
		queryMap.put("size", limit);
        list=issueApplicationService.selectAppDetailInfo(queryMap);
        appDetailPage.setResult(list);
        fillActionResult(appDetailPage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAppDetailListPage method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 获取应用类型
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void getApplicationTypes() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getApplicationTypes method.");			
		}
        try{
        	List<ApplicationCategory> list = issueApplicationService.selectApplicationCategory();
            this.fillActionResult(list);
        } catch(Exception ex) {
            this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION,ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getApplicationTypes method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 获取应用优惠政策
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void findAppCouponPolicyPage(){
    	long beginRunTime = 0;
    	List<AppCouponPolicy> list=new ArrayList<AppCouponPolicy>();
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAppCouponPolicyPage method.");			
		}
		appCouponPolicyPage.setPageNo(page);
		appCouponPolicyPage.setPageSize(limit);        
		appCouponPolicyPage.setResult(new ArrayList<AppCouponPolicy>());
		Map<String,Object> countMap = new HashMap<String,Object>();
	    Map<String,Object> queryMap = new HashMap<String,Object>();
		countMap.put("appId",appId);
		appCouponPolicyPage.setTotalCount(issueApplicationService.selectCouponPolicyByAppidCount(countMap));
		queryMap.putAll(countMap);
		queryMap.put("start",(page - 1) * limit);
		queryMap.put("size", limit);
        list=issueApplicationService.selectCouponPolicyByAppid(queryMap);
        appCouponPolicyPage.setResult(list);
        fillActionResult(appCouponPolicyPage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAppCouponPolicyPage method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    /**
     * 获取应用时长
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void findDurationManagementPage(){
    	long beginRunTime = 0;
    	List<PriceCloudThreshold> list=new ArrayList<PriceCloudThreshold>();
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findDurationManagementPage method.");			
		}
		appThresholdPage.setPageNo(page);
		appThresholdPage.setPageSize(limit);        
		appThresholdPage.setResult(new ArrayList<PriceCloudThreshold>());
		Map<String,Object> countMap = new HashMap<String,Object>();
	    Map<String,Object> queryMap = new HashMap<String,Object>();
		countMap.put("appId",appId);
		appThresholdPage.setTotalCount(issueApplicationService.selectDurationByAppidCount(countMap));
		queryMap.putAll(countMap);
		queryMap.put("start",(page - 1) * limit);
		queryMap.put("size", limit);
        list=issueApplicationService.selectDurationByAppid(queryMap);
        appThresholdPage.setResult(list);
        fillActionResult(appThresholdPage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findDurationManagementPage method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    /**
     * 获取应用价格体系
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void findPriceSystemPage(){
    	long beginRunTime = 0;
    	List<MoreAppPriceSystem> list=new ArrayList<MoreAppPriceSystem>();
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findPriceSystemPage method.");			
		}
		appPriceSystemPage.setPageNo(page);
		appPriceSystemPage.setPageSize(limit);        
		appPriceSystemPage.setResult(new ArrayList<MoreAppPriceSystem>());
		Map<String,Object> countMap = new HashMap<String,Object>();
	    Map<String,Object> queryMap = new HashMap<String,Object>();
		countMap.put("appId",appId);
		appThresholdPage.setTotalCount(issueApplicationService.selectPriceSystemByAppidCount(countMap));
		queryMap.putAll(countMap);
		queryMap.put("start",(page - 1) * limit);
		queryMap.put("size", limit);
        list=issueApplicationService.selectPriceSystemByAppid(queryMap);
        appPriceSystemPage.setResult(list);
        fillActionResult(appPriceSystemPage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findPriceSystemPage method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 获取所有套餐
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void findAllScInfo() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAllScInfo method.");			
		}
        try{
        	List<ServiceCatalog> list =serviceCatalogService.getAll(null,"defaultBrand",1L,null);
            this.fillActionResult(list);
        } catch(Exception ex) {
            this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION,ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAllScInfo method.takeTime:" + takeTime + "ms");
		}
    }
    /**
     * 保存价格体系信息
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void savePriceSysteminfo() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter savePriceSysteminfo method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	if(appPriceSystemVo!=null){
        		    appPriceSystemVo.setCreateTime(new Date());
        		    appPriceSystemVo.setCreateId(Constants.ADMINISTRATOR_ID);//"0" 代表管理员
        		    appPriceSystemVo.setDeleteFlag(Constants.DELETE_STATUS_ZERO);
                    int i=issueApplicationService.insertPriceSystemByApp(appPriceSystemVo);
                  //  facade.insertOperationLog(admin,"创建价格体系","创建价格体系",Constants.RESULT_SUCESS);
                    if(i>0){
                         Application appVo=new Application();
                         appVo=issueApplicationService.findApplication(appPriceSystemVo.getAppId());
                         if(appVo.getStatus().equals("0")){
                        	 appVo.setStatus(Constants.APPLIACTION_NO_APPLY);
                             appVo.setUpdateId(Constants.ADMINISTRATOR_ID);
                             appVo.setUpdateTime(new Date());
                             issueApplicationService.updateApplicationInfo(appVo);
                         }
                    }
            	}
            } catch(Exception e) {
                facade.insertOperationLog(admin,"创建价格体系:"+e.getMessage(),"创建价格体系",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit savePriceSysteminfo method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    /**
     * 删除价格体系信息
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void delPriceSystem() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delPriceSystem method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	if(appPriceSystemVo!=null){
            		if(appPriceSystemVo.getId()!=null){
            			appPriceSystemVo.setUpdateTime(new Date());
            		    appPriceSystemVo.setUpdateId(Constants.ADMINISTRATOR_ID); //"0" 代表管理员
            		    appPriceSystemVo.setDeleteFlag(Constants.DELETE_STATUS_ONE);
                        issueApplicationService.updatePriceSystemByApp(appPriceSystemVo);
                        facade.insertOperationLog(admin,"删除价格体系","删除价格体系",Constants.RESULT_SUCESS);
            		}
            	}
            }catch(Exception e){
                facade.insertOperationLog(admin,"删除价格体系:"+e.getMessage(),"删除价格体系",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delPriceSystem method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    /**
     * 保存优惠政策信息
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void saveCouponPolicyinfo() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter saveCouponPolicyinfo method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	if(appCouponPolicyVo!=null){
            		appCouponPolicyVo.setCreateTime(new Date());
            		appCouponPolicyVo.setCreateId(Constants.ADMINISTRATOR_ID);//"0" 代表管理员
            		appCouponPolicyVo.setDeleteFlag(Constants.DELETE_STATUS_ZERO);
                    issueApplicationService.insertCouponPolicyByApp(appCouponPolicyVo);
                    facade.insertOperationLog(admin,"创建优惠政策","创建优惠政策",Constants.RESULT_SUCESS);
            	}
            } catch(Exception e) {
                facade.insertOperationLog(admin,"创建优惠政策:"+e.getMessage(),"创建优惠政策",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit saveCouponPolicyinfo method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 删除优惠政策信息
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void delCouponPolicy() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delCouponPolicy method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	if(appCouponPolicyVo!=null){
            		if(appCouponPolicyVo.getId()!=null){
            			appCouponPolicyVo.setUpdateTime(new Date());
            			appCouponPolicyVo.setUpdateId(Constants.ADMINISTRATOR_ID); //"0" 代表管理员
            			appCouponPolicyVo.setDeleteFlag(Constants.DELETE_STATUS_ONE);
                        issueApplicationService.updateCouponPolicyByApp(appCouponPolicyVo);
                        facade.insertOperationLog(admin,"删除优惠政策","删除优惠政策",Constants.RESULT_SUCESS);
            		}
            	}
            }catch(Exception e){
                facade.insertOperationLog(admin,"删除优惠政策:"+e.getMessage(),"删除优惠政策",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delCouponPolicy method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 保存时长管理信息
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void saveDurationManagementInfo() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter saveDurationManagementInfo method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	if(priceCloudThresholdVo!=null){
            		priceCloudThresholdVo.setCreateTime(new Date());
            		priceCloudThresholdVo.setCreateId(Constants.ADMINISTRATOR_ID);//"0" 代表管理员
            		priceCloudThresholdVo.setDeleteFlag(Constants.DELETE_STATUS_ZERO);
                    issueApplicationService.insertPriceThresholdByApp(priceCloudThresholdVo);
                    facade.insertOperationLog(admin,"创建时长管理","创建时长管理",Constants.RESULT_SUCESS);
            	}
            } catch(Exception e) {
                facade.insertOperationLog(admin,"创建时长管理:"+e.getMessage(),"创建时长管理",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit saveDurationManagementInfo method.takeTime:" + takeTime + "ms");
		}
    }
    /**
     * 删除时长管理信息
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void delDurationManagement() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delDurationManagement method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	if(priceCloudThresholdVo!=null){
            		if(priceCloudThresholdVo.getId()!=null){
            			priceCloudThresholdVo.setUpdateTime(new Date());
            			priceCloudThresholdVo.setUpdateId(Constants.ADMINISTRATOR_ID); //"0" 代表管理员
            			priceCloudThresholdVo.setDeleteFlag(Constants.DELETE_STATUS_ONE);
                        issueApplicationService.updatePriceThresholdByApp(priceCloudThresholdVo);
                        facade.insertOperationLog(admin,"删除时长管理","删除时长管理",Constants.RESULT_SUCESS);
            		}
            	}
            }catch(Exception e){
                facade.insertOperationLog(admin,"删除时长管理:"+e.getMessage(),"删除时长管理",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delDurationManagement method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 查询应用镜像列表
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void findAppUploadMirrorListPage(){
    	long beginRunTime = 0;
    	List<AppUploadMirror> list=new ArrayList<AppUploadMirror>();
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAppUploadMirrorListPage method.");			
		}
		appUploadMirrorPage.setPageNo(page);
		appUploadMirrorPage.setPageSize(limit);        
		appUploadMirrorPage.setResult(new ArrayList<AppUploadMirror>());
        Map<String,Object> countMap = new HashMap<String,Object>();
		Map<String,Object> queryMap = new HashMap<String,Object>();
		countMap.put("appId",appId);
		appUploadMirrorPage.setTotalCount(issueApplicationService.selectAppUploadMirrorByAppidCount(countMap));
		queryMap.putAll(countMap);
		queryMap.put("start",(page - 1) * limit);
		queryMap.put("size", limit);
        list=issueApplicationService.selectAppUploadMirrorByAppid(queryMap);
        appUploadMirrorPage.setResult(list);
        fillActionResult(appUploadMirrorPage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAppUploadMirrorListPage method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    
    
    /**
     * 查询应用界面截图列表
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void findAppUploadPhotosListPage(){
    	long beginRunTime = 0;
    	List<Material> list=new ArrayList<Material>();
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAppUploadPhotosListPage method.");			
		}
		appUploadPhotosPage.setPageNo(page);
		appUploadPhotosPage.setPageSize(limit);        
		appUploadPhotosPage.setResult(new ArrayList<Material>());
        Map<String,Object> countMap = new HashMap<String,Object>();
		Map<String,Object> queryMap = new HashMap<String,Object>();
		countMap.put("appId",appId);
		countMap.put("appType","3");
		appUploadMirrorPage.setTotalCount(issueApplicationService.selectMaterialByTypeCount(countMap));
		queryMap.putAll(countMap);
		queryMap.put("start",(page - 1) * limit);
		queryMap.put("size", limit);
        list=issueApplicationService.selectMaterialByType(queryMap);
        appUploadPhotosPage.setResult(list);
        fillActionResult(appUploadPhotosPage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAppUploadPhotosListPage method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 删除应用镜像
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void delUploadMirror() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delUploadMirror method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	if(appUploadMirror!=null){
            		if(appUploadMirror.getId()!=null){
                        if(null!=appUploadMirror.getPath()&&!appUploadMirror.getPath().equals("")){
                        	if(delFileByPath(appUploadMirror.getPath())){
                        		appUploadMirror.setUpdateTime(new Date());
                    			appUploadMirror.setUpdateId(Constants.ADMINISTRATOR_ID); //"0" 代表管理员
                    			appUploadMirror.setDeleteFlag(Constants.DELETE_STATUS_ONE);
                                issueApplicationService.updateAppUploadMirrorInfo(appUploadMirror);
                                facade.insertOperationLog(admin,"删除应用镜像","删除应用镜像",Constants.RESULT_SUCESS);
                        	}
                        }
            		}
            		
            	}
            }catch(Exception e){
                facade.insertOperationLog(admin,"删除应用镜像:"+e.getMessage(),"删除应用镜像",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delUploadMirror method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    /**
     * 删除应用截图
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void delUploadPhoto() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delUploadPhoto method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	if(material!=null){
            		if(material.getId()!=null){
                        if(null!=material.getPath()&&!material.getPath().equals("")){
                        	if(delFileByPath(SystemParameters.getUploadRoot()+material.getPath())){
                                issueApplicationService.deleteMaterialById(material.getId());
                                facade.insertOperationLog(admin,"删除应用截图","删除应用截图",Constants.RESULT_SUCESS);
                        	}
                        }
            		}
            	}
            }catch(Exception e){
                facade.insertOperationLog(admin,"删除应用截图:"+e.getMessage(),"删除应用截图",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delUploadPhoto method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    /**
     * 删除镜像文件
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public static boolean delFileByPath(String path) {
        boolean flag = false;
        File Mfile = new File(path);
        if (!Mfile.exists()) {
         return flag;
        }
        if (Mfile.isFile()) {
        	Mfile.delete();
        	 flag = true;
         }
        return flag;
     }
    
    
    /**
	 * <上传文件> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void uploadMirror() {
	    Map<String, String> map = PropertiesUtils.getPropertiesMap(
                "image.properties", PropertiesUtils.IMAGE_MAP);
        String path = map.get("path");
	    InputStream is = null;
	    BufferedInputStream bin = null;
	    OutputStream os = null;
	    BufferedOutputStream bout = null;
	    String newFileName=getCurrentTime();
	    HttpSession session = getSession();
	    Admin admin=(Admin) super.getCurrentLoginUser();
        @SuppressWarnings("unchecked")
        Map<String, String> stateMap = (Map<String, String>)session.getAttribute("state");
        try {
            is = new FileInputStream(file);
            
            bin = new BufferedInputStream(is);
            os = new FileOutputStream(path +newFileName+fileFileName.substring(fileFileName.lastIndexOf(".")));
           
            bout = new BufferedOutputStream(os);
            
            byte buffer[] = new byte[8192];  
            int count = 0;
            BigDecimal total = new BigDecimal(file.length());
            BigDecimal readed = new BigDecimal(0);
            while ((count = bin.read(buffer)) > 0) {
                bout.write(buffer, 0, count);
                readed = readed.add(new BigDecimal(count));
                BigDecimal percent1 = readed.divide(total, 2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(20));
                BigDecimal percent2 = new BigDecimal(80);
                String result = (percent1.add(percent2)).intValue() + "";
                stateMap.put("result", Integer.valueOf(result).toString());
            }
        } catch (FileNotFoundException e) {
            facade.insertOperationLog(admin,"上传文件错误:"+e.getMessage(),"上传文件",Constants.RESULT_FAILURE);
            e.printStackTrace();
        } catch (IOException e) {
            facade.insertOperationLog(admin,"上传文件错误:"+e.getMessage(),"上传文件",Constants.RESULT_FAILURE);
            e.printStackTrace();
        } finally {
            try {
                bout.close();
                os.close();
                bin.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
	     
	    HttpServletResponse response = ServletActionContext.getResponse();
	    response.setContentType("text/html");
	    try {
	    	AppUploadMirror appUploadMirror=new AppUploadMirror();
	    	appUploadMirror.setAppId(Integer.valueOf(appId));//目前写死的
	    	appUploadMirror.setCreateTime(new Date());
	    	appUploadMirror.setCreateId(Constants.ADMINISTRATOR_ID);//0代表管理员
	    	appUploadMirror.setDeleteFlag(Constants.DELETE_STATUS_ZERO);
	    	appUploadMirror.setName(fileFileName);
	    	appUploadMirror.setPath(path +newFileName+fileFileName.substring(fileFileName.lastIndexOf(".")));
	    //	appUploadMirror.setPath(path +fileFileName);
	    	issueApplicationService.insertAppUploadMirrorInfo(appUploadMirror);
            response.getWriter().write("{success:true}");
        } catch (IOException e) {
            facade.insertOperationLog(admin,"上传文件错误:"+e.getMessage(),"上传文件",Constants.RESULT_FAILURE);
            e.printStackTrace();
        }
	    facade.insertOperationLog(admin,"上传文件","上传文件",Constants.RESULT_SUCESS);
	}
	  /**
		* <上传图片> 
		* <功能详细描述>  
		* @see [类、类#方法、类#成员]
		*/
	private boolean uploadImage(File f,String fName,String path,String folderName,String fileNameStr) {
	    InputStream is = null;
	    BufferedInputStream bin = null;
	    OutputStream os = null;
	    BufferedOutputStream bout = null;
	    Admin admin=(Admin) super.getCurrentLoginUser();
    
        try {
            is = new FileInputStream(f);
            bin = new BufferedInputStream(is);
            File ifFolder =new File(path+"/" +folderName);    
            //如果文件夹不存在则创建    
           if  (!ifFolder.exists()&& !ifFolder.isDirectory()){       
        	   ifFolder.mkdir();    
           } 
            os = new FileOutputStream(path+"/" +folderName+"/"+fileNameStr+fName.substring(fName.lastIndexOf(".")));
            bout = new BufferedOutputStream(os);
            byte buffer[] = new byte[8192];  
            int count = 0;
            BigDecimal readed = new BigDecimal(0);
            while ((count = bin.read(buffer)) > 0) {
                bout.write(buffer, 0, count);
                readed = readed.add(new BigDecimal(count));
            }
           
        } catch (FileNotFoundException e) {
            facade.insertOperationLog(admin,"上传图片错误:"+e.getMessage(),"上传图片",Constants.RESULT_FAILURE);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            facade.insertOperationLog(admin,"上传图片错误:"+e.getMessage(),"上传图片",Constants.RESULT_FAILURE);
            e.printStackTrace();
            return false;
        } finally {
            try {
                bout.close();
                os.close();
                bin.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            
        }
	    facade.insertOperationLog(admin,"上传图片","上传图片",Constants.RESULT_SUCESS);
	    return true;
	}
	
	
	
	 /**
     * 创建应用
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void saveUploadPhotosInfo() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter saveUploadPhotosInfo method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	    String path=SystemParameters.getUploadResourcePath();
            	    String dbpath=SystemParameters.getUploadResource();
                    if(file!=null&&fileFileName!=null){
                    	 String folderName=getCurrentDay();
                 		 String fileNameStr=getCurrentTime();
                    	if(uploadImage(file,fileFileName,path,folderName,fileNameStr)){
                    		  Material material=new Material();
                     		  material.setCreateId(Constants.ADMINISTRATOR_ID);
                     		  material.setCreateTime(new Date());
                     		  material.setFileName(fileFileName);
                     		  material.setPath(dbpath+"/"+folderName+"/"+fileNameStr+fileFileName.substring(fileFileName.lastIndexOf(".")));
                     		  material.setTableType(Constants.MATERIAL_STATUS_THREE); //3-代表应用界面截图
                     		  material.setFileSize(Integer.parseInt(String.valueOf(file.length()))/1024);
                     		  material.setTableId(appId);
                     		  issueApplicationService.insertMaterialinfo(material);
                    	}
                    }
            	   HttpServletResponse response = ServletActionContext.getResponse();
           	       response.setContentType("text/html");
                   response.getWriter().write("{success:true}");
            } catch(Exception e) {
                facade.insertOperationLog(admin,"添加应用截图错误:"+e.getMessage(),"添加应用截图错误",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit saveUploadPhotosInfo method.takeTime:" + takeTime + "ms");
		}
    }
	
    /**
     * 审核应用
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void auditApplication() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter auditApplication method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	   if(null!=applicationVo){
            		   applicationVo.setUpdateTime(new Date());
            		   applicationVo.setUpdateId(Constants.ADMINISTRATOR_ID);//管理员id为0
            		   applicationVo.setStatus(Constants.APPLIACTION_UP);//上架
            		   applicationVo.setAuditTime(new Date());
            		   issueApplicationService.updateApplicationInfo(applicationVo);
            	   }
            } catch(Exception e) {
                facade.insertOperationLog(admin,"审核应用错误:"+e.getMessage(),"审核应用错误",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit auditApplication method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 拒绝应用
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void refuseApplication() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter refuseApplication method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	   if(null!=applicationVo){
            		   applicationVo.setUpdateTime(new Date());
            		   applicationVo.setUpdateId(Constants.ADMINISTRATOR_ID);//管理员id为0
            		   applicationVo.setStatus(Constants.APPLIACTION_REFUSE);//拒绝
            		   issueApplicationService.updateApplicationInfo(applicationVo);
            	   }
            } catch(Exception e) {
                facade.insertOperationLog(admin,"拒绝应用错误:"+e.getMessage(),"拒绝应用错误",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit refuseApplication method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    /**
     * 下架应用
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void downApplication() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter downApplication method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	   if(null!=applicationVo){
            		   applicationVo.setUpdateTime(new Date());
            		   applicationVo.setUpdateId(Constants.ADMINISTRATOR_ID);//管理员id为0
            		   applicationVo.setStatus(Constants.APPLIACTION_DOWN);//上架
            		   issueApplicationService.updateApplicationInfo(applicationVo);
            	   }
            } catch(Exception e) {
                facade.insertOperationLog(admin,"下架应用错误:"+e.getMessage(),"下架应用错误",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit downApplication method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 推荐商品应用
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void recommendApplication() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter recommendApplication method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	   if(null!=applicationVo){
            		   applicationVo.setUpdateTime(new Date());
            		   applicationVo.setUpdateId(Constants.ADMINISTRATOR_ID);//管理员id为0
            		   applicationVo.setIsCommend(Constants.APPLIACTION_RECOMMEND_ONE);//推荐商品
            		   issueApplicationService.updateApplicationInfo(applicationVo);
            	   }
            } catch(Exception e) {
                facade.insertOperationLog(admin,"推荐应用错误:"+e.getMessage(),"推荐应用错误",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit recommendApplication method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    /**
     * 删除商品应用
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void deleteApplication() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter deleteApplication method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
            	   if(null!=applicationVo){
            		   applicationVo.setUpdateTime(new Date());
            		   applicationVo.setUpdateId(Constants.ADMINISTRATOR_ID);//管理员id为0
            		   applicationVo.setDeleteFlag(Constants.DELETE_STATUS_ONE);
            		   issueApplicationService.updateApplicationInfo(applicationVo);
            	   }
            } catch(Exception e) {
                facade.insertOperationLog(admin,"推荐应用错误:"+e.getMessage(),"推荐应用错误",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit deleteApplication method.takeTime:" + takeTime + "ms");
		}
    }
    
    
    
    
    /**
     * 获取供应商
     * <功能详细描述>  
     * @see [类、类#方法、类#成员]
     */
    public void getAvailableSupplier() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAvailableSupplier method.");			
		}
        try{
        	Map<String, Object> param=new HashMap<String, Object>();
    		param.put("userStatus", 3);
    		param.put("supplier", 1);
        	List<MoreSupplierUser> suList =issueApplicationService.getAvailableSupplier(param);
            this.fillActionResult(suList);
        } catch(Exception ex) {
            this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION,ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:" + takeTime + "ms");
		}
    }
    
    
	public Integer getNodeId() {
		return nodeId;
	}
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}
	public ApplicationCategory getApplicationCategory() {
		return applicationCategory;
	}
	public void setApplicationCategory(ApplicationCategory applicationCategory) {
		this.applicationCategory = applicationCategory;
	}
	public Page<MoreAppDetail> getAppDetailPage() {
		return appDetailPage;
	}
	public void setAppDetailPage(Page<MoreAppDetail> appDetailPage) {
		this.appDetailPage = appDetailPage;
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
	public String getAppCategoryId() {
		return appCategoryId;
	}
	public void setAppCategoryId(String appCategoryId) {
		this.appCategoryId = appCategoryId;
	}
	public String getAppStatus() {
		return appStatus;
	}
	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}
	public Page<AppCouponPolicy> getAppCouponPolicyPage() {
		return appCouponPolicyPage;
	}
	public void setAppCouponPolicyPage(Page<AppCouponPolicy> appCouponPolicyPage) {
		this.appCouponPolicyPage = appCouponPolicyPage;
	}
	public Page<PriceCloudThreshold> getAppThresholdPage() {
		return appThresholdPage;
	}
	public void setAppThresholdPage(Page<PriceCloudThreshold> appThresholdPage) {
		this.appThresholdPage = appThresholdPage;
	}
	public Page<MoreAppPriceSystem> getAppPriceSystemPage() {
		return appPriceSystemPage;
	}
	public void setAppPriceSystemPage(Page<MoreAppPriceSystem> appPriceSystemPage) {
		this.appPriceSystemPage = appPriceSystemPage;
	}
	public AppPriceSystem getAppPriceSystemVo() {
		return appPriceSystemVo;
	}
	public void setAppPriceSystemVo(AppPriceSystem appPriceSystemVo) {
		this.appPriceSystemVo = appPriceSystemVo;
	}
	public AppCouponPolicy getAppCouponPolicyVo() {
		return appCouponPolicyVo;
	}
	public void setAppCouponPolicyVo(AppCouponPolicy appCouponPolicyVo) {
		this.appCouponPolicyVo = appCouponPolicyVo;
	}
	public PriceCloudThreshold getPriceCloudThresholdVo() {
		return priceCloudThresholdVo;
	}
	public void setPriceCloudThresholdVo(PriceCloudThreshold priceCloudThresholdVo) {
		this.priceCloudThresholdVo = priceCloudThresholdVo;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public AppUploadMirror getAppUploadMirror() {
		return appUploadMirror;
	}
	public void setAppUploadMirror(AppUploadMirror appUploadMirror) {
		this.appUploadMirror = appUploadMirror;
	}
	
	
	
	public Application getApplicationVo() {
		return applicationVo;
	}
	public void setApplicationVo(Application applicationVo) {
		this.applicationVo = applicationVo;
	}
	public ApplicationDetail getApplicationDetail() {
		return applicationDetail;
	}
	public void setApplicationDetail(ApplicationDetail applicationDetail) {
		this.applicationDetail = applicationDetail;
	}
	
	
	
	public File getAppLogo() {
		return appLogo;
	}


	public void setAppLogo(File appLogo) {
		this.appLogo = appLogo;
	}


	public String getAppLogoFileName() {
		return appLogoFileName;
	}


	public void setAppLogoFileName(String appLogoFileName) {
		this.appLogoFileName = appLogoFileName;
	}


	public File getWeChat() {
		return weChat;
	}


	public void setWeChat(File weChat) {
		this.weChat = weChat;
	}


	public String getWeChatFileName() {
		return weChatFileName;
	}


	public void setWeChatFileName(String weChatFileName) {
		this.weChatFileName = weChatFileName;
	}

    
 


	public Integer getAppId() {
		return appId;
	}


	public void setAppId(Integer appId) {
		this.appId = appId;
	}


	/**
	 * Example: getCurrentTime();
	 * 
	 * return 20110820101010222
	 * 
	 * @param 
	 * @return
	 */
	private static String getCurrentTime() {

		return getSystemDateString("yyyyMMddHHmmsssss");
		
	}
	
	private static String getCurrentDay() {

		return getSystemDateString("yyyyMMdd");
		
	}
	
	
	/**
	 * Example: getSystemDate("yyyyMMdd");
	 * 
	 * return 20110820
	 * 
	 * @param format
	 * @return
	 */
	private static String getSystemDateString(String format) {

		return formatDate(new Date(), format);
	}
	/**
	 * Example: formatDate(date, "yyyyMMdd");
	 * 
	 * date = 2011/01/02, return 20110102
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	private static String formatDate(Date date, String format) {

		final SimpleDateFormat sdf = new SimpleDateFormat(format);

		if (date == null)
			return null;

		return sdf.format(date);
	}


	public Material getMaterial() {
		return material;
	}


	public void setMaterial(Material material) {
		this.material = material;
	}



	public AppApplicationDivide getApplicationDivideVo() {
		return applicationDivideVo;
	}



	public void setApplicationDivideVo(AppApplicationDivide applicationDivideVo) {
		this.applicationDivideVo = applicationDivideVo;
	}



	public String getAppName() {
		return appName;
	}



	public void setAppName(String appName) {
		this.appName = appName;
	}



	public String getAppIsCommend() {
		return appIsCommend;
	}



	public void setAppIsCommend(String appIsCommend) {
		this.appIsCommend = appIsCommend;
	}



	public String getDuration() {
		return duration;
	}



	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	
	
	
}

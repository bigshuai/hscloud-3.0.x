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
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hisoft.hscloud.common.entity.LogOperatorType;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;
import org.springside.modules.web.struts2.Struts2Utils;

import com.hisoft.hscloud.bss.billing.entity.Account;
import com.hisoft.hscloud.bss.billing.service.AccountService;
import com.hisoft.hscloud.bss.sla.om.vo.VmRefundOrderItemVo;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceCatalog;
import com.hisoft.hscloud.bss.sla.sc.service.IServiceCatalogService;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.PropertiesUtils;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.crm.usermanager.service.UserService;
import com.ibm.icu.text.SimpleDateFormat;
import com.hisoft.hscloud.vpdc.ops.service.Operation;
import com.hisoft.hscloud.web.facade.Facade;
import com.wgdawn.common.SystemParameters;
import com.wgdawn.persist.model.AppBill;
import com.wgdawn.persist.model.AppCouponPolicy;
import com.wgdawn.persist.model.AppOrder;
import com.wgdawn.persist.model.AppPriceSystem;
import com.wgdawn.persist.model.AppRefund;
import com.wgdawn.persist.model.AppUploadMirror;
import com.wgdawn.persist.model.Application;
import com.wgdawn.persist.model.ApplicationCategory;
import com.wgdawn.persist.model.ApplicationDetail;
import com.wgdawn.persist.model.Material;
import com.wgdawn.persist.model.PriceCloudThreshold;
import com.wgdawn.persist.more.model.appDetail.MoreAppDetail;
import com.wgdawn.persist.more.model.center.AppRefundVO;
import com.wgdawn.persist.more.model.priceSystem.MoreAppPriceSystem;
import com.wgdawn.service.AppOrderService;
import com.wgdawn.service.AppPayService;
import com.wgdawn.service.ApplicationCategoryService;
import com.wgdawn.service.IssueApplicationService;
import com.wgdawn.service.app.user.AppRefundService;
/** 
 * 应用管理
 * <功能详细描述> 
 * 
 * @author feifei
 * @version  [版本号, 2015-7-1] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ApplicationRefundAction extends HSCloudAction {
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(this.getClass());	
	@Autowired
	private ApplicationCategoryService applicationCategoryService;
	
    @Autowired
    private Facade facade;
    @Autowired
    private IssueApplicationService issueApplicationService;
    
    @Autowired
	private Operation operation;
    
    @Autowired
    private AppRefundService appRefundService;
    
    @Autowired
	private IServiceCatalogService serviceCatalogService;
    
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AppOrderService appOrderService;

	@Autowired
	private AppPayService appPayService;
	private String query;
	
	private Integer nodeId;
	
	private ApplicationCategory applicationCategory;
	
	private AppRefundVO appRefundVO;
	
    private Page<MoreAppDetail> appDetailPage= new Page<MoreAppDetail>();
    
    private Page<AppRefundVO> appRefundDetailPage = new Page<AppRefundVO>();
    
    private Page<AppCouponPolicy> appCouponPolicyPage = new Page<AppCouponPolicy>();
    
    private Page<PriceCloudThreshold> appThresholdPage = new Page<PriceCloudThreshold>();
    
    private Page<MoreAppPriceSystem> appPriceSystemPage = new Page<MoreAppPriceSystem>();
    
    private Page<AppUploadMirror> appUploadMirrorPage = new Page<AppUploadMirror>();
    
    private Page<Material> appUploadPhotosPage = new Page<Material>();
   
    private int page;
    
    private int limit;
    
    private int appCategoryId;
    
    private String  appStatus;
	
    private AppPriceSystem  appPriceSystemVo;
    
    private AppCouponPolicy  appCouponPolicyVo;
    
    private PriceCloudThreshold priceCloudThresholdVo;
    
    private AppRefund appRefund;
	
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
	
	@Autowired
	private UserService userService;
    
  
	 /**
     * 查询退款管理列表
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void refundFindAppDetailListPage(){
    	long beginRunTime = 0;
    	List<AppRefundVO> list=new ArrayList<AppRefundVO>();
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter refundFindAppDetailListPage method.");			
		}
		appRefundDetailPage.setPageNo(page);
		appRefundDetailPage.setPageSize(limit);        
		appRefundDetailPage.setResult(new ArrayList<AppRefundVO>());
		if(StringUtils.isNotBlank(query)){
			try {
				query=new String(query.getBytes("iSO8859_1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		//去掉query前后空格
		if (query != null && !query.equals("")) {
				query = query.trim();
		} else {
				query = null;
		}
        Map<String,Object> countMap = new HashMap<String,Object>();
		Map<String,Object> queryMap = new HashMap<String,Object>();
		countMap.put("rejectRefundReason",appCategoryId);
		countMap.put("appStatus",appStatus);
		countMap.put("query", query);
		
		appRefundDetailPage.setTotalCount(appRefundService.selectAdminAppRefundCount(countMap));
		queryMap.putAll(countMap);
		queryMap.put("start",(page - 1) * limit);
		queryMap.put("size", limit);
		
        list=appRefundService.selectAdminAppRefundList(queryMap);
        appRefundDetailPage.setResult(list);
        fillActionResult(appRefundDetailPage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit refundFindAppDetailListPage method.takeTime:" + takeTime + "ms");
		}
    }
    /**
	 * <获取退款订单信息详情> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void getAppRefundOrderInfoForApply() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAppRefundOrderInfoForApply method.");			
		}
		try {
			List<AppRefundVO> result = new ArrayList<AppRefundVO>();
			//result = facade.getVmRefundOrderItemVoForAplly(referenceId,vmRefundId);
			Map<String,Object> queryMap = new HashMap<String,Object>();
			//queryMap.put("referenceId", appRefundVO.getReferenceId());
			queryMap.put("id", appRefundVO.getId());
			
			result=appRefundService.getAppRefundOrderInfoForApply(queryMap);
			super.fillActionResult(result);
		} catch (Exception ex) {
			dealThrow(new HsCloudException("", "getAppRefundOrderInfoForApply异常",
					logger, ex),
					Constants.GET_VM_RELATED_REFUNDABLE_ORDER_ERROR, true);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAppRefundOrderInfoForApply method.takeTime:" + takeTime + "ms");
		}
	}
    

	

	/**
	 * <app退款> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void appRefundApplicationForApply() { 
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter appRefundApplicationForApply method.");			
		}
		int result3=0;
		Admin admin = null;
		double bananceTotal=0.0;
		
		admin= (Admin) super.getCurrentLoginUser();
		if (admin != null && (admin.getType()==null||admin.getType()!=2)) {
				
			Map<String,Object> queryMap = new HashMap<String,Object>();
			queryMap.put("id", appRefundVO.getId());
			queryMap.put("uuid", appRefundVO.getUuid());
			queryMap.put("orderNo", appRefundVO.getOrderNo());
			queryMap.put("referenceId", appRefundVO.getReferenceId());
			queryMap.put("ownerId", appRefundVO.getOwnerId());
			
			//1、VM停机
		    if(appRefundVO.getReferenceId()!=0){
		    	if(!appRefundVO.getVmId().equals("")&&appRefundVO.getVmId()!=null)
				    facade.closeVmByVmId(appRefundVO.getVmId(),admin,LogOperatorType.PROCESS.getName());
		    }
			//2、退款 appRefundApplicationMoney
			//根据用户信息获取用户的账户信息-----------退款金额hc_account --balance---总金额
				
			Account account = accountService.getAccountByUserId((long)appRefundVO.getOwnerId());
			long accountId = account.getId();
			String accountName=account.getName();
			Map<String,Object> accountqueryMap = new HashMap<String,Object>();
			accountqueryMap.put("userId",appRefundVO.getOwnerId());
			accountqueryMap.put("accountId",accountId);
			//获取此用户现有的账户余额
			BigDecimal bananceS=account.getBalance();
			bananceTotal=BigDecimal.valueOf(appRefundVO.getRefundtotal()).add(bananceS).doubleValue();
			accountqueryMap.put("refundtotal", bananceTotal);

			AppOrder appOrderInfo = appOrderService.selectAppOrderByOrderId(appRefundVO.getOrderId());
		    Account account1 = accountService.getAccountByUserId((long)appRefundVO.getOwnerId());
		    
		    //3、app_order将stutas改为3
		    AppOrder appOrder = new AppOrder();
			appOrder.setId(appRefundVO.getOrderId());
			appOrder.setStatus(3); 
			
			//4、在充值记录添加log---app_bill的app_price   vm_price  total_price
			Application app = appPayService.selectAppByPrimaryKey(appOrderInfo.getProductId());
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			Date date = new Date();
			String dateformat = f.format(date); 
			AppBill appBill = new AppBill();
			appBill.setAppOrderId(appOrderInfo.getId());
			appBill.setUserId(appRefundVO.getOwnerId());
			appBill.setSupplier(app.getSupplierId());
			appBill.setCreateDate(date);
			appBill.setDealDate(date);
			appBill.setAppId(appOrderInfo.getProductId());
			appBill.setType(3 + "");
			appBill.setUserName(account1.getUser().getName());
			appBill.setUserEmail(account1.getUser().getEmail());
			String strReason="云应用["+app.getName()+"]，在"+dateformat+"，执行退款操作，订单号为["+appRefundVO.getOrderNo()+"]，退款全额为"+appRefundVO.getRefundtotal()+"；";
			appBill.setRemark(strReason);
			appBill.setAppPrice(appRefundVO.getRefundappPrice());
			appBill.setVmPrice(appRefundVO.getRefundvmPrice());
			appBill.setTotalPrice(appRefundVO.getRefundtotal());
			appBill.setBalanceAfter(bananceTotal);
			appBill.setExpireDate(date);
			
			
			//5、修改app_refund中status的状态位为2，与refund_amount（应退总额），refund_amount_supplier（供应商应退金额）
			AppRefund appRefund=new AppRefund();
			appRefund.setId(appRefundVO.getId());
			appRefund.setRefundAmount(appRefundVO.getRefundtotal());
			appRefund.setRefundAmountSupplier(appRefundVO.getRefundappPrice());
			//appRefund.setStatus(2);
			appRefund.setStatus(2);
			appRefund.setOperator(admin.getName());
			appRefund.setRefundDate(new Date());

			int result5=appRefundService.getAppRefundall( appRefund, appOrder, appBill, accountqueryMap);
			
			if(result5==1){
				System.out.println("successfully");
				super.fillActionResult(Constants.OPTIONS_SUCCESS);
			}else{
				System.out.println("fail");
				super.fillActionResult(Constants.OPTIONS_FAILURE);
			}
		} else {
			super.fillActionResult(Constants.OPTIONS_FAILURE);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit appRefundApplicationForApply method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * <app退款> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void appRejectApplicationForApply() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter appRejectApplicationForApply method.");			
		}
		Admin admin = null;
		try {
			admin= (Admin) super.getCurrentLoginUser();
			
			if ((appRefundVO.getRejectRefundReason()!=null&&(!"".equals(appRefundVO.getRejectRefundReason())))&&(admin != null && (admin.getType()==null||admin.getType()!=2)) ){
				Map<String,Object> queryMap = new HashMap<String,Object>();
				queryMap.put("id", appRefundVO.getId());
				//修改app_refund中status的状态位为3--拒绝
				AppRefund appRefund=new AppRefund();
				appRefund.setId(appRefundVO.getId());
				appRefund.setRejectRefundReason(appRefundVO.getRejectRefundReason());
				appRefund.setStatus(3);
				appRefund.setOperator(admin.getName());
				appRefund.setRefundDate(new Date());
				int result=appRefundService.updateByPrimaryKeySelective(appRefund);
				if(result==1){
					System.out.println("successfully");
				}else
					System.out.println("fail");
			}else{
				super.fillActionResult(Constants.OPTIONS_FAILURE);
				
			}
			
		} catch (Exception ex) {
			//facade.insertOperationLog(admin,"退款错误:"+ex.getMessage(),"审核拒绝退款",Constants.RESULT_FAILURE);
			//dealThrow(new HsCloudException("", "appRejectApplicationForApply异常", logger, ex),"应用拒绝退款异常", true);
		}
		
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit appRejectApplicationForApply method.takeTime:" + takeTime + "ms");
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
	
	
	/**
	 * @return the appRefundDetailPage
	 */
	public Page<AppRefundVO> getAppRefundDetailPage() {
		return appRefundDetailPage;
	}

	/**
	 * @param appRefundDetailPage the appRefundDetailPage to set
	 */
	public void setAppRefundDetailPage(Page<AppRefundVO> appRefundDetailPage) {
		this.appRefundDetailPage = appRefundDetailPage;
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
	public int getAppCategoryId() {
		return appCategoryId;
	}
	public void setAppCategoryId(int appCategoryId) {
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
	 * @return the appRefund
	 */
	public AppRefund getAppRefund() {
		return appRefund;
	}


	/**
	 * @param appRefund the appRefund to set
	 */
	public void setAppRefund(AppRefund appRefund) {
		this.appRefund = appRefund;
	}


	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
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
	/**
	 * @return the appRefundVO
	 */
	public AppRefundVO getAppRefundVO() {
		return appRefundVO;
	}
	/**
	 * @param appRefundVO the appRefundVO to set
	 */
	public void setAppRefundVO(AppRefundVO appRefundVO) {
		this.appRefundVO = appRefundVO;
	}
	
	
	
	
}

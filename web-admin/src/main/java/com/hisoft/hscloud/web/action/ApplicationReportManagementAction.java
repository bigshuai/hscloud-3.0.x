package com.hisoft.hscloud.web.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.wgdawn.persist.model.AppResourceUsage;
import com.wgdawn.persist.more.model.app.MoreAppWorkOrderBean;
import com.wgdawn.persist.more.model.app.MoreIndustry;
import com.wgdawn.persist.more.model.app.MoreIndustryUsedCount;
import com.wgdawn.persist.more.model.app.MoreOrderQuantity;
import com.wgdawn.persist.more.model.app.MoreReportApp;
import com.wgdawn.persist.more.model.app.MoreResourceStatistics;
import com.wgdawn.persist.more.model.app.MoreShelvesApplied;
import com.wgdawn.persist.more.model.appReport.MoreBillReportVO;
import com.wgdawn.persist.more.model.appReport.MoreVMIncomePerMonth;
import com.wgdawn.persist.more.model.appReport.MoreVpdcReferenceReportVO;
import com.wgdawn.service.AppReportService;
import com.wgdawn.service.AppResourceStatisticsService;
import com.wgdawn.service.AppResourceUsageService;
import com.wgdawn.service.ApplicationReportService;
import com.wgdawn.service.ApplicationShelvesService;
import com.wgdawn.service.OrderQuantityService;

/**
 * 领导_云应用报表 <功能详细描述>
 * 
 * @author Effy
 * @version [版本号, 2015-7-23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ApplicationReportManagementAction extends HSCloudAction {
	private static final long serialVersionUID = 3101080193100376483L;
	private Logger logger = Logger.getLogger(this.getClass());
	private int searchyear;
	private String searchAppyear;
	private String searchAppMonth;
	private String searchAppsort;
	private String searchApptype;
	@Autowired
	private AppReportService appReportService;
	@Autowired
	private AppResourceUsageService appResourceUsageService;
	@Autowired
	private ApplicationReportService applicationReportService;
	@Autowired
	private ApplicationShelvesService applicationShelvesService;
	@Autowired
	private OrderQuantityService orderQuantityService;
	@Autowired
	private AppResourceStatisticsService appResourceStatisticsService;

	/**
	 * 获取账单报表信息
	 */
	public void getAppBillReport() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAppBillReport method.");
		}
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			params.put("year", searchyear == 0 ? year : searchyear);
			List<MoreBillReportVO> lst = appReportService.selectBillReportInfo(params);
			for (int i = 0; i < lst.size(); i++) {
				if (i != 0) {
					BigDecimal totalPrice = lst.get(i).getTotalPrice();
					if (null != totalPrice) {
							lst.get(i).setTotalPrice(lst.get(i).getTotalPrice());
					} else {
						lst.get(i).setTotalPrice(BigDecimal.ZERO);
					}
				}
			}
			fillActionResult(lst);
		} catch (HsCloudException hce) {
			dealThrow(hce, hce.getCode());
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "获取账单报表信息", logger, e), Constants.GET_REPORT_EXCEPTION, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAppBillReport method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 获取账单报表可用年份
	 */
	public void getAppYear() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAppYear method.");
		}
		try {
			List<MoreBillReportVO> lst = appReportService.selectAllYear();
			fillActionResult(lst);
		} catch (HsCloudException hce) {
			dealThrow(hce, hce.getCode());
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "获取账单报表可用年份", logger, e), Constants.GET_REPORT_EXCEPTION, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAppYear method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 获取运行中虚拟机数量统计信息
	 */
	public void selectVmStatusReport() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter selectVmStatusReport method.");
		}
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			params.put("year", searchyear == 0 ? year : searchyear);
			List<MoreVpdcReferenceReportVO> lst = appReportService.selectVmStatusReport(params);
			fillActionResult(lst);
		} catch (HsCloudException hce) {
			dealThrow(hce, hce.getCode());
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "获取运行中虚拟机数量统计信息", logger, e), Constants.GET_REPORT_EXCEPTION, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit selectVmStatusReport method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 获取运行中虚拟机数量统计可用年份
	 */
	public void selectVmStatusReportYear() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter selectVmStatusReportYear method.");
		}
		try {
			List<MoreVpdcReferenceReportVO> lst = appReportService.selectVmStatusReportYear();
			fillActionResult(lst);
		} catch (HsCloudException hce) {
			dealThrow(hce, hce.getCode());
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "获取运行中虚拟机数量统计可用年份", logger, e), Constants.GET_REPORT_EXCEPTION, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit selectVmStatusReportYear method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 领导-云应用-云应用月收入统计 <功能详细描述>
	 * 
	 * @author Effy
	 * @see [类、类#方法、类#成员]
	 */
	public void getMonthlyIncomeReport() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();

			logger.debug("enter getAvailableSupplier method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			if (null != searchAppyear && !"".endsWith(searchAppyear)) {
				param.put("searchAppyear", searchAppyear);
			} else {
				Calendar a = Calendar.getInstance();
				param.put("searchAppyear", a.get(Calendar.YEAR));
			}

			List<MoreReportApp> suList = applicationReportService.getMonthlyIncomeReport(param);
			this.fillActionResult(suList);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 领导-云应用-订购应用产品数量统计 <功能详细描述>
	 * 
	 * @author Effy
	 * @see [类、类#方法、类#成员]
	 */
	public void getQuantityApplicationReport() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();

			logger.debug("enter getQuantityApplicationReport method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			if (null != searchAppyear && !"".endsWith(searchAppyear)) {
				param.put("searchAppyear", searchAppyear);
			} else {
				Calendar a = Calendar.getInstance();
				param.put("searchAppyear", a.get(Calendar.YEAR));
			}

			List<MoreReportApp> suList = applicationReportService.getQuantityApplicationReport(param);
			this.fillActionResult(suList);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getQuantityApplicationReport method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * 查询行业类型分类(查询条件)
	 * <功能详细描述>  
	 * @see [类、类#方法、类#成员]
	 */
	public void findModel(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findReoprtYear method.");			
		}
		Map<String, Object> param = new HashMap<String, Object>();
		if (null != searchAppyear && !"".endsWith(searchAppyear)) {
			param.put("searchAppyear", searchAppyear);
		} else {
			Calendar a = Calendar.getInstance();
			param.put("searchAppyear", a.get(Calendar.YEAR));
		}
		String model = "industryName,";
		String catagoryName="行业名称,";
		List<MoreIndustryUsedCount> suList = appResourceStatisticsService.selectAppIndustryUsedCounts(param);
		for (int k = 0; k < suList.size(); k++) {
			model += "count" + k + ",";
			catagoryName += suList.get(k).getName() +",";
		}
		model = model.substring(0,model.length()-1);
		catagoryName = catagoryName.substring(0,catagoryName.length()-1);
		 HttpServletResponse response = ServletActionContext.getResponse();
 	       response.setContentType("text/html");
         try {
			response.getWriter().write("{success:true,model:'"+model+"',catagoryName:'"+catagoryName+"'}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //fillActionResult(model);
	    //fillActionResult(true);
	    if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findReoprtYear method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 领导-云应用-云应用使用情况<功能详细描述>
	 * 
	 * @author yangby
	 * @see [类、类#方法、类#成员]
	 */
	public void findAppIndustryUsedCounts() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();

			logger.debug("enter getQuantityApplicationReport method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			if (null != searchAppyear && !"".endsWith(searchAppyear)) {
				param.put("searchAppyear", searchAppyear);
			} else {
				Calendar a = Calendar.getInstance();
				param.put("searchAppyear", a.get(Calendar.YEAR));
			}
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			List<MoreIndustryUsedCount> suList = appResourceStatisticsService.selectAppIndustryUsedCounts(param);
			List<MoreIndustry> moreIndustryList = appResourceStatisticsService.selectIndustry();
			for (int i = 0;i<moreIndustryList.size();i++) {
				Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
				for (int j = 0; j < suList.size(); j++) {
					
					MoreIndustryUsedCount b = suList.get(j);
					//ResourceBundle res = ResourceBundle.getBundle("i"+moreIndustryList.get(i).getNameCode(),Locale.CHINA);
					//System.out.println(res);
			       // System.out.println(res.getString("welcome.msg"));
			        
					resultMap.put("industryName", moreIndustryList.get(i).getNameCode());
					
					if (moreIndustryList.get(i).getId() == b.getIndustryId()) {

						resultMap.put("count" + j, b.getCount());
					} else {
						resultMap.put("count" + j, 0);
					}
				}
				result.add(resultMap);
			}
			this.fillActionResult(result);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getQuantityApplicationReport method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 领导-云应用-供应商数量统计 <功能详细描述>
	 * 
	 * @author Effy
	 * @see [类、类#方法、类#成员]
	 */
	public void getSuppliersQuantityReport() {
		long beginRunTime = 0;
		Calendar a = Calendar.getInstance();
		int yeartime = a.get(Calendar.YEAR);
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();

			logger.debug("enter getSuppliersQuantityReport method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			if (null != searchAppyear && !"".equals(searchAppyear)) {
				if (searchAppyear.equals(String.valueOf(yeartime))) {
					param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);
				}
				param.put("searchAppyear", searchAppyear);
			} else {
				param.put("searchAppyear", a.get(Calendar.YEAR));
				param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);

			}

			List<MoreReportApp> suList = applicationReportService.getSuppliersQuantityReport(param);
			this.fillActionResult(suList);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getSuppliersQuantityReport method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 领导-云应用-新增产品总量统计 <功能详细描述>
	 * 
	 * @author Effy
	 * @see [类、类#方法、类#成员]
	 */
	public void getAdditionTotalReport() {
		long beginRunTime = 0;
		Calendar a = Calendar.getInstance();
		int yeartime = a.get(Calendar.YEAR);
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();

			logger.debug("enter getAdditionTotalReport method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			if (null != searchAppyear && !"".equals(searchAppyear)) {
				if (searchAppyear.equals(String.valueOf(yeartime))) {
					param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);
				}
				param.put("searchAppyear", searchAppyear);
			} else {
				param.put("searchAppyear", a.get(Calendar.YEAR));
				param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);

			}

			List<MoreReportApp> suList = applicationReportService.getAdditionTotalReport(param);
			this.fillActionResult(suList);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAdditionTotalReport method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * @Title: getWorkOrderCounting
	 * @Description: 4.客户工单数量统计
	 */
	public void getWorkOrderCounting() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAvailableSupplier method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("year", searchAppyear == null ? (new SimpleDateFormat("yyyy").format(new Date())) : searchAppyear);
			List<MoreAppWorkOrderBean> suList = orderQuantityService.selectWorkOrderCounting(param);
			this.fillActionResult(suList);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * @Title: getVMIncome
	 * @Description: 领导-云虚拟机-虚机月收入统计 ./report/hc_report_vMIncomePerMonth.html
	 */
	public void getVMIncomePerMonth() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAvailableSupplier method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("year", searchAppyear == null ? (new SimpleDateFormat("yyyy").format(new Date())) : searchAppyear);
			List<MoreVMIncomePerMonth> suList = orderQuantityService.getVMIncomePerMonth(param);
			this.fillActionResult(suList);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 领导-云应用-应用购买量前10名统计 <功能详细描述>
	 * 
	 * @author Effy
	 * @see [类、类#方法、类#成员]
	 */
	public void getAppRankingReportDESC() {
		long beginRunTime = 0;
		Calendar a = Calendar.getInstance();
		int yeartime = a.get(Calendar.YEAR);
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();

			logger.debug("enter getAdditionTotalReport method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();

			if (null != searchAppyear && !"".equals(searchAppyear)) {
				if (searchAppyear.equals(String.valueOf(yeartime))) {
					param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);
				}
				param.put("searchAppyear", searchAppyear);
				param.put("searchAppMonth", searchAppMonth);
			} else {
				param.put("searchAppyear", a.get(Calendar.YEAR));
				param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);

			}

			if ("asc".equals(searchAppsort)) {
				List<MoreReportApp> suList = applicationReportService.getAppRankingReportASC(param);
				this.fillActionResult(suList);
			} else {
				List<MoreReportApp> suList = applicationReportService.getAppRankingReportDESC(param);
				this.fillActionResult(suList);
			}
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAdditionTotalReport method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 领导-下架的应用统计 <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getShelvesOfAppliedStatistics() {
		long beginRunTime = 0;
		Calendar a = Calendar.getInstance();
		int yeartime = a.get(Calendar.YEAR);
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAvailableSupplier method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("status", 2);
			if (null != searchAppyear && !"".endsWith(searchAppyear)) {
				param.put("creatTime", searchAppyear);
				if (searchAppyear.equals(String.valueOf(yeartime))) {
					param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);
				}
			} else {
				param.put("creatTime", a.get(Calendar.YEAR));
				param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);
			}
			List<MoreShelvesApplied> suList = applicationShelvesService.selectMonthlyShelvesOfAppliedStatistics(param);
			this.fillActionResult(suList);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 订单数量统计 <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getOrderQuantityStatistics() {
		long beginRunTime = 0;
		Calendar a = Calendar.getInstance();
		int yeartime = a.get(Calendar.YEAR);
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAvailableSupplier method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("status", 1);
			if (null != searchAppyear && !"".endsWith(searchAppyear)) {
				param.put("creatTime", searchAppyear);
				if (searchAppyear.equals(String.valueOf(yeartime))) {
					param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);
				}
			} else {
				param.put("creatTime", a.get(Calendar.YEAR));
				param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);
			}
			List<MoreOrderQuantity> suList = orderQuantityService.selectOrderQuantityStatistics(param);
			this.fillActionResult(suList);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 云主机故障统计 <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getVmFaultStatistics() {
		long beginRunTime = 0;
		Calendar a = Calendar.getInstance();
		int yeartime = a.get(Calendar.YEAR);
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAvailableSupplier method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("type", 4);
			param.put("appOrNot", 0);
			if (null != searchAppyear && !"".endsWith(searchAppyear)) {
				param.put("creatTime", searchAppyear);
				if (searchAppyear.equals(String.valueOf(yeartime))) {
					param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);
				}
			} else {
				param.put("creatTime", a.get(Calendar.YEAR));
				param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);
			}
			List<MoreShelvesApplied> suList = applicationShelvesService.selectVmFaultStatistics(param);
			this.fillActionResult(suList);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 其他资源报表显示情况 <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void selectAppOtherBasicResourceUsage() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAvailableSupplier method.");
		}
		try {
			Map<String, Object> aruMap = new HashMap<String, Object>();
			if (null != searchAppyear && !"".endsWith(searchAppyear)) {
				aruMap.put("year", searchAppyear);
			} else {
				Calendar a = Calendar.getInstance();
				aruMap.put("year", a.get(Calendar.YEAR));
			}
			if (null != searchAppMonth && !"".endsWith(searchAppMonth)) {
				aruMap.put("month", searchAppMonth);
			} else {
				Calendar b = Calendar.getInstance();
				aruMap.put("month", b.get(Calendar.MONTH) + 1);
			}
			// aruMap.put("month", searchAppMonth);
			// aruMap.put("year", searchAppyear);
			List<AppResourceUsage> AppResourceUsageList = appResourceUsageService.selectAppResourceUsageByTime(aruMap);
			this.fillActionResult(AppResourceUsageList);
			// super.fillActionResult(Constants.OPTIONS_SUCCESS);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}

		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 云应用按照资源统计<功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getCloudApplicationResourceStatistics() {
		long beginRunTime = 0;
		Calendar a = Calendar.getInstance();
		int yeartime = a.get(Calendar.YEAR);
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAvailableSupplier method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("status", 1);
			List<MoreResourceStatistics> suList = null;
			if (null != searchAppyear && !"".endsWith(searchAppyear)) {
				param.put("createDate", searchAppyear);
				if (searchAppyear.equals(String.valueOf(yeartime))) {
					param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);
				}
			} else {
				param.put("createDate", a.get(Calendar.YEAR));
				param.put("searchAppMonth", a.get(Calendar.MONTH) + 1);
			}
			if (null != searchApptype && !"".endsWith(searchApptype)) {
				param.put("type", searchApptype);
			} else {
				param.put("type", 1);
			}
			if ("1".equals(searchApptype) || "".equals(searchApptype) || null == searchApptype) {
				suList = appResourceStatisticsService.selectResourceStatisticsByTime(param);
			} else if ("2".equals(searchApptype)) {
				suList = appResourceStatisticsService.selectResourceStatisticsByTime(param);
			} else if ("3".equals(searchApptype)) {
				suList = appResourceStatisticsService.selectResourceStatisticsByTime(param);
			} else if ("4".equals(searchApptype)) {
				suList = appResourceStatisticsService.selectResourceStatisticsByTime(param);
			} else if ("5".equals(searchApptype)) {
				suList = appResourceStatisticsService.selectResourceStatisticsByTime(param);
			} else if ("6".equals(searchApptype)) {
				suList = appResourceStatisticsService.selectMoreCloudDatabaseResourceByTime(param);
			} else if ("7".equals(searchApptype)) {
				suList = appResourceStatisticsService.selectResourceStatisticsByTime(param);
			}
			this.fillActionResult(suList);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:" + takeTime + "ms");
		}
	}

	public AppReportService getAppReportService() {
		return appReportService;
	}

	public void setAppReportService(AppReportService appReportService) {
		this.appReportService = appReportService;
	}

	public ApplicationReportService getApplicationReportService() {
		return applicationReportService;
	}

	public void setApplicationReportService(ApplicationReportService applicationReportService) {
		this.applicationReportService = applicationReportService;
	}

	public String getSearchAppyear() {
		return searchAppyear;
	}

	public void setSearchAppyear(String searchAppyear) {
		this.searchAppyear = searchAppyear;
	}

	public int getSearchyear() {
		return searchyear;
	}

	public void setSearchyear(int searchyear) {
		this.searchyear = searchyear;
	}

	/**
	 * @return the searchAppMonth
	 */
	public String getSearchAppMonth() {
		return searchAppMonth;
	}

	/**
	 * @param searchAppMonth
	 *            the searchAppMonth to set
	 */
	public void setSearchAppMonth(String searchAppMonth) {
		this.searchAppMonth = searchAppMonth;
	}

	/**
	 * @return the searchAppsort
	 */
	public String getSearchAppsort() {
		return searchAppsort;
	}

	/**
	 * @param searchAppsort
	 *            the searchAppsort to set
	 */
	public void setSearchAppsort(String searchAppsort) {
		this.searchAppsort = searchAppsort;
	}

	public String getSearchApptype() {
		return searchApptype;
	}

	public void setSearchApptype(String searchApptype) {
		this.searchApptype = searchApptype;
	}

}

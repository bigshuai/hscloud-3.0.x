package com.hisoft.hscloud.web.action;

import java.util.Calendar;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.wgdawn.persist.more.model.appReport.MoreBillReportVO;
import com.wgdawn.persist.more.model.appReport.MoreVpdcReferenceReportVO;
import com.wgdawn.service.AppReportService;
import com.wgdawn.persist.more.model.app.MoreOrderQuantity;
import com.wgdawn.persist.more.model.app.MoreReportApp;
import com.wgdawn.persist.more.model.app.MoreShelvesApplied;
import com.wgdawn.service.ApplicationReportService;
import com.wgdawn.service.ApplicationShelvesService;
import com.wgdawn.service.IssueApplicationService;
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

	@Autowired
	private AppReportService appReportService;
	@Autowired
	private ApplicationReportService applicationReportService;
	@Autowired
	private ApplicationShelvesService applicationShelvesService;
	@Autowired
	private OrderQuantityService orderQuantityService;
	
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
			List<MoreBillReportVO> lst = appReportService
					.selectBillReportInfo(params);
			for (int i = 0; i < lst.size(); i++) {
				if (i != 0) {
					BigDecimal totalPrice = lst.get(i).getTotalPrice();
					lst.get(i).setTotalPrice(
							totalPrice.add(lst.get(i - 1).getTotalPrice()));
				}
			}
			fillActionResult(lst);
		} catch (HsCloudException hce) {
			dealThrow(hce, hce.getCode());
		} catch (Exception e) {
			dealThrow(new HsCloudException("", "获取账单报表信息", logger, e),
					Constants.GET_REPORT_EXCEPTION, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAppBillReport method.takeTime:" + takeTime
					+ "ms");
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
			dealThrow(new HsCloudException("", "获取账单报表可用年份", logger, e),
					Constants.GET_REPORT_EXCEPTION, true);
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
			dealThrow(new HsCloudException("", "获取运行中虚拟机数量统计信息", logger, e),
					Constants.GET_REPORT_EXCEPTION, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit selectVmStatusReport method.takeTime:" + takeTime
					+ "ms");
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
			dealThrow(new HsCloudException("", "获取运行中虚拟机数量统计可用年份", logger, e),
					Constants.GET_REPORT_EXCEPTION, true);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit selectVmStatusReportYear method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 领导-云应用月收入统计 <功能详细描述>
	 * 
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

			List<MoreReportApp> suList = applicationReportService
					.getMonthlyIncomeReport(param);
			this.fillActionResult(suList);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:"
					+ takeTime + "ms");
		}
	}

	/**
	 * 领导-下架的应用统计 <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getShelvesOfAppliedStatistics() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAvailableSupplier method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("status", 2);
			if (null != searchAppyear && !"".endsWith(searchAppyear)) {
				param.put("creatTime", searchAppyear);
			} else {
				Calendar a = Calendar.getInstance();
				param.put("creatTime", a.get(Calendar.YEAR));
			}
			List<MoreShelvesApplied> suList = applicationShelvesService
					.selectMonthlyShelvesOfAppliedStatistics(param);
			this.fillActionResult(suList);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:"
					+ takeTime + "ms");
		}
	}
	
	/**
	 * 领导-下架的应用统计 <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getOrderQuantityStatistics() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAvailableSupplier method.");
		}
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("status", 1);
			if (null != searchAppyear && !"".endsWith(searchAppyear)) {
				param.put("creatTime", searchAppyear);
			} else {
				Calendar a = Calendar.getInstance();
				param.put("creatTime", a.get(Calendar.YEAR));
			}
			List<MoreOrderQuantity> suList = orderQuantityService
					.selectOrderQuantityStatistics(param);
			this.fillActionResult(suList);
		} catch (Exception ex) {
			this.dealThrow(Constants.DOMAIN_GET_ALL_EXCEPTION, ex, logger);
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAvailableSupplier method.takeTime:"
					+ takeTime + "ms");
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

	public void setApplicationReportService(
			ApplicationReportService applicationReportService) {
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
}

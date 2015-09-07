package com.hisoft.hscloud.web.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.HSCloudAction;
import com.opensymphony.xwork2.ActionContext;
import com.wgdawn.service.AppReportService;

public class ReportAction extends HSCloudAction {
	private static final long serialVersionUID = -6696591395047725041L;
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private AppReportService appReportService;

	@SuppressWarnings("all")
	public static Map<String, Object> getParam() {
		ActionContext ac = ActionContext.getContext();
		Map<String, Object> param = ac.getParameters();
		Map map = new HashMap();
		Set set = param.entrySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry mapentry = (Map.Entry) iterator.next();
			map.put(mapentry.getKey(), ((String[]) mapentry.getValue())[0]);
		}
		return map;
	}
	/**
	 * 9.对应用上传前10名的供应商统计
	 */
	public void selectSupplierAppUploadTop(){
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter selectSupplierAppUploadTop method.");
		}
		try {
			Map<String, Object> params = getParam();
			List<Map<String, Object>> lst = appReportService
					.selectSupplierAppUploadTop(params);
			fillActionResult(lst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit selectSupplierAppUploadTop method.takeTime:"
					+ takeTime + "ms");
		}
	}
	/**
	 * 10.云应用资源部署情况统计（前十）
	 */
	public void selectAppResourceDeploymentSituation(){
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter selectAppResourceDeploymentSituation method.");
		}
		try {
			Map<String, Object> params = getParam();
			List<Map<String, Object>> lst = appReportService
					.selectAppResourceDeploymentSituation(params);
			fillActionResult(lst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit selectAppResourceDeploymentSituation method.takeTime:"
					+ takeTime + "ms");
		}
	}
	/**
	 * 11.利用率统计
	 */
	public void getUtilizationStatisticsList(){
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getUtilizationStatisticsList method.");
		}
		try {
			Map<String, Object> params = getParam();
			List<Map<String, Object>> lst = appReportService
					.selectUtilizationStatisticsList(params);
			fillActionResult(lst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getUtilizationStatisticsList method.takeTime:"
					+ takeTime + "ms");
		}
	}
	/**
	 * 12.应用点评数量统计
	 */
	public void getAppReviewAmountStatisticsList() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAppReviewAmountStatisticsList method.");
		}
		try {
			Map<String, Object> params = getParam();
			List<Map<String, Object>> lst = appReportService
					.selectAppReviewList(params);
			fillActionResult(lst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAppReviewAmountStatisticsList method.takeTime:"
					+ takeTime + "ms");
		}
	}
	/**
	 * 领导-云虚机-3.资源使用情况统计
	 */
	public void selectVMUseSituation() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getAppReviewAmountStatisticsList method.");
		}
		try {
			Map<String, Object> params = getParam();
			List<Map<String, Object>> lst = appReportService
					.selectVMUseSituation(params);
			fillActionResult(lst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getAppReviewAmountStatisticsList method.takeTime:"
					+ takeTime + "ms");
		}
	}
}

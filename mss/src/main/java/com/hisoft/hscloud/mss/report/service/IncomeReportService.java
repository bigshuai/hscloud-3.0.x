package com.hisoft.hscloud.mss.report.service;

import com.hisoft.hscloud.mss.report.entity.IncomeReport;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: TOSHIBA
 * Date: 12-5-11
 * Time: 上午11:27
 * To change this template use File | Settings | File Templates.
 */
public interface IncomeReportService {
	/**
	 *
	 * @title: Income Report
	 * @description 根据年份, 获取收入报表
	 * @param beginYear
     * @param endYear
	 * @return 设定文件
	 * @return IncomeReport    返回类型
	 * @throws
	 * @version 1.1
	 * @author roger.tong
	 * @update 2012-5-11 下午2:10:36
	 */
	public List<IncomeReport> FetchByYear(int beginYear, int endYear);

	/**
	 *
	 * @title: Income Report
	 * @description 根据月份, 获取收入报表
	 * @param beginMonth
     * @param endMonth
	 * @return 设定文件
	 * @return IncomeReport    返回类型
	 * @throws
	 * @version 1.1
	 * @author roger.tong
	 * @update 2012-5-11 下午2:10:36
	 */
	public List<IncomeReport> FetchByMonth(int beginMonth, int endMonth);
}

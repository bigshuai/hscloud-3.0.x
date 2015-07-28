package com.hisoft.hscloud.mss.report.service;

import com.hisoft.hscloud.mss.report.entity.UserReport;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Roger.Tong
 * Date: 12-5-11
 * Time: 上午11:27
 * To change this template use File | Settings | File Templates.
 */
public interface UserReportService {
	/**
	 *
	 * @title: User Report
	 * @description 根据年份, 获取收入报表
	 * @param beginYear
     * @param endYear
	 * @return 设定文件
	 * @return UserReport    返回类型
	 * @throws
	 * @version 1.1
	 * @author roger.tong
	 * @update 2012-5-11 下午2:10:36
	 */
	public List<UserReport> FetchByYear(int beginYear, int endYear);

	/**
	 *
	 * @title: User Report
	 * @description 根据月份, 获取收入报表
	 * @param beginMonth
     * @param endMonth
	 * @return 设定文件
	 * @return UserReport    返回类型
	 * @throws
	 * @version 1.1
	 * @author roger.tong
	 * @update 2012-5-11 下午2:10:36
	 */
	public List<UserReport> FetchByMonth(int beginMonth, int endMonth);

	/**
	 *
	 * @title: User Report
	 * @description 根据月份, 获取收入报表
	 * @param beginDay
     * @param endDay
	 * @return 设定文件
	 * @return UserReport    返回类型
	 * @throws
	 * @version 1.1
	 * @author roger.tong
	 * @update 2012-5-11 下午2:10:36
	 */
	public List<UserReport> FetchByDay(int beginDay, int endDay);
}

/* 
* 文 件 名:  CPULoadAvgBean.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-5-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class CPULoadAvgBean {

	private Float loadAvg1;
	private Float loadAvg5;
	private Float loadAvg15;
	public Float getLoadAvg1() {
		return loadAvg1;
	}
	public void setLoadAvg1(Float loadAvg1) {
		this.loadAvg1 = loadAvg1;
	}
	public Float getLoadAvg5() {
		return loadAvg5;
	}
	public void setLoadAvg5(Float loadAvg5) {
		this.loadAvg5 = loadAvg5;
	}
	public Float getLoadAvg15() {
		return loadAvg15;
	}
	public void setLoadAvg15(Float loadAvg15) {
		this.loadAvg15 = loadAvg15;
	}	
}

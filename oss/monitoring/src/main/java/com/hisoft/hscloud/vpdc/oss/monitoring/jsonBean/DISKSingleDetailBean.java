/* 
* 文 件 名:  DISKSingleDetailBean.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-10-8 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean; 

/** 
 * <磁盘监控信息> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-10-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class DISKSingleDetailBean {

	private String title;//标题
	private Double usage;//使用率
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getUsage() {
		return usage;
	}
	public void setUsage(Double usage) {
		this.usage = usage;
	}
}

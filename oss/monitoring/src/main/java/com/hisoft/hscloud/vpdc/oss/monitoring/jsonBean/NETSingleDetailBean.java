/* 
* 文 件 名:  NETSingleDetailBean.java 
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
 * <网络监控信息> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-10-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class NETSingleDetailBean {

	private String title;//标题
	private Integer rxSpeed;//网络读
	private Integer txSpeed;//网络写
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getRxSpeed() {
		return rxSpeed;
	}
	public void setRxSpeed(Integer rxSpeed) {
		this.rxSpeed = rxSpeed;
	}
	public Integer getTxSpeed() {
		return txSpeed;
	}
	public void setTxSpeed(Integer txSpeed) {
		this.txSpeed = txSpeed;
	}
}

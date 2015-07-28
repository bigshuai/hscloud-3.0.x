/* 
* 文 件 名:  NETMonitorBean.java 
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
 * <网络读写监控信息> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class NETMonitorBean {

	private Double rxSpeed;//网络接收速率
	private Double txSpeed;//网络发送速率
	private String device;//网卡名称
	private String IP;//网卡IP
	public Double getRxSpeed() {
		return rxSpeed;
	}
	public void setRxSpeed(Double rxSpeed) {
		this.rxSpeed = rxSpeed;
	}
	public Double getTxSpeed() {
		return txSpeed;
	}
	public void setTxSpeed(Double txSpeed) {
		this.txSpeed = txSpeed;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
}

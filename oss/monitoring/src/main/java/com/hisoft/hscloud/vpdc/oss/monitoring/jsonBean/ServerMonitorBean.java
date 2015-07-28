/* 
* 文 件 名:  ServerMonitorBean.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-5-8 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean; 

/** 
 * <虚拟机实时监控信息> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ServerMonitorBean {

	private Double cpuRate;//CPU使用率
	private Integer cpuNum;//CPU核数
	private Double ramRate;//内存使用率
	private Integer ramTotal;//内存总大小
	public Double getCpuRate() {
		return cpuRate;
	}
	public void setCpuRate(Double cpuRate) {
		this.cpuRate = cpuRate;
	}
	public Integer getCpuNum() {
		return cpuNum;
	}
	public void setCpuNum(Integer cpuNum) {
		this.cpuNum = cpuNum;
	}
	public Double getRamRate() {
		return ramRate;
	}
	public void setRamRate(Double ramRate) {
		this.ramRate = ramRate;
	}
	public Integer getRamTotal() {
		return ramTotal;
	}
	public void setRamTotal(Integer ramTotal) {
		this.ramTotal = ramTotal;
	}
}

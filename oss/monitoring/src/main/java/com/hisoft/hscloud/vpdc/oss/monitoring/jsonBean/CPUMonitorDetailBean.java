/* 
* 文 件 名:  CPUMonitorDetail.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-5-9 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.oss.monitoring.jsonBean; 

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-5-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class CPUMonitorDetailBean implements Serializable{

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -4205773398836773394L;
	private Integer workloadLimit;//工作量阈值
	private Integer workloadActual;//工作量实际值
	private CPULoadAvgBean loadAvgBean;
	private List<CPUSingleDetailBean> singleDetailBeanList = new ArrayList<CPUSingleDetailBean>();
	public Integer getWorkloadLimit() {
		return workloadLimit;
	}
	public void setWorkloadLimit(Integer workloadLimit) {
		this.workloadLimit = workloadLimit;
	}
	public Integer getWorkloadActual() {
		return workloadActual;
	}
	public void setWorkloadActual(Integer workloadActual) {
		this.workloadActual = workloadActual;
	}
	public CPULoadAvgBean getLoadAvgBean() {
		return loadAvgBean;
	}
	public void setLoadAvgBean(CPULoadAvgBean loadAvgBean) {
		this.loadAvgBean = loadAvgBean;
	}
	public List<CPUSingleDetailBean> getSingleDetailBeanList() {
		return singleDetailBeanList;
	}
	public void setSingleDetailBeanList(
			List<CPUSingleDetailBean> singleDetailBeanList) {
		this.singleDetailBeanList = singleDetailBeanList;
	}

}

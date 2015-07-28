/* 
* 文 件 名:  IOPSMonitorDetailBean.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-10-9 
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
 * @version  [版本号, 2013-10-9] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class IOPSMonitorDetailBean implements Serializable{

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = 6511299401242238974L;
	private Integer readLimit;//iops读阈值
	private Integer readActual;//iops读实际值
	private Integer writeLimit;//iops写阈值
	private Integer writeActual;//iops写实际值
	private List<IOPSSingleDetailBean> singleDetailBeanList =new ArrayList<IOPSSingleDetailBean>();
	public Integer getReadLimit() {
		return readLimit;
	}
	public void setReadLimit(Integer readLimit) {
		this.readLimit = readLimit;
	}
	public Integer getReadActual() {
		return readActual;
	}
	public void setReadActual(Integer readActual) {
		this.readActual = readActual;
	}
	public Integer getWriteLimit() {
		return writeLimit;
	}
	public void setWriteLimit(Integer writeLimit) {
		this.writeLimit = writeLimit;
	}
	public Integer getWriteActual() {
		return writeActual;
	}
	public void setWriteActual(Integer writeActual) {
		this.writeActual = writeActual;
	}
	public List<IOPSSingleDetailBean> getSingleDetailBeanList() {
		return singleDetailBeanList;
	}
	public void setSingleDetailBeanList(
			List<IOPSSingleDetailBean> singleDetailBeanList) {
		this.singleDetailBeanList = singleDetailBeanList;
	}
}

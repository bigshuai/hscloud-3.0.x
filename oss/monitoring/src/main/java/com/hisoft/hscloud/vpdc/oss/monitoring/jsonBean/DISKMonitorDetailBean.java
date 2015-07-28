/* 
* 文 件 名:  DISKMonitorDetailBean.java 
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
public class DISKMonitorDetailBean implements Serializable{

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -785371897802444279L;
	private List<DISKSingleDetailBean> singleDetailBeanList =new ArrayList<DISKSingleDetailBean>();
	public List<DISKSingleDetailBean> getSingleDetailBeanList() {
		return singleDetailBeanList;
	}
	public void setSingleDetailBeanList(
			List<DISKSingleDetailBean> singleDetailBeanList) {
		this.singleDetailBeanList = singleDetailBeanList;
	}
}

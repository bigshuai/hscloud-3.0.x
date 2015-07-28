/* 
* 文 件 名:  VmExtDiskBean.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2013-2-20 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.json.bean; 

/** 
 * <扩展盘bean> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2013-2-20] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class VmExtDiskBean {

	private String ed_name;//扩展盘名称
	
	private int ed_capacity;//扩展盘大小
	
	private int volumnId;
	
	public String getEd_name() {
		return ed_name;
	}

	public void setEd_name(String ed_name) {
		this.ed_name = ed_name;
	}

	public int getEd_capacity() {
		return ed_capacity;
	}

	public void setEd_capacity(int ed_capacity) {
		this.ed_capacity = ed_capacity;
	}
	public int getVolumnId() {
		return volumnId;
	}

	public void setVolumnId(int volumnId) {
		this.volumnId = volumnId;
	}

	@Override
	public String toString() {
		return "VmExtDiskBean [ed_name=" + ed_name + ", ed_capacity=" + ed_capacity + "]";
	}
}

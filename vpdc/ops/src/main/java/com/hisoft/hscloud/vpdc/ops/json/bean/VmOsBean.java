/* 
* 文 件 名:  VmOsBean.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2012-12-13 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.json.bean; 

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2012-12-13] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class VmOsBean {
	private Long id;
	private String name;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "VmOsBean [id=" + id + ", name=" + name + "]";
	}
}

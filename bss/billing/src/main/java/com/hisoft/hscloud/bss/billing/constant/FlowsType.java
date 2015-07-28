/* 
* 文 件 名:  FlowsType.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2014-4-15 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.billing.constant; 

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2014-4-15] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public enum FlowsType {

	UNKNOW_FLOWS((short)0,"UNKNOW_FLOWS","flows.unknow"),
	FLOWS_IN((short)1,"FLOWS_IN","flows.in"),
	FLOWS_OUT((short)2,"FLOWS_OUT","flows.out");
	
	public short getIndex() {
		return index;
	}

	public void setIndex(short index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getI18n() {
		return i18n;
	}

	public void setI18n(String i18n) {
		this.i18n = i18n;
	}

	private short index;
	private String type;
	private String i18n;
	
	FlowsType(short index,String type,String i18n){		
		this.index = index;
		this.type = type;
		this.i18n = i18n;
		
	}
}

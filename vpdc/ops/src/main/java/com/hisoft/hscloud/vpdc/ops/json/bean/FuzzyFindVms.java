/**
 * @title FindVms.java
 * @package com.hisoft.hscloud.vpdc.ops.json.bean
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-4-7 下午6:24:00
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.json.bean;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author hongqin.li
 * @update 2012-4-7 下午6:24:00
 */
public class FuzzyFindVms {
	//查询类型
	private String name;
	//查询条件
	private String value;
	/**
	 * @return name : return the property name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name : set the property name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return value : return the property value.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value : set the property value.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}

/**
 * @title DataAccessException.java
 * @package com.hisoft.hscloud.vpdc.ops.util
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-4-23 下午4:10:39
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.util;

import org.springframework.dao.DataAccessException;

/**
 * @description 虚拟机别名更新异常，及1、虚拟机别名重复，2、逻辑错误照成更新不成公共
 * @version 1.0
 * @author hongqin.li
 * @update 2012-4-23 下午4:10:39
 */
public class DataAccessExceptions extends DataAccessException{
	private String code ;
	private String msg;
   
	/**
	 * 类的构造方法
	 * @param msg
	 * @param code
	 * @param msg2
	 */
	public DataAccessExceptions(String msg, String code, String msg2) {
		super(msg);
		this.code = code;
		msg = msg2;
	}
	/**
	 * 类的构造方法
	 * @param msg
	 * @param cause
	 */
	public DataAccessExceptions(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 类的构造方法
	 * @param msg
	 */
	public DataAccessExceptions(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return code : return the property code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code : set the property code.
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return msg : return the property msg.
	 */
	public String getMsg() {
		return super.getMessage();
	}
	/**
	 * @param msg : set the property msg.
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}
	 
}

/**
 * @title OpsException.java
 * @package com.hisoft.hscloud.vpdc.ops.util
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-4-16 下午4:21:58
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.util;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author hongqin.li
 * @update 2012-4-16 下午4:21:58
 */
public class OpsException extends Exception {
	private String code ;
	private String msg;
	
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
		return msg;
	}

	/**
	 * @param msg : set the property msg.
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public OpsException(String msg,String code){
		this.code = code;
		this.msg = msg;
	}

	/* (非 Javadoc) 
	 * <p>Title: getMessage</p> 
	 * <p>Description: </p> 
	 * @return 
	 * @see java.lang.Throwable#getMessage() 
	 */
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}
	
}

/**
 * @title Logger.java
 * @package com.hisoft.hscloud.vpdc.ops.log
 * @description 用一句话描述该文件做什么
 * @author hongqin.li
 * @update 2012-4-7 上午10:03:35
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.ops.log;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author hongqin.li
 * @update 2012-4-7 上午10:03:35
 */
public class Logger extends org.apache.log4j.Logger{

	/**
	 * 类的构造方法
	 * @param name
	 */
	protected Logger(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void enter(){
		debug("enter....");
	}
	
	public void exit(){
		debug("...exit");
	}
}

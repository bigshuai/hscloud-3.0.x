/**
 * @title InValidPasswordException.java
 * @package com.hisoft.hscloud.crm.usermanager.exception
 * @description 用一句话描述该文件做什么
 * @author guole.liang
 * @update 2012-6-13 下午4:18:42
 * @version V1.1
 */
package com.hisoft.hscloud.crm.usermanager.exception;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.1
 * @author guole.liang
 * @update 2012-6-13 下午4:18:42
 */
public class InValidPasswordException extends Exception{

	public InValidPasswordException() {
		super();
	}


	public InValidPasswordException(String message) {
		super("Invalid Password");
	}


	
	

}

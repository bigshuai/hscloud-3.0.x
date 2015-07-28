/**
 * @title OrderException.java
 * @package com.hisoft.hscloud.bss.sla.om.exception
 * @description 用一句话描述该文件做什么
 * @author MaDai
 * @update 2012-4-1 下午3:34:43
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.om.exception;

/**
 * @description 订单异常基类
 * @version 1.0
 * @author Madai
 * @update 2012-4-1 下午3:34:43
 */
public class OrderException extends Exception{
	private static final long serialVersionUID = 1L;

	/**
	 * 类的构造方法
	 */
	public OrderException() {
		super();
	}

	/**
	 * 类的构造方法
	 * @param message
	 * @param cause
	 */
	public OrderException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 类的构造方法
	 * @param message
	 */
	public OrderException(String message) {
		super(message);
	}

	/**
	 * 类的构造方法
	 * @param cause
	 */
	public OrderException(Throwable cause) {
		super(cause);
	}

}

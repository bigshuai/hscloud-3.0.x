/**
 * @title NotFoundPaidOrderException.java
 * @package com.hisoft.hscloud.bss.sla.om.exception
 * @description 用一句话描述该文件做什么
 * @author MaDai
 * @update 2012-4-1 下午3:32:26
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.om.exception;

/**
 * @description 没有找到订单
 * @version 1.0
 * @author Madai
 * @update 2012-4-1 下午3:32:26
 */
public class NotFoundOrderException extends OrderException {
	private static final long serialVersionUID = 1L;

	/**
	 * 类的构造方法
	 */
	public NotFoundOrderException() {
		super("没有找到订单");
	}

}

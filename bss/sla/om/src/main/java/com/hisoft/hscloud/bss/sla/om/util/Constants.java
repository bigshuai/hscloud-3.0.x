/**
 * @title Constant.java
 * @package com.hisoft.hscloud.bss.sla.om.util
 * @description 常量
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 * @version V1.0
 */
package com.hisoft.hscloud.bss.sla.om.util;

/**
 * @description 常量
 * @version 1.0
 * @author MaDai
 * @update 2012-3-28 下午5:34:57
 */
public final class Constants {
	public static final int DELAY_DAYS = 7;//订单生产后允许延迟付款的天数

	private Constants() {

	}
	//发票账户未申请状态
	public static final String INVOICE_ACCOUNT_STATUS_IDLENESS = "0";
	//发票账户申请状态
	public static final String INVOICE_ACCOUNT_STATUS_APPLICATION = "1";
	
	//发票申请状态未开
	public static final String INVOICE_APPLY_STATUS_WAIT = "0";
	//发票申请状态已开
    public static final String INVOICE_APPLY_STATUS_PASS = "1";
    //发票申请状态未通过
    public static final String INVOICE_APPLY_STATUS_NOT_PASS = "2";
}

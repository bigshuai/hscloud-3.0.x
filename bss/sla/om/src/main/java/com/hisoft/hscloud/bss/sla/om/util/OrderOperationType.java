/* 
 * 文 件 名:  OrderOperationType.java 
 * 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
 * 描    述:  <描述> 
 * 修 改 人:  houyh 
 * 修改时间:  2012-11-22 
 * 跟踪单号:  <跟踪单号> 
 * 修改单号:  <修改单号> 
 * 修改内容:  <修改内容> 
 */
package com.hisoft.hscloud.bss.sla.om.util;

/**
 * <订单操作类型> <功能详细描述>
 * 
 * @author houyh
 * @version [版本号, 2012-11-22]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public enum OrderOperationType {
	TONORMAL((short) 1, "ToNormal", "转正"), 
	RENEWORDER((short) 2, "RENEWORDER","续费"),
	REFUNDALL((short) 3, "REFUNDALL", "全额退款"),
	PARTREFUND((short) 4, "PARTREFUND", "部分退款"),
	PAY((short)5,"PAY","支付"),
	CANCELED((short)6,"CANCELED","取消"),
	TRY((short)7,"TRY","试用"),
	TRYDELAY((short)8,"TRYDELAY","试用延期"),
	UPGRADEVM((short)9,"UPGRADEVM","云主机升级"),
	UNKNOWTYPE((short) 0,"UNKNOWTYPE", "未知操作");
	
	
	private short index;
	private String type;
	private String i18n;

	OrderOperationType(short index, String type, String i18n) {
		this.i18n = i18n;
		this.index = index;
		this.type = type;
	}

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

	public static OrderOperationType getItem(short index) {

		for (OrderOperationType operationType : OrderOperationType.values()) {
			if (operationType.index == index) {
				return operationType;
			}
		}
		
		return OrderOperationType.UNKNOWTYPE;
	}

}

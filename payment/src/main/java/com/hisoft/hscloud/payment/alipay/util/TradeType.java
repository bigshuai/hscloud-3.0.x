/* 
* 文 件 名:  TradeType.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  houyh 
* 修改时间:  2012-12-17 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.payment.alipay.util; 

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  houyh 
 * @version  [版本号, 2012-12-17] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public enum TradeType {
	PAY((short)1,"PAY"),
	RECHARGE((short)2,"RECHARGE"),
	REFUND((short)3,"REFUND"),
	WITHDRAW_DEPOSIT((short)4,"WITHDRAW_DEPOSIT"),
	UNKNOWN((short)5,"UNKNOWN");
	
	private short index;
	private String tradeTypeStr;
	
	private TradeType(short index,String tradeTypeStr){
		this.index=index;
		this.tradeTypeStr=tradeTypeStr;
	}
	
	public short getIndex(){
		return this.index;
	}
	
	public static TradeType getByIndex(short index){
		for(TradeType tradeType:TradeType.values()){
			if(tradeType.index==index){
				return tradeType;
			}
		}
		
		return TradeType.UNKNOWN;
		
	}
	
	public static TradeType getByTradeTypeStr(String tradeTypeStr){
		return valueOf(tradeTypeStr);
	}

	public String getTradeTypeStr() {
		return tradeTypeStr;
	}

	public void setTradeTypeStr(String tradeTypeStr) {
		this.tradeTypeStr = tradeTypeStr;
	}

	public void setIndex(short index) {
		this.index = index;
	}
	
	
}

/* 
* 文 件 名:  RenewOrderVo.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-8-13 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.vo; 

/** 
 * <续费对象> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-8-13] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class RenewOrderVo {
    private String couponBalance; //返点余额
    private String balance;       //余额
    private String consume;       //消费
    private String couponConsume; //返点消费
    private String giftConsume; //礼金消费
    public String getCouponBalance() {
        return couponBalance;
    }
    public void setCouponBalance(String couponBalance) {
        this.couponBalance = couponBalance;
    }
    public String getBalance() {
        return balance;
    }
    public void setBalance(String balance) {
        this.balance = balance;
    }
    public String getConsume() {
        return consume;
    }
    public void setConsume(String consume) {
        this.consume = consume;
    }
    public String getCouponConsume() {
        return couponConsume;
    }
    public void setCouponConsume(String couponConsume) {
        this.couponConsume = couponConsume;
    }
	public String getGiftConsume() {
		return giftConsume;
	}
	public void setGiftConsume(String giftConsume) {
		this.giftConsume = giftConsume;
	}
    
    
}

package com.hisoft.hscloud.web.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderPlanVo implements Serializable {

	/**
	 *@name: OrderPlanVo.java
	 *@type:long
	 */
	private static final long serialVersionUID = 1L;

	private String orderno;
	private int totalnum;
	private String createon;
	private List<Long> vmList = new ArrayList<Long>();
	private String couponBalance; //返点余额
	private String balance;       //余额
	private String consume;       //消费
	private String couponConsume; //返点消费

	public String getOrderno() {
		return orderno;
	}

	public int getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(int totalnum) {
        this.totalnum = totalnum;
    }

    public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

    public String getCreateon() {
        return createon;
    }

    public void setCreateon(String createon) {
        this.createon = createon;
    }

    public List<Long> getVmList() {
        return vmList;
    }

    public void setVmList(List<Long> vmList) {
        this.vmList = vmList;
    }

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


}

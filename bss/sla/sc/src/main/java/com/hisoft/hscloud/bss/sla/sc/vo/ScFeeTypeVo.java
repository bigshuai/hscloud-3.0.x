package com.hisoft.hscloud.bss.sla.sc.vo;

import java.math.BigDecimal;

public class ScFeeTypeVo {
	private long id;
	private int period;
	private BigDecimal price;
	private boolean usePointOrNot;//是否使用点劵 true 使用 ;false 不使用
	private boolean useGiftOrNot;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

    public boolean isUsePointOrNot() {
        return usePointOrNot;
    }

    public void setUsePointOrNot(boolean usePointOrNot) {
        this.usePointOrNot = usePointOrNot;
    }

	public boolean isUseGiftOrNot() {
		return useGiftOrNot;
	}

	public void setUseGiftOrNot(boolean useGiftOrNot) {
		this.useGiftOrNot = useGiftOrNot;
	}
    
}

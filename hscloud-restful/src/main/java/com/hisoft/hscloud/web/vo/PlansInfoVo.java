package com.hisoft.hscloud.web.vo;

import java.io.Serializable;

public class PlansInfoVo implements Serializable{
	/**
	 *@name: PlansInfoVo.java
	 *@type:long
	 */
	private static final long serialVersionUID = 338584993248692477L;
	
	private int id;
	private String scname;
	private String feeType;
	private String effectiveon;
	private String expiredon;
	private String description;
	private String os_info;
	private String datacenter;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getScname() {
		return scname;
	}
	public void setScname(String scname) {
		this.scname = scname;
	}
    public String getFeeType() {
        return feeType;
    }
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
    public String getEffectiveon() {
		return effectiveon;
	}
	public void setEffectiveon(String effectiveon) {
		this.effectiveon = effectiveon;
	}
	public String getExpiredon() {
		return expiredon;
	}
	public void setExpiredon(String expiredon) {
		this.expiredon = expiredon;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOs_info() {
		return os_info;
	}
	public void setOs_info(String os_info) {
		this.os_info = os_info;
	}
    public String getDatacenter() {
        return datacenter;
    }
    public void setDatacenter(String datacenter) {
        this.datacenter = datacenter;
    }
}

package com.hisoft.hscloud.web.vo;

import java.io.Serializable;
import java.math.BigDecimal;



public class VMInfoVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal id;
	private String vmname;
	private String machineno;
	private String fixedip;
	private String floatingip;
	private String createon;
	private String expireon;
	private String vmstate;
	private String bizstate;//1:试用，null：正式
	private String osLoginUser;//操作系统登录用户名
    private String osLoginPwd;//操作系统登录密码
    private String createflag;
	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
		this.id = id;
	}
	public String getVmname() {
		return vmname;
	}
	public void setVmname(String vmname) {
		this.vmname = vmname;
	}
	public String getMachineno() {
		return machineno;
	}
	public void setMachineno(String machineno) {
		this.machineno = machineno;
	}
	public String getFixedip() {
		return fixedip;
	}
	public void setFixedip(String fixedip) {
		this.fixedip = fixedip;
	}
	public String getFloatingip() {
		return floatingip;
	}
	public void setFloatingip(String floatingip) {
		this.floatingip = floatingip;
	}
	public String getCreateon() {
		return createon;
	}
	public void setCreateon(String createon) {
		this.createon = createon;
	}
	public String getExpireon() {
		return expireon;
	}
	public void setExpireon(String expireon) {
		this.expireon = expireon;
	}
	public String getVmstate() {
		return vmstate;
	}
	public void setVmstate(String vmstate) {
		this.vmstate = vmstate;
	}
	public String getBizstate() {
		return bizstate;
	}
	public void setBizstate(String bizstate) {
		this.bizstate = bizstate;
	}
    public String getOsLoginUser() {
        return osLoginUser;
    }
    public void setOsLoginUser(String osLoginUser) {
        this.osLoginUser = osLoginUser;
    }
    public String getOsLoginPwd() {
        return osLoginPwd;
    }
    public void setOsLoginPwd(String osLoginPwd) {
        this.osLoginPwd = osLoginPwd;
    }
    public String getCreateflag() {
        return createflag;
    }
    public void setCreateflag(String createflag) {
        this.createflag = createflag;
    }
}

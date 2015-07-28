package com.hisoft.hscloud.vpdc.ops.vo;

import java.util.Date;

/**
 * @fileName       VpdcRouterVo.java
 * @packageName    com.hisoft.hscloud.vpdc.ops.vo
 * @description    TODO
 * @since          1.0
 * @createTime     Feb 10, 2014 6:03:13 PM
 */
public class VpdcRouterVo {

	private long id;
	private String name;
	private String fixIP;// 内网IP（需要回写）
	private String floatingIP;// 外网IP
	private int osId;
	private String osName;// 操作系统名称
	private int cpuCore;
	private int diskCapacity;
	private int memSize;
	private int buyLong;// 购买时长（单位：月）
	private Date usedTime;// 运行时间或使用时间
	private Long remainingTime;;// 剩余时间（单位：秒）
	private String routerStatus;// 状态
	private String routerTaskStatus;// 任务
	private String routerUUID;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFixIP() {
		return fixIP;
	}

	public void setFixIP(String fixIP) {
		this.fixIP = fixIP;
	}

	public String getFloatingIP() {
		return floatingIP;
	}

	public void setFloatingIP(String floatingIP) {
		this.floatingIP = floatingIP;
	}

	public int getOsId() {
		return osId;
	}

	public void setOsId(int osId) {
		this.osId = osId;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public int getCpuCore() {
		return cpuCore;
	}

	public void setCpuCore(int cpuCore) {
		this.cpuCore = cpuCore;
	}

	public int getDiskCapacity() {
		return diskCapacity;
	}

	public void setDiskCapacity(int diskCapacity) {
		this.diskCapacity = diskCapacity;
	}

	public int getMemSize() {
		return memSize;
	}

	public void setMemSize(int memSize) {
		this.memSize = memSize;
	}

	public int getBuyLong() {
		return buyLong;
	}

	public void setBuyLong(int buyLong) {
		this.buyLong = buyLong;
	}

	public Date getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}

	public Long getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(Long remainingTime) {
		this.remainingTime = remainingTime;
	}

	public String getRouterStatus() {
		return routerStatus;
	}

	public void setRouterStatus(String routerStatus) {
		this.routerStatus = routerStatus;
	}

	public String getRouterTaskStatus() {
		return routerTaskStatus;
	}

	public void setRouterTaskStatus(String routerTaskStatus) {
		this.routerTaskStatus = routerTaskStatus;
	}

	public String getRouterUUID() {
		return routerUUID;
	}

	public void setRouterUUID(String routerUUID) {
		this.routerUUID = routerUUID;
	}

}
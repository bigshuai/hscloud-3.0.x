/* 
* 文 件 名:  VpdcReference_Period_Log.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  dinghb 
* 修改时间:  2013-2-20 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.entity; 

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  dinghb 
 * @version  [版本号, 2013-2-20] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name = "hc_vpdcRouter_period_log")
public class VpdcRouter_Period_Log {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "routerId")
	private long routerId;
	
	//上次虚拟机到期时间
	@Column(name = "end_time_last")
	private Date endTimeLast;
	
	//本次虚拟机到期时间
	@Column(name = "end_time_now")
	private Date endTimeNow;
	
	//变更描述
	@Column(name = "description")
	private String description;
	
	//发生变更时间
	@Column(name = "createDate")
	private Date createDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getRouterId() {
		return routerId;
	}

	public void setRouterId(long routerId) {
		this.routerId = routerId;
	}

	public Date getEndTimeLast() {
		return endTimeLast;
	}

	public void setEndTimeLast(Date endTimeLast) {
		this.endTimeLast = endTimeLast;
	}

	public Date getEndTimeNow() {
		return endTimeNow;
	}

	public void setEndTimeNow(Date endTimeNow) {
		this.endTimeNow = endTimeNow;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}

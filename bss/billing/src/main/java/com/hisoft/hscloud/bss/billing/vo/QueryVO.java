/* 
* 文 件 名:  QueryVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-1-23 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.bss.billing.vo; 

import java.util.Date;
import java.util.List;

/** 
 * <查询条件及对应值> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-1-23] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class QueryVO {
	
	private Date startTime;
	private Date endTime;
	private int transactionType;
	private String email;
	private String fuzzy;
	private String platformid;
	private String month;
	private String domainId;
	private String exportWay;
	private List<Long> domainIds;
	private int accountId;
	private String description;
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFuzzy() {
		return fuzzy;
	}
	public void setFuzzy(String fuzzy) {
		this.fuzzy = fuzzy;
	}
	public String getPlatformid() {
		return platformid;
	}
	public void setPlatformid(String platformid) {
		this.platformid = platformid;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDomainId() {
		return domainId;
	}
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}
	public String getExportWay() {
		return exportWay;
	}
	public void setExportWay(String exportWay) {
		this.exportWay = exportWay;
	}
	public List<Long> getDomainIds() {
		return domainIds;
	}
	public void setDomainIds(List<Long> domainIds) {
		this.domainIds = domainIds;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}

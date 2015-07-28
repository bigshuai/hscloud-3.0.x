/* 
* 文 件 名:  AccessAccount.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-16 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.crm.usermanager.entity; 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * <API接口用户> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-16] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@Entity
@Table(name = "hc_access_account")
public class AccessAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; // 主健
    
    @Column(name="accessid",unique=true, nullable = false)
    private String accessId;
    
    @Column(name="accesskey")
    private String accessKey;
    
    @Column(name="userid")
    private String userId;
    
    @Column(name="accountid")
    private String accountId;
    
    @Column(name="emailaddr")
    private String emailAddr;
    
    //0:大客户，1：分平台
    @Column(name="type")
    private int type;
    
    @Column(name="ip")
    private String ip;
    
    @Column(name="description")
    private String description;
    
    @Column(name="factory_senquence")
    private String factorySenquence = "factory_senquence";
    
    @Column(name="request_address")
    private String requestAddress;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFactorySenquence() {
        return factorySenquence;
    }

    public void setFactorySenquence(String factorySenquence) {
        this.factorySenquence = factorySenquence;
    }

	public String getRequestAddress() {
		return requestAddress;
	}

	public void setRequestAddress(String requestAddress) {
		this.requestAddress = requestAddress;
	}
}

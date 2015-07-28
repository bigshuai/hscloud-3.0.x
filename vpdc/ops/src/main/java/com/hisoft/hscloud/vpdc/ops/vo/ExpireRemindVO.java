/* 
* 文 件 名:  ExpireRemindVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-6-19 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.vpdc.ops.vo; 

import java.util.Date;

/** 
 * <到期提醒VO> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-6-19] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ExpireRemindVO {
    private String vmId;
    private long referenceId;
    private String name;
    private long createId;
    private int vmType;
    private Integer vmBusinessStatus;
    private Long scId;
    private Date startTime;
    private Date endTime;
    private long userId;
    private String userName;
    private long domainId;
    private String email;
    private int isEnable;
    private Integer buyType;
    
    public String getVmId() {
        return vmId;
    }
    public void setVmId(String vmId) {
        this.vmId = vmId;
    }
    public long getReferenceId() {
        return referenceId;
    }
    public void setReferenceId(long referenceId) {
        this.referenceId = referenceId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getCreateId() {
        return createId;
    }
    public void setCreateId(long createId) {
        this.createId = createId;
    }
    public int getVmType() {
        return vmType;
    }
    public void setVmType(int vmType) {
        this.vmType = vmType;
    }
    public Integer getVmBusinessStatus() {
        return vmBusinessStatus;
    }
    public void setVmBusinessStatus(Integer vmBusinessStatus) {
        this.vmBusinessStatus = vmBusinessStatus;
    }
    public Long getScId() {
        return scId;
    }
    public void setScId(Long scId) {
        this.scId = scId;
    }
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
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public long getDomainId() {
        return domainId;
    }
    public void setDomainId(long domainId) {
        this.domainId = domainId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
	public int getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}
	public Integer getBuyType() {
		return buyType;
	}
	public void setBuyType(Integer buyType) {
		this.buyType = buyType;
	}
	
}

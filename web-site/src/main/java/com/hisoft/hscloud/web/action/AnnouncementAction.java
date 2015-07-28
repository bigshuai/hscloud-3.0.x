/* 
* 文 件 名:  AnnouncementAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-25 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.entity.Domain;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.message.entity.Announcement;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-25] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class AnnouncementAction extends HSCloudAction {

    /** 
    * 注释内容 
    */
    private static final long serialVersionUID = 7444751807265744225L;
    
    private static Logger logger=Logger.getLogger(AccountAction.class);
    
    @Autowired
    private Facade facade;
    //读取站外公告条数
    private final static int OUTER_COUNTER = 2;
    
    private final static int OUTER_COUNTER_MORE = 10;
    
    //读取站内公告条数
    private final static int INNER_COUNTER = 10;
    
    //more显示条数
    private final static int MORE_COUNTER = 8;
    
    private Long domainId;
    
    String domainCode;
    
    /**
     * 显示站内公告
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void showOuterAnnouncement() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter showOuterAnnouncement method.");			
		}
		Domain domain=facade.loadDomain(domainCode);
        try{
            List<Announcement> result = facade.findAnnouncementByConditoin(OUTER_COUNTER, Constants.ANNOUNCEMENT_OUTER_TYPE, domain.getId());
            fillActionResult(result);
        } catch(Exception ex) {
            this.dealThrow(Constants.ANNOUNCEMENT_OUTER_EXCEPTION, "显示站外公告错误", ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit showOuterAnnouncement method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 显示站外公告 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void showInnerAnnouncement() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter showInnerAnnouncement method.");			
		}
		Domain domain=facade.loadDomain(domainCode);
        try{
            User user=(User)super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
            if(user != null) {
                List<Announcement> result = facade.findAnnouncementByConditoin(INNER_COUNTER, Constants.ANNOUNCEMENT_INNER_TYPE, domain.getId());
                fillActionResult(result);
            }
        } catch(Exception ex) {
            this.dealThrow(Constants.ANNOUNCEMENT_INNER_EXCEPTION, "显示站内公告错误", ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit showInnerAnnouncement method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 显示more公告 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void showMoreAnnouncement() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter showMoreAnnouncement method.");			
		}
		Domain domain=facade.loadDomain(domainCode);
        try{
            List<Announcement> result = facade.findAnnouncementByConditoin(MORE_COUNTER, Constants.ANNOUNCEMENT_INNER_TYPE, domain.getId());
            fillActionResult(result);
        } catch(Exception ex) {
            this.dealThrow(Constants.ANNOUNCEMENT_MORE_EXCEPTION, "显示more公告错误", ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit showMoreAnnouncement method.takeTime:" + takeTime + "ms");
		}
    }
    
    public void showMoreOuterAnnouncement() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter showMoreOuterAnnouncement method.");			
		}
		Domain domain=facade.loadDomain(domainCode);
        try{
            List<Announcement> result = facade.findAnnouncementByConditoin(OUTER_COUNTER_MORE, Constants.ANNOUNCEMENT_OUTER_TYPE, domain.getId());
            fillActionResult(result);
        } catch(Exception ex) {
            this.dealThrow(Constants.ANNOUNCEMENT_OUTER_EXCEPTION, "显示站外公告错误", ex, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit showMoreOuterAnnouncement method.takeTime:" + takeTime + "ms");
		}
    }

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
}

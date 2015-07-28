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

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.message.vo.AnnouncementVO;
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
    
    @Autowired
    private Facade facade;
    
    private Page<Map<String, Object>> announcementPage = new Page<Map<String, Object>>();
    private Logger logger = Logger.getLogger(this.getClass());
    private int page;
    private int limit;
    private String query;
    
    private AnnouncementVO announcementVO;
    /**
     * 发布公告 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void saveAnnouncement() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter saveAnnouncement method.");			
		}
        Admin admin = (Admin) this.getCurrentLoginUser();
        if (admin == null) {
            fillActionResult(Constants.ACCOUNT_IS_LOGOUT);
        } else {
            try{
                facade.saveAnnouncement(announcementVO, admin.getId());
                facade.insertOperationLog(admin,"发布公告","发布公告",Constants.RESULT_SUCESS);
            } catch(Exception e) {
                facade.insertOperationLog(admin,"发布公告:"+e.getMessage(),"发布公告",Constants.RESULT_FAILURE);
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit saveAnnouncement method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 查询公告页 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void findAnnouncementPage() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAnnouncementPage method.");			
		}
        announcementPage.setPageNo(page);
        announcementPage.setPageSize(limit);        
        if (StringUtils.isNotBlank(query)) {
            try {
                query = new String(query.getBytes("iso8859_1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION, e, logger);
            }
        }
        announcementPage = facade.findAnnouncementPage(announcementPage, query);
        fillActionResult(announcementPage);
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAnnouncementPage method.takeTime:" + takeTime + "ms");
		}
    }
    
    /**
     * 公告删除 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void delAnnouncement() {
    	long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter delAnnouncement method.");			
		}
		Admin admin=null;
        try{
            admin = (Admin) super.getCurrentLoginUser();
            facade.delAnnouncement(announcementVO.getId());
            facade.insertOperationLog(admin,"删除公告","删除公告",Constants.RESULT_SUCESS);
        } catch(Exception e) {
            facade.insertOperationLog(admin,"删除公告错误:"+e.getMessage(),"删除公告",Constants.RESULT_FAILURE);
            this.dealThrow(Constants.ANNOUNCEMENT_EXCEPTION,e, logger);
        }
        if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit delAnnouncement method.takeTime:" + takeTime + "ms");
		}
    }

    public AnnouncementVO getAnnouncementVO() {
        return announcementVO;
    }

    public void setAnnouncementVO(AnnouncementVO announcementVO) {
        this.announcementVO = announcementVO;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Page<Map<String, Object>> getAnnouncementPage() {
        return announcementPage;
    }

    public void setAnnouncementPage(Page<Map<String, Object>> announcementPage) {
        this.announcementPage = announcementPage;
    }
    
}

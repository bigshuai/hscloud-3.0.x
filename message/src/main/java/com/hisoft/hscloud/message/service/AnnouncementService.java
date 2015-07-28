/* 
* 文 件 名:  AnnouncementService.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-24 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.message.service; 

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.message.entity.Announcement;
import com.hisoft.hscloud.message.vo.AnnouncementVO;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-24] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface AnnouncementService {
    /**
     * 保存公告信息 
    * <功能详细描述> 
     * @param adminId 
    * @param announcement
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public void saveAnnouncement(AnnouncementVO announcementVO, long adminId);

    public List<Announcement> findAnnouncementByConditoin(int counter, int type, Long domainId);
    
    /**
     * 查询公告列表页 
    * <功能详细描述> 
    * @param announcementPage
    * @param query
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Page<Map<String, Object>> findAnnouncementPage(
            Page<Map<String, Object>> announcementPage, String query);
    
    /**
     * 删除公告 
    * <功能详细描述> 
    * @param announcementId 
    * @see [类、类#方法、类#成员]
     */
    public void delAnnouncement(long announcementId);
}

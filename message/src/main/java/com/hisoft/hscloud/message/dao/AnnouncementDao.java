/* 
* 文 件 名:  AnnouncementDao.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-24 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.message.dao; 

import java.util.List;
import java.util.Map;

import org.springside.modules.orm.Page;

import com.hisoft.hscloud.message.entity.Announcement;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-24] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public interface AnnouncementDao {
    public long saveAnnouncement(Announcement announcement);

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
    /**
     * 根据id获得公告对象 
    * <功能详细描述> 
    * @param announcementId
    * @return 
    * @see [类、类#方法、类#成员]
     */
    public Announcement getAnnouncement(long announcementId);
}

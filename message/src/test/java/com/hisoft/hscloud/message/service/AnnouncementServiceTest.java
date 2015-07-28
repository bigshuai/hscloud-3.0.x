/* 
* 文 件 名:  AnnouncementServiceTest.java 
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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Constants;
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
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-message-test.xml" })
public class AnnouncementServiceTest {
    @Autowired
    private AnnouncementService announcementService;
    
    @Test
    public void testSaveAnnouncement() {
        AnnouncementVO announcement = new AnnouncementVO();
       // announcement.setId(24l);
        announcement.setTitle("title1");
        announcement.setContent("content123");
        announcement.setType(Constants.ANNOUNCEMENT_INNER_TYPE);
        announcementService.saveAnnouncement(announcement, 2);
    }
    
    @Test
    public void testFindAnnouncementByConditoin() {
        int counter = 8;
        List<Announcement> list = announcementService.findAnnouncementByConditoin(counter, Constants.ANNOUNCEMENT_INNER_TYPE, 1L);
        System.out.println(list);
//        Assert.assertEquals(list.size() <= counter, true);
    }
    
    @Test
    public void testFindAnnouncementPage() {
        String query = null;
        Page<Map<String, Object>> announcementPage = new Page<Map<String, Object>>();
        announcementPage = announcementService.findAnnouncementPage(announcementPage, query);
    }
    
    @Test
    public void testDelAnnouncement() {
        long announcementId = 18l;
        announcementService.delAnnouncement(announcementId);
    }
}

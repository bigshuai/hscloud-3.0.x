/* 
* 文 件 名:  VideoServiceTest.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-5-7 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.storage.service; 

import java.util.HashMap;
import java.util.Map;

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

import com.hisoft.hscloud.storage.entity.Video;
import com.hisoft.hscloud.storage.vo.VideoVO;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-5-7] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
@Transactional  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@ContextConfiguration(locations = { "classpath:applicationContext-hscloud-storage-test.xml" })
public class VideoServiceTest {
    @Autowired
    private VideoService videoService;
    
    @Test
    public void testSaveVideo() {
        for(int i = 0; i < 20; i++) {
        Video video = new Video();
        video.setChannel("channel");
        video.setDate("20130506");
        video.setDevice("device");
        video.setLengthStatus("0");
        video.setName("name");
        video.setPlanStatus("0");
        video.setTime("150321");
        video.setTimestamp(System.currentTimeMillis() / 1000);
        video.setFileSize("20G");
        videoService.saveVideo(video);
        }
    }
    
    @Test
    public void testFindVideo() {
        Map<String, Object> condition = new HashMap<String, Object>();
        String tenantId = "1e871ef2a88f42248d137ab6fd2c9a6c";
        
        condition.put("tenantId", tenantId);
        Page<VideoVO> page = new Page<VideoVO>();
        page.setPageNo(1);
        page.setPageSize(20);
        page = videoService.findVideo(page, condition);
        System.out.println(page.getResult().size());
    }
}

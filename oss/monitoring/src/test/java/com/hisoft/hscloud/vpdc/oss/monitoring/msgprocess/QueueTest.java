/**
 * @title QueueTest.java
 * @package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess
 * @description 
 * @author YuezhouLi
 * @update 2012-5-24 上午11:37:33
 * @version V1.0
 */
package com.hisoft.hscloud.vpdc.oss.monitoring.msgprocess;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @description 
 * @version 1.0
 * @author YuezhouLi
 * @update 2012-5-24 上午11:37:33
 */
@ContextConfiguration("classpath:applicationContext-hscloud-vpdc-oss-monitor.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class QueueTest {
	@Autowired
	Queue queue ;
	
	@Test
	public void queueSubscribeTest(){
		long s = 1L;
		while(true){
			
		}
	}
	
}

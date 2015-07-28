/**
 * @title ThreadService.java
 * @package com.hisoft.hscloud.systemmanagement.service
 * @description 用一句话描述该文件做什么
 * @author AaronFeng
 * @update 2012-10-16 下午3:32:36
 * @version V1.0
 */
package com.hisoft.hscloud.systemmanagement.service;

import org.apache.log4j.Logger;
import com.hisoft.hscloud.systemmanagement.service.vo.HscloudThreadVO;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author AaronFeng
 * @update 2012-10-16 下午3:32:36
 */
public class HscloudThread extends Thread{
	private Logger logger = Logger.getLogger(this.getClass());
	private HscloudThreadVO htv;
	public HscloudThread(HscloudThreadVO htv){
		this.htv = htv;
	}

	/* (非 Javadoc) 
	 * <p>Title: run</p> 
	 * <p>Description: </p>  
	 * @see java.lang.Thread#run() 
	 */
	@Override
	public void run() {
		try {
			while(htv.isRunning()){
				this.htv.execute();
				Thread.sleep(htv.getSleepTime());
			}			
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			logger.error("thread run InterruptedException:",e);
		}
	}
	
}

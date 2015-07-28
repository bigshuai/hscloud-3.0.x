/**
 * @title HscloudThreadVO.java
 * @package com.hisoft.hscloud.systemmanagement.service.vo
 * @description 用一句话描述该文件做什么
 * @author AaronFeng
 * @update 2012-10-16 下午3:39:38
 * @version V1.0
 */
package com.hisoft.hscloud.systemmanagement.service.vo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hisoft.hscloud.systemmanagement.entity.ProcessResource;
import com.hisoft.hscloud.systemmanagement.service.ThreadService;
import com.hisoft.hscloud.systemmanagement.service.ThreadServiceImpl;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author AaronFeng
 * @update 2012-10-16 下午3:39:38
 */
public class HscloudThreadVO implements Job{
	private Logger logger = Logger.getLogger(this.getClass());
	private String threadKey;
	private Object object;
	private Method method;
	private boolean running;
	private long sleepTime;
	private DailyIterator dailyIterator = new DailyIterator();
	private ThreadService threadService;
	/**
	 * @return threadKey : return the property threadKey.
	 */
	public String getThreadKey() {
		return threadKey;
	}
	/**
	 * @param threadKey : set the property threadKey.
	 */
	public void setThreadKey(String threadKey) {
		this.threadKey = threadKey;
	}
	/**
	 * @return object : return the property object.
	 */
	public Object getObject() {
		return object;
	}
	/**
	 * @param object : set the property object.
	 */
	public void setObject(Object object) {
		this.object = object;
	}
	/**
	 * @return method : return the property method.
	 */
	public Method getMethod() {
		return method;
	}
	/**
	 * @param method : set the property method.
	 */
	public void setMethod(Method method) {
		this.method = method;
	}
	
	/**
	 * @return running : return the property running.
	 */
	public boolean isRunning() {
		return running;
	}
	/**
	 * @param running : set the property running.
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	/**
	 * @return sleepTime : return the property sleepTime.
	 */
	public long getSleepTime() {
		return sleepTime;
	}
	/**
	 * @param sleepTime : set the property sleepTime.
	 */
	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}
	public HscloudThreadVO(){
		
	}
	public HscloudThreadVO(String threadKey, String className, String methodName, Object object, boolean running, long sleepTime) throws ClassNotFoundException, SecurityException, NoSuchMethodException{
		this.threadKey = threadKey;
		this.method = Class.forName(className).getMethod(methodName);
		this.object = object;
		this.running = running;
		this.sleepTime = sleepTime;
	}	
	public HscloudThreadVO(String threadKey, String className, String methodName, Object object, boolean running, long sleepTime,ThreadService threadService) throws ClassNotFoundException, SecurityException, NoSuchMethodException{
		this.threadKey = threadKey;
		this.method = Class.forName(className).getMethod(methodName);
		this.object = object;
		this.running = running;
		this.sleepTime = sleepTime;
		this.threadService = threadService;
	}
	public void execute() throws InterruptedException{
		try {
			logger.debug("Thread start:"+this.getThreadKey());			
			method.invoke(object);
		} catch (IllegalArgumentException e) {
			logger.error("method.invoke(object) IllegalArgumentException:",e);
			throw new InterruptedException(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error("method.invoke(object) IllegalAccessException:",e);
			throw new InterruptedException(e.getMessage());
		} catch (InvocationTargetException e) {
			logger.error("method.invoke(object) InvocationTargetException:",e);
			throw new InterruptedException(e.getMessage());
		}
	}
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		HscloudThreadVO htv = (HscloudThreadVO) context.getJobDetail().getJobDataMap().get(context.getJobDetail().getName());
		if(htv.isRunning()){
			try {
				htv.execute();
			} catch (InterruptedException e) {
				logger.error("ThreadJob InterruptedException:", e);
			}
		}
		
	}
	
	/*public void execute() throws InterruptedException{
		try {
			logger.info("Thread start:"+this.getThreadKey());
			ProcessResource processResource = threadService.getProcessResourceByProcessCode(threadKey);
			Date nextTime = processResource.getNextstartTime();
			int day = 1;
			if(processResource.getDay()!=null){
				day = processResource.getDay();
			}
			if(nextTime == null){
				method.invoke(object);
			}else if(dailyIterator.isOverTime(nextTime)){
				method.invoke(object);
				processResource.setNextstartTime(dailyIterator.nextDate(day));
				threadService.updateProcessResource(processResource);
			}
//			while(this.isRunning()){
				
//				Thread.sleep(sleepTime);
//			}			
		} catch (IllegalArgumentException e) {
			logger.error("method.invoke(object) IllegalArgumentException:",e);
			throw new InterruptedException(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error("method.invoke(object) IllegalAccessException:",e);
			throw new InterruptedException(e.getMessage());
		} catch (InvocationTargetException e) {
			logger.error("method.invoke(object) InvocationTargetException:",e);
			throw new InterruptedException(e.getMessage());
		}
	}*/
}

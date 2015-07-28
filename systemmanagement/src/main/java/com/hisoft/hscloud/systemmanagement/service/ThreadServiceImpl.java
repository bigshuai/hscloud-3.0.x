/**
 * @title ThreadServiceImpl.java
 * @package com.hisoft.hscloud.systemmanagement.service
 * @description 用一句话描述该文件做什么
 * @author AaronFeng
 * @update 2012-10-16 下午3:54:56
 * @version V1.0
 */
package com.hisoft.hscloud.systemmanagement.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.systemmanagement.dao.ProcessResourceDao;
import com.hisoft.hscloud.systemmanagement.entity.ProcessResource;
import com.hisoft.hscloud.systemmanagement.service.util.Constants;
import com.hisoft.hscloud.systemmanagement.service.vo.HscloudThreadVO;
import com.hisoft.hscloud.systemmanagement.vo.ProcessResourceVO;

/**
 * @description 这里用一句话描述这个类的作用
 * @version 1.0
 * @author AaronFeng
 * @update 2012-10-16 下午3:54:56
 */
@Service
public class ThreadServiceImpl implements ThreadService{
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private ProcessResourceDao processResourceDao;
	@Autowired
	private WebApplicationContext wc;
	private SchedulerFactory sf;
	private Scheduler sched;
	private CronTrigger trigger;
	private JobDetail job;
	
	/*@Override
	@Transactional
	public void initThread(WebApplicationContext wc) {
		this.wc = wc;
		//读数据配置的所有需要以线程方式运行的类方法
		List<ProcessResource> processResourceList = processResourceDao.findAllProcessResource();
		for(ProcessResource processResource : processResourceList){
//			addBean2Thread(processResource);
			initScheduler(processResource);
		}
	}*/	
	@Override
	@Transactional
	public void initThread() {
		//读数据配置的所有需要以线程方式运行的类方法
		List<ProcessResource> processResourceList = processResourceDao.findAllProcessResource();
		for(ProcessResource processResource : processResourceList){
			initScheduler(processResource);
		}
	}

	
	@Override
	@Transactional(readOnly = false)
	public void startThread(String threadKey) {
		HscloudThreadVO hscloudThreadVO = Constants.HSCLOUD_THREAD_VO_MAP.get(threadKey);
		if(hscloudThreadVO != null){
//			HscloudThread ht = new HscloudThread(hscloudThreadVO);
//			ht.start();
			hscloudThreadVO.setRunning(true);
//			Constants.HSCLOUD_THREAD_MAP.put(hscloudThreadVO.getThreadKey(), ht);
			ProcessResource processResource = processResourceDao.getProcessResourceByPropertyName("processCode", threadKey);
			processResource.setLaststartTime(java.util.Calendar.getInstance().getTime());
			processResourceDao.updateProcessResource(processResource);
			this.startScheduler(hscloudThreadVO,processResource);
		}		
	}

	
	@Override
	@Transactional(readOnly = false)
	public void stopThread(String threadKey) {
//		Constants.HSCLOUD_THREAD_MAP.get(threadKey).interrupt();
//		Constants.HSCLOUD_THREAD_MAP.remove(threadKey);
		Constants.HSCLOUD_THREAD_VO_MAP.get(threadKey).setRunning(false);
		ProcessResource processResource = processResourceDao.getProcessResourceByPropertyName("processCode", threadKey);
		processResource.setLaststartTime(java.util.Calendar.getInstance().getTime());
		processResourceDao.updateProcessResource(processResource);		
	}
	
	public void addBean2Thread(ProcessResource processResource){
		String threadKey = processResource.getProcessCode();
		logger.debug("threadKey:"+threadKey);
		String beanName = processResource.getBeanName();
		logger.debug("beanName:"+beanName);
		String className = processResource.getClassName();
		logger.debug("className:"+className);
		String methodName = processResource.getMethodName();
		logger.debug("methodName:"+methodName);
		boolean running = processResource.getStatus()==Constants.HSCLOUD_THREAD_INIT;
		logger.debug("running:"+processResource.getStatus());
		long sleepTime = processResource.getSleepTime();
		logger.debug("sleepTime:"+sleepTime);
		Object object = wc.getBean(beanName);
		logger.debug("object:"+object);
		ThreadService threadService = (ThreadService) wc.getBean("threadServiceImpl");
		/**如果上面的方式取不到bean,则可以用以下的方式获取
		ctx.scan(className);
		ctx.refresh();
		Object object = ctx.getBean(Class.forName(className));//此处也可以使用ctx.getBean(beanName)
		**/
		
		HscloudThreadVO hscloudThreadVO = null;
		try {
			hscloudThreadVO = new HscloudThreadVO(threadKey,className,methodName,object,running,sleepTime,threadService);
			Constants.HSCLOUD_THREAD_VO_MAP.put(hscloudThreadVO.getThreadKey(), hscloudThreadVO);
			//如果初始化时需要启动则创建并启动线程
			if(hscloudThreadVO!=null && hscloudThreadVO.isRunning()){
				HscloudThread ht = new HscloudThread(hscloudThreadVO);
				ht.start();
				Constants.HSCLOUD_THREAD_MAP.put(hscloudThreadVO.getThreadKey(), ht);
			}
		} catch (SecurityException e1) {
			logger.error("new HscloudThreadVO SecurityException:",e1);
		} catch (ClassNotFoundException e1) {
			logger.error("new HscloudThreadVO ClassNotFoundException:",e1);
		} catch (NoSuchMethodException e1) {
			logger.error("new HscloudThreadVO NoSuchMethodException:",e1);
		}		
	}
	public void initScheduler(ProcessResource processResource){	
		HscloudThreadVO hscloudThreadVO = null;
		try {
			String threadKey = processResource.getProcessCode();
			logger.debug("threadKey:"+threadKey);
			String beanName = processResource.getBeanName();
			logger.debug("beanName:"+beanName);
			String className = processResource.getClassName();
			logger.debug("className:"+className);
			String methodName = processResource.getMethodName();
			logger.debug("methodName:"+methodName);
			boolean running = processResource.getStatus()==Constants.HSCLOUD_THREAD_INIT;
			logger.debug("running:"+processResource.getStatus());
			long sleepTime = processResource.getSleepTime();
			logger.debug("sleepTime:"+sleepTime);
			Object object = wc.getBean(beanName);
			logger.debug("object:"+object);
			
			hscloudThreadVO = new HscloudThreadVO(threadKey,className,methodName,object,running,sleepTime);
			
			Constants.HSCLOUD_THREAD_VO_MAP.put(hscloudThreadVO.getThreadKey(), hscloudThreadVO);
			
			//如果初始化时需要启动则创建并启动线程
			if(hscloudThreadVO!=null && hscloudThreadVO.isRunning()){
				this.startScheduler(hscloudThreadVO,processResource);
			}			
		}  catch (SecurityException e) {
			logger.error("new HscloudThreadVO SecurityException:",e);
		} catch (NoSuchMethodException e) {
			logger.error("new HscloudThreadVO ClassNotFoundException:",e);
		} catch (ClassNotFoundException e1) {
			logger.error("new HscloudThreadVO ClassNotFoundException:",e1);
		}		
	}
	private void startScheduler(HscloudThreadVO hscloudThreadVO,ProcessResource processResource){
		String cronExpression = processResource.getCronExpression();
		if(StringUtils.isEmpty(cronExpression)){
			cronExpression = Constants.CRONEXPRESSION;
		}
		Trigger oldTrigger = null;
		try{
			sf = new StdSchedulerFactory();
			sched = sf.getScheduler();
			trigger = new CronTrigger();
			trigger.setName(hscloudThreadVO.getThreadKey());
			trigger.setGroup(hscloudThreadVO.getThreadKey());
			trigger.setJobName(hscloudThreadVO.getThreadKey());
			trigger.setCronExpression(cronExpression);
			job = new JobDetail();
			job.getJobDataMap().put(hscloudThreadVO.getThreadKey(), hscloudThreadVO);
			job.setName(hscloudThreadVO.getThreadKey());
			//job.setGroup("DOPSyncDataGroup");
			job.setJobClass(HscloudThreadVO.class);
			sched.addJob(job, true);
//			sched.scheduleJob(trigger);
			oldTrigger = sched.getTrigger(trigger.getName(), trigger.getGroup());
			if(oldTrigger == null){
				sched.scheduleJob(trigger);
			}else{
				sched.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
			}			
			sched.start();
		} catch (ParseException e) {
			logger.error("new HscloudThreadVO ParseException:",e);
		} catch (SchedulerException e) {
			logger.error("new HscloudThreadVO SchedulerException:",e);
		}  
		
	}

	@Override
//	@Transactional
	public Page<ProcessResourceVO> findAllProcess (
			Page<ProcessResourceVO> pageProcessVO) throws HsCloudException {
		ProcessResourceVO processResourceVO = null;
		HscloudThreadVO hscloudThreadVO = null;
		Page<ProcessResource> pageProcess = new Page<ProcessResource>();
		List<ProcessResourceVO> listProcessResourceVO = new ArrayList<ProcessResourceVO>();
		pageProcess.setPageNo(pageProcessVO.getPageNo());
		pageProcess.setPageSize(pageProcessVO.getPageSize());
		List<ProcessResource> listProcess = processResourceDao.findAllProcessResource(pageProcess).getResult();
		for(ProcessResource processResource :listProcess){
			processResourceVO = new ProcessResourceVO();
			processResourceVO.setId(processResource.getId());
			processResourceVO.setBeanName(processResource.getBeanName());
			processResourceVO.setClassName(processResource.getClassName());
			processResourceVO.setMethodName(processResource.getMethodName());
			processResourceVO.setProcessCode(processResource.getProcessCode());			
			processResourceVO.setLaststartTime(processResource.getLaststartTime());
			hscloudThreadVO = Constants.HSCLOUD_THREAD_VO_MAP.get(processResource.getProcessCode());
			if(hscloudThreadVO == null ){
//				addBean2Thread(processResource);
				initScheduler(processResource);
				hscloudThreadVO = Constants.HSCLOUD_THREAD_VO_MAP.get(processResource.getProcessCode());
			}
			if(hscloudThreadVO != null && hscloudThreadVO.isRunning()){				
				processResourceVO.setStatus(Constants.HSCLOUD_THREAD_RUNING);
			}else{
				processResourceVO.setStatus(Constants.HSCLOUD_THREAD_CLOSEING);
			}
			listProcessResourceVO.add(processResourceVO);
		}
		pageProcessVO.setTotalCount(listProcessResourceVO.size());
		pageProcessVO.setResult(listProcessResourceVO);
		return pageProcessVO;
	}

	@Override
	@Transactional
	public ProcessResource getProcessResourceByProcessCode(String processCode)
			throws HsCloudException {
		return processResourceDao.getProcessResourceByPropertyName("processCode", processCode);
	}

	@Override
	@Transactional
	public boolean updateProcessResource(ProcessResource processResource)
			throws HsCloudException {
		return processResourceDao.updateProcessResource(processResource);
	}

}

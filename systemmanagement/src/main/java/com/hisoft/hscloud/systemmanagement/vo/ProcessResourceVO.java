/* 
* 文 件 名:  ProcessResourceVO.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2012-12-13 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.systemmanagement.vo; 

import java.util.Date;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2012-12-13] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ProcessResourceVO {

	private long id;
	/*
	 * 注解注入的Bean名称
	 */
	private String beanName;
	/*
	 * 注入Bean的类全名
	 */
	private String className;
	/*
	 * 线程执行的业务方法名
	 */
	private String methodName;
	/*
	 * 线程执行的状态
	 */
	private int status;
	/*
	 * 上次执行的时间
	 */
	private Date laststartTime;
	
	/*
	 * 线程执行间隔时间
	 */
	private long sleepTime;
	
	/**
	 * 线程code
	 */
	private String processCode;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return beanName : return the property beanName.
	 */
	public String getBeanName() {
		return beanName;
	}
	/**
	 * @param beanName : set the property beanName.
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	/**
	 * @return className : return the property className.
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className : set the property className.
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return methodName : return the property methodName.
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @param methodName : set the property methodName.
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * @return status : return the property status.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status : set the property status.
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return laststartTime : return the property laststartTime.
	 */
	public Date getLaststartTime() {
		return laststartTime;
	}
	/**
	 * @param laststartTime : set the property laststartTime.
	 */
	public void setLaststartTime(Date laststartTime) {
		this.laststartTime = laststartTime;
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
	public String getProcessCode() {
		return processCode;
	}
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}
	/* (非 Javadoc) 
	 * <p>Title: toString</p> 
	 * <p>Description: </p> 
	 * @return 
	 * @see java.lang.Object#toString() 
	 */
	@Override
	public String toString() {
		return "ProcessResources [id=" + id + ",beanName=" + beanName + ", className="
				+ className + ", methodName=" + methodName + ", status="
				+ status + ", laststartTime=" + laststartTime + ", processCode=" + processCode +"]";
	}
}

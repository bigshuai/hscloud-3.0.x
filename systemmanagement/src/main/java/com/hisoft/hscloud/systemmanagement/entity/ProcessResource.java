/**
 * @title ProcessResources.java
 * @package com.hisoft.hscloud.systemmanagement.entity
 * @description 用一句话描述该文件做什么
 * @author AaronFeng
 * @update 2012-10-18 下午2:23:07
 * @version V1.0
 */
package com.hisoft.hscloud.systemmanagement.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.hisoft.hscloud.common.entity.AbstractEntity;

/**
 * @description 线程资源实体类
 * @version 1.0
 * @author AaronFeng
 * @update 2012-10-18 下午2:23:07
 */
@Entity
@Table(name = "hc_processResource")
public class ProcessResource extends AbstractEntity {
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
	 * 下次执行的时间
	 */
//	private Date nextstartTime;
	/*
	 * 线程执行间隔时间
	 */
	private long sleepTime;
	
	/**
	 * 线程code
	 */
	private String processCode;
	//间隔天数
//	private Integer day = 1;
	private String cronExpression = "0 0/30 * * * ?";//每半个小时执行一次
	
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
	
//	public Date getNextstartTime() {
//		return nextstartTime;
//	}
//	public void setNextstartTime(Date nextstartTime) {
//		this.nextstartTime = nextstartTime;
//	}
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
//	public Integer getDay() {
//		return day;
//	}
//	public void setDay(Integer day) {
//		this.day = day;
//	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	/* (非 Javadoc) 
	 * <p>Title: toString</p> 
	 * <p>Description: </p> 
	 * @return 
	 * @see java.lang.Object#toString() 
	 */
	@Override
	public String toString() {
		return "ProcessResources [beanName=" + beanName + ", className="
				+ className + ", methodName=" + methodName + ", status="
				+ status + ", laststartTime=" + laststartTime + ", processCode=" + processCode +"]";
	}
	
}

/**
 * @title MethodTimeAdvice.java
 * @package com.hisoft.hscloud.common.util
 * @description 用一句话描述该文件做什么
 * @author AaronFeng
 * @update 2013-4-7 上午11:14:29
 * @version V1.0
 */
package com.hisoft.hscloud.common.util;

/**
 * @description 记录方法的执行时间 
 * @version 1.0
 * @author AaronFeng
 * @update 2013-4-7 上午11:14:29
 */
import org.aopalliance.intercept.MethodInterceptor;  
import org.aopalliance.intercept.MethodInvocation;  
import org.apache.commons.lang.StringUtils;  
import org.apache.commons.lang.time.StopWatch;  
import org.apache.log4j.Logger;  

public class MethodTimeAdvice implements MethodInterceptor {  
	public static Logger logger=Logger.getLogger(MethodTimeAdvice.class);  
	/** 
	 * 拦截要执行的目标方法 
	 */  
	public Object invoke(MethodInvocation invocation) throws Throwable {  
		//用 commons-lang 提供的 StopWatch 计时，Spring 也提供了一个 StopWatch  
		StopWatch clock = new StopWatch();  
		clock.start(); //计时开始  
		Object result = invocation.proceed();  
		clock.stop();  //计时结束  
		//方法参数类型，转换成简单类型  
		Class[] params = invocation.getMethod().getParameterTypes();  
		String[] simpleParams = new String[params.length];  
		for (int i = 0; i < params.length; i++) {  
			simpleParams[i] = params[i].getSimpleName();  
		}  
		//如果耗费时间超过10秒，则打印日志
		if(clock.getTime()>10000){
			logger.debug("该方法执行耗费:" + clock.getTime() + " ms ["  
					+ invocation.getThis().getClass().getName() + "."  
					+ invocation.getMethod().getName() + "("+StringUtils.join(simpleParams,",")+")] ");
		}		  
		return result;
	}  
}  


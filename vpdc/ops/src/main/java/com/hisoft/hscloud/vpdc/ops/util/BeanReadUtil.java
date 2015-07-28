package com.hisoft.hscloud.vpdc.ops.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BeanReadUtil {
	private static ApplicationContext ctx;
	
	public static ApplicationContext getCtx() {
		if(ctx == null)
			ctx = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
		return ctx;
	}

	public static void setCtx(ApplicationContext ctx) {
		BeanReadUtil.ctx = ctx;
	}

	public static Object getBean(String beanName){
		//1. WebApplicationContext webApplicationContext = (WebApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		//2. ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletContext sc);
		//ServletContext sc = HttpServlet.getServletContext();
		//ApplicationContext ctx =  WebApplicationContextUtils.getWebApplicationContext(sc);
		return getCtx().getBean(beanName);
	}
}

package com.hisoft.hscloud.web.Servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hisoft.hscloud.systemmanagement.service.ThreadService;

public class ServletUtil extends HttpServlet {
	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = 3313187468137903508L;
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ThreadService threadService;
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */

	@Override
	public void init() throws ServletException {
		log.debug("ThreadService start ...");
		WebApplicationContext wc = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		threadService=(ThreadService)wc.getBean("threadServiceImpl");
		threadService.initThread();
	}

}

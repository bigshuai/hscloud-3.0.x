/* 
* 文 件 名:  NodeInformationSynchronization.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-10-8 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.Servlet; 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hisoft.hscloud.bss.sla.sc.service.IServerNodeService;
import com.hisoft.hscloud.systemmanagement.service.ThreadService;

/** 
 * <节点资源限制参数同步> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-10-8] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class NodeInformationSynchronization extends HttpServlet{

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -4381173390675467733L;
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private IServerNodeService serverNodeService;
	@Override
	public void init() throws ServletException {
		logger.debug("ThreadService start ...");
		WebApplicationContext wc = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		serverNodeService=(IServerNodeService)wc.getBean("serverNodeServiceImpl");
		serverNodeService.synchronizationAllNodeIsolation();
	}
	

}

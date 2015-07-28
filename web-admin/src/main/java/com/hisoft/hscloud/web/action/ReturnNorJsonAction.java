package com.hisoft.hscloud.web.action;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.web.facade.Facade;
import com.opensymphony.xwork2.ActionSupport;

public class ReturnNorJsonAction extends ActionSupport {
	private Long userId;
	private String vmId;
	@Autowired
	private Facade facade;

	private static Logger logger = Logger.getLogger(ReturnNorJsonAction.class);

	/**
	 * <获取控制面板登录的url> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getControlPanelLoginUrl() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getControlPanelLoginUrl method.");
		}
		String result = "";
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		try {
			HttpSession session=ServletActionContext.getRequest().getSession();
			Object adminObj=session.getAttribute(Constants.LOGIN_CURRENTUSER);
			if(adminObj!=null){
				Admin admin=(Admin)adminObj;
				result=facade.getControlPanelLoginUrl(vmId,admin);
			}
			
		} catch (Exception e) {
			logger.error("获取控制面板登录url异常",e);
		}
		try{
			response.getWriter().write(result);
		}catch(Exception e){
			logger.error("获取控制面板登录url write 最后结果异常",e);
		}
		
		
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getControlPanelLoginUrl method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * <获取业务平台登录掉url> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	public void getBusinessPlatformLoginUrl() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getBusinessPlatformLoginUrl method.");
		}
		String result = "";
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		try {
			HttpSession session=ServletActionContext.getRequest().getSession();
			Object adminObj=session.getAttribute(Constants.LOGIN_CURRENTUSER);
			if(adminObj!=null){
				Admin admin=(Admin)adminObj;
				result=facade.getBusinessPlatformLoginUrl(userId,admin);
			}
			
		} catch (Exception e) {
			logger.error("获取业务平台登录url异常",e);
		}
		try{
			response.getWriter().write(result);
		}catch(Exception e){
			logger.error("获取业务平台登录url write 最后结果异常",e);
		}
		
		
		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getBusinessPlatformLoginUrl method.takeTime:" + takeTime + "ms");
		}
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

}

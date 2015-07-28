/**
 * @title HscloudAction.java
 * @package com.hisoft.hscloud.common
 * @description 统一接口的Action类
 * @author AaronFeng
 * @update 2012-4-19 上午 11:13:19
 * @version V1.0
 */
package com.hisoft.hscloud.common;

import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.vo.ActionResult;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @description 用来统一处理一般性问题，如获取登录用户，返回统一的结果对像
 * @version 1.0
 * @author AaronFeng
 * @update 2012-4-19 上午 11:13:19
 */
public abstract class HSCloudAction extends ActionSupport {
	
	private ActionResult actionResult = new ActionResult();
	
	private List<Object> primKeys = null;

	/**
	 * @return actionResult : return the property actionResult.
	 */
	public ActionResult getActionResult() {
		return actionResult;
	}

	/**
	 * @param actionResult
	 *            : set the property actionResult.
	 */
	public void setActionResult(ActionResult actionResult) {
		this.actionResult = actionResult;
	}
	
	

//	/**
//	 * 
//	 * @title: getCurrentLoginUser
//	 * @description 获取当前前台登录用户，无论是普通用户，企业用户管理员或者是子用户
//	 * @return 设定文件
//	 * @return Object 返回类型
//	 * @throws
//	 * @version 1.1
//	 * @author guole.liang
//	 * @update 2012-5-29 上午9:31:43
//	 */
//	public Object getCurrentLoginUser() {
//		return ActionContext.getContext().getSession()
//				.get(Constants.LOGIN_USER_SESSION_ATTRIBUTE_NAME);
//	}
//
//	public Object getCurrentLoginAdmin() {
//		return ActionContext.getContext().getSession()
//				.get(Constants.LOGIN_ADMIN_SESSION_ATTRIBUTE_NAME);
//	}

	public List<Object> getPrimKeys() {
		return primKeys;
	}

	public void setPrimKeys(List<Object> primKeys) {
		this.primKeys = primKeys;
	}	


	protected HttpServletRequest getRequest(){
//		return (HttpServletRequest)ac.get(ServletActionContext.HTTP_REQUEST);
		
		return (HttpServletRequest)ServletActionContext.getRequest(); 
	}

	protected HttpServletResponse getResponse(){
//		return (HttpServletResponse)ac.get(ServletActionContext.HTTP_RESPONSE);
		return (HttpServletResponse)ServletActionContext.getResponse(); 
	}
	
	protected HttpSession getSession(){
//		return this.getRequest().getSession();
		return (HttpSession)ServletActionContext.getRequest().getSession();
	}
	
	protected ServletContext getApplication(){
//		return (ServletContext)ac.getApplication();
		return (ServletContext)ServletActionContext.getServletContext();
	}
	
	protected Object getCurrentLoginUser() {
		return this.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
	}
	
//	protected Object getWebAddress(){
//		this.getRequest().getRemoteAddr();
//		this.getRequest().getRemoteHost();
//		this.getRequest().getRemotePort();
//		this.getRequest().getScheme();
//		this.getRequest().getServerName();
//		this.getRequest().getServerPort();this.getRequest().get
//		return null;
//	}
	
	public void fillActionResult(String resultCode) {
		this.actionResult.setResultCode(resultCode);
	}

	public void fillActionResult(String resultCode,String resultMsg) {
		this.actionResult.setResultCode(resultCode);
		this.actionResult.setResultMsg(resultMsg);
	}
	
	public void fillActionResult(Object resultObject) {
		this.actionResult.setResultObject(resultObject);
	}

	public void fillActionResult(String resultCode, String resultMsg,
			Object resultObject) {
		this.actionResult.setResultCode(resultCode);
		this.actionResult.setResultMsg(resultMsg);
		this.actionResult.setResultObject(resultObject);
	}

	/**
	 * 
	 * @param ex
	 *            错误异常
	 * @param defaultCode
	 *            错误编码替补值
	 * @param flag
	 *            强制处理标志
	 * @return
	 */
	protected String dealThrow(HsCloudException ex, String defaultCode, boolean flag) {
		String code = ((HsCloudException) ex).getCode();
		if ((code == null) || (flag == true)) {
			code = defaultCode;
		}
		fillActionResult(code);
		return code;
	}
	
	/**
	 * 
	 * @param ex   错误异常
	 * @param defaultCode  错误编码替补值
	 * @return
	 */
	protected String dealThrow(HsCloudException ex, String defaultCode) {
		return dealThrow(ex, defaultCode, false); 
	}
	
	protected String dealThrow(String code, String message, Exception ex, Logger logger) {
	    String result = (new HsCloudException(code, message, logger, ex)).getCode();
	    fillActionResult(result);
        return code;
	}
	protected String dealThrow(String code, Exception ex, Logger logger) {
	    String result = (new HsCloudException(code, logger, ex)).getCode();
        fillActionResult(result);
        return code;
    }

}

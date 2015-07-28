package com.hisoft.hscloud.web.action;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.entity.LogOperatorType;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudDateUtil;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.PropertiesUtils;
import com.hisoft.hscloud.common.util.SecurityHelper;
import com.hisoft.hscloud.crm.usermanager.entity.MaintenanceLog;
import com.hisoft.hscloud.crm.usermanager.entity.User;
import com.hisoft.hscloud.web.facade.Facade;
import com.opensymphony.xwork2.ActionSupport;

public class UserLoginByUrlAction extends ActionSupport {
	/** 
    * 注释内容 
    */
    private static final long serialVersionUID = 4408783780728169686L;
    private Long userId;
	private String authorKey;
	private String dateKey;
	
	private String operator;
	@Autowired
	private Facade facade;
	private static Logger logger = Logger.getLogger(UserLoginByUrlAction.class);
	/**
	 * <获取控制面板登录的url> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	/**
	 * 通过email登录
	 */
	public String userLoginByUrl() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter userLoginByEmail method.");
		}
		String result = "error";
		try {
			User loginUser = facade.getUserById(userId);
			if(loginUser.getEnable() != 3) {
			    return result;
			}
			if (loginUser != null) {
				StringBuilder originalKey=new StringBuilder();
				originalKey.append(loginUser.getEmail()).append(loginUser.getPassword());
				String encypKey=PasswordUtil.getEncyptedPasswd(originalKey.toString());
				if(encypKey.equals(authorKey)){
					String dateStr=SecurityHelper.DecryptData(dateKey, Constants.DEFAULT_SECURITY_KEY);
					Date requestLoginDate=HsCloudDateUtil.transferStr2Date("yyyy-MM-dd HH:mm:ss",dateStr);
					Map<String,String> properties=PropertiesUtils.getProperties("common.properties");
					String expirationStr=properties.get("loginExpiration");
					Date now=new Date();
					Date expirationDate=null;
					if(StringUtils.isNotBlank(expirationStr)){
						int expirationTime=Integer.parseInt(expirationStr.trim());
						expirationDate=DateUtils.addMinutes(requestLoginDate, expirationTime);
					}else{
						expirationDate=DateUtils.addMinutes(requestLoginDate, 10);
					}
					if(now.before(expirationDate)){
						ServletContext sc = ServletActionContext.getServletContext();
						String email = loginUser.getEmail();
						HttpSession session = null;
						Object sessionObject = sc.getAttribute(email);
						if (sessionObject != null)
							session = (HttpSession) sessionObject;
						if (null != session) {
							session = null;
						}
						HttpSession currentSession = ServletActionContext.getRequest().getSession();
						currentSession.setAttribute("enUid", loginUser.getId());
						currentSession.setAttribute(Constants.LOGIN_CURRENTUSER,
								loginUser);
						sc.setAttribute(email, currentSession);
						MaintenanceLog ml=new MaintenanceLog();
						ml.setUserId(userId);
						ml.setLoginDate(new Date());
						ml.setOperator(operator);
						ml.setUserType(LogOperatorType.USER.getIndex());
						facade.saveLogByLoginWithoutPass(ml);
						result = "success";
					}
				}
			}
		} catch (Exception e) {
			logger.error("login error:", e);
		}

		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit userLoginByEmail method.takeTime:" + takeTime
					+ "ms");
		}
		return result;
	}
	
	public String userLoginForPaymentByUrl() {
		long beginRunTime = 0;
		if (logger.isDebugEnabled()) {
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter userLoginByEmail method.");
		}
		String result = "error";
		try {
			User loginUser = facade.getUserById(userId);
			if(loginUser.getEnable() != 3) {
			    return result;
			}
			if (loginUser != null) {
				StringBuilder originalKey=new StringBuilder();
				originalKey.append(loginUser.getEmail()).append(loginUser.getPassword());
				String encypKey=PasswordUtil.getEncyptedPasswd(originalKey.toString());
				if(encypKey.equals(authorKey)){
					String dateStr=SecurityHelper.DecryptData(dateKey, Constants.DEFAULT_SECURITY_KEY);
					Date requestLoginDate=HsCloudDateUtil.transferStr2Date("yyyy-MM-dd HH:mm:ss",dateStr);
					Map<String,String> properties=PropertiesUtils.getProperties("common.properties");
					String expirationStr=properties.get("loginExpiration");
					Date now=new Date();
					Date expirationDate=null;
					if(StringUtils.isNotBlank(expirationStr)){
						int expirationTime=Integer.parseInt(expirationStr.trim());
						expirationDate=DateUtils.addMinutes(requestLoginDate, expirationTime);
					}else{
						expirationDate=DateUtils.addMinutes(requestLoginDate, 10);
					}
					if(now.before(expirationDate)){
						ServletContext sc = ServletActionContext.getServletContext();
						String email = loginUser.getEmail();
						HttpSession session = null;
						Object sessionObject = sc.getAttribute(email);
						if (sessionObject != null)
							session = (HttpSession) sessionObject;
						if (null != session) {
							session = null;
						}
						HttpSession currentSession = ServletActionContext.getRequest().getSession();
						currentSession.setAttribute("enUid", loginUser.getId());
						currentSession.setAttribute(Constants.LOGIN_CURRENTUSER,
								loginUser);
						sc.setAttribute(email, currentSession);
						MaintenanceLog ml=new MaintenanceLog();
						ml.setUserId(userId);
						ml.setLoginDate(new Date());
						ml.setOperator(operator);
						ml.setUserType(LogOperatorType.USER.getIndex());
						facade.saveLogByLoginWithoutPass(ml);
						result = "success";
					}
				}
			}
		} catch (Exception e) {
			logger.error("login error:", e);
		}

		if (logger.isDebugEnabled()) {
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit userLoginByEmail method.takeTime:" + takeTime
					+ "ms");
		}
		return result;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAuthorKey() {
		return authorKey;
	}

	public void setAuthorKey(String authorKey) {
		this.authorKey = authorKey;
	}

	public String getDateKey() {
		return dateKey;
	}

	public void setDateKey(String dateKey) {
		this.dateKey = dateKey;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
}

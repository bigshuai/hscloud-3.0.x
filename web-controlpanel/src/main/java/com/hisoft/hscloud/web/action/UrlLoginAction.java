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
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.PasswordUtil;
import com.hisoft.hscloud.common.util.PropertiesUtils;
import com.hisoft.hscloud.common.util.SecurityHelper;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.hisoft.hscloud.crm.usermanager.entity.MaintenanceLog;
import com.hisoft.hscloud.web.facade.Facade;
import com.opensymphony.xwork2.ActionSupport;

public class UrlLoginAction extends ActionSupport {

	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(this.getClass());

	private String vmId;

	private String authorKey;

	private String dateKey;
	
	private String operator;

	@Autowired
	private Facade facFade;

	/**
	 * 用户登录
	 */
	public String urlLogin() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter urlLogin method.");			
		}
		String result="error";
		try {
			ControlPanelUser controlPanelUser=facFade.getCPUserByVmId(vmId);
			if (controlPanelUser != null) {
				StringBuilder originalKey=new StringBuilder();
				originalKey.append(vmId).append(controlPanelUser.getUserPassword());
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
						String vmId = controlPanelUser.getVmId();
						HttpSession session = null;
						Object sessionObject = sc.getAttribute(vmId);
						if(sessionObject != null){
							session = (HttpSession)sessionObject;
							session.setAttribute(Constants.LOGIN_CURRENTUSER, null);
							sc.removeAttribute(controlPanelUser.getVmId());
							sc.removeAttribute(session.getId());
						}
						
						HttpSession currentSession = (HttpSession)ServletActionContext.getRequest().getSession();
						currentSession.setAttribute(Constants.LOGIN_CURRENTUSER, controlPanelUser);
						sc.setAttribute(currentSession.getId(), controlPanelUser.getVmId());
						sc.setAttribute(vmId, currentSession);
						MaintenanceLog ml=new MaintenanceLog();
						ml.setUuid(vmId);
						ml.setLoginDate(new Date());
						ml.setOperator(operator);
						ml.setUserType(LogOperatorType.CP.getIndex());
						facFade.saveLogByLoginWithoutPass(ml);
						result="success";
					}else{
						result="url_expiration";
					}
				}
				
			} 
		}catch(HsCloudException hse){
			logger.error(hse.getMessage(),hse);
		} catch (Exception e) {
			logger.error("login error:", e);
		}
		
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit urlLogin method.takeTime:" + takeTime + "ms");
		}
		
		return result;
	}
	
	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
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

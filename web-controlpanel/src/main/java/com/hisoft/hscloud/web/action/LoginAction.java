package com.hisoft.hscloud.web.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.controlpanel.entity.ControlPanelUser;
import com.hisoft.hscloud.web.facade.Facade;

public class LoginAction extends HSCloudAction {

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(this.getClass());

	// 需排除验证码字符
	private final String pattern = "[iIlL1oO0]";

	// 验证码
	private String code;

	// 用户名
	private String userName;

	// 密码
	private String password;
	
	// 修改新密码
	private String newPassword;
	
	// 原密码
	private String oldPassword;

	@Autowired
	private Facade facade;

	/**
	 * 用户登录
	 */
	public void login() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter login method.");			
		}
		try {
			// 检查验证码是否正确
			String sessionCode = (String) ServletActionContext.getRequest()
					.getSession().getAttribute("code");
			logger.info("sessionCode:"+sessionCode);
			logger.info("code:"+code);
			if (null == this.code || null == sessionCode
					|| !sessionCode.equalsIgnoreCase(this.code)) {
				if(logger.isDebugEnabled()){	
					logger.debug("this.code:"+this.code);
					logger.debug("sessionCode:"+sessionCode);	
				}
				fillActionResult(Constants.VERCODE_IS_ERROR);
				return;
			}
			ControlPanelUser controlPanelUser = facade.login(userName, password);
			if (controlPanelUser != null) {

				/*ServletContext sc = super.getApplication();
				String vmId = controlPanelUser.getVmId();
				HttpSession session = null;
				Object sessionObject = sc.getAttribute(vmId);
				if(sessionObject != null){
					session = (HttpSession)sessionObject;
					session.setAttribute(Constants.LOGIN_CURRENTUSER, null);
					sc.removeAttribute(controlPanelUser.getVmId());
					sc.removeAttribute(session.getId());
				}*/
				HttpSession currentSession = super.getSession();
				currentSession.setAttribute(Constants.LOGIN_CURRENTUSER, controlPanelUser);
				//sc.setAttribute(currentSession.getId(), controlPanelUser.getVmId());
				//sc.setAttribute(vmId, currentSession);

			} else {
				fillActionResult(Constants.ACCOUNT_NOT_FOUND);
			}
		}catch(HsCloudException hse){
			fillActionResult(hse);
			dealThrow(hse, "");
		} catch (Exception e) {
			logger.error("login error:", e);
			fillActionResult(null, e.getMessage());
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit login method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 用户退出
	 * 
	 * @return
	 */
	public void logout() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter logout method.");			
		}
		HttpSession currentSession = super.getSession();
		ControlPanelUser controlPanelUser = (ControlPanelUser)super.getCurrentLoginUser();
		if(null != currentSession){
			currentSession.setAttribute(Constants.LOGIN_CURRENTUSER, null);
			if(null != controlPanelUser){
				super.getApplication().removeAttribute(controlPanelUser.getVmId());
			}
		}
		fillActionResult(true);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit logout method.takeTime:" + takeTime + "ms");
		}
	}

	public void getSessionUser(){
		
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getSessionUser method.");			
		}
		ControlPanelUser controlPanelUserv = (ControlPanelUser)super.getCurrentLoginUser();
		if(controlPanelUserv!=null){
			fillActionResult(controlPanelUserv);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getSessionUser method.takeTime:" + takeTime + "ms");
		}
		
	}
	/**
	 * 生成验证码
	 */
	public void getImageCode() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getImageCode method.");			
		}
		int width = 70;
		int height = 20;
		try {
			Random random = new Random();
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);
			g.setFont(new Font("Arial", Font.PLAIN, 18));

			String code = RandomStringUtils.random(5, true, true);
			Pattern p = Pattern.compile(pattern);
			Matcher matcher = p.matcher(code);
			while (matcher.find()) {

				code = RandomStringUtils.random(5, true, true);
				matcher = p.matcher(code);

			}
			if (logger.isDebugEnabled()) {
				logger.debug("---code:" + code);
			}
			for (int i = 0; i < code.length(); i++) {
				g.setColor(new Color(20 + random.nextInt(110), 20 + random
						.nextInt(110), 20 + random.nextInt(110)));
				g.drawString(String.valueOf(code.charAt(i)),
						(int) ((width * 0.20) * (i + 0.06)),
						(int) (height * 0.8));
			}
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			request.getSession().setAttribute("code", code);
			g.dispose();
			ServletOutputStream output = response.getOutputStream();
			ImageOutputStream imageOut = ImageIO
					.createImageOutputStream(output);
			ImageIO.write(image, "JPEG", imageOut);
			imageOut.flush();
			imageOut.close();
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getImageCode method.takeTime:" + takeTime + "ms");
		}
	}

	/**
	 * 重置密码
	 */
	public void resetPassword(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter resetPassword method.");			
		}
		try {
			ControlPanelUser currentUser = (ControlPanelUser)super.getCurrentLoginUser();
			facade.modifyPassword(oldPassword, newPassword,currentUser.getId());
			fillActionResult(true);
		} catch (HsCloudException hse) {
			dealThrow(hse, "");
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit resetPassword method.takeTime:" + takeTime + "ms");
		}
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	
}

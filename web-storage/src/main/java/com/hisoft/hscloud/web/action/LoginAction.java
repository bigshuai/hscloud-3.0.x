/* 
* 文 件 名:  LoginAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-4-10 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
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
import com.hisoft.hscloud.storage.vo.OperationType;
import com.hisoft.hscloud.storage.vo.UserVO;
import com.hisoft.hscloud.web.facade.Facade;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-4-10] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class LoginAction extends HSCloudAction {

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = 2721272533937219822L;
	private Logger logger = Logger.getLogger(this.getClass());
	// 需排除验证码字符
	private final String pattern = "[iIlL1oO0]";
	// 验证码
	private String code;
	// 用户名
	private String userName;
	// 密码
	private String password;
	@Autowired
	private Facade facade;
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

	public void login() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter login method.");			
		}
		try {
		    UserVO userVO = facade.login(userName, password);
		    if (userVO != null) {
		        addOperationLog(userVO.getName(), OperationType.LOGIN.getId());
		        
				//为了避免一个账号同时多次登陆，需要把登陆状态缓存为application级别
				ServletContext sc = super.getApplication();	
				String email = userName;
				HttpSession session = null;
				Object sessionObject = sc.getAttribute(email);
				//已经有账号登陆
				if(sessionObject != null)
					session = (HttpSession)sessionObject;
				//踢出已经登陆的账号
				if(null != session){
					session=null;
				}
				//把自己登陆的状态存到application中，以便保证一个账号同时只有一个登陆
				HttpSession currentSession = super.getSession();
				currentSession.setAttribute(Constants.LOGIN_CURRENTUSER, userVO);
				sc.setAttribute(email, currentSession);
				fillActionResult(userVO);
			} else {
				fillActionResult(Constants.ACCOUNT_NOT_FOUND);
			}
		}catch(HsCloudException hse){
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
		try{
		    UserVO userVO = (UserVO)this.getCurrentLoginUser();
		    if(userVO != null) {
		        addOperationLog(userVO.getName(), OperationType.LOGOUT.getId());
	            super.getSession().setAttribute(Constants.LOGIN_CURRENTUSER,null);
		    }
		}catch(Exception e){
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE, "logout Exception:",
					logger, e) ,"000");
		}
		//fillActionResult(true);
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
		Object obj = super.getCurrentLoginUser();
		if(obj!=null){
			fillActionResult(obj);
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
	
	private void addOperationLog(String userName, int operationType) {
	    String ip = this.getRequest().getRemoteHost();
	    facade.addOperationLog(userName, ip, operationType);
	}
}

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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class ImageAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(ImageAction.class);
	
	private final String  pattern = "[iIlL1oO0]";
	
	
	/**
	 * 生成验证码
	 */
	public void getImageCode(){
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
			logger.info("--------Graphics---------"+g);
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);
			g.setFont(new Font("Arial", Font.PLAIN, 18));
			String code = RandomStringUtils.random(5, true, true);
			
			Pattern p = Pattern.compile(pattern);
			Matcher matcher = p.matcher(code);
			while(matcher.find()){
				
				code = RandomStringUtils.random(5, true, true);
				matcher = p.matcher(code);
				
			}
			
			logger.info("---------code-------"+code);
			for (int i = 0; i < code.length(); i++) {
				g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
				g.drawString(String.valueOf(code.charAt(i)),
						(int) ((width * 0.20) * (i + 0.06)),
						(int) (height * 0.8));
			}
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			logger.info("----------request--------------"+request);
			logger.info("-------------response-----------------"+response);
			response.setHeader("Pragma","No-cache");
			response.setHeader("Cache-Control","no-cache");
			response.setDateHeader("Expires", 0);
			request.getSession().setAttribute("code", code);
			logger.debug("code is " + code);
			g.dispose();
			logger.info("--------Graphics---------"+g);
			ServletOutputStream output = response.getOutputStream();
			ImageOutputStream imageOut = ImageIO
					.createImageOutputStream(output);
			logger.info("------------imageOut------------"+imageOut);
			ImageIO.write(image, "JPEG", imageOut);
			imageOut.flush();
		    imageOut.close();
		    logger.info("------------imageOut------------"+imageOut);
		    output.flush();
		    
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getImageCode method.takeTime:" + takeTime + "ms");
		}
	}
	
	
	

}

package com.hisoft.hscloud.web.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;
import com.hisoft.hscloud.bss.sla.sc.entity.Software;
import com.hisoft.hscloud.web.facade.Facade;
import com.opensymphony.xwork2.ActionSupport;

public class WebSiteAction extends ActionSupport{
	
	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = 5675438952039436506L;
	private Logger logger = Logger.getLogger(this.getClass());
	private String siId;
	
	private final String  pattern = "[iIlL1oO0]";
	
	@Autowired
	private Facade facade;
	
	/**
	 * 设置语言
	 */
	public void setLocale(){
		
		if(logger.isDebugEnabled()){
			logger.debug("enter setLocale method");
			logger.debug("exit  setLocale method");
		}
		
	}
	
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
			if(logger.isDebugEnabled()){
				logger.debug("code:"+code);
			}
			for (int i = 0; i < code.length(); i++) {
				g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
				g.drawString(String.valueOf(code.charAt(i)),
						(int) ((width * 0.20) * (i + 0.06)),
						(int) (height * 0.8));
			}
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("Pragma","No-cache");
			response.setHeader("Cache-Control","no-cache");
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
	
	public void getIcon() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter getIcon method.");			
		}
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			if (StringUtils.isNotBlank(siId)) {
				int siIdInt = Integer.parseInt(siId);
				ServiceItem si = facade.getSIById(siIdInt);
				Blob icon = null;
				String contentType = "";
				if (si.getServiceType() == 4) {
					Os os = (Os) si;
					contentType = os.getImageType();
					icon = os.getIcon();
				} else if (si.getServiceType() == 6) {
					Software st = (Software) si;
					contentType = st.getImageType();
					icon = st.getIcon();
				}
				InputStream in=null;
				if(StringUtils.isNotBlank(contentType)&&icon!=null){
					response.setContentType(contentType);
					in=icon.getBinaryStream();
				}else{
					response.setContentType("image/jpeg");
					String contextPath=ServletActionContext.getServletContext().getRealPath("/");
					String relativePath="/skin/xr/images/vpdc/os/default.png";
					in = new FileInputStream(new File(contextPath+relativePath));
				}
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				OutputStream out = response.getOutputStream();
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		} catch (Exception e) {
			logger.debug("获取图片异常", e);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit getIcon method.takeTime:" + takeTime + "ms");
		}
	}

	public String getSiId() {
		return siId;
	}

	public void setSiId(String siId) {
		this.siId = siId;
	}
	

}

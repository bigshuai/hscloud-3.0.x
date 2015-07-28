package com.hisoft.hscloud.web.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hisoft.hscloud.bss.sla.sc.entity.Os;
import com.hisoft.hscloud.bss.sla.sc.entity.ServiceItem;
import com.hisoft.hscloud.bss.sla.sc.entity.Software;
import com.hisoft.hscloud.web.facade.Facade;
import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
public class FileUploadAction extends ActionSupport {

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -720193750742970250L;
	private File image; // 上传的文件
	private String imageFileName; // 文件名称
	private String imageContentType; // 文件类型
	private String resourceType;
	private String siId;
	private Integer zoneGroupId;
	private static Logger logger = Logger.getLogger(FileUploadAction.class);
	@Autowired
	private Facade facade;

	public void uploadFile() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter uploadFile method.");			
		}
		try {
			ServletActionContext.getResponse().setContentType("text/html");
			if (image != null) {
				BufferedImage imageObj = ImageIO.read(image);
				int width = imageObj.getWidth();
				int height = imageObj.getHeight();
//				if (width != 90 || height != 90) {
//					ServletActionContext
//							.getResponse()
//							.getWriter()
//							.print("{\"success\":\"false\",\"message\":\"图片宽度和高度必须等于90像素\"}");
//					return;
//				}
				FileInputStream fi = new FileInputStream(image);
				Blob blob = Hibernate.createBlob(fi);
				int siIdRet = 0;
				if (StringUtils.isNotBlank(siId) && !"0".equals(siId)) {
					int siIdInt = Integer.parseInt(siId);
					ServiceItem si = facade.getSIById(siIdInt);
					if (si != null) {
						if (si.getServiceType() == 4) {
							Os os = (Os) si;
							os.setIcon(blob);
							os.setImageType(imageContentType);
							siIdRet = facade.saveServiceItem(os);
						} else if (si.getServiceType() == 6) {
							Software soft = (Software) si;
							soft.setIcon(blob);
							soft.setImageType(imageContentType);
							siIdRet = facade.saveServiceItem(soft);
						}
					}
				} else {
					if (org.apache.commons.lang.StringUtils
							.isNotBlank(resourceType)) {
						if ("software".equals(resourceType)) {
							Software soft = new Software();
							soft.setIcon(blob);
							soft.setImageType(imageContentType);
							siIdRet = facade.saveServiceItem(soft);
						} else if ("os".equals(resourceType)) {
							Os os = new Os();
							os.setIcon(blob);
							os.setImageType(imageContentType);
							siIdRet = facade.saveServiceItem(os);
						}
					}
				}
				ServletActionContext
						.getResponse()
						.getWriter()
						.print("{\"success\":\"true\",\"id\":\"" + siIdRet
								+ "\"}");
			} else {
				ServletActionContext.getResponse().getWriter()
						.print("{\"success\":\"false\"}");
			}
		} catch (Exception e) {
			try {
				logger.error("上传图片异常",e);
				ServletActionContext.getResponse().getWriter()
						.print("{\"success\":\"false\"}");
			} catch (IOException e1) {
				logger.error("上传图片异常",e1);
			}
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit uploadFile method.takeTime:" + takeTime + "ms");
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
				response.setContentType(contentType);
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				InputStream in = icon.getBinaryStream();
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

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getImageContentType() {
		return imageContentType;
	}

	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}

	public String getSiId() {
		return siId;
	}

	public void setSiId(String siId) {
		this.siId = siId;
	}

	public Integer getZoneGroupId() {
		return zoneGroupId;
	}

	public void setZoneGroupId(Integer zoneGroupId) {
		this.zoneGroupId = zoneGroupId;
	}

}
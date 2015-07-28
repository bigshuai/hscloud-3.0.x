package com.hisoft.hscloud.web.action; 

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.common.util.PropertiesUtils;
import com.hisoft.hscloud.crm.usermanager.entity.Admin;
import com.hisoft.hscloud.crm.usermanager.util.Utils;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo.FileVO;
import com.hisoft.hscloud.vpdc.cmdbmanagement.resource.image.vo.ImageVO;
import com.hisoft.hscloud.web.facade.Facade;

public class ImageManagementAction  extends HSCloudAction{

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -5542350268070825031L;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private String fileName;
	private String query;
	private int page;
	private int limit;
	
	private ImageVO image = new ImageVO();
	
	private File file;
	
	private String fileFileName;
	@Autowired
	private Facade facade;
	
	/**
	 * <上传文件> 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void upload() {
	    Map<String, String> map = PropertiesUtils.getPropertiesMap(
                "image.properties", PropertiesUtils.IMAGE_MAP);
        String path = map.get("path");
	    InputStream is = null;
	    BufferedInputStream bin = null;
	    OutputStream os = null;
	    BufferedOutputStream bout = null;
	    
	    HttpSession session = getSession();
	    Admin admin=(Admin) super.getCurrentLoginUser();
        @SuppressWarnings("unchecked")
        Map<String, String> stateMap = (Map<String, String>)session.getAttribute("state");
        try {
            is = new FileInputStream(file);
            
            bin = new BufferedInputStream(is);
            os = new FileOutputStream(path + fileFileName);
            bout = new BufferedOutputStream(os);
            
            byte buffer[] = new byte[8192];  
            int count = 0;
            BigDecimal total = new BigDecimal(file.length());
            BigDecimal readed = new BigDecimal(0);
            while ((count = bin.read(buffer)) > 0) {
                bout.write(buffer, 0, count);
                readed = readed.add(new BigDecimal(count));
                BigDecimal percent1 = readed.divide(total, 2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(20));
                BigDecimal percent2 = new BigDecimal(80);
                String result = (percent1.add(percent2)).intValue() + "";
                stateMap.put("result", Integer.valueOf(result).toString());
            }
        } catch (FileNotFoundException e) {
            facade.insertOperationLog(admin,"上传文件错误:"+e.getMessage(),"上传文件",Constants.RESULT_FAILURE);
            e.printStackTrace();
        } catch (IOException e) {
            facade.insertOperationLog(admin,"上传文件错误:"+e.getMessage(),"上传文件",Constants.RESULT_FAILURE);
            e.printStackTrace();
        } finally {
            try {
                bout.close();
                os.close();
                bin.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
	     
	    HttpServletResponse response = ServletActionContext.getResponse();
	    response.setContentType("text/html");
	    try {
            response.getWriter().write("{success:true}");
        } catch (IOException e) {
            facade.insertOperationLog(admin,"上传文件错误:"+e.getMessage(),"上传文件",Constants.RESULT_FAILURE);
            e.printStackTrace();
        }
	    facade.insertOperationLog(admin,"上传文件","上传文件",Constants.RESULT_SUCESS);
	}
	
	public void waitForUpload() {
	    HttpSession session = getSession();
	    @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>)session.getAttribute("state");
	    String result = map.get("result");
	    fillActionResult((Object)result);
	}
	
	/**
	 * 显示上传文件列表 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void findAllUploadInfo(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findAllUploadInfo method.");			
		}
		List<FileVO> imageList = null;
		try{
			imageList = facade.getFileList();
		} catch(Exception ex) {
			dealThrow(new HsCloudException(Constants.IMAGE_UPLOAD_LIST_EXCEPTION,
                    "addUploadInfo Exception", logger, ex), Constants.IMAGE_EXCEPTION);
		}		
		fillActionResult(imageList);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findAllUploadInfo method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 删除上传文件 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void deleteUploadInfo() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter deleteUploadInfo method.");			
		}
		Admin admin = null;
		try {
		    admin = (Admin) super.getCurrentLoginUser();
		    facade.deleteFile(fileName);
		    facade.insertOperationLog(admin,"删除上传文件","删除上传文件",Constants.RESULT_SUCESS);
		} catch(Exception ex) {
		    facade.insertOperationLog(admin,"删除上传文件错误:"+ex.getMessage(),"删除上传文件",Constants.RESULT_FAILURE);
		    dealThrow(Constants.IMAGE_EXCEPTION, ex, logger);
		}
		
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit deleteUploadInfo method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 新增imag文件 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void addUploadInfo() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter addUploadInfo method.");			
		}
		Admin admin = null;
		try{
		    admin = (Admin) super.getCurrentLoginUser();
		    facade.addUploadInfo(image, fileName);
			facade.insertOperationLog(admin,"保存镜像","保存镜像",Constants.RESULT_SUCESS);
		} catch(Exception e) {
		    facade.insertOperationLog(admin,"保存镜像错误:"+e.getMessage(),"保存镜像",Constants.RESULT_FAILURE);
		    dealThrow(new HsCloudException(Constants.IMAGE_ADD_EXCEPTION,
                    "addUploadInfo Exception", logger, e), Constants.IMAGE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit addUploadInfo method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * 显示image列表
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void showImageList() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter showImageList method.");			
		}
		try {
			if (!StringUtils.isEmpty(query)) { // 如果查询框不为空时，进入模糊查询
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
	        }
			List<ImageVO> list = facade.showImageList(query);
			
			Page<ImageVO> pageImage = new Page<ImageVO>(Constants.PAGE_NUM);
			pageImage.setPageNo(page);
			pageImage.setPageSize(limit);
			pageImage.setResult(new ArrayList<ImageVO>());
			int nextPageCount = page * limit;
			int count = nextPageCount > list.size() ? list.size() : nextPageCount;
			for(int i = (page - 1) * limit; i < count; i++) {
				pageImage.getResult().add(list.get(i));
			}
			pageImage.setTotalCount(list.size());
			fillActionResult(pageImage);
		} catch (UnsupportedEncodingException e) {
			dealThrow(new HsCloudException(Constants.IMAGE_EXCEPTION,
                    "addUploadInfo Exception", logger, e), Constants.IMAGE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit showImageList method.takeTime:" + takeTime + "ms");
		}
	}
	
	/**
	 * 删除image文件 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void deleteImage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter deleteImage method.");			
		}
		Admin admin = null;
		try {
		    admin = (Admin) super.getCurrentLoginUser();
		    image = Utils.strutsJson2Object(ImageVO.class);
			facade.deleteImage(image.getId());
			facade.insertOperationLog(admin,"删除镜像","删除镜像",Constants.RESULT_SUCESS);
		} catch (Exception e) {
		    facade.insertOperationLog(admin,"删除镜像错误:"+e.getMessage(),"删除镜像",Constants.RESULT_FAILURE);
		    dealThrow(new HsCloudException(Constants.IMAGE_DELETE_EXCEPTION,
                    "addUploadInfo Exception", logger, e), Constants.IMAGE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit deleteImage method.takeTime:" + takeTime + "ms");
		}
	}
	/**
	 * 编辑镜像 
	* <功能详细描述>  
	* @see [类、类#方法、类#成员]
	 */
	public void editImage() {
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter editImage method.");			
		}
		Admin admin = null;
		try {
		    admin = (Admin) super.getCurrentLoginUser();
		    facade.editImage(image);
			facade.insertOperationLog(admin,"编辑镜像","编辑镜像",Constants.RESULT_SUCESS);
		} catch (Exception e) {
		    facade.insertOperationLog(admin,"编辑镜像错误:"+e.getMessage(),"编辑镜像",Constants.RESULT_FAILURE);
		    dealThrow(new HsCloudException(Constants.IMAGE_EDIT_EXCEPTION,
	                "addUploadInfo Exception", logger, e), Constants.IMAGE_EXCEPTION);
		}
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit editImage method.takeTime:" + takeTime + "ms");
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ImageVO getImage() {
		return image;
	}

	public void setImage(ImageVO image) {
		this.image = image;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }
}

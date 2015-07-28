/* 
* 文 件 名:  CloudStorageAction.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  ljg 
* 修改时间:  2013-4-10 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.action; 


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.hisoft.hscloud.common.util.Utils;
import com.hisoft.hscloud.common.HSCloudAction;
import com.hisoft.hscloud.common.util.Constants;
import com.hisoft.hscloud.common.util.HsCloudException;
import com.hisoft.hscloud.storage.entity.Module;
import com.hisoft.hscloud.storage.vo.OperationType;
import com.hisoft.hscloud.storage.vo.UserVO;
import com.hisoft.hscloud.storage.vo.VideoVO;
import com.hisoft.hscloud.web.facade.Facade;
import com.hisoft.hscloud.web.vo.CloudStorageVO;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  ljg 
 * @version  [版本号, 2013-4-10] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class CloudStorageAction  extends HSCloudAction {

	/** 
	* 注释内容 
	*/
	private static final long serialVersionUID = -6450425523465963312L;
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Facade facade;
	private int page;
	private int limit;
	private String query;// 模糊查询条件
	private Date startDate;
	private Date endDate;
	private String sort;//排序
	private String module;
	private Page<CloudStorageVO> cloudStoragePage = new Page<CloudStorageVO>();
	
	private Page<VideoVO> videoPage = new Page<VideoVO>();
	
	private VideoVO videoVO = new VideoVO();
	
	private final String ALL = "全部";

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

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Page<CloudStorageVO> getCloudStoragePage() {
		return cloudStoragePage;
	}

	public void setCloudStoragePage(Page<CloudStorageVO> cloudStoragePage) {
		this.cloudStoragePage = cloudStoragePage;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
    public Page<VideoVO> getVideoPage() {
        return videoPage;
    }

    public void setVideoPage(Page<VideoVO> videoPage) {
        this.videoPage = videoPage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public VideoVO getVideoVO() {
        return videoVO;
    }

    public void setVideoVO(VideoVO videoVO) {
        this.videoVO = videoVO;
    }
    
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    /**
	 * <云存储数据集> 
	* <功能详细描述> 
	* @return 
	* @see [类、类#方法、类#成员]
	 */
	public void findCloudStorage(){
		long beginRunTime = 0;
		if(logger.isDebugEnabled()){
			beginRunTime = System.currentTimeMillis();
			logger.debug("enter findCloudStorage method.");			
		}
		int pageNo=page;
		int pageSize=limit;	
		HttpSession currentSession = super.getSession();
		UserVO userVO = (UserVO)currentSession.getAttribute(Constants.LOGIN_CURRENTUSER);
		if(userVO == null) {
		    super.fillActionResult((Object)Constants.LOGIN);
		    return;
		}
		
		addOperationLog(userVO.getName(), OperationType.FIND.getId());
		try{
		    Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("tenantId", userVO.getTenantId());
		    if(query!=null && !"".equals(query)){
				query = new String(query.getBytes("iso8859_1"), "UTF-8");
				condition.put("query", "%" + query + "%");
			}
		    if(startDate != null ){
                condition.put("startDate", startDate.getTime() / 1000);
            }
		    if(endDate != null ){
                condition.put("endDate", endDate.getTime() / 1000);
            }
		    
		    if(StringUtils.isNotBlank(module)) {
		        module = new String(module.getBytes("iso8859_1"), "UTF-8");
		        if(!ALL.equals(module)) {
		            condition.put("module", module);
		        }
		    }
		    pageOrderBy(videoPage, sort);
		    videoPage.setPageNo(pageNo);
			videoPage.setPageSize(pageSize);
			videoPage = facade.findVideo(videoPage, condition);
		}catch (Exception ex) {
			dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
					"findCloudStorage Exception:", logger, ex), Constants.OPTIONS_FAILURE);
		}
		super.fillActionResult(videoPage);
		if(logger.isDebugEnabled()){
			long takeTime = System.currentTimeMillis() - beginRunTime;
			logger.debug("exit findCloudStorage method.takeTime:" + takeTime + "ms");
		}
	}
	
    public void getPlayUrl() {
        try {
            UserVO userVO = (UserVO) super.getSession().getAttribute(Constants.LOGIN_CURRENTUSER);
            if (userVO == null) {
                super.fillActionResult((Object)Constants.LOGIN);
                return;
            }
            
            
            videoVO = Utils.strutsJson2Object(VideoVO.class);
            
            addOperationLog(userVO.getName(), videoVO.getOperationType());
            String result = facade.getPlayUrl(videoVO, userVO.getEndPoint(), userVO.getTenantId());
            fillActionResult((Object) result);
        } catch (Exception e) {
            dealThrow(new HsCloudException(Constants.OPTIONS_FAILURE,
                    "findCloudStorage Exception:", logger, e),
                    Constants.OPTIONS_FAILURE);
        }
    }
    
    private void pageOrderBy(Page<VideoVO> videoPage, String sort) {
        if(StringUtils.isNotBlank(sort)) {
            JSONArray jsonArray = JSONArray.fromObject(sort);
            JSONObject jsonObject = (JSONObject)jsonArray.get(0);
            videoPage.setOrder(jsonObject.get("direction").toString());
            videoPage.setOrderBy(jsonObject.get("property").toString());
        }
    }
    
    /**
     * <加载模块列表> 
    * <功能详细描述>  
    * @see [类、类#方法、类#成员]
     */
    public void loadModule() {
        HttpSession currentSession = super.getSession();
        UserVO userVO = (UserVO)currentSession.getAttribute(Constants.LOGIN_CURRENTUSER);
        if(userVO == null) {
            super.fillActionResult((Object)Constants.LOGIN);
            return;
        }
        String tenantId = userVO.getTenantId();
        List<Module> list = facade.loadModule(tenantId);
        Module module = new Module();
        module.setId(-1l);
        module.setModuleName(ALL);
        list.add(0, module);
        this.fillActionResult(list);
    }
    
    public String downloadPlayer() {
        Properties properties = new Properties();
        URL url = Thread.currentThread().getContextClassLoader().getResource("/");
        try {
            properties.load(new FileInputStream(url.getPath() + "swift.properties" ));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        HttpServletResponse response = ServletActionContext.getResponse();
        
      //  String path = "E://test.txt";
        String path = (String)properties.get("downloadPlayer");
        // path是指欲下载的文件的路径。
        File file = new File(path);
        if (!file.exists()) {
            response.setContentType("text/html;charset=UTF-8");
            try {
                response.getWriter().print("指定文件不存在！");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(path);

            int len = 0;
            byte[] buffers = new byte[1024];
            response.reset();
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=\""
                    + file.getName() + "\"");
            OutputStream os = null;
            
            os = response.getOutputStream();
            // 把文件内容通过输出流打印到页面上供下载
                while ((len = is.read(buffers)) != -1) {
                    
                    os.write(buffers, 0, len);
                }


            is.close();
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void addOperationLog(String userName, int operationType) {
        String ip = this.getRequest().getRemoteHost();
        facade.addOperationLog(userName, ip, operationType);
    }
}

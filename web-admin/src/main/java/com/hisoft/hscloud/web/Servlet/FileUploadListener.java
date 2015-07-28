/* 
* 文 件 名:  FileUploadListener.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-22 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.Servlet; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-22] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class FileUploadListener implements ProgressListener{
    private HttpSession session;  
    
    public FileUploadListener(HttpServletRequest request) {  
           session = request.getSession();  
           Map<String, String> map = new HashMap<String, String>();
           session.setAttribute("state", map);  
    }  
    @Override  
    public void update(long readedBytes, long totalBytes, int currentItem) {  
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>)session.getAttribute("state");
        double readed = Double.valueOf(readedBytes);
        if(totalBytes < 0) {
            totalBytes = 5000000000l;
        }
        if(totalBytes < readedBytes) {
            totalBytes = readedBytes;
        }
        double total = Double.valueOf(totalBytes);
        map.put("result", String.valueOf((int)(readed / total * 80)));
    }
}

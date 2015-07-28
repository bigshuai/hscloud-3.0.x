/* 
* 文 件 名:  ExceptionServiceUtil.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-1-25 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.common.util; 

import org.apache.log4j.Logger;

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-1-25] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ExceptionServiceUtil {
    private Logger logger;
    
    private boolean flag;
    
    private String message;
    
    public ExceptionServiceUtil(Logger logger) {
        this.logger = logger;
    }
    
    public void throwException(String code, Exception ex) {
        if(flag == true) {
            flag = false;
            throw new HsCloudException(code, message, logger, ex);
        } else {
            throw new HsCloudException(code, logger, ex);
        }
    }
    
    public void throwException(String code, String message, Exception ex) {
        throw new HsCloudException(code, message, logger, ex);
    }

    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.flag = true;
        this.message = message;
    }

}

/* 
* 文 件 名:  ResultVo.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-10 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.hisoft.hscloud.web.vo; 

/** 
 * <返回值对象> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-10] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class ResultVo {
    private boolean success = true;
    private Object resultObject;
    private String reason;
    
    public ResultVo(){
    	
    }
    public ResultVo(boolean success,String reason ){
    	this.success = success;
    	this.reason = reason;
    }
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public Object getResultObject() {
        return resultObject;
    }
    public void setResultObject(Object resultObject) {
        this.resultObject = resultObject;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    
}

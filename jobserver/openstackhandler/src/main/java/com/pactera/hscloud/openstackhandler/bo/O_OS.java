/* 
* 文 件 名:  O_OS.java 
* 版    权:  hiSoft Technologies Co., Ltd. Copyright 2011-2012,  All rights reserved 
* 描    述:  <描述> 
* 修 改 人:  lihonglei 
* 修改时间:  2013-10-14 
* 跟踪单号:  <跟踪单号> 
* 修改单号:  <修改单号> 
* 修改内容:  <修改内容> 
*/ 
package com.pactera.hscloud.openstackhandler.bo; 

/** 
 * <一句话功能简述> 
 * <功能详细描述> 
 * 
 * @author  lihonglei 
 * @version  [版本号, 2013-10-14] 
 * @see  [相关类/方法] 
 * @since  [产品/模块版本] 
 */
public class O_OS {
    private Long os_id;
    private String port;
    
    public Long getOs_id() {
        return os_id;
    }
    public void setOs_id(Long os_id) {
        this.os_id = os_id;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    
}
